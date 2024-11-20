/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran.ex3;

import java.io.BufferedReader;
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

import com.google.gson.Gson;

import di.uniba.it.mri2324.lucene.cran.CranDocument;

/**
 *
 * @author pierpaolo
 */
public class CranIndexer {

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
            // Store TermVectors
            FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
            ft.setStoreTermVectors(true);
            
            FSDirectory fsdir = FSDirectory.open(new File("./resources/cran_index").toPath());
            IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            
            IndexWriter writer = new IndexWriter(fsdir, iwc);
            
            
            BufferedReader reader = new BufferedReader(new FileReader("./resources/cran/cran.all.1400.json"));
            Gson gson = new Gson();
            while (reader.ready()) {
                CranDocument crandoc = gson.fromJson(reader.readLine(), CranDocument.class);
                Document doc = new Document();
                doc.add(new StringField("id", crandoc.getId(), Field.Store.YES));
                doc.add(new Field("title", crandoc.getTitle(), ft));
                doc.add(new Field("text", crandoc.getText(), ft));
                writer.addDocument(doc);
            }
            writer.close();
    }
}
