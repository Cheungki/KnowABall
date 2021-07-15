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
@Document(indexName = "player")
public class Player {

    //id
    @Id
    private int id;

    private String imgURL;

    private int teamId;

    private String url;

    private String club;

    private String country;

    private String role;

    private int height;

    private int weight;

    private int age;

    private String number;

    private String birthday;

    private int foot;

    private String name;

    private String englishName;

    private String capablity;

}
