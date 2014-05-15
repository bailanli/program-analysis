package hw.dataflow.samples;

public class Example1 {


	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.out.println("Usage: <> ");
		}
		
		int number1 = Integer.parseInt(args[0]);
		int number2 = Integer.parseInt(args[1]);
		
		if (number1<0) {
			number1=Math.abs(number1);
		}
		if (number2<0) {
			number2=Math.abs(number2);
		}
		
		int sum =0;
		
		for (int i=0; i< number1; i++) {
			sum = sum + number2;
		}
		
		if (sum>0) {
			System.out.println("Positive news, the operation was greater than zero.");
			System.out.println("Sum = " + sum);
		}
		

	}

}
