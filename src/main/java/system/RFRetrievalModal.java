package system;


import java.io.IOException;
import java.util.*;

import system.classes.Document;
import system.indexHandler.MyIndexReader;
import system.utils.Calculator;
import system.utils.QueryManipulation;
//import Search.*;

public class RFRetrievalModal {

    MyIndexReader ixreader;
    QueryRetrievalModel model;
    List<Document> feedbackDocs;
    Calculator calculator;
    QueryManipulation queryManipulation;
    int mu = 2000;

    public RFRetrievalModal(MyIndexReader ixreader)
    {
        try {
            this.ixreader=ixreader;
            this.model = new QueryRetrievalModel();
            this.calculator = new Calculator(ixreader);
            this.queryManipulation = new QueryManipulation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Search for the topic with pseudo relevance feedback in 2020 Fall assignment 4.
     * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
     *
     * @param Query The query to be searched for.
     * @param TopN The maximum number of returned document
     * @param TopK The count of feedback documents
     * @param alpha parameter of relevance feedback model
     * @return TopN most relevant document, in List structure
     */
    public List<Document> RetrieveQuery(String Query, int TopN, int TopK, double alpha) throws Exception {
        // (1) you should first use the original retrieval model to get TopK documents, which will be regarded as feedback documents
        feedbackDocs = model.retrieveQuery(Query, TopK);

        // (2) implement GetTokenRFScore to get each query token's P(token|feedback model) in feedback document. get P(token|feedback documents)
        HashMap<String,Double> TokenRFScore= GetTokenRFScore(Query,TopK);

        // (3) implement the relevance feedback model for each token: combine the each query token's original retrieval score P(token|document) with its score in feedback documents P(token|feedback model)
        HashMap<Integer, HashMap<String, Double>> DocScoreForEachTokenMap = GetTokenScore(Query, alpha, TokenRFScore);

        // (4) for each document, use the query likelihood language model to get the whole query's new score, P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')
        List<Document> results = GetQueryScoreForEachDoc(DocScoreForEachTokenMap);
        // (5) sort all retrieved documents from most relevant to least, and return TopN
        results.sort(new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {
                if (o1.getScore() != o2.getScore())
                    return o1.getScore() < o2.getScore() ? 1 : -1;
                else
                    return 0;
            }
        });

        return results.subList(0, Math.min(results.size(), TopN));
    }

    /*
     * for each token in the query, you should calculate token's score in feedback documents: P(token|feedback documents)
     * use Dirichlet smoothing
     * save <token, score> in HashMap TokenRFScore, and return it
     * */

    /**
     *
     * @param Query
     * @param TopK
     * @return
     * @throws Exception
     */
    public HashMap<String,Double> GetTokenRFScore(String Query, int TopK) throws Exception
    {
        HashMap<String,Double> TokenRFScore=new HashMap<>();

        // Initialize
        String[] tokens = queryManipulation.querySplit(Query);
        int[] docIds = new int[feedbackDocs.size()];
        for (int i = 0; i < docIds.length; i++) {
            docIds[i] = Integer.parseInt(feedbackDocs.get(i).getDocid());
        }

        // Calculate score for each token
        for (String token: tokens
        ) {
            Double score = calculator.tokenDirichletSmoothProb(token, docIds, mu);
            TokenRFScore.put(token, score);
        }

        return TokenRFScore;
    }

    /**
     *
     * @param Query query
     * @param alpha weight
     * @param tokenRFScore token score from ef
     * @return <[DocID], Map<Token, Score>>
     * @throws IOException exception
     */
    public HashMap<Integer, HashMap<String, Double>> GetTokenScore(String Query, double alpha, HashMap<String, Double> tokenRFScore) throws IOException {
        String[] tokens = queryManipulation.querySplit(Query);
        // Generate Original probability relevant doc list
        TreeSet<Integer> queryRelevantDocSet = new TreeSet<>();
        for (String token : tokens) {
            // Error handle for un-appear token
            int[][] postingList = ixreader.getPostingList(token);
            if(postingList != null){
                for (int[] ints : postingList) {
                    queryRelevantDocSet.add(ints[0]);
                }
            }
        }
        // Calculate overall score for each token
        HashMap<Integer, HashMap<String, Double>> docScoreForEachTokenMap = new HashMap<>();
        for (Integer docId: queryRelevantDocSet) {
            HashMap<String, Double> tokenScore = new HashMap<>();
            for(String token: tokens){
                Double score = calculator.tokenScoreQLRFM(tokenRFScore, token, docId, alpha, mu);
                tokenScore.put(token, score);
            }
            docScoreForEachTokenMap.put(docId, tokenScore);
        }
        return docScoreForEachTokenMap;
    }

    /**
     *
     * @param docScoreForEachTokenMap HashMap<Integer, HashMap<String, Double>>
     * @return List<Document>
     * @throws IOException Nonsense
     */
    public List<Document> GetQueryScoreForEachDoc(HashMap<Integer, HashMap<String, Double>> docScoreForEachTokenMap) throws IOException {
        List<Document> documentList = new ArrayList<>();
        for (Integer docId: docScoreForEachTokenMap.keySet()) {
            // Multiple tokens together
            HashMap<String, Double> tokenScore = docScoreForEachTokenMap.get(docId);
            double score = 1.0;
            for (String token: tokenScore.keySet()) {
                score *= tokenScore.get(token);
            }
            // Add Doc
            documentList.add(new Document(String.valueOf(docId), ixreader.getDocno(docId), score));
        }
        return documentList;
    }

}

