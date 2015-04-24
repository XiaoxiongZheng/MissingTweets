package cn.edu.fudan.admis.missingtweets.preprocessing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by zhengxx on 15/4/21.
 */
public class LuceneTest {
    public static void main(String[] args) throws IOException {

        CharArraySet stop_words = new CharArraySet(StopWords.stop_words, true);
        Analyzer analyzer = new EnglishAnalyzer(stop_words);
        String text = "fly to i'm it is that's don't sky flies and time to child children";
        TokenStream stream = analyzer.tokenStream("field", text);
        CharTermAttribute term = stream.addAttribute(CharTermAttribute.class);
        try{
            stream.reset();
            while(stream.incrementToken())
            {
                System.out.println(term.toString());
            }
        }
        finally {
            stream.close();
        }
    }
}
