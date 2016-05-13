package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Disjunction extends LogicalOperator {
	/**
	 * Creates a Disjunction of the provided statements
	 * @param disjuncts The Statements being disjuncted
	 */
	public Disjunction(Statement... disjuncts) {
		statements = new ArrayList<Statement>();
		Collections.addAll(statements, disjuncts);
	}
	
	public Disjunction(List<Statement> disjuncts) {
		statements = new ArrayList<Statement>();
		statements.addAll(disjuncts);
	}
	
	public String toString()
	{
		ArrayList<Statement> statementsAL = (ArrayList<Statement>) statements;
		String statementString = "";
		for (int i = 0; i < statementsAL.size()-1; i++)
			statementString += statementsAL.get(i).toStringParen() + " \u2228 ";
		return statementString + statementsAL.get(statementsAL.size()-1).toStringParen();
	}

	public boolean verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore)
	{
		if (branches.size() != statements.size()) // there must be one branch per disjunct
			return false;
		// boolean arrays in Java default to false
		boolean disjuncts[] = new boolean[statements.size()]; 	// Every disjunct must match up to a branch...
		for (List<Statement> curBranch : branches) 				// ... and every branch must match up to a disjunct
		{
			boolean satisfied = false;
			if (curBranch.size() != 1) 					// Every branch must have one and only one statement in it
				return false;
			for (int i = 0; i < statements.size(); i++) // try to map a disjunct to the current branch
			{
				if (disjuncts[i]) continue; 					// skip already used disjuncts 
				if (statements.get(i).equals(curBranch.get(0))) // the current branch is equal to this disjunct
				{
					satisfied = true;		// branch satisfied
					disjuncts[i] = true; 	// flag this disjunct to avoid duplicate use
					break;
				}
			}
			if (!satisfied) // no disjunct matched this branch
				return false;
		}
		return true;
	}

	public boolean equals(Statement other)
	{
		if (!(other instanceof Disjunction))
			return false;
		List<Statement> otherStatements = ((Disjunction) other).getOperands();
		if (statements.size() != otherStatements.size())
			return false;
		for (int i = 0; i < statements.size(); i++)
		{
			//TODO accept statements in different order?
			if ( !(statements.get(i).equals(otherStatements.get(i))) )
				return false;
		}
			return true;
	}

	@Override
	public Set<String> getVariables() {
		Set<String> union = new LinkedHashSet<String>();
		for (Statement curStatement : statements)
		{
			union.addAll(curStatement.getVariables());
		}
		return union;
	}

	@Override
	public Set<String> getConstants() {
		Set<String> union = new LinkedHashSet<String>();
		for (Statement curStatement : statements)
		{
			union.addAll(curStatement.getConstants());
		}
		return union;
	}
}
