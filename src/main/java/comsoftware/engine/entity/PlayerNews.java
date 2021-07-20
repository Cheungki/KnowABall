package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerNews {
    private String titles;

    private String urls;

    private String img_urls;

    public String getTitles() {
        return titles;
    }

    public String getImg_urls() {
        return img_urls;
    }

    public String getUrls() {
        return urls;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public void setImg_urls(String img_urls) {
        this.img_urls = img_urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
