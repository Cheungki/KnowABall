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
                        "tokenizer":"ik_max_word",
                        "filter":"pinyin_filter"
                    },
                    "pinyin_analyzer" : {
                        "tokenizer" : "my_pinyin",
                        "filter" : "word_delimiter"
                    }
                },
                "filter":{
                    "pinyin_filter":{
                        "type": "pinyin",
                        "keep_first_letter": False,
                        "keep_joined_full_pinyin": False,
                        "keep_separate_first_letter": False,
                        "none_chinese_pinyin_tokenize": True,
                        "keep_full_pinyin": True
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
            "imgURL": {
                "type": "keyword"
            },
            "url": {
                "type": "keyword"
            },
            "birthYear": {
                "type": "integer"
            },
            "country": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "city": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "stadium": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "audience": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "phone": {
                "type": "keyword"
            },
            "email": {
                "type": "keyword"
            },
            "name": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer",
                "fields": {
                    "pinyin":{
                        "type":"text",
                        "analyzer": "ik_pinyin_analyzer"
                    }
                }
            },
            "nameSuggest": {
                "type": "completion",
                "analyzer": "ik_pinyin_analyzer"
            },
            "englishName": {
                "type": "text"
            },
            "address": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            }
        }
        

    }
}
# 创建索引
#es.indices.delete(index = "team")
if es.indices.exists(index="team") is not True:
    es.indices.create(index="team", body=_index_mappings)
else:
    es.indices.delete(index = "team")
    es.indices.create(index = "team", body=_index_mappings)

sql = "select * from teamBaseInfo;"
cursor = db.cursor()
cursor.execute(sql)
data = cursor.fetchall()
db.commit()

count = 0
actions = []
for i in data:
    new_sql = "select imgURL from teamImgURL where teamId = " + str(i[0])
    print(new_sql)
    new_cursor = db.cursor()
    new_cursor.execute(new_sql)
    new_data = new_cursor.fetchall()
    db.commit()
    if(len(new_data) == 0):
        imgUrl = ["https://img1.dongqiudi.com/fastdfs3/M00/B5/98/ChOxM1xC37CADGDCAAANHjH55fo314.png"]
    else:
        imgUrl = new_data[0]
    line = {
        "id": i[0],
        "imgURL" : imgUrl[0],
        "url": i[1],
        "birthYear": i[2],
        "country": i[3],
        "city": i[4],
        "stadium": i[5],
        "audience": i[6],
        "phone": i[7],
        "email": i[8],
        "name": i[9],
        "nameSuggest": i[9],
        "englishName": i[10],
        "address": i[11]
    }
    action = {"_index": "team", '_id': count, "_source": line}
    count += 1
    actions.append(action)

success, err = helpers.bulk(es, actions, raise_on_error=True)
print(success, err)