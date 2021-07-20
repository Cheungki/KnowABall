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
            "teamId": {
                "type": "keyword"
            },
            "url": {
                "type": "keyword"
            },
            "club": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "country": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "role": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
            },
            "height": {
                "type": "integer"
            },
            "weight": {
                "type": "integer"
            },
            "age": {
                "type": "integer"
            },
            "number": {
                "type": "integer"
            },
            "birthday": {
                "type": "date"
            },
            "foot": {
                "type": "text",
                "analyzer": "ik_pinyin_analyzer"
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
            "nameSuggest":{
                "type": "completion",
                "analyzer": "ik_pinyin_analyzer"
            },
            "englishName": {
                "type": "text"
            },
            "engSuggest":{
                "type": "completion"
            },
            "capablity": {
                "type": "integer"
            }
        }
    }
}
# 创建索引
if es.indices.exists(index="player") is not True:
    es.indices.create(index="player", body=_index_mappings)

else:
    es.indices.delete(index = "player")
    es.indices.create(index = "player", body=_index_mappings)


# 将数据导入索引
sql = "select * from PlayerBaseInfo;"
cursor = db.cursor()
cursor.execute(sql)
data = cursor.fetchall()
db.commit()

count = 0
actions = []
for i in data:
    new_sql = "select imgURL from playerImgURL where playerId = " + str(i[0]);
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
        "imgURL": imgUrl[0],
        "teamId": i[1],
        "url": i[2],
        "club": i[3],
        "country": i[4],
        "role": i[5],
        "height": i[6],
        "weight": i[7],
        "age": i[8],
        "number": i[9],
        "birthday": i[10],
        "foot": i[11],
        "name": i[12],
        "nameSuggest": i[12],
        "englishName": i[13],
        "engSuggest": i[13],
        "capablity": i[14]
    }
    action = {"_index": "player", '_id': count, "_source": line}
    count += 1
    actions.append(action)

success, err = helpers.bulk(es, actions, raise_on_error=True)
print(success, err)
