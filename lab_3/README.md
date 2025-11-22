================================================================
ЛАБОРАТОРНАЯ РАБОТА 3 — ТЕХНИЧЕСКОЕ ЗАДАНИЕ
================================================================

############################################################
# НАЗВАНИЕ
############################################################
Мини-сервер на сервлетах (Tomcat) с Caffeine Cache вместо БД:
REST-API пользователей, аутентификация через cookie, фильтр проверки сессии.

############################################################
# ОБЩЕЕ ОПИСАНИЕ
############################################################
Разработать веб-приложение на чистых сервлетах и фильтрах (БЕЗ Spring и Spring Boot),
разворачиваемое в Apache Tomcat как WAR-архив. Приложение предоставляет REST-эндпоинты
для CRUD-операций над пользователями и эндпоинт логина. Данные пользователей и сессий
хранятся в памяти с помощью Caffeine Cache (персистентность не требуется).
Аутентификация — cookie-ориентированная сессия; доступ к защищённым ресурсам
контролируется сервлет-фильтром.

############################################################
# АРХИТЕКТУРА И СЛОИ
############################################################
• Слой сервлетов (HTTP API): HttpServlet-классы, принимают/возвращают JSON.
• Слой фильтров: аутентификация/авторизация на основе cookie; наполнение контекста запроса.
• Сервисный слой: бизнес-логика (регистрация, логин, CRUD пользователей).
• Слой данных (DAO): абстракции и реализация поверх Caffeine Cache.
• Утилиты: JSON (Jackson), валидация, криптография (PBKDF2), генерация идентификаторов.

Рекомендуемая структура пакетов (пример):
com.example.authapp
├─ web/servlet/         (UsersServlet, UserByIdServlet, LoginServlet, LogoutServlet)
├─ web/filter/          (AuthFilter)
├─ service/             (UserService, AuthService)
├─ dao/                 (UserDao, CaffeineUserDao, SessionDao, CaffeineSessionDao)
├─ domain/              (User, Session, Role, ErrorDto)
├─ dto/                 (UserDto, CreateUserRequest, UpdateUserRequest, LoginRequest, ...)
├─ util/                (JsonUtil, PasswordHasher, Validation)
└─ config/              (CacheConfig, AppConfig)

############################################################
# ТЕХНОЛОГИЧЕСКИЕ ОГРАНИЧЕНИЯ
############################################################
• Запрещено: любые части Spring-экосистемы (Spring MVC, Spring Boot и т.п.).
• Обязателен: Apache Tomcat установлен вручную (не встроенный boot-сервер).
• Сборка: Maven (разрешено) — packaging=war.
• Зависимости (минимум): servlet-api (jakarta.* или javax.* — на ваш выбор, но согласованно
с версией Tomcat), Jackson, Caffeine. Дополнительно допускается JUnit/AssertJ/Mockito для тестов.

############################################################
# МОДЕЛЬ ДАННЫХ
############################################################
User
• id : String (UUID)
• username : String (уникально, 3..32, латиница/цифры/_/-.)
• displayName : String (1..64)
• email : String (валидный формат)
• passwordHash : String (результат PBKDF2; пароли в явном виде не хранить)
• roles : Set<Role> (минимум USER)

Role : enum { USER, ADMIN } — при необходимости; авторизация базовая (см. ниже).

Session
• id : String (случайный токен/UUID) — значение для cookie
• userId : String
• createdAt : Instant
• expiresAt : Instant
• ip : String (опционально), userAgent : String (опционально)

############################################################
# REST API (JSON, UTF-8)
############################################################
Базовый префикс: /api

Анонимно доступные:
GET    /api/health
→ 200 {"status":"OK"}

POST   /api/auth/login
Request: {"username":"...", "password":"..."}
→ 200 Set-Cookie: SESSIONID=<token>; HttpOnly; SameSite=Lax; Path=/; Max-Age=1800
Body: {"user":{"id":"...","username":"...","displayName":"...","email":"..."}}
→ 401 {"error":"INVALID_CREDENTIALS"}

POST   /api/auth/logout
Требует авторизации.
→ 204 (а также Set-Cookie: SESSIONID=; Max-Age=0)

Требуют авторизации (валидная cookie SESSIONID):
GET    /api/users?offset=0&limit=50
→ 200 [{"id":"...","username":"...","displayName":"...","email":"..."}]

POST   /api/users
Request: {"username":"...","displayName":"...","email":"...","password":"..."}
→ 201 {"id":"..."}  (пароль хэшируется на сервере)

GET    /api/users/{id}
→ 200 {"id":"...","username":"...","displayName":"...","email":"..."}
→ 404 {"error":"NOT_FOUND"}

PUT    /api/users/{id}
Request: {"displayName":"...", "email":"...", "password": "...?"}
→ 200 {"id":"..."} | 204
→ 404 {"error":"NOT_FOUND"}

DELETE /api/users/{id}
→ 204
→ 404 {"error":"NOT_FOUND"}

Примечания:
• Авторизация минимальная: доступ к /api/users/* разрешён аутентифицированным пользователям.
(Если вводите роли, уничтожение/изменение пользователей разрешайте только ADMIN или владельцу id.)
• Формат ошибки (общий): {"error":"CODE","message":"human-readable"} со статусами 400/401/403/404/409/500.

############################################################
# АУТЕНТИФИКАЦИЯ И СЕССИИ
############################################################
• Логин: валидация учётных данных через UserService.
• Сгенерировать SESSIONID (криптографически стойкий токен) и сохранить Session в Caffeine с TTL
(например, 30 минут). Cookie помечать HttpOnly, SameSite=Lax; Secure включать при https.
• Logout: удалить запись сессии из кэша и занулить cookie.
• Продление сессии: опционально — скользящее окно (обновлять expiresAt при активности).
• Пароли: хранить только как PBKDF2-хэши (SecretKeyFactory PBKDF2WithHmacSHA256, соль 16-байт,
итерации ≥ 100_000, длина ключа ≥ 256 бит).

############################################################
# ФИЛЬТР АУТЕНТИФИКАЦИИ (AuthFilter)
############################################################
• Перехватывает все запросы к /api/** кроме /api/auth/login и /api/health.
• Извлекает cookie SESSIONID, проверяет наличие сессии в SessionDao (Caffeine).
• При успехе: добавляет в request атрибуты request.setAttribute("auth.userId", ...).
• При провале: 401 с JSON-ошибкой.
• Фильтр не должен блокировать рабочие потоки — выполнять I/O минимально; все операции в памяти.

############################################################
# DAO И КЭШИРОВАНИЕ (Caffeine)
############################################################
• UserDao: CRUD поверх Caffeine Cache<String, User> с ограничением размера (например, 10_000 записей).
• SessionDao: Caffeine Cache<String, Session> с policy по TTL/expireAfterWrite.
• Потокобезопасность: операции через Caffeine — lock-free; дополнительные синхронизации избегать.
• Конфликты: создать уникальный индекс по username (проверка в UserService → 409 CONFLICT).

############################################################
# СЕРИАЛИЗАЦИЯ JSON
############################################################
• Jackson ObjectMapper: strict-режим (fail on unknown properties).
• Медиа-тип: application/json; charset=utf-8.
• Все ответы и ошибки — JSON. Пустые ответы: 204 No Content.

############################################################
# ЛОГИРОВАНИЕ
############################################################
• INFO: успешные входы/выходы, CRUD-операции (без логирования паролей!).
• WARN: подозрительная активность (много неуспешных логинов).
• ERROR: необработанные исключения; возвращать 500 с JSON-ошибкой.
• Корреляция: генерировать requestId и логировать его сквозным параметром.

############################################################
# БЕЗОПАСНОСТЬ (МИНИМУМ)
############################################################
• Не хранить пароли в явном виде; приемлемы только надёжные хэши.
• HttpOnly/SameSite для cookie; Secure при https.
• Защититься от XSS/JSON-инъекций: корректный Content-Type, экранирование строк при логировании.
• Брутфорс-защита (опционально): счётчик попыток логина в Caffeine, временная блокировка.
• CORS: для локальной разработки можно разрешить localhost-происхождение; в проде — ограничить.

############################################################
# СБОРКА И РАЗВЁРТЫВАНИЕ
############################################################
• Maven: packaging=war, плагин maven-war-plugin; servlet-api как provided.
• Конфигурация сервлетов/фильтров: либо через web.xml, либо через @WebServlet/@WebFilter.
• Сборка артефакта: target/authapp.war.
• Развёртывание: вручную в Tomcat webapps/ (или через Tomcat Manager).
• Переменные окружения/настройки: таймауты сессий, размеры кэша, параметры PBKDF2.


############################################################
# МАТЕРИАЛЫ ДЛЯ ПРОЧТЕНИЯ
############################################################
• Что такое Tomcat, как соотносится со Spring MVC/Boot (понять роль контейнера сервлетов).
https://www.infoworld.com/article/2265307/what-is-apache-tomcat-the-original-java-servlet-container.html
А какие еще бывают:
https://www.baeldung.com/spring-boot-servlet-containers?utm_source=chatgpt.com
• Servlet API: жизненный цикл, поточность, блокировка потоков из-за долгих операций.
https://www3.ntu.edu.sg/home/ehchua/programming/java/JavaServlets.html
• Фильтры: цепочка фильтров, порядок, типичные use-cases (аутентификация, CORS, логирование).
https://www.baeldung.com/intercepting-filter-pattern-in-java
https://www.oracle.com/java/technologies/filters.html
• Caffeine Cache: базовые политики, TTL, ограничения по памяти.
https://www.baeldung.com/java-caching-caffeine
https://github.com/ben-manes/caffeine (README)
• PBKDF2 в Java (SecretKeyFactory), безопасное хранение паролей.
https://www.baeldung.com/java-password-hashing
https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
https://stackoverflow.com/questions/1473324/is-there-a-standard-for-using-pbkdf2-as-a-password-hash
• Настройка Maven-WAR и деплой в Tomcat Manager.
https://www.baeldung.com/tomcat-deploy-war?utm_source=chatgpt.com

================================================================
КОНЕЦ ТЕХНИЧЕСКОГО ЗАДАНИЯ
================================================================
