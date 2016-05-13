package perl.aaron.TruthTrees.logic;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ExistentialQuantifier extends Quantifier {

	public ExistentialQuantifier(Variable var, Statement statement) {
		super(var, statement);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "\u2203" + var.toString() + " " + statement.toStringParen();
	}

	@Override
	public String toStringParen() {
		return toString();
	}

	@Override
	public boolean verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) {

		System.out.println("Constants before: " + constantsBefore.toString());
		if (branches.size() == 1) // Single intantiation with new constant
		{
			System.out.println("Instantiated with a new constant");
			if (branches.get(0).size() != 1) // There should be only 1 statement
				return false;
			Binding b = branches.get(0).get(0).determineBinding(statement);
			if ( b != null && !constantsBefore.contains( b.getConstant().toString() ) )
				return true;
			else
				return false;
		}
		else if (branches.size() > 1)
		{
			System.out.println("Instantiated with all old constants as well as a new one");
			Set<String> constantsInstatiated = new LinkedHashSet<String>();
			for (List<Statement> curBranch : branches)
			{
				if (curBranch.size() != 1) // There should be exactly 1 statement per branch
					return false;
				Binding b = curBranch.get(0).determineBinding(statement);
				if (b != null)
				{
					constantsInstatiated.add(b.getConstant().toString());
				}
				else return false; // One of these bindings is incorrect
			}
			System.out.println("Constants instatiated: " + constantsInstatiated.toString());
			// Check if every constant has been instantiated as well as a new constant
			if (constantsInstatiated.size() == constantsBefore.size() + 1 &&
					constantsInstatiated.containsAll(constantsBefore))
			{
				return true;
			}
			else return false; // Not every constant has been instantiated
				
		}
		else
		{
			return false;
		}
	}

}
