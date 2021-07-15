package comsoftware.engine.repository;

import comsoftware.engine.entity.News;
import comsoftware.engine.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NewsRepository extends ElasticsearchRepository<News, Long> {
    List<News> findById(int id);

    List<News> findByTags(String tags);

    List<News> findByContent(String content);

}
