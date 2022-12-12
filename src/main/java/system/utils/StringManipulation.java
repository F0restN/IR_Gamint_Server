package system.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringManipulation {

    public static String convertStringArrayToString(String[] strArr) {

        StringBuilder sb = new StringBuilder();
        for (String str : strArr){
            if(str.length() != 0){
                sb.append(str);
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String[] convertStringToList(String rawStr){
//        return new ArrayList<>(Arrays.asList(rawStr.split(" ")));
        return rawStr.split(" ");
    }
}
