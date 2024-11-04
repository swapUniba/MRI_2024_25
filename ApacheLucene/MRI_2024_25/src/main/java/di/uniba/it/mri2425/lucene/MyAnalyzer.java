/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2425.lucene;

import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

/**
 *
 * @author pierpaolo
 */
public class MyAnalyzer extends Analyzer {

    /**
     *
     */
    public static final CharArraySet STOP_WORDS;

    static {
        final List<String> stopWords = Arrays.asList("a", "an", "and", "are", "the", "is", "but", "by", "org");
        final CharArraySet stopSet = new CharArraySet(stopWords, false);
        STOP_WORDS = CharArraySet.unmodifiableSet(stopSet);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = new LetterTokenizer();
        TokenStream filter = new LowerCaseFilter(source);
        filter = new StopFilter(filter, STOP_WORDS);
        return new TokenStreamComponents(source, filter);

    }

}
