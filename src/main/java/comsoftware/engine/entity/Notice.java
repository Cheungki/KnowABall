package comsoftware.engine.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

// 测试用的一个类，用于测试ES接口能否正常使用
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "notice")
public class Notice {

    //id
    @JsonProperty("id")
    private Long id;

    //标题
    @JsonProperty("title")
    private String title;

}

