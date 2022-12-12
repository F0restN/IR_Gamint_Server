package system;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.json.simple.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import system.preprocess.WordNormalizer;
import system.utils.StringManipulation;
import system.classes.Game;
import system.classes.Path;
import system.preprocess.WordCleaner;
import system.preprocess.PreProcessedCorpusReader;
import system.indexHandler.MyIndexWriter;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws Exception {
		Application application = new Application();
		// 1. Pre-process data
		//application.dataClean();

		// 2. Write index
		//application.WriteIndex();

		// 3. Load game data will be in Controller modal
		SpringApplication.run(Application.class, args);
	}

	public void dataClean() throws IOException, ParseException {
		WordCleaner wordCleaner = new WordCleaner();
		WordNormalizer wordNormalizer = new WordNormalizer();

		// Read data
		FileReader fileReader = new FileReader(Path.rawData);
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);

		// Write data
		FileWriter writerIdGame = new FileWriter(Path.dataCleanedIDGame);
		FileWriter writerIdContent = new FileWriter(Path.dataCleanedIDContent);
		BufferedWriter writerIdCon =new BufferedWriter(writerIdContent);
		JSONArray jsArrGames = new JSONArray();

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject agent = (JSONObject) jsonArray.get(i);
			String id = (String) agent.get("id");
			String name = (String) agent.get("name");
			String desc = (String) agent.get("desc");
			String shortDesc = (String) agent.get("shortDesc");
			String review = (String) agent.get("reviews");
			String imageUrl = (String) agent.get("img");

			// Write Game json
			JSONObject jsonObjectIdGame = new JSONObject();
			jsonObjectIdGame.put("id", id);
			jsonObjectIdGame.put("name", name);
			jsonObjectIdGame.put("shortDesc", shortDesc);
			jsonObjectIdGame.put("imageUrl", imageUrl);
			jsArrGames.add(jsonObjectIdGame);

			// Write Content Txt file
			// Remove Tag & special characters & url etc.
			String rawContent = name + " " + desc + " " + shortDesc + " " + review;
			String[] temp1 = wordCleaner.removeTag(rawContent).toLowerCase().split(" ");
			String[] temp2 = wordCleaner.removeStopWord(temp1);
			String[] temp3 = wordNormalizer.stem(temp2);
			String cleanContent = StringManipulation.convertStringArrayToString(temp3);

			writerIdCon.write(id);
			writerIdCon.write("\n");
			writerIdCon.write(cleanContent);
			writerIdCon.write("\n");
		}

		// Write down
		writerIdGame.write(jsArrGames.toJSONString());
		// Close
		writerIdCon.close();
		writerIdGame.close();
		writerIdContent.close();
	}

	public void WriteIndex() throws Exception {
		// Initiate pre-processed collection file reader
		PreProcessedCorpusReader corpus=new PreProcessedCorpusReader();

		// initiate the output object
		MyIndexWriter output=new MyIndexWriter();

		// initiate a doc object, which can hold document number and document content of a document
		Map<String, Object> doc = null;

		int count=0;
		// build index of corpus document by document

		while ((doc = corpus.nextDocument()) != null) {
			// load document number and content of the document
			String docno = doc.keySet().iterator().next();
			char[] content = (char[]) doc.get(docno);
			// index this document
			output.index(docno, content);

			count++;
			if(count%10000==0)
				System.out.println("finish "+count+" docs");
		}
		System.out.println("totaly document count:  "+count);
		output.close();
	}
}
