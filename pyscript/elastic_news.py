import MySQLdb
import json
from elasticsearch import helpers
from elasticsearch import Elasticsearch

# 连接数据库
db = MySQLdb.connect(
    "localhost",
    "root",
    "123456pkh",
    "dqd",
    charset='utf8'
)
es = Elasticsearch(hosts="localhost:9200")

_index_mappings = {
    "settings":{
        "number_of_shards":"1",
        "index.refresh_interval":"15s",
        "index":{
            "analysis":{
                "analyzer":{
                    "ik_pinyin_analyzer":{
                        "type":"custom",
                        "tokenizer":"ik_smart",
                        "filter":"pinyin_filter"
                    },
                    "pinyin_analyzer" : {
                        "tokenizer" : "my_pinyin",
                        "filter" : "word_delimiter"
                    }
                },
                "filter":{
                    "pinyin_filter":{
                        "type":"pinyin",
                        "keep_first_letter": False
                    }
                },
                "tokenizer" : {
                    "my_pinyin" : {
                        "type" : "pinyin",
                        "first_letter" : "none",
                        "padding_char" : " "
                    }
                }
            }
        }
    },
    "mappings": {
        "properties": {
            "id": {
                "type": "keyword"
            },
            "url": {
                "type": "keyword"
            },
            "title": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "author": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "content": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "tags": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            }
        }
        

    }
}
# 创建索引

if es.indices.exists(index="news") is not True:
    es.indices.create(index="news", body=_index_mappings)
else:
    es.indices.delete(index = "news")
    es.indices.create(index = "news", body=_index_mappings)


sql = "select * from news;"
cursor = db.cursor()
cursor.execute(sql)
data = cursor.fetchall()
db.commit()

count = 0
actions = []
for i in data:
    line = {
        "id": i[0],
        "url": i[1],
        "title": i[2],
        "author": i[3],
        "content": i[4],
        "tags": i[5],
    }
    action = {"_index": "news", '_id': count, "_source": line}
    count += 1
    actions.append(action)

success, err = helpers.bulk(es, actions, raise_on_error=True)
print(success, err)