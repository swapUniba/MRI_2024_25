/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.se;

import java.io.File;
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
 * @author marco
 */
public class SearchSE {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws org.apache.lucene.queryparser.classic.ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {
        //if (args.length > 1) {
            FSDirectory fsdir = FSDirectory.open(new File("./resources/indexbooks").toPath());
            //FSDirectory fsdir = FSDirectory.open(new File(args[0]).toPath());
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(fsdir));
            //Create the query parser with the default field and analyzer
            QueryParser qp = new QueryParser("text", new StandardAnalyzer());
            //Parse the query
            Query q = qp.parse("aliens world war");
            //Query q = qp.parse("woman");
            //Query q = qp.parse(args[1]);
            //Search
            TopDocs topdocs = searcher.search(q, 10);
            for (ScoreDoc sdoc : topdocs.scoreDocs) {
                System.out.println("Found doc, path=" + searcher.doc(sdoc.doc).get("id") + ", score " + sdoc.score);
            }
        }
//    }

}
