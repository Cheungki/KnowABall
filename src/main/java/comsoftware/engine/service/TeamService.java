package comsoftware.engine.service;

import comsoftware.engine.entity.*;
import comsoftware.engine.mapper.TeamMapper;
import comsoftware.engine.repository.TeamRepository;
import comsoftware.engine.utils.Utils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.*;

@Service
public class TeamService {
    @Autowired
    TeamMapper teamMapper;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RestHighLevelClient client;

    public List<Map<String,Object>> complexTeamSearch(String keyword, boolean isUnique, int page, int size, SearchInfo si, String _country) throws IOException {
        ArrayList<Pair> wordsList = Utils.getAllKeywords(keyword);
        //NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        SearchRequest searchRequest = new SearchRequest("team");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder newBoolQueryBuilder = null;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);
        for(int i=0; i<wordsList.size(); i++){
            System.out.println(wordsList.get(i).getKeyword()+" : "+wordsList.get(i).getType());
            String kw = wordsList.get(i).getKeyword();
            if(wordsList.get(i).getType() == 1){

                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", kw))
                        .should(QueryBuilders.matchPhraseQuery("englishName", kw))
                        .should(QueryBuilders.matchPhraseQuery("stadium", kw))
                        .should(QueryBuilders.matchPhraseQuery("country", kw))
                        .should(QueryBuilders.matchPhraseQuery("city", kw));
                if(isUnique){
                    qb.should(QueryBuilders.matchPhraseQuery("audience", kw));
                }
                boolQueryBuilder.must(qb);

                //boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("name", kw));
            }
            else if(wordsList.get(i).getType() == 2){
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", kw))
                        .should(QueryBuilders.matchPhraseQuery("englishName", kw))
                        .should(QueryBuilders.matchPhraseQuery("stadium", kw))
                        .should(QueryBuilders.matchPhraseQuery("audience", kw))
                        .should(QueryBuilders.matchPhraseQuery("country", kw))
                        .should(QueryBuilders.matchPhraseQuery("city", kw));
                if(isUnique){
                    qb.should(QueryBuilders.matchPhraseQuery("audience", kw));
                }
                boolQueryBuilder.mustNot(qb);
            }
            else if(wordsList.get(i).getType() == 3){
                QueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("name", kw))
                        .should(QueryBuilders.matchPhraseQuery("englishName", kw));
                boolQueryBuilder.must(qb);
            }
            else if(wordsList.get(i).getType() == 4){
                if(newBoolQueryBuilder==null)   newBoolQueryBuilder = QueryBuilders.boolQuery();
                BoolQueryBuilder qb = null;
                qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("name", kw).minimumShouldMatch("10%"),
                                ScoreFunctionBuilders.weightFactorFunction(1000)))
                        //.should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("name.fields.pinyin", kw).minimumShouldMatch("10%"),
                        //        ScoreFunctionBuilders.weightFactorFunction(300)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("englishName", kw).minimumShouldMatch("80%"),
                                ScoreFunctionBuilders.weightFactorFunction(1000)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("stadium", kw).minimumShouldMatch("80%"),
                                ScoreFunctionBuilders.weightFactorFunction(500)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("country", kw).minimumShouldMatch("80%"),
                                ScoreFunctionBuilders.weightFactorFunction(300)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("city", kw).minimumShouldMatch("80%"),
                                ScoreFunctionBuilders.weightFactorFunction(300)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("audience", kw).minimumShouldMatch("80%"),
                                ScoreFunctionBuilders.weightFactorFunction(250)));
                newBoolQueryBuilder.should(qb);
            }
            else{
                if(newBoolQueryBuilder==null)   newBoolQueryBuilder = QueryBuilders.boolQuery();
                BoolQueryBuilder qb = null;
                if(isUnique){
                    qb = QueryBuilders.boolQuery()
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("name", kw).minimumShouldMatch("70%"),
                                    ScoreFunctionBuilders.weightFactorFunction(1000)))
                            //.should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("name.fields.pinyin", kw).minimumShouldMatch("70%"),
                            //        ScoreFunctionBuilders.weightFactorFunction(1000)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("englishName", kw).minimumShouldMatch("70%"),
                                    ScoreFunctionBuilders.weightFactorFunction(1000)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("stadium", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(500)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("country", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(300)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("city", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(300)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("audience", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(250)));
                }
                else{
                    /*
                    qb = QueryBuilders.boolQuery()
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("name", kw),
                                    ScoreFunctionBuilders.weightFactorFunction(1000)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("name.fields.pinyin", kw),
                                    ScoreFunctionBuilders.weightFactorFunction(200)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("englishName", kw),
                                    ScoreFunctionBuilders.weightFactorFunction(1000)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("stadium", kw),
                                    ScoreFunctionBuilders.weightFactorFunction(500)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("country", kw),
                                    ScoreFunctionBuilders.weightFactorFunction(300)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("city", kw),
                                    ScoreFunctionBuilders.weightFactorFunction(300)));
                     */
                    qb = QueryBuilders.boolQuery()
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("name", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(1000)))
                            //.should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("name.fields.pinyin", kw).minimumShouldMatch("100%"),
                            //        ScoreFunctionBuilders.weightFactorFunction(300)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("englishName", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(1000)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("stadium", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(500)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("country", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(300)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("city", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(300)))
                            .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("audience", kw).minimumShouldMatch("100%"),
                                    ScoreFunctionBuilders.weightFactorFunction(250)));

                }
                newBoolQueryBuilder.should(qb);
            }
        }
        if(newBoolQueryBuilder!=null) boolQueryBuilder.must(newBoolQueryBuilder);
        //BoolQueryBuilder bqb = boolQueryBuilder;
        //if(bqb!=null) boolQueryBuilder.must(bqb);
        if(!_country.equals("all")){
            boolQueryBuilder.filter(QueryBuilders.matchPhraseQuery("country", _country));
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
                .Field("stadium")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("country")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("city")); // 高亮字段
        if(isUnique){
            fields.add(new HighlightBuilder
                    .Field("audience"));
        }
        // 添加高亮查询条件到搜索源
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        long totals = search.getHits().getTotalHits().value;
        long pages = totals / (long)size + (long)1;
        si.setTotalNum(totals);  si.setPages(pages);
        //SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //返回结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField name = highlightFields.get("name");
            HighlightField englishName = highlightFields.get("englishName");
            HighlightField stadium = highlightFields.get("stadium");
            HighlightField country = highlightFields.get("country");
            HighlightField city = highlightFields.get("city");
            HighlightField audience = null;
            if(isUnique){
                audience = highlightFields.get("audience");
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
            if (stadium != null) {
                Text[] fragments = stadium.fragments();
                String newStadium = "";
                for (Text fragment : fragments) {
                    newStadium += fragment;
                }
                sourceAsMap.put("stadium", newStadium);
            }
            if (country != null) {
                Text[] fragments = country.fragments();
                String newCountry = "";
                for (Text fragment : fragments) {
                    newCountry += fragment;
                }
                sourceAsMap.put("country", newCountry);
            }
            if (city != null) {
                Text[] fragments = city.fragments();
                String newCity = "";
                for (Text fragment : fragments) {
                    newCity += fragment;
                }
                sourceAsMap.put("city", newCity);
            }
            if(isUnique && audience!=null){
                Text[] fragments = audience.fragments();
                String newAudience = "";
                for (Text fragment : fragments) {
                    newAudience += fragment;
                }
                sourceAsMap.put("audience", newAudience);
            }
            list.add(sourceAsMap);

        }
        return list;
    }

    public List<String> getSuggestCompletion(String suggestValue){
        String suggestField = "nameSuggest";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        CompletionSuggestionBuilder suggestionBuilderDistrict =
                new CompletionSuggestionBuilder(suggestField).prefix(suggestValue).size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("team_name", suggestionBuilderDistrict);
        searchSourceBuilder.suggest(suggestBuilder);
        SearchRequest searchRequest = new SearchRequest("team");
        //searchRequest.types(esType);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Suggest suggest = response.getSuggest();
        List<String> keywords = new ArrayList<String>();
        if (suggest != null) {
            List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries =
                    suggest.getSuggestion("team_name").getEntries();
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry: entries) {
                for (Suggest.Suggestion.Entry.Option option: entry.getOptions()) {
                    String keyword = option.getText().string();
                    keyword.replace("-", "");
                    if (!StringUtils.isEmpty(keyword)) {
                        if (keywords.contains(keyword)) {
                            continue;
                        }
                        keywords.add(keyword);
                        if (keywords.size() >= 10) {
                            break;
                        }
                    }
                }

            }
        }
        return keywords;
    }

    public List<Recommend> getTeamRecommend(int id){
        List<Recommend> recommendList = new ArrayList<Recommend>();
        List<TeamRelatedPerson> persons = teamMapper.getTeamPerson(id);
        for(TeamRelatedPerson person:persons){
            String pName = person.getName();
            List<Player> players = playerService.findPlayerByName(pName);
            if(players.size()>0 && recommendList.size()<6){
                Player p = players.get(0);
                if(p.getId() != id)
                    recommendList.add(new Recommend(1, p.getId(), p.getName(), p.getImgURL()));
            }
        }
        Collections.shuffle(recommendList);
        return recommendList;
    }

    public List<TeamBaseInfo> findTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    public List<TeamBaseInfo> findTeamByNameLike(String name) {
        return teamRepository.findByNameLike(name);
    }

    public List<TeamBaseInfo> findTeamById(int id) {
        return teamRepository.findById(id);
    }

    public String getTeamImgURL(int id){
        try {
            String ret = teamMapper.getTeamImgURL(id);
            if(ret==null || ret.equals("")){
                ret = "https://img1.dongqiudi.com/fastdfs3/M00/B5/98/ChOxM1xC37CADGDCAAANHjH55fo314.png";
            }
            return ret;
        } catch(Exception e){
            String ret = "https://img1.dongqiudi.com/fastdfs3/M00/B5/98/ChOxM1xC37CADGDCAAANHjH55fo314.png";
            return ret;
        }
    }

    public TeamBaseInfo getTeamBaseInfo(int id) {
        return teamMapper.getTeamBaseInfo(id);
    }

    public List<TeamHonorRecord> getTeamHonor(int id) {
        return teamMapper.getTeamHonorRecord(id);
    }

    public List<TeamRelatedPerson> getTeamMember(int id) {
        return teamMapper.getTeamPerson(id);
    }

    public Map<String, Object> getTeamKnowledgeGraph(int id) {
        TeamBaseInfo team = getTeamBaseInfo(id);
        String name = team.getName();

        List<TeamRelatedPerson> teamRelatedPeople = teamMapper.getTeamPerson(id);
        List<TeamHonorRecord> teamHonorRecords = teamMapper.getTeamHonorRecord(id);
        List<String> teamsFromSameCountry = teamMapper.getTeamByCountry(id, team.getCountry());
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        nodes.add(new Node(1, name, "self"));
        nodes.add(new Node(2, "球队成员", "relation"));
        nodes.add(new Node(3, "球队荣誉", "relation"));
        nodes.add(new Node(4, "竞争对手", "relation"));
        edges.add(new Edge(1, 2));
        edges.add(new Edge(1, 3));
        edges.add(new Edge(1, 4));
        int count = 5;
        for (TeamRelatedPerson teamRelatedPerson: teamRelatedPeople) {
            nodes.add(new Node(count, teamRelatedPerson.getName(), teamRelatedPerson.getRole()));
            edges.add(new Edge(2, count));
            count += 1;
        }

        for (TeamHonorRecord teamHonorRecord: teamHonorRecords) {
            nodes.add(new Node(count, teamHonorRecord.getHonor(), "honor"));
            edges.add(new Edge(3, count));
            count += 1;
        }

        for (String teamFromSameCountry: teamsFromSameCountry) {
            nodes.add(new Node(count, teamFromSameCountry, "rival"));
            edges.add(new Edge(4, count));
            count += 1;
        }
        result.put("nodes", nodes);
        result.put("edges", edges);
        return result;
    }

    public List<TeamNews> getTeamNews(int id) {
        TeamNews teamNews = teamMapper.getTeamNews(id);
        String[] titles = teamNews.getTitles().split("&&");
        String[] urls = teamNews.getUrls().split("&&");
        String[] imgs = teamNews.getImg_urls().split("&&");
        List<TeamNews> result = new ArrayList<>();
        int num = Math.min(10, titles.length);
        for (int i = 0; i < num; i ++) {
            result.add(new TeamNews(titles[i], urls[i], imgs[i]));
        }
        return result;
    }

}
