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
# Чтобы открыть графический интерфейс GraphQL нужно перейти (при запущенном приложении) по ссылке http://localhost:8081/graphiql (хост и порт свои указываем)
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

## Гайд по запуску Kafka на Windows (официальный сайт: https://kafka.apache.org/downloads)

* Открыть папку с `Kafka`, далее `config` -> `server.properties` -> ищем строку `log.dirs=tmp/kafka-logs` и меняем её на `log.dirs=C:/Kafka/tmp/kafka-logs 1?`;
* По аналогии также сделать в файле `zookeeper.properties` в строке `dataDir`. Пример: `dataDir=c:/Kafka/zookeeper-data`;
* Открыть `CMD` от имени администратора;
* Перейти в папку 2? с `Kafka` и ввести команду `.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`;
* Открыть еще раз `CMD` от имени администратора, также перейти в папку с Kafka и ввести команду `.\bin\windows\kafka-server-start.bat .\config\server.properties`;
* Kafka запущена!

Сноски:

1? - `C:/Kafka` - это МОЙ путь, где лежит `Kafka`;

2? - Чтобы в `CMD` перейти в какую-то папку надо использовать команду `cd C:\Ваша директория`.