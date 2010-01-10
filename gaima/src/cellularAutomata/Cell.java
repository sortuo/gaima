package cellularAutomata;

import java.math.BigInteger;

public class Cell {
	
	public static void main (String [] args){
		BigInteger cell = new BigInteger(new byte [] {0x7});
		String binary = cell.toString(2);
		System.out.println(binary);

		System.out.println(Long.toString(7, 2));		
	}
	
}
