package cn.edu.fudan.admis.missingtweets.algorithms;


import cn.edu.fudan.admis.missingtweets.util.Config;
import cn.edu.fudan.admis.missingtweets.util.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LDA {
    private int[][] doc;                                    //word index array
    private int termNum;                                    //terms' number
    private int docNum;                                     //documents' number
    private int[][] z;                                      //topic label array
    private int[][] docTopic;                               //given document m, count times of topic k, docNum * topicNum
    private int[][] topicTerm;                              //given topic k, count times of term t, topicNum * termNum
    private int[] docTopicSum;                              //sum for each row in docTopic
    private int[] topicTermSum;                             //sum for each row in topicTerm
    private double[][] phi;                                 //parameters for topic-word distribution, topicNum * termNum
    private double[][] theta;                               //parameters for doc-topic distribution, docNum * topicNum
    private double alpha;                                   //hype-parameters for docs' prior distribution
    private double beta;                                    //hype-parameters for topics' prior distribution
    private int topicNum;                                   //topics' number
    private int saveStep;                                   //save steps
    private int iterations;                                 //iterations
    private int beginSaveIters;

    /**
     * default constructor
     * get initial config arguments
     */
    public LDA()
    {
        alpha = Config.alpha;
        beta = Config.beta;
        topicNum = Config.topicNum;
        saveStep = Config.saveStep;
        iterations = Config.iterations;
        beginSaveIters = Config.beginSaveIters;
    }

    /**
     * initial LDA Model parameters
     * @param docs is corpus
     */
    public void initialModel(Documents docs)
    {
        docNum = docs.getDocs().size();
        termNum = docs.getTermToIndexMap().size();
        docTopic = new int[docNum][topicNum];
        topicTerm = new int[topicNum][termNum];
        docTopicSum = new int[docNum];
        topicTermSum = new int[topicNum];
        phi = new double[topicNum][termNum];
        theta = new double[docNum][topicNum];

        doc = new int[docNum][];
        for (int i = 0; i < docNum; i++)
        {
            int docTermsNum = docs.getDocs().get(i).docWords.length;
            doc[i] = new int[docTermsNum];
            for (int j = 0; j < docTermsNum; j++)
                doc[i][j] = docs.getDocs().get(i).docWords[j];
        }

        z = new int[docNum][];
        for (int i = 0; i < docNum; i++)
        {
            int docTermsNum = docs.getDocs().get(i).docWords.length;
            z[i] = new int[docTermsNum];

            for (int j = 0; j < docTermsNum; j++)
            {
                int initTopic = (int)(Math.random() * topicNum);
                z[i][j] = initTopic;
                docTopic[i][initTopic]++;
                topicTerm[initTopic][doc[i][j]]++;
                topicTermSum[initTopic]++;
            }
            docTopicSum[i] = docTermsNum;
        }
    }

    /**
     *
     * @param docs is a corpus
     * @throws IOException
     */
    public void inferenceModel(Documents docs) throws IOException {
        if (iterations < saveStep + beginSaveIters)
        {
            System.out.println("Error: the number of iterations should be larger than " + (saveStep + beginSaveIters));
            System.exit(0);
        }

        for (int i = 0; i < iterations; i++)
        {
            System.out.println("Iteration " + i);
            if (i >= beginSaveIters && ((i - beginSaveIters) % saveStep) == 0)
            {
                System.out.println("Saving model at iteration " + i + " ... ");
                updateEstimatedParameters();
                saveIterateModel(i, docs);
            }

            for (int m = 0; m < docNum; m++)
            {
                int docTermsNum = docs.getDocs().get(m).docWords.length;
                for (int n = 0; n < docTermsNum; n++)
                {
                    int newTopic = sampleTopic(m, n);
                    z[m][n] = newTopic;
                }
            }
        }
    }

    private void updateEstimatedParameters(){
        for (int i = 0; i < topicNum; i++)
        {
            for (int j = 0; j < termNum; j++)
            {
                phi[i][j] = (topicTerm[i][j] + beta) / (topicTermSum[i] + beta);
            }
        }

        for (int i = 0; i < docNum; i++)
        {
            for (int j = 0; j < topicNum; j++)
            {
                theta[i][j] = (docTopic[i][j] + alpha) / (topicTermSum[i] + alpha);
            }
        }
    }

    private int sampleTopic(int m, int n)
    {
        int currTopic = z[m][n];
        docTopic[m][currTopic]--;
        topicTerm[currTopic][doc[m][n]]--;
        docTopicSum[m]--;
        topicTermSum[currTopic]--;

        double[] p = new double[topicNum];
        for (int k = 1; k < topicNum; k++)
            p[k] += p[k - 1];

        double u = Math.random() * p[topicNum - 1];
        int newTopic;
        for (newTopic = 0; newTopic < topicNum; newTopic++)
            if (u < p[newTopic])
                break;

        docTopic[m][newTopic]++;
        topicTerm[newTopic][doc[m][n]]++;
        docTopicSum[m]++;
        topicTermSum[newTopic]++;
        return newTopic;
    }

    public void saveIterateModel(int iters, Documents docs) throws IOException {
        String modelName = "lda_" + iters;
        ArrayList<String> lines = new ArrayList<>();
        lines.add("alpha = " + alpha);
        lines.add("beta = " + beta);
        lines.add("topicNum = " + topicNum);
        lines.add("docNum = " + docNum);
        lines.add("termNum = " + termNum);
        lines.add("iterations = " + iterations);
        lines.add("saveStep = " + saveStep);
        lines.add("beginSaveIters = " + beginSaveIters);

        String prefixName = Config.ldaResultPath + modelName;
        Util.writeFile(prefixName + ".params", lines);

        BufferedWriter bw = new BufferedWriter(new FileWriter(prefixName + ".phi"));
        for (int i = 0; i < topicNum; i++)
        {
            for (int j = 0; j < termNum; j++)
                bw.write(phi[i][j] + "\t");
            bw.write("\n");
        }
        bw.close();

        bw = new BufferedWriter(new FileWriter(prefixName + ".theta"));
        for (int i = 0; i < docNum; i++)
        {
            for (int j = 0; j < topicNum; j++)
                bw.write(theta[i][j] + "\t");
            bw.write("\n");
        }
        bw.close();

        bw = new BufferedWriter(new FileWriter(prefixName + ".z"));
        for (int i = 0; i < docNum; i++)
        {
            for (int j = 0; j < doc[i].length; j++)
                bw.write(doc[i][j] + ":" + z[i][j] + "\t");
            bw.write("\n");
        }
        bw.close();

        // todo
        // output top words in every topic
        bw = new BufferedWriter(new FileWriter(prefixName + ".top"));
        for (int i = 0; i < topicNum; i++)
        {
            List<Integer> topWordsIndexArray = new ArrayList<>();
            for (int j = 0; j < termNum; j++)
                topWordsIndexArray.add(j);
            Collections.sort(topWordsIndexArray, new LDA.topWordsComparable(phi[i]));
            bw.write("topic: " + i + "\n");
            for (int j = 0; j < Config.topNum; j++)
                bw.write(docs.getIndexToTermMap().get(topWordsIndexArray.get(j)) + " " + phi[i][topWordsIndexArray.get(j)] + "\t");
            bw.write("\n");
        }
        bw.close();
    }

    public class topWordsComparable implements Comparator<Integer>
    {
        private double[] sortPro;

        public topWordsComparable(double[] sortPro)
        {
            this.sortPro = sortPro;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            if (sortPro[o1] > sortPro[o2]) return -1;
            else if (sortPro[o1] < sortPro[o2]) return 1;
            else return 0;
        }
    }

}



























