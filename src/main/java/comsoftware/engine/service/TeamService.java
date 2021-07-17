package comsoftware.engine.service;

import comsoftware.engine.entity.Pair;
import comsoftware.engine.entity.TeamBaseInfo;
import comsoftware.engine.entity.TeamHonorRecord;
import comsoftware.engine.entity.TeamRelatedPerson;
import comsoftware.engine.mapper.TeamMapper;
import comsoftware.engine.repository.TeamRepository;
import comsoftware.engine.utils.Utils;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;



import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    @Autowired
    TeamMapper teamMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private TeamRepository teamRepository;

    public List<TeamBaseInfo> complexTeamSearch(String keyword) {
        ArrayList<Pair> wordsList = Utils.getAllKeywords(keyword);
        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        for(int i=0; i<wordsList.size(); i++){
            String kw = wordsList.get(i).getKeyword();
            if(wordsList.get(i).getType() == 1){
                boolQueryBuilder = boolQueryBuilder
                        .must(QueryBuilders.multiMatchQuery(kw, "name").boost(3))
                        .must(QueryBuilders.multiMatchQuery(kw, "englishName").boost(3))
                        .must(QueryBuilders.multiMatchQuery(kw, "stadium").boost(2))
                        .must(QueryBuilders.multiMatchQuery(kw, "country").boost(1))
                        .must(QueryBuilders.multiMatchQuery(kw, "city").boost(2));
            }
            else if(wordsList.get(i).getType() == 2){
                boolQueryBuilder = boolQueryBuilder
                        .mustNot(QueryBuilders.multiMatchQuery(kw, "name").boost(3))
                        .must(QueryBuilders.multiMatchQuery(kw, "englishName").boost(3));
            }
            else if(wordsList.get(i).getType() == 3){
                boolQueryBuilder = boolQueryBuilder
                        .must(QueryBuilders.multiMatchQuery(kw, "name").boost(3))
                        .must(QueryBuilders.multiMatchQuery(kw, "englishName").boost(3));
            }
            else{
                boolQueryBuilder = boolQueryBuilder
                        .should(QueryBuilders.multiMatchQuery(kw, "name", "englishName").boost(3))
                        .should(QueryBuilders.multiMatchQuery(kw, "stadium").boost(2))
                        .should(QueryBuilders.multiMatchQuery(kw, "country", "city").boost(1));
            }
        }
        QueryBuilder queryBuilder = boolQueryBuilder;
        searchQuery.withQuery(queryBuilder);


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

}
