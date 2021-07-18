package comsoftware.engine.service;

import comsoftware.engine.entity.*;
import comsoftware.engine.mapper.TeamMapper;
import comsoftware.engine.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getTeamImgURL(int id){
        try {
            String ret = teamMapper.getTeamImgURL(id);
            if(ret==null || ret.equals("")){
                ret = "https://img1.dongqiudi.com/fastdfs3/M00/B5/98/ChOxM1xC37CADGDCAAANHjH55fo314.png";
            }
            return ret;
        } catch(Exception e){
            String ret = "https://img1.dongqiudi.com/fastdfs3/M00/B5/98/ChOxM1xC37CADGDCAAANHjH55fo314.png";
            return ret;
        }
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

    public Map<String, Object> getTeamKnowledgeGraph(int id) {
        TeamBaseInfo team = getTeamBaseInfo(id);
        String name = team.getName();

        List<TeamRelatedPerson> teamRelatedPeople = teamMapper.getTeamPerson(id);
        List<TeamHonorRecord> teamHonorRecords = teamMapper.getTeamHonorRecord(id);
        List<String> teamsFromSameCountry = teamMapper.getTeamByCountry(id, team.getCountry());
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        nodes.add(new Node(1, name, "self"));
        nodes.add(new Node(2, "球队成员", "relation"));
        nodes.add(new Node(3, "球队荣誉", "relation"));
        nodes.add(new Node(4, "竞争对手", "relation"));
        edges.add(new Edge(1, 2));
        edges.add(new Edge(1, 3));
        edges.add(new Edge(1, 4));
        int count = 5;
        for (TeamRelatedPerson teamRelatedPerson: teamRelatedPeople) {
            nodes.add(new Node(count, teamRelatedPerson.getName(), teamRelatedPerson.getRole()));
            edges.add(new Edge(2, count));
            count += 1;
        }

        for (TeamHonorRecord teamHonorRecord: teamHonorRecords) {
            nodes.add(new Node(count, teamHonorRecord.getHonor(), "honor"));
            edges.add(new Edge(3, count));
            count += 1;
        }

        for (String teamFromSameCountry: teamsFromSameCountry) {
            nodes.add(new Node(count, teamFromSameCountry, "rival"));
            edges.add(new Edge(4, count));
            count += 1;
        }
        result.put("nodes", nodes);
        result.put("edges", edges);
        return result;
    }

}
