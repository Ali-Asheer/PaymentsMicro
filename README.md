## Microservices-applikation med Spring Boot och MySQL

 I have build a microservice that is one of four microservices in E-handel application. It connect to user microservice an order user microservice.
 - Payment server port is 8082
 - User server port is 8081
 - Order server port is 8083
 
REST API endpoints:
-  [POST]  - /payments <=> Creates a new payment. It needs a body like this:
```
{
    "userId": 1,
    "orderId": 2,
    "amount": 1152.00,
    "paymentStatus": "ok",
    "paymentDate": "02-07-2024 22:13:39"
}
```
-  [GET] - /payments/{id} <=> Get a payment with user and orders and Total amount by a payment ID.
-  [GET] - /payments/payments_details <=> Get all payments with users and orders and Total amount.
-  [GET] - /payments <=> Get all payments.
-  [DELETE] - /payments/{id} <=> Delete a payments by a payment ID.
-  [PUT] /payments/{id} <=> Updates a payments by a payment ID. It needs a body like this:
```
{
    "userId": 1,
    "orderId": 2,
    "amount": 1152.00,
    "paymentStatus": "ok",
    "paymentDate": "02-07-2024 22:13:39"
}
```

Made by
Ali Asheer 2025-03



