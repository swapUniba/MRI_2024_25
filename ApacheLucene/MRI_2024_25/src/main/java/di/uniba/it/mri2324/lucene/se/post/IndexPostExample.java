/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.se.post;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Marco
 */
public class IndexPostExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        //define a custom field type that stores post information
        FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
        ft.setStoreTermVectors(true);
        ft.setStoreTermVectorPositions(true);
        ft.setStoreTermVectorOffsets(true);
        
        
        FSDirectory fsdir = FSDirectory.open(new File("./resources/postex").toPath());
        IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(fsdir, iwc);
        
        Document doc1 = new Document();
        doc1.add(new StringField("id", "1", Field.Store.YES));
        doc1.add(new Field("text", new FileReader("./resources/text/es1.txt"), ft));
        writer.addDocument(doc1);
        System.out.println("Document Added");
        
        Document doc2 = new Document();
        doc2.add(new StringField("id", "2", Field.Store.YES));
        doc2.add(new Field("text", new FileReader("./resources/text/es2.txt"), ft));
        writer.addDocument(doc2);
        System.out.println("Document Added");

        writer.close();
    }

}
