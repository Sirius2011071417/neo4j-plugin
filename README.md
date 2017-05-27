# neo4j restful plugins extensions

## Usage
```
cd {your-path}/neo4j-community-3.2.0
bin/neo4j start

open localhost:7474
```

```
CREATE (o:Org{org_id:"00000581-4f51-65b7-ea0f-47d7ae27b420", org_name:"shanghai"})-[r:Owner{rel_events:"event", events_cnt:10}]->(o1:Org{org_id:"00000581-4f51-65b7-ea0f-47d7ae27b421", org_name:"zhejiang"})
```
```
cd neo4j-plugin
cp target/neo-0.0.1-SNAPSHOT.jar {your-path}/neo4j-community-3.2.0/plugins

cd {your-path}/neo4j-community-3.2.0
vim conf/neo4j.conf
dbms.unmanaged_extension_classes=com.zhongbiao.neo=/api
bin/neo4j restart
```

```
Response: JSON

GET localhost:7474/api/uuid/00000581-4f51-65b7-ea0f-47d7ae27b420
GET localhost:7474/api/uuid/00000581-4f51-65b7-ea0f-47d7ae27b420/?page_size=1
GET localhost:7474/api/uuid/00000581-4f51-65b7-ea0f-47d7ae27b420/?page_size=1&page=1

GET localhost:7474/api/uuid/
relations?startNode=00000581-4f51-65b7-ea0f-47d7ae27b420&endNode=00000581-4f51-65b7-ea0f-47d7ae27b421
GET localhost:7474/api/uuid/
relations?startNode=00000581-4f51-65b7-ea0f-47d7ae27b420&endNode=00000581-4f51-65b7-ea0f-47d7ae27b421&page_size=1
GET localhost:7474/api/uuid/
relations?startNode=00000581-4f51-65b7-ea0f-47d7ae27b420&endNode=00000581-4f51-65b7-ea0f-47d7ae27b421&page=1&page_size=1

POST localhost:7474/api/uuid/post/
{"query": {"start_node": "00000581-4f51-65b7-ea0f-47d7ae27b420", "end_node": "00000581-4f51-65b7-ea0f-47d7ae27b421"}}
POST localhost:7474/api/uuid/post/
{"page_size": 2, "query": {"start_node": "00000581-4f51-65b7-ea0f-47d7ae27b420", "end_node": "00000581-4f51-65b7-ea0f-47d7ae27b421"}}
POST localhost:7474/api/uuid/post/
{"page_size": 2, "page":1, "query": {"start_node": "00000581-4f51-65b7-ea0f-47d7ae27b420", "end_node": "00000581-4f51-65b7-ea0f-47d7ae27b421"}}
```
