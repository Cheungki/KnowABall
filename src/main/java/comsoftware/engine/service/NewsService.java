package comsoftware.engine.service;

import comsoftware.engine.entity.News;
import comsoftware.engine.entity.returnPojo.HotNews;
import comsoftware.engine.mapper.NewsMapper;
import comsoftware.engine.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsMapper newsMapper;

    @Autowired
    private NewsRepository newsRepository;

    public List<News> findNewsByTag(String tag) {
        return newsRepository.findByTags(tag);
    }

    public List<News> findNewsById(int id) {
        return newsRepository.findById(id);
    }

    public List<News> findNewsByContent(String content) {
        return newsRepository.findByContent(content);
    }

    public News getNewsById(int id) {
        return newsMapper.getNewsById(id);
    }
/*
    public List<HotNews> getHotTenNews(){

    }
*/
}
