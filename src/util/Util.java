package util;

public class Util {
	private Util()
	{
		throw new AssertionError();
	}

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
