package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTransferData {
    @Id
    private int id;

    private int playerId;

    private String transferMonth;

    private String inClub;

    private String outClub;

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getInClub() {
        return inClub;
    }

    public String getOutClub() {
        return outClub;
    }

    public String getTransferMonth() {
        return transferMonth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInClub(String inClub) {
        this.inClub = inClub;
    }

    public void setOutClub(String outClub) {
        this.outClub = outClub;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setTransferMonth(String transferMonth) {
        this.transferMonth = transferMonth;
    }
}
