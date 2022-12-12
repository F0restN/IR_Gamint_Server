package system.preprocess;


import system.classes.Path;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PreProcessedCorpusReader {


    private BufferedReader br;
    private FileInputStream instream_collection;
    private InputStreamReader is;

    public PreProcessedCorpusReader() throws IOException {
        // This constructor should open the file in Path.DataTextDir
        // and also should make preparation for function nextDocument()
        // remember to close the file that you opened, when you do not use it any more
        instream_collection = new FileInputStream(Path.dataCleanedIDContent);
        is = new InputStreamReader(instream_collection);
        br = new BufferedReader(is);
    }


    public Map<String, Object> nextDocument() throws IOException {
        Map<String, Object> doc = new HashMap<>();
        String docno = br.readLine();
        if (docno == null) {
            instream_collection.close();
            is.close();
            br.close();
            return null;
        }
        String content = br.readLine();
        doc.put(docno, content.toCharArray());
        return doc;
    }

}
