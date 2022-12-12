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

public class IdGameMap {
    public static HashMap<String, Game> setIdGameMap() throws IOException, ParseException {
        FileReader fileReader = new FileReader(Path.dataCleanedIDGame);
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
        HashMap<String, Game> idGameMap = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject agent = (JSONObject) jsonArray.get(i);
            String id = (String) agent.get("id");
            String name = (String) agent.get("name");
            String shortDesc = (String) agent.get("shortDesc");
            String imageUrl = (String) agent.get("imageUrl");
            Game game = new Game(id,name,shortDesc,imageUrl);
            idGameMap.put(id,game);
        }
        return idGameMap;
    }
}
