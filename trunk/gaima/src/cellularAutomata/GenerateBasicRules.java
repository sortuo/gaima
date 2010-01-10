package cellularAutomata;

import java.util.HashSet;
import java.util.Set;

public class GenerateBasicRules {

	public int[] neighbors;
	public int[] ruleOutput;
	public int maxNeighbors;
	public int maxRules;

	Set<int[]> rules = new HashSet<int[]>();

	public Set<int[]> calculateRules(int paramMaxNeighbors, int paramMaxRules) {
		this.maxNeighbors = paramMaxNeighbors;
		this.maxRules = paramMaxRules;
		neighbors = new int[paramMaxNeighbors];
		ruleOutput = new int[paramMaxRules];

		for (int j = 0; j < (maxRules); j++) {
			ruleOutput[j] = j;
		}

		for (int i = 0; i < (maxNeighbors); i++) {
			neighbors[i] = i;
		}
		rules.add(neighbors);
		rules.add(ruleOutput);
		return rules;
	}
}
