package comsoftware.engine.service;

import comsoftware.engine.entity.TeamBaseInfo;
import comsoftware.engine.entity.TeamHonorRecord;
import comsoftware.engine.entity.TeamRelatedPerson;
import comsoftware.engine.mapper.TeamMapper;
import comsoftware.engine.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Service
public class TeamService {
    @Autowired
    TeamMapper teamMapper;

    @Autowired
    private TeamRepository teamRepository;

    public List<TeamBaseInfo> findTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    public List<TeamBaseInfo> findTeamByNameLike(String name) {
        return teamRepository.findByNameLike(name);
    }

    public List<TeamBaseInfo> findTeamById(int id) {
        return teamRepository.findById(id);
    }


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
