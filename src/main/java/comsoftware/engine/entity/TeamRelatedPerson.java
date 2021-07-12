package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamRelatedPerson {
    @Id
    private int id;

    private int teamId;

    private String role;

    private int number;

    private String name;

    public int getTeamId() {
        return teamId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getNumber() {
        return number;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

