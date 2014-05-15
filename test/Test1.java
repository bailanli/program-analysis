package hw.controlflow.samples;

public class Test1 {
	
	public static void main(String[] args) {
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		int z = Integer.parseInt(args[2]);
		int m;

		if (x < y) {
			if (z < x)
				m = x;
			else if (y < z)
				m = y;
			else
				m = z;
		} else {
			if (z < y)
				m = y;
			else if (x < z)
				m = x;
			else
				m = z;
		}

		System.out.println("Median value is: " + m);
	}
}
