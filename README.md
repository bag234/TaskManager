# TaskManager

TaskManager — это Spring Boot приложение для управления задачами с поддержкой авторизации, ролей пользователей, REST API и интеграцией с PostgreSQL.

### Приложение
Приложение запущено по адресу: 
http://am.mrbag.org:9093 - приложение работает в контейнер 

## Возможности
- CRUD для задач и пользователей
- Авторизация через JWT
- Роли пользователей (User, Admin)
- REST API для работы с задачами, пользователями, авторизацией
- Swagger/OpenAPI (только во время тестов)
- Интеграция с PostgreSQL через Docker

## Структура проекта
- `src/main/java/org/mrbag/test/TaskManager/` — основной код
  - `Controller/` — REST-контроллеры (Task, User, Auth)
  - `Entity/` — сущности JPA (Task, User, Role и др.)
  - `Repository/` — репозитории Spring Data JPA
  - `Service/` — бизнес-логика
  - `Secure/` — сервисы и фильтры для JWT
- `src/main/resources/` — конфигурация приложения
  - `application.yaml` — настройки БД, портов, springdoc
- `src/test/java/org/mrbag/test/TaskManager/` — тесты
  - `Mockito/`, `MVC/`, `Service/JWT/` — примеры unit и интеграционных тестов
- `docker-compose.yaml` — запуск приложения и БД через Docker

## Быстрый старт
Файлы проекта содержат минимальный необходимую конфигурацию для развёртывания в любых средах:
_В приложении отключен CORS `src/main/java/org/mrbag/test/TaskManager/SecurutyCfg.java`_

### 1. Клонирование и запуск через Docker-compose
```sh
git clone <repo-url>
cd TaskManager
mvn clean compile
docker-compose up --build
```
- Приложение: http://localhost:9093
- База данных: порт 5433

**В случае ошибки первоначально выполнить пункт 3**
### 2. Локальный запуск
1. Установите JDK 21 и Maven
2. Настройте параметры БД в `src/main/resources/application.yaml`
3. Запустите:
```sh
mvn spring-boot:run
```
### 3. Контейнер docker 
Создания исполняемого контейнера: 
```sh
sudo docker build task-manager:latest .
```
Пример команды для создания контейнера 

### 4. PipeLine
Файл проекта содержит простой pipeline для jenkis который в случае чего позволит выполнить простую разверту на хосте jenkins не используя лишние плагины.
**Необходимы специальные конфигурации для docker хоста**

### 5. Windows
Файлы проекта содержат maven wraper. Рекомендую использовать его для компиляции проекта:
```cmd 
./mvnw clean compile package spring-boot:run
```

## API
- Все основные эндпоинты начинаются с `/api/`
- Примеры:
  - `POST /api/auth/login` — авторизация
  - `GET /api/tasks` — список задач
  - `POST /api/tasks` — создать задачу
  - `GET /api/users` — список пользователей

## Swagger и OpenAPI
- Swagger UI: http://localhost:9093/open-api (только во время тестов)
- Так файл появляется во корне приложения после упаковки.
### Создания и обновления файла openapi
Для создания обновление файла нужно выполнить стадию интеграционных тестов. Во всех остальных стадиях за исключения тестов пути для openAPI не доступны для взаимодействия.
```sh 
mvn post-integration-test
```
**Важно:**
Что стадия интеграционных тестов должна быть выполнена полностью, иначе произойдет запуск приложения на хосте, который решил выполнять стадию! (Запуск происходит с конфигурацией `src/main/resources/application-doc.yaml`)

## Авторизация и роли
- JWT-токен выдается при логине
- Для доступа к защищённым эндпоинтам требуется токен
- Роли: User (обычные пользователи), Admin (расширенные права)
**Admin выдаться только по средство операции в базе данных**

## Тестирование
- Запуск всех тестов:
```sh
mvn test
```
- Тесты покрывают сервисы, контроллеры, авторизацию
### Содержания тестов
- `src/test/java/org/mrbag/test/TaskManager/MVC/ComplecsTestMVC.java`- тест который тестирует комплексное использование приложения, по средство эмуляции рабочего режима (Опрос End-point).
- `src/test/java/org/mrbag/test/TaskManager/Service/JWT/TestServiceJWT.java` -простой JUnit тест, который тестирует целостность и корректность работы jwt сервиса.
- `src/test/java/org/mrbag/test/TaskManager/Mockito/TestServiceMockito.java` - тест который за мокирован и проводит тестирование корректности работы TaskService, а так же условий фильтрации не корректных данных  
### Конфигурация
- В обычном режиме Swagger и OpenAPI отключены
- Для тестов включаются через `src/test/resources/application.yaml`:
```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
```

## Контакты
- Автор: Mr. bagef (root@mrbag.org || bag234.work@gmail.com)

---

## Примеры запросов:
_Для запросов будем использовать curl_

### Контролер Регистрации и авторизации `/api/auth/`

#### Регистрация пользователя:
```sh
curl  -X POST \
  'http://am.mrbag.org:9093/api/auth/register' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "username":"test",
  "email":"test@test.com",
  "password":"test"
}'
```
Пример результата ответа:
Код 200, в теле сообщения true:

#### Авторизация пользователя:
```sh
curl  -X POST \
  'http://am.mrbag.org:9093/api/auth/login' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email":"test@test.com",
  "password":"test"
}'
```
Результат ответа:
```json
{"token":"eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw"}
```

### Контролер пользователя `/api/user/`
_Для обычного пользователя доступены только `/me`_
```sh
curl  -X GET \
  'http://am.mrbag.org:9093/api/user/me' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw'
```
Результат ответа (Экземпляр хранимого пользователя во контексте приложения):
```json
{"id":1,"username":"test","email":"test@test.com","password":"$2a$10$J6sWN21H6joiH5MxGyM7uO256NjQW3Ny7JTpP8x5rbmiFbVucvM22","role":"USER"}
```
_Остальные контроллеры доступны только администратору_

### Вспомогательные API:
#### Получение всех статусов задач возможных в приложении:
```sh
curl  -X GET \
  'http://am.mrbag.org:9093/api/service/status' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw'
```
Результат ответа:
```json
[
  "TODO",
  "IN_PROGRESS",
  "DONE"
]
```
#### Получение всех приоритетов задач возможных в приложении:
```sh
curl  -X GET \
  'http://am.mrbag.org:9093/api/service/priority' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw'
```
Результат ответа:
```json
[
  "LOW",
  "MEDIUM",
  "HIGH"
]
```

## Контролер управления задачами `/api/tasks`

#### Создание задачи:
_Минимально необходимые данные_
```sh
curl  -X POST \
  'http://am.mrbag.org:9093/api/tasks' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "title":"test minimal"
}'
```
Результат ответа:
```json
{
  "id": 1,
  "title": "test minimal",
  "description": null,
  "status": "TODO",
  "priority": "LOW",
  "author": 1,
  "assigner": null,
  "createdAt": "2025-08-25T07:23:08.669122",
  "updateAt": "2025-08-25T07:23:08.669277"
}
```

#### Получение задачи по ID `/{id}`:
```sh
curl  -X GET \
  'http://am.mrbag.org:9093/api/tasks/2' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw'
```
Результат ответа:
```json
{
  "id": 2,
  "title": "test 1",
  "description": "Task for test 1",
  "status": "TODO",
  "priority": "LOW",
  "author": 1,
  "assigner": null,
  "createdAt": "2025-08-25T07:24:32.784363",
  "updateAt": "2025-08-25T07:24:32.784428"
}
```

#### Получение списка задач, согласно фильтрам:
_Без Фильтрев_
```sh
curl  -X GET \
  'http://am.mrbag.org:9093/api/tasks' \
  --header 'priority: ' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw'
```
Результат ответа:
```json
[
  {
    "id": 1,
    "title": "test minimal",
    "description": null,
    "status": "TODO",
    "priority": "LOW",
    "author": 1,
    "assigner": null,
    "createdAt": "2025-08-25T07:23:08.669122",
    "updateAt": "2025-08-25T07:23:08.669277"
  },
  {
    "id": 2,
    "title": "test 1",
    "description": "Task for test 1",
    "status": "TODO",
    "priority": "LOW",
    "author": 1,
    "assigner": null,
    "createdAt": "2025-08-25T07:24:32.784363",
    "updateAt": "2025-08-25T07:24:32.784428"
  },
  {
    "id": 3,
    "title": "test 2",
    "description": "Task for test 2",
    "status": "TODO",
    "priority": "HIGH",
    "author": 1,
    "assigner": null,
    "createdAt": "2025-08-25T07:24:51.228467",
    "updateAt": "2025-08-25T07:24:51.228514"
  },
  {
    "id": 4,
    "title": "test 3",
    "description": "Task for test 3",
    "status": "DONE",
    "priority": "HIGH",
    "author": 1,
    "assigner": null,
    "createdAt": "2025-08-25T07:25:07.709088",
    "updateAt": "2025-08-25T07:25:07.709142"
  }
]
```
_С фильтрами_
```sh
curl  -X GET \
  'http://am.mrbag.org:9093/api/tasks?priority=HIGH&status=TODO&status=DONE' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw'
```
Результат ответа:
```json
[
  {
    "id": 3,
    "title": "test 2",
    "description": "Task for test 2",
    "status": "TODO",
    "priority": "HIGH",
    "author": 1,
    "assigner": null,
    "createdAt": "2025-08-25T07:24:51.228467",
    "updateAt": "2025-08-25T07:24:51.228514"
  },
  {
    "id": 4,
    "title": "test 3",
    "description": "Task for test 3",
    "status": "DONE",
    "priority": "HIGH",
    "author": 1,
    "assigner": null,
    "createdAt": "2025-08-25T07:25:07.709088",
    "updateAt": "2025-08-25T07:25:07.709142"
  }
]
```

#### Обновление задачи по ID `/{id}`:
```sh
curl  -X PUT \
  'http://am.mrbag.org:9093/api/tasks/4' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw' \
  --header 'Content-Type: application/json' \
  --data-raw '  {
    "title": "test 3",
    "description": "Task for test 3(UPDATE)",
    "status": "DONE",
    "priority": "LOW",
    "author": 1,
    "assigner": null,
    "createdAt": "2025-08-25T07:25:07.709088",
    "updateAt": "2025-08-25T07:25:07.709142"
  }'
```
Результат ответа:
Код ответа 202.

#### Удаление задачи по ID `/{id}`:
```sh
curl  -X DELETE \
  'http://am.mrbag.org:9093/api/tasks/1' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6Ik9GRiIsIlJvbGUiOiJVU0VSIiwiVXNlcm5hbWUiOiJ0ZXN0Iiwic3ViIjoidGVzdEB0ZXN0LmNvbSIsImlhdCI6MTc1NjEwNDc2NCwiZXhwIjoxNzU2MTkxMTY0fQ.LsBHsRW3W2goqm740wnwXwzM7jEi8DUQyxeC8yH9MYw'
```
Результат ответа:
Код ответа 202.

**_НЕ демонстрируется случаи не правильного и использования API, потому что их покрывает тесты (смотри: `src/test/java/org/mrbag/test/TaskManager/MVC/ComplecsTestMVC.java`)_**

## Примечания
- Для работы с Postgres используйте параметры из `docker-compose.yaml` или настройте свои.
- В случае использования другой базы данных, добавьте драйвер в зависимости проекта.


