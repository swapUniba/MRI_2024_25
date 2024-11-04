package di.uniba.it.mri2324.lucene;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class AliceInWonderland {


		@SuppressWarnings("resource")
		public static void main(String[] args) throws IOException {
			
					FSDirectory fsdir = FSDirectory.open(new File("./resources/alice").toPath());
					
					StandardAnalyzer analyzer = new StandardAnalyzer();
					
					IndexWriterConfig config = new IndexWriterConfig(analyzer);
					
					config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
					IndexWriter writer = new IndexWriter(fsdir, config);

					//TextField with term vector
					//FieldType ft = new FieldType(TextField.TYPE_STORED);
					//ft.setTokenized(true); //done as default
					//ft.setStoreTermVectors(true);
					//ft.setStoreTermVectorPositions(true);
					//ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
					
					
			 try {
				    //ALICE in WONDERLAND
		            File f = new File("./resources/Alice_Adv_Lewis_Carroll_utf8.txt");
		            BufferedReader b = new BufferedReader(new FileReader(f));

		            String readLine = "";
		            System.out.println("Reading file using Buffered Reader...");
		           
		            String title = b.readLine();
		            //empthy line
		            b.readLine();
		            
		            //author
		            String author = b.readLine(); //riga 3
		            //empthy line
		            b.readLine();
		            
		            String chapter = "";
		            String paragraph = "";
		            
		            while ((readLine = b.readLine()) != null) {
		            	
		                if(readLine.contains("CHAPTER ")) {
		                	chapter = readLine;
		                	//skip next empty line
		                	b.readLine();
		                }else if(!readLine.equals("")){
		                	paragraph = paragraph + readLine;
		                }else {
		                	//Fine paragrafo
		                	System.out.println(chapter);
		                	System.out.println(paragraph.trim());
		                	
		                	//SALVO i dati nell'indice
		    				Document doc = new Document();
		    				doc.add(new TextField("id", title, Field.Store.NO));
		    				doc.add(new TextField("author", author, Field.Store.YES));
		    				doc.add(new TextField("chapter", chapter, Field.Store.YES));
							doc.add(new TextField("chapter_text", paragraph, Field.Store.YES));
		                	//Field textField = new Field("chapter_text", paragraph, ft);
		    				//doc.add(textField);
		    				
		    				writer.addDocument(doc);
		                	
		                	paragraph = "";
		                }
		            }
		            writer.close();

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			
			
		}


}
