package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalData {
    int type = 0; // 1-球员  2-球队  3-新闻
    News newsReturn = null;
    Player playerReturn = null;
    TeamBaseInfo teamReturn = null;
}
