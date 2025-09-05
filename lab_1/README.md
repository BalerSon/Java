================================================================
## ЛАБОРАТОРНАЯ РАБОТА 1 — ТЕХНИЧЕСКОЕ ЗАДАНИЕ
================================================================

############################################################
# НАЗВАНИЕ
############################################################
Консольная программа для вычисления хэш-значений файлов.

############################################################
# ОБЩЕЕ ОПИСАНИЕ
############################################################
Написать консольную программу, которая вычисляет хэш-значения (алгоритм MD5, SHA-256,
BLAKE2s-256, BLAKE2b-256, SHAKE-256) для
указанного файла или набора файлов. Программа поставляется в виде
исполняемого JAR-файла.

############################################################
# РЕЖИМЫ РАБОТЫ
############################################################
1) Интерактивный режим:
   • Запуск при передаче флага -i.
   • Программа не завершает работу, пока пользователь явно не выйдет.
   • Должна быть возможность последовательно вычислять хэши для разных файлов.

2) Неинтерактивный режим:
   • При отсутствии флага -i.
   • Алгоритм выбирается флагами (см. «Параметры командной строки»).
   • Пути к файлам передаются через флаг -f.

############################################################
# ПАРАМЕТРЫ КОМАНДНОЙ СТРОКИ
############################################################
-i
    Включает интерактивный режим.

-md5
    Выбирает алгоритм MD5.

-sha256
    Выбирает алгоритм SHA-256.

-blake2b256
    Выбирает алгоритм BLAKE2b-256 (через внешний провайдер).

-blake2s256
    Выбирает алгоритм BLAKE2s-256 (через внешний провайдер).

-shake256 [bits]
    Выбирает алгоритм SHAKE-256 (XOF) с длиной вывода в битах (необязательный
    параметр; если указан — должен быть кратен 8).

-f <путь1> <путь2> ...
    Перечень путей к файлам через пробел, для которых требуется вычислить хэш.

-h | --help
    Краткая справка по использованию.

Примечания к совместимости параметров:
   • В неинтерактивном режиме выбирается ровно один алгоритм.
   • Если алгоритм не указан — использовать SHA-256 по умолчанию.

############################################################
# ТРЕБОВАНИЯ К ФУНКЦИОНАЛЬНОСТИ
############################################################
• Вычисление хэш-значений должно поддерживаться для следующих алгоритмов:
  MD5, SHA-256 (стандартная библиотека Java),
  BLAKE2b-256, BLAKE2s-256, SHAKE-256 (через внешний провайдер).
• Поддерживается обработка нескольких файлов за один запуск (флаг -f).
• В интерактивном режиме допускается многократный последовательный расчёт
  для разных файлов.
• Текстовый вывод хэш-значений в консоль.

############################################################
# ТРЕБОВАНИЯ К РЕАЛИЗАЦИИ АЛГОРИТМОВ
############################################################
• MD5 и SHA-256 должны использоваться из стандартной библиотеки Java.
• Для BLAKE2b-256, BLAKE2s-256 и SHAKE-256 необходимо использовать
  внешний криптографический провайдер Bouncy Castle для Java
  (bcprov-jdk18on).
• Реализация хэш-функций самостоятельно не требуется.

############################################################
# ТРЕБОВАНИЯ К ПОСТАВКЕ И СБОРКЕ
############################################################
• Итогом работы является исполняемый JAR-файл.
• Использование сборщиков (например, Maven/Gradle) не допускается.
• Сборка результирующего JAR производится из исходников средствами JDK.
• При использовании внешней библиотеки(ек) требуется учесть их наличие
  при запуске JAR (оформление на усмотрение исполнителя в рамках JDK-инструментов).

############################################################
# ТРЕБОВАНИЯ К СТРУКТУРЕ КОДА
############################################################
• Функциональность взаимодействия с пользователем в интерактивном режиме
  и функциональность работы через флаги командной строки должны быть
  разделены как минимум на два отдельных класса.

############################################################
# ПРИМЕР ЗАПУСКА (НЕИНТЕРАКТИВНЫЙ)
############################################################
java -jar myHashFunc.jar -md5 -f file_samples/example.xml

(пример приведён для иллюстрации интерфейса командной строки)

############################################################
# МАТЕРИАЛЫ ДЛЯ ПРОЧТЕНИЯ
############################################################
• Пример о создании консольного приложения на Java
  https://medium.com/@mohamed.enn/jumpstarting-java-creating-your-first-console-application-27f3fc5a6459
• Класс Scanner для работы с консолью
  https://www.javatpoint.com/Scanner-class
• Сборка JAR-файла
  https://docs.oracle.com/javase/tutorial/deployment/jar/build.html
• Сборка с библиотекой вручную
  http://bethecoder.com/applications/articles/java/basics/how-to-create-java-executable-jar-with-jar-dependencies.html
• О формате JAR
  https://docs.oracle.com/javase/tutorial/deployment/jar/index.html
• Общие материалы по Java
  http://tutorials.jenkov.com/

# Java / JCA / MessageDigest
• Обзор безопасности Java (как устроены провайдеры, свойства безопасности)
  https://docs.oracle.com/en/java/javase/11/security/java-security-overview1.html
  [Зачем: понять модель провайдеров и где настраивается java.security]. :contentReference[oaicite:0]{index=0}

• MessageDigest — официальная дока
  https://docs.oracle.com/javase/8/docs/api/java/security/MessageDigest.html
  [Зачем: API для MD5/SHA-256, специфика update/digest]. :contentReference[oaicite:1]{index=1}

• Standard Algorithm Names (JCA)
  https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html
  [Зачем: канонические имена алгоритмов в JCA]. :contentReference[oaicite:2]{index=2}

• Класс Provider и регистрация провайдера
  https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/security/Provider.html
  https://docs.oracle.com/en/java/javase/24/security/security-properties-file.html
  [Зачем: как статически/динамически добавлять провайдеры]. :contentReference[oaicite:3]{index=3}

# Bouncy Castle (внешний провайдер)
• Главная страница документации BC (Java)
  https://www.bouncycastle.org/documentation/documentation-java/
  [Зачем: точка входа в доки и примеры]. :contentReference[oaicite:4]{index=4}

• SHAKEDigest (XOF) — javadoc
  https://downloads.bouncycastle.org/java/docs/bcprov-jdk18on-javadoc/org/bouncycastle/crypto/digests/SHAKEDigest.html
  [Зачем: интерфейс Xof, doOutput/doFinal]. :contentReference[oaicite:5]{index=5}

• Blake2s / Blake2b — javadoc провайдера
  https://downloads.bouncycastle.org/java/docs/bcprov-jdk14-javadoc/org/bouncycastle/jcajce/provider/digest/Blake2s.html
  [Зачем: какие варианты доступны у BC (в т.ч. *-256)]. :contentReference[oaicite:6]{index=6}

# Спецификации алгоритмов (первичные источники)
• BLAKE2 — RFC 7693
  https://datatracker.ietf.org/doc/html/rfc7693
  [Зачем: официальная спецификация BLAKE2b/s и тест-векторы]. :contentReference[oaicite:7]{index=7}

• SHA-3 и SHAKE — NIST FIPS 202 (PDF)
  https://nvlpubs.nist.gov/nistpubs/fips/nist.fips.202.pdf
  [Зачем: определение SHAKE-256 и примеры]. :contentReference[oaicite:8]{index=8}

# JAR / манифест / Class-Path
• jar — официальная документация инструмента (Java 11+)
  https://docs.oracle.com/en/java/javase/11/tools/jar.html
  [Зачем: ключи jar для сборки и манифеста]. :contentReference[oaicite:9]{index=9}

• JAR File Specification (манифест и атрибут Class-Path)
  https://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html
  [Зачем: как корректно ссылать внешние JAR’ы]. :contentReference[oaicite:10]{index=10}

• Class-Path в манифесте: относительные пути
  https://docs.oracle.com/javase/tutorial/deployment/jar/downman.html
  [Зачем: правила указания зависимостей через Class-Path]. :contentReference[oaicite:11]{index=11}

# Потоковый ввод-вывод (большие файлы)
• Официальный туториал Oracle по I/O Streams
  https://docs.oracle.com/javase/tutorial/essential/io/streams.html
  [Зачем: чтение блоками через InputStream]. :contentReference[oaicite:12]{index=12}

# Необязательные утилиты (по желанию)
• Apache Commons CLI — User Guide/Javadoc
  https://commons.apache.org/cli/
  [Зачем: удобный парсинг флагов]. :contentReference[oaicite:13]{index=13}

• Apache Commons Codec — Hex
  https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/binary/Hex.html
  [Зачем: готовый HEX encoder]. :contentReference[oaicite:14]{index=14}


================================================================
КОНЕЦ ТЕХНИЧЕСКОГО ЗАДАНИЯ
================================================================