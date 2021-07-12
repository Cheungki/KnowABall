package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerNewsTitles {
    private int playerId;

    private String name;

    private String english_name;

    private String titles;

    public String getName() {
        return name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public String getTitles() {
        return titles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

}
