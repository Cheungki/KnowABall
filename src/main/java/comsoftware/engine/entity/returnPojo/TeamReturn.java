package comsoftware.engine.entity.returnPojo;

import comsoftware.engine.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamReturn {
    int code;
    String imgURL;
    TeamBaseInfo teamBaseInfo;
    List<TeamRelatedPerson> teamRelatedPeopleList;
    List<TeamHonorRecord> teamHonorRecordList;
    List<Recommend> recommendList;
}
