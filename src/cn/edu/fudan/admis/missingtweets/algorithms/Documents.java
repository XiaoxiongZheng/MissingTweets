package cn.edu.fudan.admis.missingtweets.algorithms;

import cn.edu.fudan.admis.missingtweets.preprocessing.DataProcessing;
import cn.edu.fudan.admis.missingtweets.util.Config;
import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengxx on 15/4/14.
 */
public class Documents {
    private ArrayList<Document> docs;                                               // corpus
    // todo termToIndexMap is only calculate how many different term in the corpus
    private Map<String, Integer> termToIndexMap;                                    // every term map to the document
    private ArrayList<String> indexToTermMap;                                       // the first time which term appeared
    private Map<String, Integer> termCountMap;                                      // every term's number

    Documents(){
        docs = new ArrayList<>();
        termToIndexMap = new HashMap<>();
        indexToTermMap = new ArrayList<>();
        termCountMap = new HashMap<>();
    }

    /**
     * read docs
     * @throws IOException
     */

    public void readDocs() throws IOException {
        File dir = new File(Config.dirPath);
        // todo if dir is null, how to handle
        for (File docFile : dir.listFiles())
        {
            Document doc = new Document(docFile.getAbsolutePath());
            doc.readDoc(termToIndexMap, indexToTermMap, termCountMap);
            docs.add(doc);
        }
    }

    public static class Document{

        private String filename;
        int[] docWords;
        public Document(String name){
            filename = name;
        }

        /**
         * read documents
         * @throws IOException
         */
        public void readDoc(Map<String, Integer> termToIndexMap, ArrayList<String> indexToTermMap, Map<String, Integer> termCountMap) throws IOException {
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String> words = new ArrayList<>();
            Util.readFile(filename, lines);
            for (String line : lines)
                DataProcessing.processLine(line, words);
            this.docWords = new int[words.size()];
            for (int i = 0; i < words.size(); i++)
            {
                String word = words.get(i);
                if (!termToIndexMap.containsKey(word))
                {
                    int newIndex = termToIndexMap.size();
                    termToIndexMap.put(word, newIndex);
                    indexToTermMap.add(word);
                    termCountMap.put(word, 1);
                    docWords[i] = newIndex;
                }
                else
                {
                    docWords[i] = termToIndexMap.get(word);
                    termCountMap.put(word, termCountMap.get(word) + 1);
                }
            }
            words.clear();
        }

    }

    public ArrayList<Document> getDocs() {
        return docs;
    }

    public Map<String, Integer> getTermToIndexMap() {
        return termToIndexMap;
    }

    public ArrayList<String> getIndexToTermMap() {
        return indexToTermMap;
    }

    public Map<String, Integer> getTermCountMap() {
        return termCountMap;
    }
}
