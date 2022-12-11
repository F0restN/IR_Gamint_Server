package system.utils;

import system.indexHandler.MyIndexReader;
import system.classes.Query;

import java.io.IOException;
import java.util.HashMap;

public class Calculator {
    public MyIndexReader indexReader;
    public long collectionLength;

    public Calculator(){
        super();
    }
    /*
     * @param [optional] mu
     * */
    public Calculator(MyIndexReader ixreader) throws IOException {
        this.indexReader = ixreader;
        this.collectionLength = ixreader.collectionLength();
    }

    public double queryScoreQLRFM(){
        return 0.0;
    }

    public double queryScoreQLM(Query aQuery, int[] docId, int mu) throws IOException {
        double score = 1.00;
        String[] tokens = aQuery.GetQueryContent().split(" ");
        for (String token: tokens){
            score *= tokenDirichletSmoothProb(token, docId, mu);
        }
        return score;
    }

    /**
     *
     * @param tokenRFScore [each token score from rf]
     * @param token [token itself]
     * @param docId [which document we are evaluating]
     * @param alpha [weight]
     * @param mu [weight]
     * @return score [overall score for a single token]
     * @throws IOException
     */
    public double tokenScoreQLRFM(HashMap<String,Double> tokenRFScore, String token, int docId, double alpha, int mu) throws IOException {
        double orig = tokenDirichletSmoothProb(token, new int[]{docId}, mu);
        double feed = tokenRFScore.getOrDefault(token, 1.0);
        double score = ((alpha * orig) + ((1 - alpha) * feed));
//        return score > 0 ? score : 1.0;
        return score;
    }

    public double tokenDirichletSmoothProb(String token, int[] docId, int mu) throws IOException {
        long docLength = 0;
        long wordCountInDoc = 0;
        for (int id: docId) {
            // Document Length
            docLength += indexReader.docLength(id);
            // Term frequency in document
            wordCountInDoc += indexReader.DocTermsFreq(token, id);
        }

        // Term frequency in collection
        long wordCountInCol = indexReader.CollectionFreq(token);

        // Prior probability of word w = wordCountInCollection / collectionLength
        double priorProb = (double) (wordCountInCol / collectionLength);
        return ((wordCountInDoc + mu * priorProb) / (docLength + mu));
    }
}

