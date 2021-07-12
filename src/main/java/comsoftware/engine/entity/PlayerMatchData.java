package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerMatchData {
    @Id
    private int id;

    private int playerId;

    private String season;

    private String club;

    private int play;

    private int start;

    private int goal;

    private int assist;

    private int yellowCard;

    private int redCard;

    private int subsititute;


    public int getPlayerId() {
        return playerId;
    }

    public int getYellowCard() {
        return yellowCard;
    }

    public int getId() {
        return id;
    }

    public String getClub() {
        return club;
    }

    public int getAssist() {
        return assist;
    }

    public int getGoal() {
        return goal;
    }

    public int getPlay() {
        return play;
    }

    public int getRedCard() {
        return redCard;
    }

    public int getStart() {
        return start;
    }

    public int getSubsititute() {
        return subsititute;
    }

    public String getSeason() {
        return season;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public void setRedCard(int redCard) {
        this.redCard = redCard;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setSubsititute(int subsititute) {
        this.subsititute = subsititute;
    }

    public void setYellowCard(int yellowCard) {
        this.yellowCard = yellowCard;
    }
}
