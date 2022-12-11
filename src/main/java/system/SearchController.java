package system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.classes.Document;
import system.classes.Path;

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

    public SearchController() throws IOException {
            rd = new FileReader(Path.stopWord);
            reader = new BufferedReader(rd);
            String stopWord;
            HashSet<String> set = new HashSet<>();
            while ((stopWord = reader.readLine()) != null) {
                set.add(stopWord);
            }
            this.stopSet = set;
    }

    @GetMapping(path = "/{query}")
    public List<Document> check(@PathVariable("query") String query) throws Exception {
        QueryRetrievalModel queryRetrievalModel =new QueryRetrievalModel();
        query = query.toLowerCase();
        String [] tokens = query.split(" ");
        for (int i = 0; i < tokens.length; i++) {

        }
        List<Document> ans = queryRetrievalModel.retrieveQuery(query,100);
        for (int i = 0; i < ans.size(); i++) {
            System.out.println(ans.get(i).docno+"/" + ans.get(i).docid + "/"+ans.get(i).score);
        }
        //return "hello";
        return ans;
    }


}
