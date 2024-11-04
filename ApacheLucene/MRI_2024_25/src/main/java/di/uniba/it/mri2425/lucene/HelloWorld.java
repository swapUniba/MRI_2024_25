package di.uniba.it.mri2425.lucene;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
*
* @author marco
*/
public class HelloWorld {

   /**
    * @param args the command line arguments
 * @throws ParseException 
    */
	public static void main(String[] args) throws ParseException {
       try {
           //Open a directory from the file system (index directory)
           FSDirectory fsdir = FSDirectory.open(new File("./resources/documenti_news").toPath());

           //IndexWriter configuration
           IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
           
           //Index directory is created if not exists or overwritten
           iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
           
           
           //Create IndexWriter
           IndexWriter writer = new IndexWriter(fsdir, iwc);
           //Create document and add fields
           
           
           Document doc = new Document();
           doc.add(
        		   new TextField("titolo", "Articolo Web Numero 1", Field.Store.YES)
        		   );
           
           doc.add(new TextField("introduzione", "questa è l'introduzione del mio documento", Field.Store.YES));
           doc.add(new TextField("contenuto", "questo è il contenuto del mio documento", Field.Store.NO));
           doc.add(new TextField("commenti", "questo è un commento di un utente di esempio", Field.Store.NO));
           
           //add document to index
           writer.addDocument(doc);

           //close IndexWriter
           writer.close();

           // Crea un IndexReader
           IndexReader reader = DirectoryReader.open(fsdir);

           // Conta il numero di documenti attivi (non cancellati)
           int numDocs = reader.numDocs();
           System.out.println("Numero di documenti nell'indice: " + numDocs);

           //Create the IndexSearcher
           IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(fsdir));

           //Create the query parser with the default field and analyzer
           QueryParser qp = new QueryParser("contenuto", new StandardAnalyzer());
           
           //Parse the query
           Query q = qp.parse("contenuto");
           
           //Search
           TopDocs topdocs = searcher.search(q, 10);
           System.out.println("Found " + topdocs.totalHits.value + " document(s).");
           
       } catch (IOException ex) {
           Logger.getLogger(HelloWorld.class.getName()).log(Level.SEVERE, null, ex);
       }
   }

}
