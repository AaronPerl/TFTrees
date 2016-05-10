package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Negation extends LogicalOperator {

	private Statement negand;
	
	/**
	 * Creates a Negation of a given statement
	 * @param proposition The Statement to be negated
	 */
	public Negation(Statement proposition)
	{
		negand = proposition;
	}
	
	/**
	 * Returns the negated statement
	 * @return The negated statement
	 */
	public Statement getNegand()
	{
		return negand;
	}
	
	public String toString() {
		return "\u00AC"+negand.toStringParen();
	}
	
	public String toStringParen() {
		return toString();
	}

	public boolean equals(Statement other) {
		if (!(other instanceof Negation))
			return false;
		System.out.println("other is a negation");
		System.out.println("returning " + ((Negation)other).getNegand().equals(negand));
		return ((Negation)other).getNegand().equals(negand);
	}

	public boolean verifyDecomposition(List<List<Statement>> branches) {
		if (negand instanceof Conjunction)
		{
			System.out.println("Negation of a conjunction");
			Conjunction con = (Conjunction) negand;
			ArrayList<Statement> negatedConjuncts = new ArrayList<Statement>(con.getOperands().size());
			for (Statement s : con.getOperands())
				negatedConjuncts.add(new Negation(s));
			return new Disjunction(negatedConjuncts).verifyDecomposition(branches);
		}
		else if (negand instanceof Disjunction)
		{
			System.out.println("Negation of a disjunction");
			Disjunction dis = (Disjunction) negand;
			ArrayList<Statement> negatedDisjuncts = new ArrayList<Statement>(dis.getOperands().size());
			for (Statement s : dis.getOperands())
				negatedDisjuncts.add(new Negation(s));
			return new Conjunction(negatedDisjuncts).verifyDecomposition(branches);
		}
		else if (negand instanceof Conditional)
		{
			System.out.println("Negation of a conditional");
			Conditional con = (Conditional) negand;
			ArrayList<Statement> conjuncts = new ArrayList<Statement>(2);
			conjuncts.add(con.getOperands().get(0));
			conjuncts.add(new Negation(con.getOperands().get(1)));
			return new Conjunction(conjuncts).verifyDecomposition(branches);
		}
		else if (negand instanceof Biconditional)
		{
			Biconditional bicon = (Biconditional) negand;
			Statement a = bicon.getOperands().get(0);
			Statement b = bicon.getOperands().get(1);
			Conjunction con1 = new Conjunction(new Negation(a), b);
			Conjunction con2 = new Conjunction(a, new Negation(b));
			List<List<Statement>> branch1 = new ArrayList<List<Statement>>();
			branch1.add(branches.get(0));
			List<List<Statement>> branch2 = new ArrayList<List<Statement>>();
			branch2.add(branches.get(1));
			return 	(con1.verifyDecomposition(branch1) && con2.verifyDecomposition(branch2)) ||
					(con1.verifyDecomposition(branch2) && con2.verifyDecomposition(branch1));
		}
		else if (negand instanceof Negation) // double negation decomposition
		{
			if (branches.size() != 1)
				return false;
			if (branches.get(0).size() != 1)
				return false;
			Negation negandNegation = (Negation) negand;
			return (negandNegation.getNegand().equals(branches.get(0).get(0)));
		}
		else if (negand instanceof AtomicStatement)
			return true;
		return false;
	}

	@Override
	public Set<String> getVariables() {
		return negand.getVariables();
	}

}
