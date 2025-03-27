## Microservices-applikation med Spring Boot och MySQL

 I have build an microservice that is one of four microservices in E-handel application. It connect to user microservices.
 
REST API endpoints:
-  [POST]  - /payments <=> Creates a new advertised channel. It needs a body like this: Retrieves a list of advertised channels.
```
{
    "userId": 2,
    "amount": 55990.00,
    "paymentStatus": "Success",
    "paymentDate": "02-07-2025 22:13:39"
}
```
-  [GET] - /payments/{id} <=> Get a payment with user by a payments ID.
-  [GET] - /payments/payment-and-users <=> Get all payments with users.
-  [GET] - /payments <=> Get all payments.
-  [DELETE] - /payments/{id} <=> Delete a payments by a channel ID.
-  [PUT] /payments/{id} <=> Updates a payments by a payments ID. It needs a body like this:
```
{
    "userId": 2,
    "amount": 55990.00,
    "paymentStatus": "Success",
    "paymentDate": "02-07-2025 22:13:39"
}
```



