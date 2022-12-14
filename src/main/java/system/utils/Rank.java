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
}
