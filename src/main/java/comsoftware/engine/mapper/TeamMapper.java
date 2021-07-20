package comsoftware.engine.mapper;

import comsoftware.engine.entity.TeamBaseInfo;
import comsoftware.engine.entity.TeamHonorRecord;
import comsoftware.engine.entity.TeamNews;
import comsoftware.engine.entity.TeamRelatedPerson;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMapper {
    TeamBaseInfo getTeamBaseInfo(int id);

    String getTeamImgURL(int id);

    List<TeamHonorRecord> getTeamHonorRecord(int id);

    List<TeamRelatedPerson> getTeamPerson(int id);

    List<String> getTeamByCountry(int id, String country);

    TeamNews getTeamNews(int id);
}
