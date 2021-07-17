package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotWord {
    private String text;

    private int value;

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
