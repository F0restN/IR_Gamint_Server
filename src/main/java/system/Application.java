package system;


import com.fasterxml.jackson.core.JsonParser;
import org.apache.tomcat.jni.File;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import system.classes.Game;
import system.classes.Path;
import system.indexHandler.MyIndexWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import system.preprocess.PreProcessedCorpusReader;
import org.json.simple.*;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import system.preprocess.Stemmer;
@SpringBootApplication
public class Application {


	public static void main(String[] args) throws Exception {
		Application demoApplication = new Application();
		demoApplication.dataClean();
		//demoApplication.WriteIndex();

		SpringApplication.run(Application.class, args);
	}

	public static String convertStringArrayToString(String[] strArr) {
		if(strArr.length < 2){
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (String str : strArr)
			sb.append(str);
		return sb.substring(0, sb.length() - 1);
	}

	public void dataClean() throws IOException, ParseException {
		HashSet <String> stopWordsSet = new HashSet<>();
		FileReader rd =new FileReader("data//stopWord.txt");
		BufferedReader reader = new BufferedReader(rd);
		String stopWord;
		while ((stopWord = reader.readLine()) != null) {
			stopWordsSet.add(stopWord);
		}

		JSONArray jsonArray = new JSONArray();
		JSONParser jsonParser = new JSONParser();
		FileReader fileReader = new FileReader(Path.rawData);
		Object object = jsonParser.parse(fileReader);
		jsonArray = (JSONArray) object;
		HashMap<String, Game> idGameMap = new HashMap<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject agent = (JSONObject) jsonArray.get(i);
			String id = (String) agent.get("id");
			String name = (String) agent.get("name");
			String desc = (String) agent.get("desc");
			String shortDesc = (String) agent.get("shortDesc");
			String review = (String) agent.get("reviews");
			String imageUrl = (String) agent.get("imageUrl");
			Game game = new Game();
			game.setId(id);
			game.setName(name);
			game.setShortDesc(shortDesc);
			game.setImageUrl(imageUrl);
			StringBuffer descBuffer = new StringBuffer();
			boolean flagDesc = false;
			for (int j = 0; j < desc.length(); j++) {
				if (flagDesc == true) {
					continue;
				}
				if (desc.charAt(j) == '<' || desc.charAt(j) == '[') {
					flagDesc = true;
					continue;
				} else if (desc.charAt(j) == '>' || desc.charAt(j) == ']') {
					flagDesc = false;
					continue;
				}
				descBuffer.append(desc.charAt(j));
			}
			String descString = descBuffer.toString();

			descString = descString.toLowerCase();
			String[] tokensDesc = descString.split(" ");
			for (int k = 0; k < tokensDesc.length; k++) {
				if(stopWordsSet.contains(tokensDesc[k]))
				{
					tokensDesc[k] = "";
				}
				else
				{
					Stemmer s = new Stemmer();
					char[] word=tokensDesc[k].toCharArray();
					s.add(word, word.length);
					s.stem();
					tokensDesc[k] = s.toString();
					//System.out.println(s.);
				}
			}
			String descFinish = convertStringArrayToString(tokensDesc);
			game.setDesc(descFinish);

			if(review != null && review.length() != 0) {
				boolean flagReview = false;
				StringBuffer reviewBuffer = new StringBuffer();
				for (int j = 0; j < review.length(); j++) {
//					if (flagReview == true) {
//						continue;
//					}
					if (review.charAt(j) == '<' || review.charAt(j) == '[') {
						flagReview = true;
						continue;
					} else if (review.charAt(j) == '>' || review.charAt(j) == '[') {
						flagReview = false;
						continue;
					}
					reviewBuffer.append(review.charAt(j));
					flagReview = false;
				}
				String reviewString = String.valueOf(reviewBuffer);
				///System.out.println(reviewString);
				reviewString = reviewString.toLowerCase();
				String[] tokensReview = reviewString.split(" ");
				for (int k = 0; k < tokensReview.length; k++) {
					if (stopWordsSet.contains(tokensReview[k])) {
						tokensReview[k] = "";
					} else {
						Stemmer s = new Stemmer();
						char[] word = tokensReview[k].toCharArray();
						s.add(word, word.length);
						s.stem();
						tokensReview[k] = s.toString();
//						System.out.println(tokensReview[k]);
					}
				}
				String reviewFinish = convertStringArrayToString(tokensReview);
				System.out.println(reviewFinish);
				game.setReview(reviewFinish);
			}
			idGameMap.put(id,game);
			//System.out.println(game.getDesc());
			//System.out.println(game.getReview());
		}

		FileWriter writerIdGame = new FileWriter("data//dataCleanedIdGame.json");
		FileWriter writerIdContent = new FileWriter("data//dataCleanedIdContent.json");
		JSONArray jsArrGames = new JSONArray();
		JSONArray jsArrContent = new JSONArray();
		for (String id : idGameMap.keySet()) {
			// Initialize
			JSONObject jsonObjectIdGame = new JSONObject();
			JSONObject jsonObjectIdContent = new JSONObject();

			// Add attributes
			Game game = idGameMap.get(id);
			jsonObjectIdGame.put("id", id);
			jsonObjectIdGame.put("name", game.getName());
			jsonObjectIdGame.put("shortDesc", game.getShortDesc());
			jsonObjectIdGame.put("desc", game.getDesc());
			jsonObjectIdGame.put("imageUrl", game.getImageUrl());
			jsonObjectIdGame.put("reviews", game.getReview());
			jsonObjectIdContent.put("id",id);
			jsonObjectIdContent.put("content",game.getContent());

			// Add to list
			jsArrGames.add(jsonObjectIdGame);
			jsArrContent.add(jsonObjectIdContent);
		}
		// Write down
		writerIdGame.write(jsArrGames.toJSONString());
		writerIdContent.write(jsArrContent.toJSONString());
		// Close
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
