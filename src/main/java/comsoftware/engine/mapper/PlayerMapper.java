package comsoftware.engine.mapper;

import comsoftware.engine.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMapper {

    PlayerBaseInfo getPlayerBaseInfo(int id);

    List<PlayerInjuredData> getPlayerInjuredData(int id);

    List<PlayerMatchData> getPlayerMatchData(int id);

    List<PlayerTransferData> getPlayerTransferData(int id);

    List<PlayerNewsTitles> getPlayerHotNews(int id);

    String getPlayerImgURL(int id);
}
