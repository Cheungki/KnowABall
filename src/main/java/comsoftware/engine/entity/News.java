package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    private int id;

    private String url;

    private String title;

    private String author;

    private String text;

    private String tags;

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTags() {
        return tags;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
