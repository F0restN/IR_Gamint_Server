package system.preprocess;

import java.util.ArrayList;
import java.util.List;

public class WordNormalizer {
    public String[] stem(String[] input) {
        int index = 0;
        String[] output = new String[input.length];
        for (String in: input){
            char[] chars = in.toCharArray();
            Stemmer s = new Stemmer();
            s.add(chars, chars.length);
            s.stem();
            output[index] = s.toString();
            index++;
        }
        return output;
    }


}