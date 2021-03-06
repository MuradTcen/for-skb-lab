#### Тестовое задание
Форма регистрации с отправкой имейла после одобрения из внешней системы.
 
Дана форма регистрации в нашем приложении, в которой необходимо заполнить:

- логин,
- пароль,
- адрес электронной почты,
- ФИО.

После отправки формы, мы регистрируем данные из нее в нашей БД, а также отправляем ее для одобрения во внешней системе.
 Пусть обмен с этой внешней системой будет через некое messaging решение. После одобрения или отклонения заявки,
  наше приложение должно отправить сообщение на электронную почту нашему пользователю с результатом проверки.

Стэк: JavaSE 8+, Spring boot 2, dbms - h2. Для тестов предпочтение Junit/Mockito/Assertj, т.к. на проекте будут именно они.
 Остальное по вкусу.

В качестве абстракции над шиной предлагаем взять такой набросок: 
https://pastebin.com/qWjRPuyp

Возвращать из примеров в наброске можно заглушки, дабы сэкономить время на реализацию тестового задания.
 Неплохо при этом помнить, что в реальной эксплуатации любая часть нашей системы может отказать.
  Будем очень рады обоснованиям принятых архитектурных решений. Комментарии в коде к ним крайне приветствуются.
  
------
#### Что получилось:
* Есть три сервиса MessagingStub - сервис с заглушками, MessagingRabbitmq и MessagingActivemq - использующие шину и 
отправку email пользователям.

Есть соответствующие этим сервисам роуты, регистрирующие пользователя 
`POST-запрос, например: {"name": "User Userovich","login":"UserUserovich","email":"oops@yandex.ru","password":"password"}`

* В контролере получаем параметры пользователя, при помощи userDto создаём пользователя, сохраняем в БД, отправляем 
данные пользователя на валидацию. Валидацию производит сервер (registration-server) на котором есть два лисенера 
(для rabbitmq и activemq) и которые присылают ответ - успешна ли валидация данных пользователя или нет
Далее этот ответ отсылается на почту пользвателю, если валидация не прошла по техническим причинам (получили ошибку от шины)
 - об этом также оповещается пользователь 

Если сообщение на email не отправилось, то принимается попытка ещё раз отправить (лучше реализовать это поведение, как и отправку 
на почту через брокер сообщений)

* Сервис c Activemq использует в явном виде CorrelationId, Rabbitmq нет
* Есть тесты для RegistrationService и MessagingService

* Для поднятия Activemq и rabbitmq используются контейнеры (docker-compose в registration_server), команда `docker-compose up`
* Для работы сервисов нужен запущенный registration_server, к сожалению при помощи docker-compose не получилось сделать запуск,
поэтому, чтобы запустить registration_server `./gradlew bootRun`
 
 * Работа с БД происходит через UserRepository
 * Пользователю высылается сообщение о статусе валидации регистрации 
 (в application.properties стоят заглушки spring.mail.username=fake-email, spring.mail.password=fake-password)
 * Есть логгирование 
 * Есть работа с исключениями
 
 #### Что не получилось:
 * Не понятно, насколько сделано то, что надо
 * Не получилось сделать запуск сервера через docker
 * Больше тестов
 
#### Затрачено времени:
Три дня (среда, четверг, пятница)