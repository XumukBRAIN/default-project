# Проект, внутри которого вы сможете найти взаимодействие разных протоколов общения между микросервисами

## Цепочка вызовов сервисов следующая:

* Клиент обращается по `HTTP (GraphQL)` в сервис `srv-client-graphql-rest`
* Запрос принимается, на основе его создается новый запрос в формате `proto` и отправляется по `REST` в
  сервис `srv-back-rest-grpc`
* Запрос принимается, конвертируется в `protobuf` и отправется по `gRPC` в сервис `srv-back-grpc-kafka`
* Запрос принимается, конвертируется в `DTO`, которго в свою очередь ожидает сервис `srv-back-kafka`
* P.S. Можно было бы и `SOAP` прикрутить, но посчитал это неактуальной технологией. Плюс `SOAP` можно также
  назвать `RPC` технологией, как и `gRPC`

## Упрощенная схема взаимодействия микросервисов (только протоколы):

    `GraphQL -> REST -> gRPC -> Kafka`

## Описание запросов, которые приходят на каждый сервис:

1. `srv-client-graphql-rest`:

```graphql
type Item {
    id: ID!
    title: String!
    description: String
    price: Float!
}

type Query {
    getItemById(id: ID!): Item
}

# Пример самого запроса
# query {
#   getItemById(id: 1) {
#     title
#     price
#     description
#   }
# }
```

2. `srv-back-rest-grpc`:

```http request
POST http://localhost:8082/api/getItemById
Content-Type: application/json
reqId: UUID

{
  "itemId": "Intger",
  "reqId": "UUID",
  "reqTm": "LocalDateTime",
  "fromSystemName": "String"
}
```

3. `srv-back-grpc-kafka`:

```protobuf
syntax = "proto3";
package com.dev.grpc.item;

import "google/protobuf/wrappers.proto";

service ItemService {
  rpc getAllItems(GetItemByIdRq) returns (GetItemByIdRs);
}

message GetItemByIdRq {
  int32 itemId = 1;
  string reqId = 2;
  string reqTm = 3;
  string fromSystemName = 4;
}

message GetItemByIdRs {
  Item item = 1;
}

message Item {
  string title = 1;
  string description = 2;
  double price = 3;
}
```

4. `srv-back-kafka`:

```json
{
  "itemId": "Integer",
  "reqId": "UUID",
  "reqTm": "LocalDateTime",
  "fromSystemName": "String"
}
```