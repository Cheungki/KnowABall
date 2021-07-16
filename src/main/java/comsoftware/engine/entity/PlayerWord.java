package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerWord {
    @Id
    private int id;

    private String name;

    private String word;

    private int count;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getWord() {
        return word;
    }
}
