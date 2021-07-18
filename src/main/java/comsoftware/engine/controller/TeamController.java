package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.entity.returnPojo.SearchReturn;
import comsoftware.engine.entity.returnPojo.TeamReturn;
import comsoftware.engine.repository.TeamRepository;
import comsoftware.engine.service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    static int MAX_RECORD = 10;

    @RequestMapping(value = "/search/team/{keyword}/{pageNum}", method = RequestMethod.GET)
    public SearchReturn ComplexTeamSearch(@PathVariable String keyword, @PathVariable int pageNum) {
        try {
            int totalNum = 0;
            List<TotalData> dataList = new ArrayList<TotalData>();
            List<Map<String, Object>> retList = teamService.complexTeamSearch(keyword, true);
            totalNum = retList.size();
            int pages = totalNum / MAX_RECORD;

            int start = (pageNum-1)*MAX_RECORD;
            if(start < totalNum){
                for(int i=0; i<MAX_RECORD && start+i<totalNum; i++){
                    TotalData cur = new TotalData(2, null, null, retList.get(i+start));
                    dataList.add(cur);
                }
            }


            return new SearchReturn(200, totalNum, pages, dataList);

        } catch(Exception e){
            e.printStackTrace();
            return new SearchReturn(400, 0, 0, new ArrayList<TotalData>());
        }
    }
    //针对Elastic Search
    @RequestMapping(value = "/search/team/name/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<TeamBaseInfo> findTeamByName(@PathVariable String name) {
        return teamService.findTeamByName(name);
    }

    @RequestMapping(value = "/search/team/nameLike/{name}", method = RequestMethod.GET)
    @ResponseBody
    public List<TeamBaseInfo> findTeamByNameLike(@PathVariable String name) {
        return teamService.findTeamByNameLike(name);
    }

    @RequestMapping(value = "/search/team/id/{id}", method = RequestMethod.GET)
    public List<TeamBaseInfo> findTeamById(@PathVariable int id) {
        return teamService.findTeamById(id);
    }

    // 针对数据库
    @RequestMapping(value = "/team/getInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TeamBaseInfo getTeamBaseInfo(@PathVariable int id) {
        return teamService.getTeamBaseInfo(id);
    }

    @RequestMapping(value = "/team/getPerson/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<TeamRelatedPerson> getTeamPerson(@PathVariable int id) {
        return teamService.getTeamMember(id);
    }

    @RequestMapping(value = "/team/getHonor/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<TeamHonorRecord> getTeamHonor(@PathVariable int id) {
        return teamService.getTeamHonor(id);
    }

    @RequestMapping(value = "/team/getImgURL/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getTeamImgURL(@PathVariable int id) {
        return teamService.getTeamImgURL(id);
    }

    @RequestMapping(value = "/team/getAll/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TeamReturn getTeamAllInfo(@PathVariable int id) {
        TeamBaseInfo teamBaseInfo = getTeamBaseInfo(id);
        List<TeamRelatedPerson> teamRelatedPersonList = getTeamPerson(id);
        List<TeamHonorRecord> teamHonorRecordList = getTeamHonor(id);
        String imgURL = getTeamImgURL(id);
        return new TeamReturn(200, imgURL, teamBaseInfo, teamRelatedPersonList, teamHonorRecordList);
    }

    @RequestMapping(value = "/team/kg/{id}", method = RequestMethod.GET)
    public Map<String, Object> getTeamKnowledgeGraph(@PathVariable int id) {
        return teamService.getTeamKnowledgeGraph(id);
    }

}
