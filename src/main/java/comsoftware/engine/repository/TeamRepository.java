package comsoftware.engine.repository;

import comsoftware.engine.entity.Player;
import comsoftware.engine.entity.TeamBaseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TeamRepository extends ElasticsearchRepository<TeamBaseInfo, Long>{
    List<TeamBaseInfo> findById(int id);

    List<TeamBaseInfo> findByName(String name);

    List<TeamBaseInfo> findByNameLike(String name);
}
