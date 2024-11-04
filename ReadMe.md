# Проект, внутри которого вы сможете найти взаимодействие разных протоколов общения между микросервисами

## Цепочка вызовов сервисов следующая:

* Клиент обращается по `HTTP (GraphQL)` в сервис `srv-client-graphql-rest`
* Запрос принимается, на основе его создается новый запрос и отправляется по `REST` в
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

## Полезные ссылки:
* https://kubernetes.io/ru/docs/tasks/tools/install-minikube/
* https://kubernetes.io/ru/docs/setup/learning-environment/minikube/#указание-драйвера-виртуальной-машины

  ## Основные команды k8s
  Получение информации о кластере:

`kubectl cluster-info` — показывает информацию о текущем кластере.

`kubectl get nodes` — выводит список узлов (nodes) в кластере.

  Работа с подами:

`kubectl get pods` — отображает список всех подов в текущем пространстве имен.

`kubectl get pods -o wide` — показывает более подробную информацию о подах.

`kubectl describe pod имя_пода` — выводит детальную информацию о конкретном поде.

`kubectl logs имя_пода` — выводит логи контейнера в указанном поде.

`kubectl exec -it имя_пода -- команда` — выполняет команду внутри работающего контейнера.

  Создание и удаление подов:

`kubectl run имя_пода --image=образ` — создает новый под с указанным образом.

`kubectl delete pod имя_пода` — удаляет указанный под.

  Работа с деплойментами:

`kubectl create deployment имя_деплоя --image=образ` — создает новый деплоймент.

`kubectl scale deployment имя_деплоя --replicas=число` — изменяет количество реплик в деплойменте.

`kubectl get deployments` — показывает список всех деплойментов.

  Работа с сервисами:

`kubectl expose deployment имя_деплоя --port=80` — создает сервис для доступа к деплойменту через указанный порт.
`kubectl get services` — отображает список всех сервисов в кластере.

  Работа с конфигурацией:

`kubectl config view` — показывает текущую конфигурацию kubectl.

`kubectl config use-context имя_контекста` — переключает контекст на указанный.

  Работа с YAML-файлами:

`kubectl apply -f файл.yaml` — применяет конфигурацию из YAML-файла (создает или обновляет ресурсы).

`kubectl delete -f файл.yaml` — удаляет ресурсы, описанные в YAML-файле.

  ### Дополнительные команды

  Просмотр всех ресурсов:

`kubectl get all --all-namespaces` — выводит все ресурсы во всех пространствах имен.

  Создание namespace:

`kubectl create namespace имя_ns` — создает новое пространство имен.

  Эти команды являются основными инструментами для работы с Kubernetes и помогут вам эффективно управлять вашими приложениями и ресурсами.

P.S. При старте приложения в кластере k8s меняется ip адрес, поэтому нужно менять его каждый раз в конфигах кубера!
Если не хотите каждый раз это делать, то стоит запускать класстер `minikube start --driver=hyperv --static-ip=192.168.200.200` вот так, указывая статичный ip-адрес.
