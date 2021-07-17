package comsoftware.engine.entity.returnPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotNews {
    int id;
    String title;
    String url;
    int comment;  // 0 - 新, 1 - 热, 2 - 爆
}
