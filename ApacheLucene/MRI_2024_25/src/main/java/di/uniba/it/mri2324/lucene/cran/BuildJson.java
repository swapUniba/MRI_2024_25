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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class BuildJson {

    private static void convertDocs2Json(File docfile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(docfile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(docfile.getAbsolutePath() + ".json"));
        StringBuilder bufI = null;
        StringBuilder bufB = null;
        StringBuilder bufT = null;
        StringBuilder bufW = null;
        StringBuilder bufA = null;
        StringBuilder c = null;
        Gson gson = new Gson();
        while (reader.ready()) {
            String line = reader.readLine();
            if (line.startsWith(".I")) {
                if (bufI != null) {
                    CranDocument doc = new CranDocument(bufI.toString().trim(),
                            bufW.toString().trim(),
                            bufA.toString().trim(),
                            bufT.toString().trim(),
                            bufB.toString().trim());
                    gson.toJson(doc, writer);
                    writer.newLine();
                }
                bufI = new StringBuilder();
                bufB = new StringBuilder();
                bufT = new StringBuilder();
                bufW = new StringBuilder();
                bufA = new StringBuilder();
                bufI.append(line.substring(2).trim());
            } else if (line.startsWith(".B")) {
                c = bufB;
            } else if (line.startsWith(".T")) {
                c = bufT;
            } else if (line.startsWith(".W")) {
                c = bufW;
            } else if (line.startsWith(".A")) {
                c = bufA;
            } else if (c != null) {
                c.append(line).append(" ");
            }
        }
        reader.close();
        if (bufI != null) {
            CranDocument doc = new CranDocument(bufI.toString().trim(),
                    bufW.toString().trim(),
                    bufA.toString().trim(),
                    bufT.toString().trim(),
                    bufB.toString().trim());
            gson.toJson(doc, writer);
            writer.newLine();
        }
        writer.close();
    }

    private static void convertTopics2Json(File topicfile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(topicfile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(topicfile.getAbsolutePath() + ".json"));
        StringBuilder bufI = null;
        StringBuilder bufW = null;
        StringBuilder c = null;
        Gson gson = new Gson();
        while (reader.ready()) {
            String line = reader.readLine();
            if (line.startsWith(".I")) {
                if (bufI != null) {
                    CranQuery query = new CranQuery(String.valueOf(Integer.parseInt(bufI.toString().trim())),
                            bufW.toString().trim());
                    gson.toJson(query, writer);
                    writer.newLine();
                }
                bufI = new StringBuilder();
                bufW = new StringBuilder();
                bufI.append(line.substring(2).trim());
            } else if (line.startsWith(".W")) {
                c = bufW;
            } else if (c != null) {
                c.append(line).append(" ");
            }
        }
        reader.close();
        if (bufI != null) {
            CranQuery query = new CranQuery(String.valueOf(Integer.parseInt(bufI.toString().trim())),
                    bufW.toString().trim());
            gson.toJson(query, writer);
            writer.newLine();
        }
        writer.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                File docfile = new File(args[0] + "/cran.all.1400");
                File topicfile = new File(args[0] + "/cran.qry");
                convertDocs2Json(docfile);
                convertTopics2Json(topicfile);
            } catch (IOException ex) {
                Logger.getLogger(BuildJson.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
