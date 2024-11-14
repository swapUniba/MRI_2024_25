/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author pierpaolo
 */
public class CranUtils {

    /**
     *
     * @throws IOException
     */
    public static void generateRandomOutput() throws IOException {
        List<String> docIds = new ArrayList<>();
        File docfile = new File("./resources/cran/cran.all.1400.json");
        BufferedReader reader = new BufferedReader(new FileReader(docfile));
        Gson gson = new Gson();
        while (reader.ready()) {
            CranDocument doc = gson.fromJson(reader.readLine(), CranDocument.class);
            docIds.add(doc.getId());
        }
        reader.close();

        List<String> topicIds = new ArrayList<>();
        File topicfile = new File("./resources/cran/cran.qry.json");
        reader = new BufferedReader(new FileReader(topicfile));
        while (reader.ready()) {
            CranQuery query = gson.fromJson(reader.readLine(), CranQuery.class);
            topicIds.add(query.getId());
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter("./resources/cran/example.out"));
        for (String qid : topicIds) {
            Collections.shuffle(docIds);
            List<String> rank = docIds.subList(0, 100);
            int r = 1;
            for (String docid : rank) {
                writer.append(qid).append(" 0 ").append(docid).append(" ")
                        .append(String.valueOf(r)).append(" ").append(String.valueOf(1f / (float) r)).append(" random");
                writer.newLine();
                r++;
            }
        }
        writer.close();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        generateRandomOutput();
    }

}
