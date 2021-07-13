package comsoftware.engine.service;

import comsoftware.engine.entity.News;
import comsoftware.engine.entity.PlayerNewsTitles;
import comsoftware.engine.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsMapper newsMapper;

    public List<PlayerNewsTitles> getPlayerHotNews(int id) {
        return newsMapper.getPlayerHotNews(id);
    }

    public News getNewsById(int id) {
        return newsMapper.getNewsById(id);
    }

}
