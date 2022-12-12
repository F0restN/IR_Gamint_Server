package system.utils;

import system.classes.Document;
import system.classes.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rank {
    public List<Game> getRank(List<Document> rawRank, HashMap<String,Game>idGameMap){
        List <Game> Rank = new ArrayList<>();
        for (Document document : rawRank)
        {
            Game game = idGameMap.get(document.getDocno());
            Rank.add(game);
        }
        return Rank;
    }

    public static void main(String[] args) {
        String a = "lizard";
        String [] arr= a.split(" ");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(i + " "+ arr[i]);
        }
    }
}
