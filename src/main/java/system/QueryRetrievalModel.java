package system;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import system.indexHandler.MyIndexReader;
import system.classes.Path;
import system.classes.Document;

public class QueryRetrievalModel {

	protected MyIndexReader indexReader;

	private Directory directory;
	private DirectoryReader ireader;
	private IndexSearcher indexSearcher;

	public QueryRetrievalModel() {
		try {
			directory = FSDirectory.open(Paths.get(Path.IndexData));
			ireader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(ireader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Document> retrieveQuery(String aQuery, int TopN) throws Exception {
		List<Document> results = new ArrayList<>();
		Query theQ = new QueryParser("CONTENT", new WhitespaceAnalyzer()).parse(aQuery);
		ScoreDoc[] scoreDoc = indexSearcher.search(theQ, TopN).scoreDocs;
//		for (ScoreDoc score : scoreDoc) {
//			results.add(new Document(score.doc + "", ireader.document(score.doc).get("DOCNO"), score.score));
//		}
		return results;
	}

}