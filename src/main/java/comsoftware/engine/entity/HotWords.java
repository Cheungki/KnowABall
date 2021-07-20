package comsoftware.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotWords implements Serializable {
    private static final long serialVersionUID = -8183942491930372236L;
    private List<String> words = new ArrayList<String>();
    public void addWord(String word){
        for(String w:words){
            if(w.equals(word))  return;
        }
        if(words.size()<10) words.add(word);
        else {
            while (words.size()>=10) {
                words.remove(words.size()-1);
            }
            words.add(0, word);
        }
    }

}
