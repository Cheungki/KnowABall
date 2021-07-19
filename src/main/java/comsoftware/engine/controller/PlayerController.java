package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.entity.returnPojo.PlayerReturn;
import comsoftware.engine.entity.returnPojo.SearchReturn;
import comsoftware.engine.repository.PlayerRepository;
import comsoftware.engine.service.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    static int MAX_RECORD = 10;

    @RequestMapping(value = "/search/player/{keyword}/{pageNum}", method = RequestMethod.GET)
    public SearchReturn ComplexPlayerSearch(@PathVariable String keyword, @PathVariable int pageNum) {
        try {
            int totalNum = 0;
            List<TotalData> dataList = new ArrayList<TotalData>();
            SearchInfo si = new SearchInfo(0L, 0L);
            List<Map<String, Object>> retList = playerService.complexPlayerSearch(keyword, true, pageNum, MAX_RECORD, si);
            for(Map<String, Object> map : retList){
                TotalData cur = new TotalData(1, null, map, null);
                dataList.add(cur);
            }
            return new SearchReturn(200, si, dataList);

        } catch(Exception e){
            e.printStackTrace();
            return new SearchReturn(400, new SearchInfo(0L, 0L), new ArrayList<TotalData>());
        }
    }

    @RequestMapping(value = "/search/suggest/player/{keyword}", method = RequestMethod.GET)
    public List<String> getPlayerSuggest(@PathVariable String keyword){
        return playerService.getSuggestCompletion(keyword);
    }
    //针对Elastic Search
    @RequestMapping(value = "/search/player/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<Player> findPlayerByName(@PathVariable String name) {
        return playerService.findPlayerByName(name);
    }

    @RequestMapping(value = "/search/player/club/{club}", method = RequestMethod.GET)
    @ResponseBody
    public List<Player> findPlayerByClub(@PathVariable String club) {
        return playerService.findPlayerByClub(club);
    }

    // 针对数据库
    @RequestMapping(value = "/player/kg/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getPlayerKnowledgeGraph(@PathVariable int id) {
        return playerService.getPlayerKnowledgeGraph(id);
    }


    @RequestMapping(value = "/player/getInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PlayerBaseInfo getPlayer(@PathVariable int id) {
        return playerService.getPlayerBaseInfo(id);
    }

    @RequestMapping(value = "/player/getInjure/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerInjuredData> getPlayerInjuredData(@PathVariable int id) {
        return playerService.getPlayerInjuredData(id);
    }

    @RequestMapping(value = "/player/getMatch/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerMatchData> getPlayerMatchData(@PathVariable int id) {
        return playerService.getPlayerMatchData(id);
    }

    @RequestMapping(value = "/player/getTrans/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerTransferData> getPlayerTransferData(@PathVariable int id) {
        return playerService.getPlayerTransferData(id);
    }

    @RequestMapping(value = "/player/getNews/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerNewsTitles> getPlayerNewsTitles(@PathVariable int id) {
        return playerService.getPlayerHotNews(id);
    }

    @RequestMapping(value = "/player/getImgURL/{id}", method = RequestMethod.GET)
    public String getPlayerImgURL(@PathVariable int id) {
        return playerService.getPlayerImgURL(id);
    }

    @RequestMapping(value = "/player/getAll/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PlayerReturn getPlayerAllInfo(@PathVariable int id) {
        String imgURL = getPlayerImgURL(id);
        PlayerBaseInfo playerBaseInfo = getPlayer(id);
        List<PlayerInjuredData> playerInjuredDataList = getPlayerInjuredData(id);
        List<PlayerTransferData> playerTransferDataList = getPlayerTransferData(id);
        List<PlayerNewsTitles> playerNewsTitlesList = getPlayerNewsTitles(id);
        return new PlayerReturn(200, imgURL, playerBaseInfo, playerInjuredDataList
                , playerTransferDataList, playerNewsTitlesList);
    }


    @RequestMapping(value = "/player/hotWord/{id}", method = RequestMethod.GET)
    public List<HotWord> getPlayerHotWord(@PathVariable int id) {
        return playerService.getPlayerHotWords(id);
    }

    @RequestMapping(value = "/player/match/{id}/{type}", method = RequestMethod.GET)
    public List<PlayerMatchData2> getPlayerMatchDataByType(@PathVariable int id, @PathVariable int type) {
        return playerService.getPlayerMatchDataByType(id, type);
    }
}
