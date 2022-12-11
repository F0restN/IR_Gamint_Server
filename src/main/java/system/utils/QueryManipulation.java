package system.utils;

import system.classes.Document;
import system.classes.Query;

import java.util.HashMap;
import java.util.List;

public class QueryManipulation {

    public QueryManipulation() {
    }

    public String[] querySplit (String aQuery){
        return aQuery.split(" ");
    }

    public HashMap<String, List<Document>> queryRelevantDocList (){
        HashMap<String, List<Document>> docList = new HashMap<>();
        // ...
        return docList;
    }
}

