package comsoftware.engine.mapper;

import comsoftware.engine.entity.News;
import comsoftware.engine.entity.PlayerNewsTitles;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsMapper {

    News getNewsById(int id);

}
