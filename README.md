# DaData API Tests

Автоматизированные API-тесты для сервиса [DaData](https://dadata.ru/api/), реализованные на Java с использованием Rest Assured, JUnit 5 и Maven.

## Технологии

* Java 17
* Maven
* Rest Assured
* JUnit 5
* Jackson

## Требования

Перед запуском установите:

* JDK 17 или новее;
* Maven;
* IntelliJ IDEA или другую Java IDE.

Проверить установку:

```bash
java -version
mvn -version
```

## Настройка API-токена

Для запуска тестов необходим API-токен DaData.

Токен передаётся через переменную окружения:

```text
DADATA_API_KEY
```

### Linux и macOS

```bash
export DADATA_API_KEY="ваш_токен"
```

### Windows PowerShell

```powershell
$env:DADATA_API_KEY="ваш_токен"
```

### Windows CMD

```cmd
set DADATA_API_KEY=ваш_токен
```

### IntelliJ IDEA

Откройте:

```text
Run → Edit Configurations → Environment variables
```

Добавьте переменную:

```text
DADATA_API_KEY=ваш_токен
```

## Запуск тестов

Все команды необходимо выполнять из корневой папки проекта.

### Запустить все тесты

```bash
mvn test
```

### Очистить проект и запустить все тесты

```bash
mvn clean test
```
