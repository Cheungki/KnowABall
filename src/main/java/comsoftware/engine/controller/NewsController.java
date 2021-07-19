package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.entity.returnPojo.NewsReturn;
import comsoftware.engine.entity.returnPojo.SearchReturn;
import comsoftware.engine.service.NewsService;

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
public class NewsController {
    @Autowired
    private NewsService newsService;

    static int MAX_RECORD = 10;

    @RequestMapping(value = "/search/news/{keyword}/{pageNum}", method = RequestMethod.GET)
    public SearchReturn ComplexNewsSearch(@PathVariable String keyword, @PathVariable int pageNum) {
        try {
            int totalNum = 0;
            List<TotalData> dataList = new ArrayList<TotalData>();
            SearchInfo si = new SearchInfo(0L, 0L);
            List<Map<String, Object>> retList = newsService.complexNewsSearch(keyword, true, pageNum, MAX_RECORD, si, 0);
            for(Map<String, Object> map : retList){
                TotalData cur = new TotalData(3, map, null, null);
                dataList.add(cur);
            }
            return new SearchReturn(200, si, dataList);

        } catch(Exception e){
            e.printStackTrace();
            return new SearchReturn(400, new SearchInfo(0L, 0L), new ArrayList<TotalData>());
        }
    }

    @RequestMapping(value = "/search/suggest/news/{keyword}", method = RequestMethod.GET)
    public List<String> getNewsSuggest(@PathVariable String keyword){
        return newsService.getSuggestCompletion(keyword);
    }
    //针对Elastic Search
    @RequestMapping(value = "/search/news/tag/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public List<News> findNewsByTag(@PathVariable String tag) {
        return newsService.findNewsByTag(tag);
    }

    @RequestMapping(value = "/search/news/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<News> findNewsById(@PathVariable int id) {
        return newsService.findNewsById(id);
    }

    @RequestMapping(value = "/search/news/content/{content}", method = RequestMethod.GET)
    @ResponseBody
    public List<News> findNewsByContent(@PathVariable String content) {
        return newsService.findNewsByContent(content);
    }

    // 针对数据库
    @RequestMapping(value = "/news/getInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public News getNewsById(@PathVariable int id) {
        return newsService.getNewsById(id);
    }

    @RequestMapping(value = "/news/getAll/{id}", method = RequestMethod.GET)
    @ResponseBody
    public NewsReturn getNewsAllInfo(@PathVariable int id) {
        News news = getNewsById(id);
        return new NewsReturn(200, news);
    }
}
