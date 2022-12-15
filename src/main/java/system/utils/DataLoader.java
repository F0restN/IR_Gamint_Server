package system.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import system.classes.Game;
import system.classes.Path;


import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DataLoader {
    public static HashMap<String, Game> loadGameMap() throws IOException, ParseException {
        // Init
        FileReader fileReader = new FileReader(Path.dataCleanedIDGame);
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
        // Loading
        HashMap<String, Game> idGameMap = new HashMap<>();
        for (Object o : jsonArray) {
            JSONObject agent = (JSONObject) o;
            String id = (String) agent.get("id");
            String name = (String) agent.get("name");
            String shortDesc = (String) agent.get("shortDesc");
            String imageUrl = (String) agent.get("imageUrl");
            String rating = (String) agent.get("rating");
            Game game = new Game(id, name, shortDesc, imageUrl, rating);
            idGameMap.put(id, game);
        }
        return idGameMap;
    }
}
