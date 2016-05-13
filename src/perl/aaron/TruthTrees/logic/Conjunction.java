package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Conjunction extends LogicalOperator {
	/**
	 * Creates a Conjunction of the provided statements
	 * @param disjuncts The Statements being conjuncted
	 */
	public Conjunction(Statement... conjuncts) {
		statements = new ArrayList<Statement>();
		Collections.addAll(statements, conjuncts);
	}
	
	public Conjunction(List<Statement> conjuncts) {
		statements = new ArrayList<Statement>();
		statements.addAll(conjuncts);
	}
	
	public String toString()
	{
		ArrayList<Statement> statementsAL = (ArrayList<Statement>) statements;
		String statementString = "";
		for (int i = 0; i < statementsAL.size()-1; i++)
			statementString += statementsAL.get(i).toStringParen() + " \u2227 ";
		return statementString + statementsAL.get(statementsAL.size()-1).toStringParen();
	}

	public boolean verifyDecomposition(List< List<Statement> > branches, Set<String> constants, Set<String> constantsBefore) {
		if (branches.size() != 1) // There should be only 1 branch
			return false;
		System.out.println(branches.toString());
		List<Statement> decomposedList = branches.get(0);
		if (decomposedList.size() != statements.size()) // One decomposed statement per conjunct
			return false;
		boolean[] conjuncts = new boolean[statements.size()]; 	// Every conjunct must match up to a statement...
		for (Statement curStatement : decomposedList)			// ... and every statement must match up to a conjunct
		{
			boolean satisfied = false;
			for (int i = 0; i < statements.size(); i++) // try to map a conjunct to the current statement
			{
				if (conjuncts[i]) continue; 				// skip already used conjuncts 
				if (statements.get(i).equals(curStatement)) // the current statement is equal to this conjunct
				{
					satisfied = true;		// branch satisfied
					conjuncts[i] = true; 	// flag this conjunct to avoid duplicate use
					break;
				}
			}
			if (!satisfied) // no conjunct matched this statement
				return false;
		}
		return true;
	}

	public boolean equals(Statement other) {
		if (!(other instanceof Conjunction))
			return false;
		List<Statement> otherStatements = ((Conjunction) other).getOperands();
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
