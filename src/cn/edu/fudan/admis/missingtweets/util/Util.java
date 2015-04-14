package cn.edu.fudan.admis.missingtweets.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Util
{
	private Util()
	{
		throw new AssertionError();
	}

    /**
     * read file
     * @param path is the file's path
     * @param lines is an ArrayList
     * @throws IOException
     */
    public static void readFile(String path, ArrayList<String> lines) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(""));
        try {
            String line;
            while ((line = br.readLine()) != null)
            {
                lines.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * general usage
     * @param c
     * @param args
     */
	public static void Usage(Class<?> c, String[] args)
	{
		System.err.print("Usage: ");
		System.err.print(c.getSimpleName());
		for (String string : args)
		{
			System.err.print(" [" + string + "]");
		}
		System.err.println();
		System.exit(1);
	}
}
