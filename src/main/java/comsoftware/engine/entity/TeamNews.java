package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamNews {
    private String titles;

    private String urls;

    private String img_urls;

    public String getUrls() {
        return urls;
    }

    public String getImg_urls() {
        return img_urls;
    }

    public String getTitles() {
        return titles;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public void setImg_urls(String img_urls) {
        this.img_urls = img_urls;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }
}
