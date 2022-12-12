package system.preprocess;

import system.classes.Path;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class WordCleaner {
    public static HashSet<String> stopwordsMap;

    public WordCleaner() {
        try {
            stopwordsMap = new HashSet<String>();
            BufferedReader reader = new BufferedReader(new FileReader(new File(Path.stopWord)));
            String line = "";
            while((line = reader.readLine()) != null){
                stopwordsMap.add(line.trim());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String removeTag(String rawStr){
//        return Pattern
//                .compile("<[^>]*>|\\[[^\\]]*]|\\\\[a-z]{1}|[^a-zA-Z0-9 ]|https[^ ]* |[^\\x00-\\x7F]+")
//                .matcher(rawStr)
//                .replaceAll(" ");
        return rawStr.replaceAll("<[^>]*>|\\[[^\\]]*]|\\\\[a-z]{1}|[^a-zA-Z0-9 ]|https[^ ]* |[^\\x00-\\x7F]+","");
    }

    public List<String> removeStopWord(List<String> token) {
        List<String> filteredList = new ArrayList<>();
        for (String str : token) {
            if(!isStopwords(str)){
                filteredList.add(str);
            }
        }
        return filteredList;
    }

    public String[] removeStopWord(String[] tokens){
        String[] filteredList = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String curr = tokens[i];
            if(!isStopwords(curr)){
                filteredList[i] = curr;
            } else {
                filteredList[i] = "";
            }
        }
        return filteredList;
    }

    public String removeStopWord(String rawStr){
        String[] rawArr = rawStr.split(" ");
        StringBuilder result = new StringBuilder();
        for(String str: rawArr){
            if(!isStopwords(str)){
                result.append(str);
                result.append(" ");
            }
        }
        return result.toString();
    }

    public HashSet<String> getStopwordsMap() {
        return stopwordsMap;
    }

    private boolean isStopwords(String str) {
        if(Objects.equals(str, "") || str == null || str.length() == 0){
            return false;
        } else {
            return stopwordsMap.contains(str);
        }
    }



}

