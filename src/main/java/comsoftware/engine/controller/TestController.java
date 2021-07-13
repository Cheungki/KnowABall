package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.repository.PlayerRepository;
import comsoftware.engine.service.NewsService;
import comsoftware.engine.service.PlayerService;
import comsoftware.engine.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 用于开发早期进行测试的类
 */
@CrossOrigin
@RestController
public class TestController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private NewsService newsService;

    static int MAX_RECORD = 10;

    @RequestMapping(value = "/test/hello", method = RequestMethod.GET)
    public String helloSearchEngine() {
        return "Hello World";
    }

    @RequestMapping(value = "/test/search", method = RequestMethod.GET)
    public Iterable<Player> search() {
        return playerRepository.findAll();
    }

    @RequestMapping(value = "/test/find/{name}/{page}", method = RequestMethod.GET)
    public Page<Player> query(@PathVariable String name, @PathVariable int page) {
        Pageable pageable = PageRequest.of(page, MAX_RECORD);
        return playerRepository.findNameFuzzy(name, pageable);
    }

    @RequestMapping(value = "/test/club/{club}", method = RequestMethod.GET)
    public List<Player> findClub(@PathVariable String club) {
        return playerRepository.findByClub(club);
    }

    @RequestMapping(value = "/test/player/{id}", method = RequestMethod.GET)
    public PlayerBaseInfo getPlayer(@PathVariable int id) {
        return playerService.getPlayerBaseInfo(id);
    }

    @RequestMapping(value = "/test/player/injure/{id}", method = RequestMethod.GET)
    public List<PlayerInjuredData> getPlayerInjuredData(@PathVariable int id) {
        return playerService.getPlayerInjuredData(id);
    }

    @RequestMapping(value = "/test/player/match/{id}", method = RequestMethod.GET)
    public List<PlayerMatchData> getPlayerMatchData(@PathVariable int id) {
        return playerService.getPlayerMatchData(id);
    }

    @RequestMapping(value = "/test/player/trans/{id}", method = RequestMethod.GET)
    public List<PlayerTransferData> getPlayerTransferData(@PathVariable int id) {
        return playerService.getPlayerTransferData(id);
    }

    @RequestMapping(value = "/test/team/{id}", method = RequestMethod.GET)
    public TeamBaseInfo getTeamBaseInfo(@PathVariable int id) {
        return teamService.getTeamBaseInfo(id);
    }

    @RequestMapping(value = "/test/team/person/{id}", method = RequestMethod.GET)
    public List<TeamRelatedPerson> getTeamPerson(@PathVariable int id) {
        return teamService.getTeamMember(id);
    }

    @RequestMapping(value = "/test/team/honor/{id}", method = RequestMethod.GET)
    public List<TeamHonorRecord> getTeamHonor(@PathVariable int id) {
        return teamService.getTeamHonor(id);
    }

    @RequestMapping(value = "/test/player/news/{id}", method = RequestMethod.GET)
    public List<PlayerNewsTitles> getPlayerHotNews(@PathVariable int id) {
        return newsService.getPlayerHotNews(id);
    }

    @RequestMapping(value = "/test/player/kg/{id}", method = RequestMethod.GET)
    public List<Triple> getPlayerKnowledgeGraph(@PathVariable int id) {
        return playerService.getPlayerKnowledgeGraph(id);
    }

    @RequestMapping(value = "/test/news/{id}", method = RequestMethod.GET)
    public News getNewsById(@PathVariable int id) {
        return newsService.getNewsById(id);
    }
}
