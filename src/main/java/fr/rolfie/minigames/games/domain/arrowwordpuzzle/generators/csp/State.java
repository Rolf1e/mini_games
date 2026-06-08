package fr.rolfie.minigames.games.domain.arrowwordpuzzle.generators.csp;

import java.util.ArrayList;
import java.util.List;

public class State {

	final List<Variable> variables;

	final List<String> wordsUsed;
	
	String selectedWord;

	State(List<Variable> variables) {
		this.variables = variables;
		wordsUsed = new ArrayList<>();

        for (Variable variable : variables) {
            if (variable.mostConstrainingVariableHeuristic == -1) {
                wordsUsed.add(variable.currentValue);
            }
        }

	}

	public String toString() {
		return variables.toString();
	}

}
