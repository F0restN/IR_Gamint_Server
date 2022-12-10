package system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.classes.Document;
import java.util.List;

@RequestMapping(path = "query")
@RestController
public class SearchController {

    @GetMapping(path = "/{query}")
    public String check(@PathVariable("query") String query) throws Exception {
        QueryRetrievalModel queryRetrievalModel =new QueryRetrievalModel();
        List<Document> ans = queryRetrievalModel.retrieveQuery(query,100);
        for (int i = 0; i < ans.size(); i++) {
            System.out.println(ans.get(i).docno+"/" + ans.get(i).docid + "/"+ans.get(i).score);
        }
        return "hello";
    }

}
