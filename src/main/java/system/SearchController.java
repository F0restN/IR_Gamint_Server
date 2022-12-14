package system;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import system.classes.Document;
import system.classes.Game;
import system.classes.Path;
import system.indexHandler.MyIndexReader;
import system.preprocess.WordCleaner;
import system.preprocess.WordNormalizer;
import system.utils.DataLoader;
import system.utils.Rank;
import system.utils.StringManipulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@RequestMapping(path = "query")
@RestController

public class SearchController {
    static MyIndexReader ixreader;
    static WordCleaner wordCleaner;
    static WordNormalizer wordNormalizer;
    static HashMap<String, Game> idGameMap;
    static RFRetrievalModal rfRetrievalModal;
    public SearchController() throws IOException, ParseException {
        // Initialize
        idGameMap = DataLoader.loadGameMap();
        ixreader = new MyIndexReader();
        wordCleaner = new WordCleaner();
        wordNormalizer = new WordNormalizer();
        rfRetrievalModal = new RFRetrievalModal(ixreader);
    }

    @PostMapping
    public List<Game> check(@RequestBody Map<String,String> query) throws Exception {
        // Process query
        String[] temp1 = wordCleaner.removeTag(query.get("queryContent")).toLowerCase().split(" ");
        String[] temp2 = wordCleaner.removeStopWord(temp1);
        String[] temp3 = wordNormalizer.stem(temp2);
        String queryClean = StringManipulation.convertStringArrayToString(temp3);

        // RFQLM Retrieve
        List<Document> results = rfRetrievalModal.RetrieveQuery(queryClean, 20, 100, 0.4);
        Rank rank = new Rank();
        return rank.getRank(results, idGameMap);

        // Lucene Retrieve
//        QueryRetrievalModel queryRetrievalModel = new QueryRetrievalModel();
//        List<Document> ans = queryRetrievalModel.retrieveQuery(query, 100);
//        for (int i = 0; i < ans.size(); i++) {
//            System.out.println(ans.get(i).docno + "/" + ans.get(i).docid + "/" + ans.get(i).score);
//        }
//        return "hello";
//        return final rank
    }


}
