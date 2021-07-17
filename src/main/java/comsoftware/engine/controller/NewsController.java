package comsoftware.engine.controller;

import comsoftware.engine.entity.*;
import comsoftware.engine.entity.returnPojo.HotNews;
import comsoftware.engine.entity.returnPojo.NewsReturn;
import comsoftware.engine.service.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class NewsController {
    @Autowired
    private NewsService newsService;

    static int MAX_RECORD = 10;

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

    // 针对数据库
    /*
    @RequestMapping(value = "/news/getHotTen", method = RequestMethod.GET)
    @ResponseBody
    public List<HotNews> getHotTenNews() {
        return newsService.getHotTenNews();
    }
    */

}
