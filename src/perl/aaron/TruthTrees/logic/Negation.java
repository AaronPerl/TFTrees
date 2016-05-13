package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Negation extends LogicalOperator {
	
	/**
	 * Creates a Negation of a given statement
	 * @param proposition The Statement to be negated
	 */
	public Negation(Statement proposition)
	{
		statements = Collections.singletonList(proposition);
	}
	
	/**
	 * Returns the negated statement
	 * @return The negated statement
	 */
	public Statement getNegand()
	{
		return statements.get(0);
	}
	
	public String toString() {
		return "\u00AC"+statements.get(0).toStringParen();
	}
	
	public String toStringParen() {
		return toString();
	}

	public boolean equals(Statement other) {
		if (!(other instanceof Negation))
			return false;
		return ((Negation)other).getNegand().equals(statements.get(0));
	}

	public boolean verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) {
		if (statements.get(0) instanceof Conjunction)
		{
			System.out.println("Negation of a conjunction");
			Conjunction con = (Conjunction) statements.get(0);
			ArrayList<Statement> negatedConjuncts = new ArrayList<Statement>(con.getOperands().size());
			for (Statement s : con.getOperands())
				negatedConjuncts.add(new Negation(s));
			return new Disjunction(negatedConjuncts).verifyDecomposition(branches, constants, constantsBefore);
		}
		else if (statements.get(0) instanceof Disjunction)
		{
			System.out.println("Negation of a disjunction");
			Disjunction dis = (Disjunction) statements.get(0);
			ArrayList<Statement> negatedDisjuncts = new ArrayList<Statement>(dis.getOperands().size());
			for (Statement s : dis.getOperands())
				negatedDisjuncts.add(new Negation(s));
			return new Conjunction(negatedDisjuncts).verifyDecomposition(branches, constants, constantsBefore);
		}
		else if (statements.get(0) instanceof Conditional)
		{
			System.out.println("Negation of a conditional");
			Conditional con = (Conditional) statements.get(0);
			ArrayList<Statement> conjuncts = new ArrayList<Statement>(2);
			conjuncts.add(con.getOperands().get(0));
			conjuncts.add(new Negation(con.getOperands().get(1)));
			return new Conjunction(conjuncts).verifyDecomposition(branches, constants, constantsBefore);
		}
		else if (statements.get(0) instanceof Biconditional)
		{
			Biconditional bicon = (Biconditional) statements.get(0);
			Statement a = bicon.getOperands().get(0);
			Statement b = bicon.getOperands().get(1);
			Conjunction con1 = new Conjunction(new Negation(a), b);
			Conjunction con2 = new Conjunction(a, new Negation(b));
			List<List<Statement>> branch1 = new ArrayList<List<Statement>>();
			branch1.add(branches.get(0));
			List<List<Statement>> branch2 = new ArrayList<List<Statement>>();
			branch2.add(branches.get(1));
			return 	(con1.verifyDecomposition(branch1, constants, constantsBefore) && con2.verifyDecomposition(branch2, constants, constantsBefore)) ||
					(con1.verifyDecomposition(branch2, constants, constantsBefore) && con2.verifyDecomposition(branch1, constants, constantsBefore));
		}
		else if (statements.get(0) instanceof Negation) // double negation decomposition
		{
			if (branches.size() != 1)
				return false;
			if (branches.get(0).size() != 1)
				return false;
			Negation negandNegation = (Negation) statements.get(0);
			return (negandNegation.getNegand().equals(branches.get(0).get(0)));
		}
		else if (statements.get(0) instanceof UniversalQuantifier)
		{
			if (branches.size() != 1)
				return false;
			if (branches.get(0).size() != 1)
				return false;
			UniversalQuantifier negandUQ = (UniversalQuantifier) statements.get(0);
			ExistentialQuantifier newQuantifier =
					new ExistentialQuantifier(negandUQ.getQuantifiedVariable(),
							new Negation(negandUQ.getStatement()));
			System.out.println("New: " + newQuantifier);
			System.out.println("Old: " + branches.get(0).get(0));
			return (newQuantifier.equals(branches.get(0).get(0)));
		}
		else if (statements.get(0) instanceof ExistentialQuantifier)
		{
			if (branches.size() != 1)
				return false;
			if (branches.get(0).size() != 1)
				return false;
			ExistentialQuantifier negandEQ = (ExistentialQuantifier) statements.get(0);
			UniversalQuantifier newQuantifier =
					new UniversalQuantifier(negandEQ.getQuantifiedVariable(),
							new Negation(negandEQ.getStatement()));
			System.out.println("New: " + newQuantifier);
			System.out.println("Old: " + branches.get(0).get(0));
			return (newQuantifier.equals(branches.get(0).get(0)));
		}
		else if (!(statements.get(0) instanceof Decomposable))
			return true;
		return false;
	}

	@Override
	public Set<String> getVariables() {
		return statements.get(0).getVariables();
	}

	@Override
	public Set<String> getConstants() {
		return statements.get(0).getConstants();
	}

}
