package system;

import org.springframework.web.bind.annotation.*;
import system.classes.Document;
import system.classes.Path;
import system.classes.Query;
import system.indexHandler.MyIndexReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@RequestMapping(path = "query")
@RestController
public class SearchController {
    HashSet<String> stopSet;
    FileReader rd;
    BufferedReader reader;
    MyIndexReader ixreader;

    public SearchController() throws IOException {
        rd = new FileReader(Path.stopWord);
        reader = new BufferedReader(rd);
        String stopWord;
        HashSet<String> set = new HashSet<>();
        while ((stopWord = reader.readLine()) != null) {
            set.add(stopWord);
        }
        this.stopSet = set;
        this.ixreader = new MyIndexReader();
    }

    @PostMapping
    public List<Document> check(@RequestBody Query query) throws Exception {
        RFRetrievalModal rfRetrievalModal = new RFRetrievalModal(ixreader);

        // Query processing
        // ...

        List<Document> results = rfRetrievalModal.RetrieveQuery(query.GetQueryContent(), 20, 100, 0.4);
        if (results != null) {
            int rank = 1;
            for (Document result : results) {
                System.out.println(query + " Q0 " + result.docno() + " " + rank + " " + result.score());
                rank++;
            }
            System.out.println();
        }

//        QueryRetrievalModel queryRetrievalModel = new QueryRetrievalModel();
//        List<Document> ans = queryRetrievalModel.retrieveQuery(query, 100);
//        for (int i = 0; i < ans.size(); i++) {
//            System.out.println(ans.get(i).docno + "/" + ans.get(i).docid + "/" + ans.get(i).score);
//        }
//        return "hello";
        return results;
    }


}
