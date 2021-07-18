package comsoftware.engine.service;

import comsoftware.engine.entity.*;
import comsoftware.engine.mapper.PlayerMapper;
import comsoftware.engine.mapper.TeamMapper;
import comsoftware.engine.repository.PlayerRepository;
import comsoftware.engine.utils.Utils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ThreeTenBackPortConverters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Service
public class PlayerService {
    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    TeamMapper teamMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RestHighLevelClient client;

    public List<Map<String,Object>> complexPlayerSearch(String keyword, boolean isUnique) throws IOException {
        ArrayList<Pair> wordsList = Utils.getAllKeywords(keyword);
        SearchRequest searchRequest = new SearchRequest("player");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(1);
        searchSourceBuilder.size(2000);
        searchSourceBuilder.trackTotalHits(true);
        for(int i=0; i<wordsList.size(); i++){
            System.out.println(wordsList.get(i).getKeyword()+" : "+wordsList.get(i).getType());
            String kw = wordsList.get(i).getKeyword();
            if(wordsList.get(i).getType() == 1){
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", kw))
                        .should(QueryBuilders.matchPhraseQuery("englishName", kw))
                        .should(QueryBuilders.matchPhraseQuery("country", kw))
                        .should(QueryBuilders.matchPhraseQuery("role", kw));
                if(isUnique){
                    qb.should(QueryBuilders.matchPhraseQuery("club", kw));
                }
                boolQueryBuilder.must(qb);
            }
            else if(wordsList.get(i).getType() == 2){
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", kw))
                        .should(QueryBuilders.matchPhraseQuery("englishName", kw))
                        .should(QueryBuilders.matchPhraseQuery("country", kw))
                        .should(QueryBuilders.matchPhraseQuery("role", kw));
                if(isUnique){
                    qb.should(QueryBuilders.matchPhraseQuery("club", kw));
                }
                boolQueryBuilder.mustNot(qb);
            }
            else if(wordsList.get(i).getType() == 3){
                QueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", kw))
                        .should(QueryBuilders.matchPhraseQuery("englishName", kw));
                boolQueryBuilder.must(qb);
            }
            else{
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("name", kw).minimumShouldMatch("100%"),
                                ScoreFunctionBuilders.weightFactorFunction(1000)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("englishName", kw).minimumShouldMatch("100%"),
                                ScoreFunctionBuilders.weightFactorFunction(1000)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("role", kw).minimumShouldMatch("100%"),
                                ScoreFunctionBuilders.weightFactorFunction(500)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("country", kw).minimumShouldMatch("100%"),
                                ScoreFunctionBuilders.weightFactorFunction(300)));
                if(isUnique){
                    qb.should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("club", kw).minimumShouldMatch("100%"),
                            ScoreFunctionBuilders.weightFactorFunction(400)));
                }
                boolQueryBuilder.should(qb);
            }
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮前缀
        highlightBuilder.postTags("</span>"); // 高亮后缀
        List<HighlightBuilder.Field> fields = highlightBuilder.fields();
        fields.add(new HighlightBuilder
                .Field("name")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("englishName")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("country")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("role")); // 高亮字段
        if(isUnique){
            fields.add(new HighlightBuilder
                    .Field("club"));
        }
        // 添加高亮查询条件到搜索源
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(search.getHits().getTotalHits());
        //SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //返回结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField name = highlightFields.get("name");
            HighlightField englishName = highlightFields.get("englishName");
            HighlightField country = highlightFields.get("country");
            HighlightField role = highlightFields.get("role");
            HighlightField club = null;
            if(isUnique){
                club = highlightFields.get("club");
            }
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            if (name != null) {
                Text[] fragments = name.fragments();
                String newName = "";
                for (Text fragment : fragments) {
                    newName += fragment;
                }
                sourceAsMap.put("name", newName);
            }
            if (englishName != null) {
                Text[] fragments = englishName.fragments();
                String newEnglishName = "";
                for (Text fragment : fragments) {
                    newEnglishName += fragment;
                }
                sourceAsMap.put("englishName", newEnglishName);
            }
            if (country != null) {
                Text[] fragments = country.fragments();
                String newCountry = "";
                for (Text fragment : fragments) {
                    newCountry += fragment;
                }
                sourceAsMap.put("country", newCountry);
            }
            if (role != null) {
                Text[] fragments = role.fragments();
                String newRole = "";
                for (Text fragment : fragments) {
                    newRole += fragment;
                }
                sourceAsMap.put("role", newRole);
            }
            if(isUnique && club!=null){
                Text[] fragments = club.fragments();
                String newClub = "";
                for (Text fragment : fragments) {
                    newClub += fragment;
                }
                sourceAsMap.put("club", newClub);
            }
            list.add(sourceAsMap);

        }
        return list;

    }

    public List<Player> findPlayerByName(String name) {
        return playerRepository.findByName(name);
    }

    public List<Player> findPlayerByClub(String club) {
        return playerRepository.findByClub(club);
    }

    public PlayerBaseInfo getPlayerBaseInfo(int id) {
        return playerMapper.getPlayerBaseInfo(id);
    }

    public List<PlayerInjuredData> getPlayerInjuredData(int id) {
        return playerMapper.getPlayerInjuredData(id);
    }

    public List<PlayerMatchData> getPlayerMatchData(int id) {
        return playerMapper.getPlayerMatchData(id);
    }

    public List<PlayerTransferData> getPlayerTransferData(int id) {
        return playerMapper.getPlayerTransferData(id);
    }

    public List<PlayerNewsTitles> getPlayerHotNews(int id) {
        return playerMapper.getPlayerHotNews(id);
    }

    public String getPlayerImgURL(int id) {
        try {
            String ret = playerMapper.getPlayerImgURL(id);
            if(ret==null || ret.equals("")){
                ret = "https://img1.dongqiudi.com/fastdfs3/M00/B5/98/ChOxM1xC37CADGDCAAANHjH55fo314.png";
            }
            return ret;
        } catch(Exception e){
            String ret = "https://img1.dongqiudi.com/fastdfs3/M00/B5/98/ChOxM1xC37CADGDCAAANHjH55fo314.png";
            return ret;
        }
    }

    /**
    public List<Triple> getPlayerKnowledgeGraph(int id) {
        PlayerBaseInfo playerBaseInfo = getPlayerBaseInfo(id);
        String head = playerBaseInfo.getName();
        int team = playerBaseInfo.getTeamId();
        String teamName = teamMapper.getTeamBaseInfo(team).getName();
        List<TeamRelatedPerson> persons = teamMapper.getTeamPerson(team);
        // 开始输入结果
        List<Triple> result = new ArrayList<>();
        result.add(new Triple(head, "现役球队", teamName));

        for (TeamRelatedPerson person: persons) {
            result.add(new Triple(head, "队友", person.getName()));
        }

        List<PlayerTransferData> transfer = playerMapper.getPlayerTransferData(id);
        for (PlayerTransferData playerTransferData: transfer) {
            result.add(new Triple(head, "老东家", playerTransferData.getInClub()));
        }

        List<PlayerInjuredData> injured = playerMapper.getPlayerInjuredData(id);
        Map<String, Boolean> injury = new HashMap<>();
        for (PlayerInjuredData playerInjuredData: injured) {
            String ill = playerInjuredData.getInjury();
            if (injury.get(ill) == null) {
                injury.put(ill, true);
                result.add(new Triple(head, "伤病", ill));
            }
        }

        List<PlayerHonorRecord> honorRecords = playerMapper.getPlayerHonorRecord(id);
        for (PlayerHonorRecord record: honorRecords) {
            result.add(new Triple(head, "曾获荣誉", record.getHonor()));
        }
        return result;

    }
     */

    public Map<String, Object> getPlayerKnowledgeGraph(int id) {
        PlayerBaseInfo playerBaseInfo = getPlayerBaseInfo(id);
        String head = playerBaseInfo.getName();
        int team = playerBaseInfo.getTeamId();
        String teamName = teamMapper.getTeamBaseInfo(team).getName();
        // 首先获取基本信息
        List<TeamRelatedPerson> persons = teamMapper.getTeamPerson(team);
        List<PlayerTransferData> transfer = playerMapper.getPlayerTransferData(id);
        List<PlayerInjuredData> injured = playerMapper.getPlayerInjuredData(id);
        List<PlayerHonorRecord> honorRecords = playerMapper.getPlayerHonorRecord(id);

        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> idMap = new HashMap<>();
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        idMap.put(head, 1);
        idMap.put("队友", 2);
        idMap.put("转会历史", 3);
        idMap.put("伤病数据", 4);
        idMap.put("荣誉", 5);
        idMap.put("所属球队", 6);
        nodes.add(new Node(1, head, "self"));
        nodes.add(new Node(2, "队友", "relation"));
        nodes.add(new Node(3, "转会历史", "relation"));
        nodes.add(new Node(4, "伤病数据", "relation"));
        nodes.add(new Node(5, "荣誉", "relation"));
        nodes.add(new Node(6, "所属球队", "relation"));
        edges.add(new Edge(1,2));
        edges.add(new Edge(1,3));
        edges.add(new Edge(1,4));
        edges.add(new Edge(1,5));
        edges.add(new Edge(1,6));
        int count = 7;

        for (TeamRelatedPerson person: persons) {
            nodes.add(new Node(count, person.getName(), "teammate"));
            edges.add(new Edge(2, count));
            count += 1;
        }
        for (PlayerTransferData transferData: transfer) {
            nodes.add(new Node(count, transferData.getOutClub(), "oldTeam"));
            edges.add(new Edge(3, count));
            count += 1;
        }
        for (PlayerInjuredData injuredData: injured) {
            nodes.add(new Node(count, injuredData.getPeriod().substring(0, 8) + injuredData.getInjury(), "injure"));
            edges.add(new Edge(4, count));
            count += 1;
        }
        for (PlayerHonorRecord honorRecord: honorRecords) {
            nodes.add(new Node(count, honorRecord.getHonor(), "honor"));
            edges.add(new Edge(5, count));
            count += 1;
        }
        nodes.add(new Node(count, teamName, "team"));
        edges.add(new Edge(6, count));
        result.put("nodes", nodes);
        result.put("edges", edges);
        return result;
    }

    public List<HotWord> getPlayerHotWords(int id) {
        List<HotWord> result = playerMapper.getPlayerHotWords(id);
        List<String> tags = getPlayerTags(id);
        for (String tag: tags) {
            result.add(new HotWord(tag, 30));
        }
        return result;
    }

    public List<String> getPlayerTags(int id) {
        PlayerTags playerTags = playerMapper.getPlayerTag(id);
        System.out.println(playerTags.getTag());
        return playerTags.getPlayerTags();
    }

}
