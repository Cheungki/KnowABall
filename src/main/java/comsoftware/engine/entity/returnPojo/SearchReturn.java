package comsoftware.engine.entity.returnPojo;
import comsoftware.engine.entity.TotalData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchReturn {
    int code;
    int totalNum;
    int pages;
    List<TotalData> totalDataList;
}
