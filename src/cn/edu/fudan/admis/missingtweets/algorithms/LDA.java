package cn.edu.fudan.admis.missingtweets.algorithms;

import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.special.Gamma;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LDA {
	int[][] doc;			
	int v, m;

    // topics' number
    private int k;

	int[][] z;

    private double[] thelta;
	
	// dirichlet prior parmeter
	private double[] alpha;
	private double[] beta;

    private Pow p;
    //constructor function
    LDA(int topics)
    {
        k = topics;
        alpha = new double[k];
        beta = new double[k];
        p = new Pow();
    }

    double theltaCondition()
    {
        double alpha_sum = 0;
        for (double a : alpha)
            alpha_sum += a;
        double gammaNumerator = Gamma.gamma(alpha_sum);
        double gammaDenominator = 1;
        for (double a : alpha)
            gammaDenominator *= Gamma.gamma(a);
        double theltaProduct = 1;
        for (int i = 0; i < k; i++)
            theltaProduct *= p.value(thelta[i], alpha[i] - 1);
        return gammaNumerator * theltaProduct / gammaDenominator;
    }
    void initial() throws IOException {
        String path = "";
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = "";
        while((line = br.readLine())!= null)
        {
            String[] words = line.split(" ");

        }
    }
    void LDATraining()
    {

    }
}
