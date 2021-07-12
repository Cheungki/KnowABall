package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInjuredData {
    @Id
    private int id;

    private int playerId;

    private String club;

    private String injury;

    private String period;

    public String getClub() {
        return club;
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getInjury() {
        return injury;
    }

    public String getPeriod() {
        return period;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setInjury(String injury) {
        this.injury = injury;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
