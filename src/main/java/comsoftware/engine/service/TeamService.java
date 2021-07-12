package comsoftware.engine.service;

import comsoftware.engine.entity.TeamBaseInfo;
import comsoftware.engine.entity.TeamHonorRecord;
import comsoftware.engine.entity.TeamRelatedPerson;
import comsoftware.engine.mapper.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Service
public class TeamService {
    @Autowired
    TeamMapper teamMapper;

    public TeamBaseInfo getTeamBaseInfo(int id) {
        return teamMapper.getTeamBaseInfo(id);
    }

    public List<TeamHonorRecord> getTeamHonor(int id) {
        return teamMapper.getTeamHonorRecord(id);
    }

    public List<TeamRelatedPerson> getTeamMember(int id) {
        return teamMapper.getTeamPerson(id);
    }

}
