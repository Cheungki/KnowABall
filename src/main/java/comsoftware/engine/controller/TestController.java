package comsoftware.engine.controller;

import comsoftware.engine.entity.Notice;
import comsoftware.engine.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
public class TestController {
    @Autowired
    private NoticeRepository noticeRepository;

    @RequestMapping(value = "/test/hello", method = RequestMethod.GET)
    public String helloSearchEngine() {
        return "Hello World";
    }

    @RequestMapping(value = "/test/search", method = RequestMethod.GET)
    public Iterable<Notice> search() {
        return noticeRepository.findAll();
    }

    @RequestMapping(value = "/test/find/{query}", method = RequestMethod.GET)
    public Optional<Notice> query(@PathVariable int query) {
        return noticeRepository.findById((long) query);
    }

}
