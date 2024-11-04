/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2425.lucene;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 *
 * @author marco
 */
public class TestAnalyzer {

    /**
     *
     * @param reader
     * @param analyzer
     * @return
     * @throws IOException
     */
    public static List<String> getTokens(Reader reader, Analyzer analyzer) throws IOException {
        List<String> tokens = new ArrayList<>();
        TokenStream tokenStream = analyzer.tokenStream("text", reader);
        tokenStream.reset();
        CharTermAttribute cattr = tokenStream.addAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            String token = cattr.toString();
            tokens.add(token);
        }
        tokenStream.end();
        return tokens;
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println(getTokens(new StringReader("the full-text indexing and search API: lucene.apache.org"), new WhitespaceAnalyzer()));
        System.out.println(getTokens(new StringReader("the full-text indexing and search API: lucene.apache.org"), new StandardAnalyzer()));
        System.out.println(getTokens(new StringReader("the full-text indexing and search API: lucene.apache.org"), new StandardAnalyzer(new FileReader("resources/en_stopword"))));
        System.out.println(getTokens(new StringReader("the full-text indexing and search API: lucene.apache.org"), new EnglishAnalyzer()));
        System.out.println(getTokens(new StringReader("the full-text indexing and search API: lucene.apache.org"), new MyAnalyzer()));
    }

}
