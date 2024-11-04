/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.se;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author marco
 */
public class IndexSE {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
    	
        if (args.length == 0) {
            
            	//Path where to save index
                //FSDirectory fsdir = FSDirectory.open(new File(args[0]).toPath());
                FSDirectory fsdir = FSDirectory.open(new File("./resources/indexbooks").toPath());
                IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                IndexWriter writer = new IndexWriter(fsdir, iwc);
                
                //List of files in the directory
                //File dir = new File(args[1]);
                File dir = new File("./resources/books");
                if (dir.isDirectory()) {
                	
                File[] files = dir.listFiles();
                int i = 0;
                
                //Inxed files
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".text")) {
                    	
                        Document doc = new Document();
                        doc.add(new StringField("id", file.getAbsolutePath(), Field.Store.YES));
                        doc.add(new TextField("text", new FileReader(file)));
                        writer.addDocument(doc);
                        i = i+1;
                    
                    }
                }
                writer.close();
                System.out.print("Succesfully saved: "+i+" documents!");
            } else {
                System.err.println("The first argument is not a directory.");
            }
        }else {
        	System.err.println("No arguments received!");
        }
    }

}
