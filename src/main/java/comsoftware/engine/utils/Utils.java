package comsoftware.engine.utils;

import comsoftware.engine.entity.Pair;
import java.util.ArrayList;

public class Utils {

    public static ArrayList<Pair> getAllKeywords(String input){
        ArrayList<Pair> wordLists = new ArrayList<Pair>();
        String[] words_1 =  input.split(" ");
        int ori_pos, pos = 1;
        for(int i=0; i<words_1.length; i++){
            pos = 1;
            ori_pos  = words_1[i].length();
            while(pos > 0) {
                int pos_1 = words_1[i].lastIndexOf("+", ori_pos-1);
                int pos_2 = words_1[i].lastIndexOf("-", ori_pos-1);
                int pos_3 = words_1[i].lastIndexOf(":intitle:", ori_pos-1);
                int pos_4 = words_1[i].lastIndexOf("~", ori_pos-1);
                pos = (pos_1>pos_2)?pos_1:pos_2;
                pos = (pos_3>pos)?pos_3:pos;
                pos = (pos_4>pos)?pos_4:pos;
                //System.out.println("111*:"+pos);
                if(pos==-1) pos = 0;
                String curStr = words_1[i].substring(pos, ori_pos);
                if(curStr.startsWith("+")){
                    wordLists.add(new Pair(1, curStr.substring(1)));
                }
                else if(curStr.startsWith("-")){
                    wordLists.add(new Pair(2, curStr.substring(1)));
                }
                else if(curStr.startsWith(":intitle:")){
                    wordLists.add(new Pair(3, curStr.substring(9)));
                }
                else if(curStr.startsWith("~")){
                    wordLists.add(new Pair(4, curStr.substring(1)));
                }
                else{
                    wordLists.add(new Pair(0, curStr));
                }
                ori_pos = pos;
            }
        }
        return wordLists;
    }
}
