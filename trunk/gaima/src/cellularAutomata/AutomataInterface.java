package cellularAutomata;

public interface AutomataInterface {

	public int [] calculateAutomata(int [] cells);
	
	
	/*
	 * Example of automata patterns
	 * if [y-1,x-1] == 1 and [y-1,x] == 0 and [y-1,x+1] == 0 result = 1
	 * 
	 */
}
