package comsoftware.engine.entity.returnPojo;

import comsoftware.engine.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerReturn {
    int code;
    String imgURL;
    PlayerBaseInfo playerBaseInfo;
    List<PlayerInjuredData> playerInjuredDataList;
    List<PlayerTransferData> playerTransferDataList;
    List<PlayerNewsTitles> playerNewsTitlesList;
    List<Recommend> recommendList;
}
