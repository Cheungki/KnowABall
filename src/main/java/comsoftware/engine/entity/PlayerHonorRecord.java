package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerHonorRecord {
    private int id;

    private int playerId;

    private String honor;

    private String years;

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getHonor() {
        return honor;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }
}
