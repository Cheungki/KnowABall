package comsoftware.engine.mapper;

import comsoftware.engine.entity.PlayerBaseInfo;
import comsoftware.engine.entity.PlayerInjuredData;
import comsoftware.engine.entity.PlayerMatchData;
import comsoftware.engine.entity.PlayerTransferData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMapper {

    PlayerBaseInfo getPlayerBaseInfo(int id);

    List<PlayerInjuredData> getPlayerInjuredData(int id);

    List<PlayerMatchData> getPlayerMatchData(int id);

    List<PlayerTransferData> getPlayerTransferData(int id);
}
