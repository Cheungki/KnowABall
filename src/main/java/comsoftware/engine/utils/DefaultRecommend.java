package comsoftware.engine.utils;

import comsoftware.engine.entity.Recommend;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DefaultRecommend {
    @Bean(name="defaultRe")
    public List<Recommend> initRecommend() {
        List<Recommend> recommendList = new ArrayList<Recommend>();
        recommendList.add(new Recommend(2, 50001756, "巴塞罗那",
                "https://img1.dongqiudi.com/fastdfs3/M00/B5/7E/ChOxM1xC2RyAO0y3AAAVIBAG1T0205.png"));
        recommendList.add(new Recommend(2, 50001755, "皇家马德里",
                "https://img1.dongqiudi.com/fastdfs3/M00/B5/7E/ChOxM1xC2RyAN-6QAAB1jvar4XU631.png"));
        recommendList.add(new Recommend(2, 50000804, "拜仁慕尼黑",
                "https://img1.dongqiudi.com/fastdfs3/M00/B5/77/ChOxM1xC2ISAfyePAAC4rD9SXTo781.png"));
        recommendList.add(new Recommend(1, 50055255, "德布劳内",
                "https://img1.dongqiudi.com/fastdfs3/M00/B6/69/ChOxM1xC8cKAZUyZAAAtLgM_xnc2027413"));
        recommendList.add(new Recommend(1, 50030207, "莱万多夫斯基",
                "https://img1.dongqiudi.com/fastdfs4/M00/DA/47/ChMf8F1mYDiAK5NXAAAb3UaHbJo386.jpg"));
        recommendList.add(new Recommend(1, 50001488, "本泽马",
                "https://img1.dongqiudi.com/fastdfs3/M00/B5/9D/ChOxM1xC4DOACOkDAAAkkQuYMgI583.jpg"));
        recommendList.add(new Recommend(1, 50069499, "内马尔",
                "https://img1.dongqiudi.com/fastdfs3/M00/B6/8A/ChOxM1xC9B6AX1pgAAAvbsXVufA228.jpg"));
        recommendList.add(new Recommend(1, 50064414, "格列兹曼",
                "https://img1.dongqiudi.com/fastdfs5/M00/5B/C4/rB8CCl_RhzqAB3U2AAAcDeCa8dU730.jpg"));
        recommendList.add(new Recommend(1, 50091010, "萨拉赫",
                "https://img1.dongqiudi.com/fastdfs5/M00/3F/7D/rB8BO19ciRaAHlVVAAAcrz12iiQ413.jpg"));
        return recommendList;
    }
}
