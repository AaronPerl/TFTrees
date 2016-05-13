package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Biconditional extends BinaryOperator {

	public Biconditional(Statement a, Statement b) {
		super(a, b);
	}

	public boolean verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) {
		if (branches.size() != 2)
			return false;
		Conjunction AandB = new Conjunction(statements.get(0),statements.get(1));
		Conjunction NotAandNotB = new Conjunction(new Negation(statements.get(0)), new Negation(statements.get(1)));
		List<List<Statement>> branch1 = new ArrayList<List<Statement>>();
		branch1.add(branches.get(0));
		List<List<Statement>> branch2 = new ArrayList<List<Statement>>();
		branch2.add(branches.get(1));
		return 	(AandB.verifyDecomposition(branch1, constants, constantsBefore) && NotAandNotB.verifyDecomposition(branch2, constants, constantsBefore)) ||
				(AandB.verifyDecomposition(branch2, constants, constantsBefore) && NotAandNotB.verifyDecomposition(branch1, constants, constantsBefore));
	}

	public String toString() {
		return statements.get(0).toStringParen() + " \u2194 " + statements.get(1).toStringParen();
	}

	public boolean equals(Statement other) {
		if (!(other instanceof Biconditional))
			return false;
		Biconditional otherBiconditional = (Biconditional) other;
		for (int i = 0; i < 2; i++)
		{
			if (!statements.get(i).equals(otherBiconditional.getOperands().get(i)))
				return false;
		}
		return true;
	}

}
