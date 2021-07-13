package comsoftware.engine.service;

import comsoftware.engine.entity.*;
import comsoftware.engine.mapper.PlayerMapper;
import comsoftware.engine.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Service
public class PlayerService {
    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    TeamMapper teamMapper;

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

    public List<Triple> getPlayerKnowledgeGraph(int id) {
        PlayerBaseInfo playerBaseInfo = getPlayerBaseInfo(id);
        String head = playerBaseInfo.getName();
        int team = playerBaseInfo.getTeamId();
        String teamName = teamMapper.getTeamBaseInfo(team).getName();
        List<TeamRelatedPerson> persons = teamMapper.getTeamPerson(team);
        // 开始输入结果
        List<Triple> result = new ArrayList<>();
        result.add(new Triple(head, "所属球队", teamName));

        for (TeamRelatedPerson person: persons) {
            result.add(new Triple(teamName, person.getRole(), person.getName()));
        }
        return result;
    }

}
