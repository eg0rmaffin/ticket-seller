# Ticket Seller

## Описание

Сервис для управления билетами, включающий функции создания, просмотра, покупки билетов, а также управление пользователями с различными ролями (USER и ADMIN).

## Структура проекта

- `ticket-service` - Микросервис для управления билетами.
- `user-service` - Микросервис для управления пользователями.
- `eureka-server` - Микросервис для управления службой обнаружения (Service Discovery).
- `api-gateway` - API шлюз для маршрутизации запросов к соответствующим микросервисам.
- `swagger-config` - Модуль для интеграции Swagger UI.
- `common-dto` - Модуль с общими DTO объектами для обмена данными между микросервисами.
- `kafka-config` - Модуль конфигурации для интеграции с Apache Kafka.

## Технологии

- Java 17
- Spring Boot
- Spring Security
- Spring Cloud Netflix Eureka
- Apache Kafka
- Springdoc OpenAPI (Swagger)
- PostgreSQL
- Gradle

## API Документация

Swagger UI доступен по следующим URL:

- `user-service`: `http://localhost:8080/swagger-ui.html`
- `ticket-service`: `http://localhost:8084/swagger-ui.html`
- `security-service`: `http://localhost:8082/swagger-ui.html`

## Роли

- `USER`: Доступ к просмотру и покупке билетов.
- `ADMIN`: Полный доступ к управлению билетами и пользователями.

## Авторизация

Для доступа к защищённым ресурсам используйте JWT токены. Токены можно получить через эндпоинты `security-service`.
