package comsoftware.engine.service;

import comsoftware.engine.mapper.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@Service
public class MatchService {
    @Autowired
    MatchMapper matchMapper;

}
