package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamHonorRecord {
    private int id;

    private int teamId;

    private String honor;

    private String years;

    public int getId() {
        return id;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getHonor() {
        return honor;
    }

    public String getYears() {
        return years;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public void setYears(String years) {
        this.years = years;
    }

}
