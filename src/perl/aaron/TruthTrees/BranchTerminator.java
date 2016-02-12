package perl.aaron.TruthTrees;

import java.util.ArrayList;

import perl.aaron.TruthTrees.logic.AtomicStatement;
import perl.aaron.TruthTrees.logic.Negation;
import perl.aaron.TruthTrees.logic.Statement;

public class BranchTerminator extends BranchLine {

	public BranchTerminator(Branch branch) {
		super(branch);
	}
	
	public String toString()
	{
		return "\u2715";
	}
	
	public void setIsPremise(boolean isPremise) {}
	public boolean isPremise() { return false; }
	public void setStatement(Statement s) {}
	public void setDecomposedFrom(BranchLine decomposedFrom) {}
	public BranchLine getDecomposedFrom() { return null; }
	
	public String verifyDecomposition()
	{
		if (selectedLines.size() != 2)
		{
			return "Invalid number of supporting statements for branch termination";
		}
		ArrayList<BranchLine> selectedList = new ArrayList<BranchLine>();
		selectedList.addAll(selectedLines);
		AtomicStatement atomic;
		Statement negatedStatement;
		if (selectedList.get(0).getStatement() instanceof AtomicStatement)
		{
			atomic = (AtomicStatement)selectedList.get(0).getStatement();
			negatedStatement = selectedList.get(1).getStatement();
		}
		else if (selectedList.get(1).getStatement() instanceof AtomicStatement)
		{
			atomic = (AtomicStatement)selectedList.get(1).getStatement();
			negatedStatement = selectedList.get(0).getStatement();
		}
		else
			return "No atomic statement found in branch termination justification";
		
		if (!(negatedStatement instanceof Negation))
			return "No negation found in branch termination justification";
		
		Negation negated = (Negation) negatedStatement;
		if (!negated.getNegand().equals(atomic))
			return "Incorrect atomic statement/negation pair in branch termination justification";
		return null;
	}

}
