## Сервис для управления задачами  

RESTfull приложение с логированием через аспекты, реализованными в стартере, 
работающий с KAFKA и направляющий email при обновлении статуса задачи, с модульными и интеграционными тестами.  

#### Стек:  
Java-21, Spring Framework (Boot-starter (Web, Data JPA, Aspects, Mail, Test), WebMVC, Boot-TestContainer,
Kafka, Integration Kafka), Apache Kafka (Clients, Streams), Maven, GSON, PostgreSQL.  

Приложение на порту 8081  
PostgreSQL на порту 5432  
UI for Apache Kafka на порту 8080  
Apache Kafka на порту 9092  

1. Создан RESTfull сервис для управления задачами:  
   Task(id, title, description, userId)  
   1.1. POST /tasks - создание новой задачи.  
   1.2. GET /tasks/{id} - получение задачи по ID.  
   1.3. PUT /tasks/{id} - обновление задачи.  
   1.4. DELETE / \tasks/{id} - удаление задачи.  
   1.5. GET /tasks - получение списка всех задач.
  
2. Разработан собственный Spring Boot стартер, который добавляет функциональность логирования.  
   Реализованы аспект, со следующими advice:  
   * Before  
   * AfterThrowing  
   * AfterReturning  
   * Around (замер выполнения)  
   Реализована возможность настройки логирования через параметры в application.yml.  
   Пользователям разрешено включать или отключать логирование, а также выбирать уровень логов(info, debug, warn, error).  
     
3. Kafka  
   Использован docker-compose для установки Kafka в Docker.  
   Установлен UI Kafka для работы с Kafka в Docker.
   Сконфигурирована Kafka, Producer, Consumer для работы с задачами.  
   Продюсер пишет в топик id и новый статус задач у которых он изменился. 
   Консьюмер слушает этот топик, читает оттуда сообщения, и через NotificationService направляется email пользователю.  

4. Сервис покрыт тестами.  
   Модульные тесты для каждого метода сервиса и маппера с использованием JUnit и Mockito.  
   Интеграционные тесты с использованием Spring Boot Test и MockMvc.  
   Тесты проверяют поведение, как при корректных ответах, так и при возникновении ошибок.