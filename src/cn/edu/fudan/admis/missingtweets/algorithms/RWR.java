package cn.edu.fudan.admis.missingtweets.algorithms;

public class RWR
{
	private int[][] transitiveMatrix;
	private int dimension;
	private double[] initial;
	private double alpha;

	/**
	 * constructor matrix
	 * 
	 * @param matrix
	 * @param dimension
	 * @param initialVector
	 * @param alpha
	 */
	public RWR(int[][] matrix, int dimension, double[] initialVector,
			double alpha)
	{
		// TODO Auto-generated constructor stub
		transitiveMatrix = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				transitiveMatrix[i][j] = matrix[i][j];
		this.dimension = dimension;
		this.alpha = alpha;
		initial = new double[dimension];
		for (int i = 0; i < dimension; i++)
			initial[i] = initialVector[i];
	}

	/**
	 * set dimension
	 * 
	 * @param d
	 */
	public void setDimension(int d)
	{
		dimension = d;
	}

	/**
	 * set alpha
	 * 
	 * @param a
	 */
	public void setAlpha(double a)
	{
		alpha = a;
	}

	/**
	 * matrix product
	 * 
	 * @return
	 */
	public int[][] transition()
	{
		int[][] temp = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++)
		{
			for (int j = 0; j < dimension; j++)
			{
				temp[i][j] = 0;
				for (int k = 0; k < dimension; k++)
				{
					temp[i][j] = transitiveMatrix[i][k]
							* transitiveMatrix[k][j];
				}
			}
		}
		return temp;
	}

	/**
	 * iterator
	 * 
	 * @param Matrix
	 * @return
	 */
	public double[] iterative(double[] Matrix)
	{
		double[] nextMatrix = new double[dimension];
		for (int i = 0; i < dimension; i++)
		{
			nextMatrix[i] = 0;
			double temp = 0;
			for (int j = 0; j < dimension; j++)
			{
				temp += Matrix[j] * transitiveMatrix[j][i];
			}
			nextMatrix[i] += (1 - alpha) * temp + alpha * initial[i];
		}
		if (!isEqual(Matrix, nextMatrix))
		{
			iterative(nextMatrix);
		}
		return nextMatrix;
	}

	/**
	 * judge whether the matrix is convergence or not
	 * 
	 * @param Matrix
	 * @param nextMatrix
	 * @return
	 */
	public boolean isEqual(double[] Matrix, double[] nextMatrix)
	{
		boolean judge = true;
		for (int i = 0; i < dimension; i++)
		{

			if (Matrix[i] == nextMatrix[i])
			{
				continue;
			}
			else
			{
				judge = false;
			}
		}
		return judge;
	}

}
