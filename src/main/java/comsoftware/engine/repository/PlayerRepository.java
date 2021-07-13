package comsoftware.engine.repository;

import comsoftware.engine.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PlayerRepository extends ElasticsearchRepository<Player, Long> {

    List<Player> findByName(String name);

    List<Player> findByClub(String club);

    @Query("{\"match\":{\"name\":\"?0\"}}")
    Page<Player> findNameFuzzy(String name, Pageable pageable);

}

