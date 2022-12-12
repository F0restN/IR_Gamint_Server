package system;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import system.classes.Document;
import system.classes.Game;
import system.classes.Path;
import system.classes.Query;
import system.indexHandler.MyIndexReader;
import system.preprocess.WordCleaner;
import system.preprocess.WordNormalizer;
import system.utils.IdGameMap;
import system.utils.Rank;
import system.utils.StringManipulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@RequestMapping(path = "query")
@RestController

public class SearchController {
    HashSet<String> stopSet;
    FileReader rd;
    BufferedReader reader;
    MyIndexReader ixreader;
    static HashMap<String, Game> idGameMap = new HashMap<>();
    public SearchController() throws IOException, ParseException {
        rd = new FileReader(Path.stopWord);
        reader = new BufferedReader(rd);
        String stopWord;
        HashSet<String> set = new HashSet<>();
        while ((stopWord = reader.readLine()) != null) {
            set.add(stopWord);
        }
        this.stopSet = set;
        this.ixreader = new MyIndexReader();
        idGameMap = IdGameMap.setIdGameMap();
    }

    @PostMapping
    public List<Game> check(@RequestBody Map<String,String> query) throws Exception {
        //query clean
        WordCleaner wordCleaner = new WordCleaner();
        WordNormalizer wordNormalizer = new WordNormalizer();

        String queryRaw = query.get("queryContent");

        String[] temp1 = wordCleaner.removeTag(queryRaw).toLowerCase().split(" ");
        System.out.println("temp1" );
        String[] temp2 = wordCleaner.removeStopWord(temp1);
        System.out.println("temp2" );
        String[] temp3 = wordNormalizer.stem(temp2);
        System.out.println("temp3" +temp3[0]);
        String queryClean = StringManipulation.convertStringArrayToString(temp3);
        System.out.println("clean" + queryClean);
        RFRetrievalModal rfRetrievalModal = new RFRetrievalModal(ixreader);
        System.out.println("model" + queryClean);

        //retrieve process
        List<Document> results = rfRetrievalModal.RetrieveQuery(queryClean, 20, 100, 0.4);
        if (results != null) {
            int rank = 1;
            for (Document result : results) {
                System.out.println(query + " Q0 " + result.getDocno() + " " + rank + " " + result.getScore());
                rank++;
            }
            System.out.println();
        }
        System.out.println(results.size());
//        QueryRetrievalModel queryRetrievalModel = new QueryRetrievalModel();
//        List<Document> ans = queryRetrievalModel.retrieveQuery(query, 100);
//        for (int i = 0; i < ans.size(); i++) {
//            System.out.println(ans.get(i).docno + "/" + ans.get(i).docid + "/" + ans.get(i).score);
//        }
//        return "hello";
        //return final rank
        Rank rank = new Rank();
        List <Game> finalRank = rank.getRank(results,idGameMap);
        return finalRank;
    }


}
