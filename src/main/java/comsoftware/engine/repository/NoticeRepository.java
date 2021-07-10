package comsoftware.engine.repository;

import comsoftware.engine.entity.Notice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface NoticeRepository extends ElasticsearchRepository<Notice, Long> {

}

