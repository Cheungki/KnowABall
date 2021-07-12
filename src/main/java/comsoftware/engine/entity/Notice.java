package comsoftware.engine.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

// 测试用的一个类，用于测试ES接口能否正常使用
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "notice")
public class Notice {

    //id
    @JsonProperty("id")
    @Id
    private Long id;

    //标题
    @JsonProperty("title")
    @Field(type = FieldType.Text)
    private String title;

}
