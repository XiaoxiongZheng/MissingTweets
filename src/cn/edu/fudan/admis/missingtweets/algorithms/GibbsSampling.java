package cn.edu.fudan.admis.missingtweets.algorithms;

import cn.edu.fudan.admis.missingtweets.util.Config;

import java.io.IOException;

/**
 * Created by zhengxx012 on 2015/3/14.
 */
public class GibbsSampling {
    public static void main(String[] args) throws IOException {
        Documents docs = new Documents();
        docs.readDocs();
        LDA ldaModel = new LDA();
        System.out.println("Initialize the model ... ");
        ldaModel.initialModel(docs);
        System.out.println("Inference the model ... ");
        ldaModel.inferenceModel(docs);
        System.out.println("Output the model ... ");
        ldaModel.saveIterateModel(Config.iterations, docs);
        System.out.println("Done ... ");
    }
}
