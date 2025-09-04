# Установка JDK
С сайта https://adoptium.net/temurin/releases/ загрузить tar.gz архив версии java21 на linux под нужный процессор (у меня x64). \
После этого вбить в терминал: **cd /mnt/c/Users/тут_твое_имя_пользователя/Downloads/** \
JDK лучше всего устанавливать в папку /usr/lib/jvm, поэтому скачанный архив распаковать туда \
Если такой папки нет, ее надо создать с помощью **sudo mkdir -p /usr/lib/jvm** \
Распаковать архив в папку /usr/lib/jvm: **sudo tar -xzf OpenJDK21U-jdk_x64_linux_hotspot_21.0.8_9.tar.gz -C /usr/lib/jvm/** \
Далее установить переменные окружения. Зайти в файл **nano ~/.bashrc**, пролистать в самый конец и написать две строки  \
**export JAVA_HOME=/usr/lib/jvm/jdk-21.0.8+9** \
**export PATH=$JAVA_HOME/bin:$PATH** \
Они делают так, что программа переходит в PATH, там находит JDK и сам javac, и запускает его. JAVA_HOME - это адрес JDK 

Чтобы проверить, что все правильно установилось, написать в терминале: \
**which java** (/usr/lib/jvm/jdk-21.0.8+9/bin/java) \
**java -version** (openjdk version "21.0.8" 2025-07-15 LTS
OpenJDK Runtime Environment Temurin-21.0.8+9 (build 21.0.8+9-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.8+9 (build 21.0.8+9-LTS, mixed mode, sharing)) \
**javac -version** (javac 21.0.8) \
**echo $JAVA_HOME** (/usr/lib/jvm/jdk-21.0.8+9) \
В скобках то, что должно выдаваться
