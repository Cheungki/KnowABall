package comsoftware.engine.service;

import comsoftware.engine.entity.News;
import comsoftware.engine.entity.Pair;
import comsoftware.engine.entity.SearchInfo;
import comsoftware.engine.mapper.NewsMapper;
import comsoftware.engine.repository.NewsRepository;
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
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NewsService {
    @Autowired
    NewsMapper newsMapper;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private RestHighLevelClient client;

    public List<Map<String,Object>> complexNewsSearch(String keyword, boolean isUnique, int page, int size, SearchInfo si, int bias) throws IOException {
        ArrayList<Pair> wordsList = Utils.getAllKeywords(keyword);
        SearchRequest searchRequest = new SearchRequest("news");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from((page-1)*size+bias);
        searchSourceBuilder.size(size);
        searchSourceBuilder.trackTotalHits(true);
        for(int i=0; i<wordsList.size(); i++){
            System.out.println(wordsList.get(i).getKeyword()+" : "+wordsList.get(i).getType());
            String kw = wordsList.get(i).getKeyword();
            if(wordsList.get(i).getType() == 1){
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("title", kw))
                        //.should(QueryBuilders.matchPhraseQuery("content", kw))
                        .should(QueryBuilders.matchPhraseQuery("tags", kw))
                        .should(QueryBuilders.matchPhraseQuery("author", kw));
                boolQueryBuilder.must(qb);
            }
            else if(wordsList.get(i).getType() == 2){
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("title", kw))
                        .should(QueryBuilders.matchPhraseQuery("tags", kw))
                        .should(QueryBuilders.matchPhraseQuery("author", kw));
                boolQueryBuilder.mustNot(qb);
            }
            else if(wordsList.get(i).getType() == 3){
                QueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchPhraseQuery("title", kw));
                boolQueryBuilder.must(qb);
            }
            else{
                BoolQueryBuilder qb = QueryBuilders.boolQuery()
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("title", kw).minimumShouldMatch("100%"),
                                ScoreFunctionBuilders.weightFactorFunction(1000)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("tags", kw).minimumShouldMatch("100%"),
                                ScoreFunctionBuilders.weightFactorFunction(800)))
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("author", kw).minimumShouldMatch("100%"),
                                ScoreFunctionBuilders.weightFactorFunction(300)))
                        //.should(QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("content", kw).minimumShouldMatch("100%"),
                        //        ScoreFunctionBuilders.weightFactorFunction(600)));
                        .should(QueryBuilders.functionScoreQuery(QueryBuilders.matchPhraseQuery("content", kw),
                                ScoreFunctionBuilders.weightFactorFunction(600)));
                boolQueryBuilder.should(qb);
            }
        }
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮前缀
        highlightBuilder.postTags("</span>"); // 高亮后缀
        List<HighlightBuilder.Field> fields = highlightBuilder.fields();
        fields.add(new HighlightBuilder
                .Field("title")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("tags")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("author")); // 高亮字段
        fields.add(new HighlightBuilder
                .Field("content")); // 高亮字段
        // 添加高亮查询条件到搜索源
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);

        long totals = search.getHits().getTotalHits().value;
        System.out.println("totals:"+totals);
        long pages = 0L;
        if(size != 0L){
            pages = totals / (long)size + (long)1;
        }
        si.setTotalNum(totals);  si.setPages(pages);
        //SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        //返回结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : search.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            HighlightField tags = highlightFields.get("tags");
            HighlightField author = highlightFields.get("author");
            HighlightField content = highlightFields.get("content");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            if (title != null) {
                Text[] fragments = title.fragments();
                String newTitle = "";
                for (Text fragment : fragments) {
                    newTitle += fragment;
                }
                sourceAsMap.put("title", newTitle);
            }
            if (tags != null) {
                Text[] fragments = tags.fragments();
                String newTags = "";
                for (Text fragment : fragments) {
                    newTags += fragment;
                }
                sourceAsMap.put("tags", newTags);
            }
            if (author != null) {
                Text[] fragments = author.fragments();
                String newAuthor = "";
                for (Text fragment : fragments) {
                    newAuthor += fragment;
                }
                sourceAsMap.put("author", newAuthor);
            }
            if (content != null) {
                Text[] fragments = content.fragments();
                String newContent = "";
                for (Text fragment : fragments) {
                    newContent += fragment;
                }
                sourceAsMap.put("content", newContent);
            }
            list.add(sourceAsMap);

        }
        return list;
    }

    public List<String> getSuggestCompletion(String suggestValue){
        String suggestField = "titleSuggest";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        CompletionSuggestionBuilder suggestionBuilderDistrict =
                new CompletionSuggestionBuilder(suggestField).prefix(suggestValue).size(10);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("news_title", suggestionBuilderDistrict);
        searchSourceBuilder.suggest(suggestBuilder);
        SearchRequest searchRequest = new SearchRequest("news");
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
                    suggest.getSuggestion("news_title").getEntries();
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry: entries) {
                for (Suggest.Suggestion.Entry.Option option: entry.getOptions()) {
                    String keyword = option.getText().string();
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

    public List<News> findNewsByTag(String tag) {
        return newsRepository.findByTags(tag);
    }

    public List<News> findNewsById(int id) {
        return newsRepository.findById(id);
    }

    public List<News> findNewsByContent(String content) {
        return newsRepository.findByContent(content);
    }

    public News getNewsById(int id) {
        return newsMapper.getNewsById(id);
    }

}
