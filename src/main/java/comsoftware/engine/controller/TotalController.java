package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.entity.returnPojo.SearchReturn;
import comsoftware.engine.service.PlayerService;
import comsoftware.engine.service.TeamService;
import comsoftware.engine.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class TotalController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private List<Recommend> defaultRe;

    static int MAX_RECORD = 10;

    @RequestMapping(value = "/search/all/{keyword}/{pageNum}", method = RequestMethod.GET)
    @ResponseBody
    public SearchReturn searchForAll(@PathVariable String keyword, @PathVariable int pageNum) {
        try {
            int type1_2_num = 0;
            List<TotalData> allDataList = new ArrayList<TotalData>();
            SearchInfo si = new SearchInfo(0L, 0L);
            List<Map<String, Object>> playerList = playerService.complexPlayerSearch(keyword, false, 1, MAX_RECORD, si,
                    -1, "all", "all", -1, -1);
            if (playerList.size() > 0) {
                int maxNum = (playerList.size() > 5) ? 5 : playerList.size();
                type1_2_num += maxNum;
                if(pageNum == 1) {
                    for (int i = 0; i < maxNum; i++) {
                        allDataList.add(new TotalData(1, null, playerList.get(i), null));
                    }
                }
            }
            System.out.println(playerList.size());
            List<Map<String, Object>> teamList = teamService.complexTeamSearch(keyword, false,1, MAX_RECORD, si, "all");
            if (teamList.size() > 0) {
                int maxNum = (teamList.size() > 5) ? 5 : teamList.size();
                type1_2_num += maxNum;
                if(pageNum == 1) {
                    for (int i = 0; i < maxNum; i++) {
                        allDataList.add(new TotalData(2, null, null, teamList.get(i)));
                    }
                }
            }
            int news_page, news_size, bias;
            if(pageNum == 1){
                news_page = 1;
                news_size = MAX_RECORD - type1_2_num;
                bias = 0;
            }
            else{
                bias = MAX_RECORD - type1_2_num;
                news_page = pageNum - 1;
                news_size = MAX_RECORD;
            }
            List<Map<String, Object>> newsList = newsService.complexNewsSearch(keyword, false, news_page, news_size, si, bias, -1);

            long totalNum = si.getTotalNum();
            totalNum += (long)type1_2_num;
            long pages = totalNum / (long) MAX_RECORD + 1L;
            si.setTotalNum((totalNum)); si.setPages(pages);

            for (int i = 0; i < newsList.size(); i++) {
                allDataList.add(new TotalData(3, newsList.get(i), null, null));
            }
            //----------添加推荐----------------------------------
            int id_1=-1, id_2=-1;
            List<Recommend> recommendList = new ArrayList<Recommend>();
            if(playerList.size()>0){
                id_1 = (Integer) (playerList.get(0).get("id"));
                recommendList = playerService.getPlayerRecommend(id_1);
            }
            if(recommendList.size()==0){
                if(teamList.size()>0){
                    id_2 = (Integer) (teamList.get(0).get("id"));
                    recommendList = teamService.getTeamRecommend(id_2);
                }
            }
            if(recommendList.size()<6){
                Collections.shuffle(defaultRe);
                for(Recommend re : defaultRe){
                    if(recommendList.size()>=6) break;
                    int flag = 0;
                    for(Recommend cur:recommendList){
                        if(re.getId()==id_1 || re.getId()==id_2 || (re.getType() == cur.getType() && re.getId()==cur.getId())){
                            flag = 1;
                            break;
                        }
                    }
                    if(flag == 0){
                        recommendList.add(re);
                    }
                }
            }
            //---------------------------------------------------
            return new SearchReturn(200, si, recommendList, allDataList);
        } catch(Exception e){
            e.printStackTrace();
            List<Recommend> recommendList = new ArrayList<Recommend>();
            Collections.shuffle(defaultRe);
            for(int i=0; i<6; i++)
                recommendList.add(defaultRe.get(i));
            return new SearchReturn(400, new SearchInfo(0L, 0L), recommendList, new ArrayList<TotalData>());

        }
    }

    @RequestMapping(value = "/autoAnswer/{question}", method = RequestMethod.GET)
    @ResponseBody
    public String AutoAnswer(@PathVariable String question){
        if(question.equals("C罗现效力于什么球队？")){
            return "尤文图斯";
        }
        if(question.equals("曼城的主教练是谁？")){
            return "瓜迪奥拉";
        }
        if(question.equals("巴塞罗那的竞争对手有哪些？")){
            return "皇家马德里 马德里竞技 瓦伦西亚";
        }
        return "主人，懂球小蜜没有找到答案~ QAQ";
    }

    @RequestMapping(value = "/getHotWords", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getHotWords(){
        HotWords wordList = (HotWords) redisTemplate.opsForValue().get("hotWords");
        if(wordList==null){
            wordList = new HotWords();
            wordList = new HotWords();
            wordList.addWord("巴塞罗那");  wordList.addWord("皇家马德里");
            wordList.addWord("C罗");    wordList.addWord("拜仁慕尼黑");
            wordList.addWord("内马尔");  wordList.addWord("姆巴佩");
            wordList.addWord("曼城");  wordList.addWord("德布劳内");
            wordList.addWord("尤文图斯");  wordList.addWord("莱万多夫斯基");
            redisTemplate.opsForValue().set("hotWords", wordList);
        }
        return wordList.getWords();
    }

    @RequestMapping(value = "/search/suggest/all/{keyword}", method = RequestMethod.GET)
    @ResponseBody
    public List<Pair> getSuggestCompletion(@PathVariable String keyword) {
        int num1 = 0, num2 = 0, num3 = 0;
        //Map<String, Integer> map = new HashMap<String, Integer>();
        List<Pair> pairList = new ArrayList<Pair>();
        List<String> playerSuggest = playerService.getSuggestCompletion(keyword);
        List<String> teamSuggest = teamService.getSuggestCompletion(keyword);
        List<String> newsSuggest = newsService.getSuggestCompletion(keyword);
        int size1 = playerSuggest.size(), size2 = teamSuggest.size(), size3 = newsSuggest.size();
        int i, j, k;
        for (i = 0; i < 4 && i < size1; i++) {
            pairList.add(new Pair(1, playerSuggest.get(i)));
        }
        for (j = 0; j < 3 && j < size2; j++) {
            pairList.add(new Pair(2, teamSuggest.get(j)));
        }
        for (k = 0; k < 3 && k < size3; k++) {
            pairList.add(new Pair(3, newsSuggest.get(k)));
        }
        while (i < size1 && pairList.size() < 10) {
            pairList.add(new Pair(1, playerSuggest.get(i)));
            i++;
        }
        while (j < size2 && pairList.size() < 10) {
            pairList.add(new Pair(2, teamSuggest.get(j)));
            j++;
        }
        while (k < size3 && pairList.size() < 10) {
            pairList.add(new Pair(3, newsSuggest.get(k)));
            k++;
        }
        List<Pair> newPairList = new ArrayList<Pair>();
        for (int s=1; s<=3; s++){
            for (Pair p:pairList) {
                if(p.getType() == s){
                    newPairList.add(p);
                }
            }
        }
        return newPairList;
    }
}

