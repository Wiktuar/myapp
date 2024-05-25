Миша, добрый! Я использую не docker-compose, а обычный docker.
Команда на запуск БД
docker run -d -p 5432:5432 --name postdb -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=myTestDB postgres:latest

C:\Users\wiktu>docker ps -a
CONTAINER ID   IMAGE             COMMAND                  CREATED          STATUS          PORTS                    NAMES
fb83e65e7256   postgres:latest   "docker-entrypoint.s…"   14 seconds ago   Up 13 seconds   0.0.0.0:5432->5432/tcp   postdb

Приложение с этими настройками подключается к запущенному контейнеру совершенно штатно. Все работает

Контейнер находится в сети. Все норм
C:\Users\wiktu>docker network inspect bridge
[
    {
        "Name": "bridge",
        "Id": "ecbb1d9e78ecef9d5381b823f4ce6a5e0cdab11ae566180a7e8f05f96134171d",
        "Created": "2024-05-25T08:47:33.3046367Z",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": null,
            "Config": [
                {
                    "Subnet": "172.17.0.0/16",
                    "Gateway": "172.17.0.1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {
            "fb83e65e7256dcfe34c792a6379e16aa697020b18d0d225815fcd83b35759a4e": {
                "Name": "postdb",
                "EndpointID": "285dd05da65dd8613c938fcc500c0bc2d9c591286cefb79ccbcc5f3f0a186662",
                "MacAddress": "02:42:ac:11:00:02",
                "IPv4Address": "172.17.0.2/16",
                "IPv6Address": ""
            }
        },
        "Options": {
            "com.docker.network.bridge.default_bridge": "true",
            "com.docker.network.bridge.enable_icc": "true",
            "com.docker.network.bridge.enable_ip_masquerade": "true",
            "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
            "com.docker.network.bridge.name": "docker0",
            "com.docker.network.driver.mtu": "1500"
        },
        "Labels": {}
    }
]

Теперь я меняю localhost на имя моего контейнера postgres, то есть postdb

У меня получается spring.datasource.url=jdbc:postgresql://postdb:5432/myTestDB
После этого я делаю mvn clean package и собираю проект.
Далее создаю образ
docker build -t my-app:0.0.1 .

Образ создается штатно
 => [3/3] COPY target/demoDocker-1.0-SNAPSHOT.jar app.jar                              2.4s
 => exporting to image                                                                 3.3s
 => => exporting layers                                                                3.2s
 => => writing image sha256:091d534bdb6a97d1ef29fa65a5715827b64791eb5c47f4212bb5a0070  0.0s
 => => naming to docker.io/library/my-app:0.0.1

 Далее запускаем контейнер docker run -d -p 8090:8080 my-app:0.0.1
 И получаю я вот это

 java.net.UnknownHostException: postdb
         at java.base/java.net.AbstractPlainSocketImpl.connect(Unknown Source) ~[na:na]
         at java.base/java.net.SocksSocketImpl.connect(Unknown Source) ~[na:na]
         at java.base/java.net.Socket.connect(Unknown Source) ~[na:na]
         at org.postgresql.core.PGStream.createSocket(PGStream.java:231) ~[postgresql-42.2.23.jar!/:42.2.23]
         at org.postgresql.core.PGStream.<init>(PGStream.java:95) ~[postgresql-42.2.23.jar!/:42.2.23]
         at org.postgresql.core.v3.ConnectionFactoryImpl.tryConnect(ConnectionFactoryImpl.java:98) ~[postgresql-42.2.23.jar!/:42.2.23]

Причем контейнеры в сети есть и они работают

[
    {
        "Name": "bridge",
        "Id": "ecbb1d9e78ecef9d5381b823f4ce6a5e0cdab11ae566180a7e8f05f96134171d",
        "Created": "2024-05-25T08:47:33.3046367Z",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": null,
            "Config": [
                {
                    "Subnet": "172.17.0.0/16",
                    "Gateway": "172.17.0.1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {
            "65ef99fb250151dd1c536d7cd9f9677dba87fe02bcdc121f5d8eecd6706a0d4e": {
                "Name": "brave_wing",
                "EndpointID": "fb64c42928b2770b187afb90ebf4b02fc24d62502d7c4e02c317e375091102b9",
                "MacAddress": "02:42:ac:11:00:03",
                "IPv4Address": "172.17.0.3/16",
                "IPv6Address": ""
            },
            "fb83e65e7256dcfe34c792a6379e16aa697020b18d0d225815fcd83b35759a4e": {
                "Name": "postdb",
                "EndpointID": "285dd05da65dd8613c938fcc500c0bc2d9c591286cefb79ccbcc5f3f0a186662",
                "MacAddress": "02:42:ac:11:00:02",
                "IPv4Address": "172.17.0.2/16",
                "IPv6Address": ""
            }
        },
        "Options": {
            "com.docker.network.bridge.default_bridge": "true",
            "com.docker.network.bridge.enable_icc": "true",
            "com.docker.network.bridge.enable_ip_masquerade": "true",
            "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
            "com.docker.network.bridge.name": "docker0",
            "com.docker.network.driver.mtu": "1500"
        },
        "Labels": {}
    }
]