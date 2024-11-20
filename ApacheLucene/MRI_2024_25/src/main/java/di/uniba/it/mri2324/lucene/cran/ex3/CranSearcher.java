/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran.ex3;

import com.google.gson.Gson;

import di.uniba.it.mri2324.lucene.cran.CranQuery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author pierpaolo
 */
public class CranSearcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
            FSDirectory fsdir = FSDirectory.open(new File("./resources/cran_index").toPath());
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(fsdir));
            // set classic cosine similarity, remove for BM25 similarity
            //searcher.setSimilarity(new ClassicSimilarity());
            BufferedReader reader = new BufferedReader(new FileReader("./resources/cran/cran.qry.json"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("./resources/cran/cran.out"));
            Gson gson = new Gson();
            QueryParser qp = new QueryParser("text", new StandardAnalyzer());
            int idq=1;
            while (reader.ready()) {
                CranQuery query = gson.fromJson(reader.readLine(), CranQuery.class);
                String t=query.getQuery().replace("?", "").replace("*", ""); // remove special chars
                Query lq = qp.parse(t);
                System.out.println(lq);
                TopDocs topdocs = searcher.search(lq, 100);
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
