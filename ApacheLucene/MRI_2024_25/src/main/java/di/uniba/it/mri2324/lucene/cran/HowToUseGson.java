/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author pierpaolo
 */
public class HowToUseGson {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        File docfile=new File("./resources/cran/cran.all.1400.json");
        BufferedReader reader=new BufferedReader(new FileReader(docfile));
        Gson gson=new Gson();
        while (reader.ready()) {
            CranDocument doc = gson.fromJson(reader.readLine(), CranDocument.class);
            System.out.println(doc.getId()+"\t"+doc.getTitle());
        }
        reader.close();
        
        File topicfile=new File("./resources/cran/cran.qry.json");
        reader=new BufferedReader(new FileReader(topicfile));
        while (reader.ready()) {
            CranQuery query = gson.fromJson(reader.readLine(), CranQuery.class);
            System.out.println(query.getId()+"\t"+query.getQuery());
        }
        reader.close();
    }
    
}
