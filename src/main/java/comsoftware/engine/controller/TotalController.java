package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.service.PlayerService;
import comsoftware.engine.service.TeamService;
import comsoftware.engine.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class TotalController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/search/all/{name}", method = RequestMethod.GET)
    @ResponseBody
    private List<TotalData> searchForAll(@PathVariable String name){
        List<TotalData> dataList = new ArrayList<TotalData>();
        List<Player> playerList = playerService.findPlayerByName(name);
        if (playerList.size()>0){
            int maxNum = (playerList.size() > 5) ? 5 : playerList.size();
            for (int i=0; i<maxNum; i++){
                dataList.add(new TotalData(1, null, playerList.get(i), null));
            }
        }
        System.out.println(playerList.size());
        List<TeamBaseInfo> teamList = teamService.findTeamByName(name);
        if (teamList.size()>0){
            int maxNum = (teamList.size() > 5) ? 5 : teamList.size();
            for (int i=0; i<maxNum; i++){
                dataList.add(new TotalData(2, null, null, teamList.get(i)));
            }
        }
        System.out.println(teamList.size());
        List<News> newsList = newsService.findNewsByContent(name);
        for (int i=0; i < newsList.size(); i++){
            dataList.add(new TotalData(3, newsList.get(i), null, null));
        }
        return dataList;
    }
}
