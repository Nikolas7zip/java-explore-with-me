# java-explore-with-me
Repository for ExploreWithMe project.  
Here develop [pull request](https://github.com/Nikolas7zip/java-explore-with-me/pull/2)  
### Feature: Admin advanced moderation  
- GET /admin/events/pending - получить все события, ожидающие модерации;
- POST /admin/events/{eventId}/comments - добавить служебный комментарий для организатора мероприятия (указать причину отказа публикации мероприятия);
- GET /admin/events/{eventId}/comments - получить все служебные комментарии для мероприятия (между админом и организатором);
- DEL /admin/events/{eventId}/comments/{commentId} - удалить служебный комментарий к мероприятию, если он потерял свою актуальность;
- GET /users/{userId}/events/{eventId} - когда ораганизатор запрашивает собственное мероприятие, также передаются служебные комментарии с админом в поле comments;
- PUT /admin/users/{userId}/block - блокировка функций создания/редактирования мероприятий для конкретного пользователя, если он нарушил правила сайта;
- DEL /admin/users/{userId}/unlock - снять досрочно блокировку с пользователя на создание/редактирование мероприятий;
- GET /admin/users/{userId} - получить информацию о пользователе, а также данные по возможной блокировке.

