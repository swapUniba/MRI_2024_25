/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2324.lucene.cran.rocchio.entity;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author pierpaolo
 */
public class BoWConstructionExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        FSDirectory fsdir = FSDirectory.open(new File("./resources/postex").toPath());
        DirectoryReader ireader = DirectoryReader.open(fsdir);
        int docid = 0;
        Fields fields = ireader.getTermVectors(docid);
        for (String field : fields) {
            Terms terms = fields.terms(field);
            TermsEnum termsEnum = terms.iterator();
            BytesRef term = null;
            BoW bow = new BoW();
            while ((term = termsEnum.next()) != null) {
                String word = term.utf8ToString();
                PostingsEnum postings = termsEnum.postings(null, PostingsEnum.FREQS);
                while (postings.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
                    //term frequency
                    //bow.putWord(word, postings.freq());
                    //TFIDF
                    double tfidf = (1 + Math.log(postings.freq())) * Math.log(ireader.numDocs() / ireader.docFreq(new Term(field, word)));
                    bow.putWord(word, new Float(tfidf));
                }
            }
            System.out.println("Field: " + field);
            System.out.println(bow.toString());
        }

    }
}
