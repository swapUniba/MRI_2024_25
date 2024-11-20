/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran.rocchio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import com.google.gson.Gson;

import di.uniba.it.mri2324.lucene.cran.CranQuery;
import di.uniba.it.mri2324.lucene.cran.rocchio.entity.BoW;
import di.uniba.it.mri2324.lucene.cran.rocchio.entity.BoWUtils;
import di.uniba.it.mri2324.lucene.cran.rocchio.entity.TermEntry;

/**
 *
 * @author pierpaolo
 */
public class CranRocchioSearcher {

    private static final int MAX_TERM = 40;

    private static final int TOP_K = 5;

    private static final float ALPHA = 0.8f;

    private static final float BETA = 0.2f;

    private static BoW getDocumentBow(IndexSearcher searcher, int docid, Set<String> fieldsSet, boolean computeTfidf) throws IOException {
        IndexReader ireader = searcher.getIndexReader();
        Fields fields = ireader.getTermVectors(docid);
        BoW bow = new BoW();
        for (String field : fields) {
            if (fieldsSet.contains(field)) {
                Terms terms = fields.terms(field);
                TermsEnum termsEnum = terms.iterator();
                BytesRef term = null;
                while ((term = termsEnum.next()) != null) {
                    String word = term.utf8ToString();
                    if (!computeTfidf) {
                        PostingsEnum postings = termsEnum.postings(null, PostingsEnum.FREQS);
                        while (postings.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
                            bow.putWord(word, postings.freq());
                        }
                    } else {
                        PostingsEnum postings = termsEnum.postings(null, PostingsEnum.FREQS);
                        while (postings.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
                            double tfidf = (1 + Math.log(postings.freq())) * Math.log(ireader.numDocs() / ireader.docFreq(new Term(field, word)));
                            float tfidf_float = (float) tfidf;
                            bow.putWord(word, tfidf_float);
                        }
                    }
                }
            }
        }
        return bow;
    }

    /**
     *
     * @param reader
     * @param analyzer
     * @return
     * @throws IOException
     */
    private static List<String> getTokens(Reader reader, Analyzer analyzer) throws IOException {
        List<String> tokens = new ArrayList<>();
        TokenStream tokenStream = analyzer.tokenStream("text", reader);
        tokenStream.reset();
        CharTermAttribute cattr = tokenStream.addAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            String token = cattr.toString();
            tokens.add(token);
        }
        tokenStream.end();
        tokenStream.close();
        return tokens;
    }

    /**
     * arguments: "query file" "index directory" "output file"
     *
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws org.apache.lucene.queryparser.classic.ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {
        String query_file = "resources\\cran\\cran.qry_1.json";
        String index_directory = "resources\\cran_index";
        String output_file = "resources\\cran\\cran.rocchio_res.out";
        args = new String[]{query_file, index_directory, output_file};
        FSDirectory fsdir = FSDirectory.open(new File(args[1]).toPath());
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(fsdir));
        Analyzer analyzer = new StandardAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[2]));
        Set<String> fieldsSet = new HashSet<>();
        fieldsSet.add("title");
        fieldsSet.add("text");
        Gson gson = new Gson();
        QueryParser qp = new QueryParser("text", analyzer);
        int idq = 1;
        while (reader.ready()) {
            CranQuery query = gson.fromJson(reader.readLine(), CranQuery.class);
            String t = query.getQuery().replace("?", "").replace("*", "");
            Query lq = qp.parse(t);
            System.out.println("IDQ: " + idq + ", original query: " + lq);
            // search using the original query and build the BoW of relevant documents
            List<BoW> relDocs = new ArrayList<>();
            TopDocs topdocs = searcher.search(lq, 100);
            for (int i = 0; i < TOP_K && i < topdocs.scoreDocs.length; i++) {
                BoW documentBow = getDocumentBow(searcher, i, fieldsSet, true);
                BoWUtils.normalize(documentBow);
                relDocs.add(documentBow);
            }
            // build the centroid of relevant documents
            BoW bowReldocs = BoWUtils.average(relDocs.toArray(new BoW[relDocs.size()]));
            // scalar product BETA*centroid of relevant documents
            BoWUtils.scalarProduct(BETA, bowReldocs);
            // extract query tokens using the analyser
            List<String> qtokens = getTokens(new StringReader(t), analyzer);
            // build the BoW of query terms
            BoW bowQuery = new BoW();
            for (String qt : qtokens) {
                // weight = ALPHA*1
                bowQuery.putWord(qt, ALPHA);
            }
            // normalize BoW of query terms
            BoWUtils.normalize(bowQuery);
            // create new query
            BoW newQuery = BoWUtils.add(bowReldocs, bowQuery);
            List<TermEntry> topTerms = BoWUtils.getTopTerms(newQuery, MAX_TERM);
            BooleanQuery.Builder qb = new BooleanQuery.Builder();
            // to add
            for (TermEntry e : topTerms) {
                qb.add(new BoostQuery(new TermQuery(new Term("text", e.getWord())), e.getWeight()), BooleanClause.Occur.SHOULD);
            }
            Query newq = qb.build();
            System.out.println("IDQ: " + idq + ", new query: " + newq);
            topdocs = searcher.search(newq, 100);
            int r = 1;
            for (ScoreDoc sd : topdocs.scoreDocs) {
                writer.append(String.valueOf(idq)).append(" 0 ");
                writer.append(searcher.doc(sd.doc).get("id"));
                writer.append(" ").append(String.valueOf(r));
                writer.append(" ").append(String.valueOf(sd.score));
                writer.append(" exp0");
                writer.newLine();
                r++;
            }
            idq++;
        }
        reader.close();
        writer.close();
    }

}
