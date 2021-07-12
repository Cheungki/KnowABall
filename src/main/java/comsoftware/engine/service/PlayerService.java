package comsoftware.engine.service;

import comsoftware.engine.entity.PlayerBaseInfo;
import comsoftware.engine.entity.PlayerInjuredData;
import comsoftware.engine.entity.PlayerMatchData;
import comsoftware.engine.entity.PlayerTransferData;
import comsoftware.engine.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Service
public class PlayerService {
    @Autowired
    PlayerMapper playerMapper;

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

}
