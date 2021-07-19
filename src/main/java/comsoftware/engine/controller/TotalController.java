package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.entity.returnPojo.SearchReturn;
import comsoftware.engine.service.PlayerService;
import comsoftware.engine.service.TeamService;
import comsoftware.engine.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class TotalController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private NewsService newsService;

    static int MAX_RECORD = 10;

    @RequestMapping(value = "/search/all/{keyword}/{pageNum}", method = RequestMethod.GET)
    @ResponseBody
    public SearchReturn searchForAll(@PathVariable String keyword, @PathVariable int pageNum) {
        try {
            int type1_2_num = 0;
            List<TotalData> allDataList = new ArrayList<TotalData>();
            SearchInfo si = new SearchInfo(0L, 0L);
            List<Map<String, Object>> playerList = playerService.complexPlayerSearch(keyword, false, 1, MAX_RECORD, si);
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
            List<Map<String, Object>> teamList = teamService.complexTeamSearch(keyword, false,1, MAX_RECORD, si);
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
            List<Map<String, Object>> newsList = newsService.complexNewsSearch(keyword, false, news_page, news_size, si, bias);

            long totalNum = si.getTotalNum();
            totalNum += (long)type1_2_num;
            long pages = totalNum / (long) MAX_RECORD + 1L;
            si.setTotalNum((totalNum)); si.setPages(pages);

            for (int i = 0; i < newsList.size(); i++) {
                allDataList.add(new TotalData(3, newsList.get(i), null, null));
            }
            /*
            System.out.println("total: "+allDataList.size());
            List<TotalData> dataList = new ArrayList<TotalData>();
            int totalNum = allDataList.size();
            int pages = totalNum / MAX_RECORD;
            if(pageNum <= pages){
                int start = (pageNum-1)*MAX_RECORD;
                for(int i=0; i<MAX_RECORD && start+i<totalNum; i++) {
                    dataList.add(allDataList.get(start+i));
                }
            }

             */
            return new SearchReturn(200, si, allDataList);
        } catch(Exception e){
            e.printStackTrace();
            return new SearchReturn(400, new SearchInfo(0L, 0L), new ArrayList<TotalData>());

        }
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

