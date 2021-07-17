package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTags {
    private String trait;

    private String tag;

    public List<String> getPlayerTags() {
        if (tag == null && trait == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        if (tag != null) {
            result.addAll(Arrays.asList(tag.split(",")));
        }
        if (trait != null) {
            result.addAll(Arrays.asList(trait.split(",")));
        }
        return result;
    }

    public String getTag() {
        return tag;
    }

    public String getTrait() {
        return trait;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }
}
