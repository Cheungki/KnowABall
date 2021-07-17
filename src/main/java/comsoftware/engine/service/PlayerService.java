package comsoftware.engine.service;

import comsoftware.engine.entity.*;
import comsoftware.engine.mapper.PlayerMapper;
import comsoftware.engine.mapper.TeamMapper;
import comsoftware.engine.repository.PlayerRepository;
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
        return result;

    }

    public List<HotWord> getPlayerHotWords(int id) {
        return playerMapper.getPlayerHotWords(id);
    }

}
