/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author marco
 */
public class TestSearch2 {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws ParseException 
     */
    public static void main(String[] args) throws IOException, ParseException {
        
    	FSDirectory fsdir = FSDirectory.open(new File("./resources/helloworld").toPath()); 
        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(fsdir));
        
        //Single term query
        //Query q = new TermQuery(new Term("name", "parker"));
        
        //Boolean query
        BooleanQuery.Builder qb = new BooleanQuery.Builder();
        qb.add(new TermQuery(new Term("name", "parker")), BooleanClause.Occur.SHOULD);
        qb.add(new TermQuery(new Term("powers", "agility")), BooleanClause.Occur.SHOULD);
        Query q = qb.build();
        
        
        TopDocs topdocs = searcher.search(q, 10);
        System.out.println("Found " + topdocs.totalHits.value + " document(s). Method 1");
        
        ScoreDoc[] hits = topdocs.scoreDocs;
		
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcher.doc(hits[i].doc);
			System.out.println(hitDoc.get("name") + ") " + hitDoc.get("super_name") + " " +  hits[i].score);
		}
		
		
		//Use of Lucene query Syntax
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Query query = new QueryParser("name", analyzer).parse("name:parker OR powers:agility");
		
		topdocs = searcher.search(query, 10);
        System.out.println("\nFound " + topdocs.totalHits.value + " document(s). Method 2");
        
        hits = topdocs.scoreDocs;
		
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcher.doc(hits[i].doc);
			System.out.println(hitDoc.get("name") + ") " + hitDoc.get("super_name") + " " +  hits[i].score);
		}
		
		
		
		//Multifield Query
	    //add all possible fileds in multifieldqueryparser using indexreader getFieldNames method
		QueryParser parser = new MultiFieldQueryParser( new String[] {"name", "super_name", "category", "powers"}, analyzer);
		
		query = parser.parse("agility");
		
		topdocs = searcher.search(query, 10);
        System.out.println("\nFound " + topdocs.totalHits.value + " document(s). Method ");
        
		hits = topdocs.scoreDocs;
		
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = searcher.doc(hits[i].doc);
			System.out.println(hitDoc.get("name") + ") " + hitDoc.get("super_name") + " " +  hits[i].score);
		}

		
        
    }

}
