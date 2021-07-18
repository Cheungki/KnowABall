package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalData {
    int type = 0; // 1-球员  2-球队  3-新闻
    Map<String,Object> newsReturn = null;
    Map<String,Object> playerReturn = null;
    Map<String,Object> teamReturn = null;
}
