package perl.aaron.TruthTrees.logic;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Equality extends Statement implements Decomposable {
	
	LogicObject obj1;
	LogicObject obj2;
	
	public Equality(LogicObject obj1, LogicObject obj2)
	{
		this.obj1 = obj1;
		this.obj2 = obj2;
	}

	@Override
	public boolean verifyDecomposition(List<List<Statement>> branches, Set<String> branchConstants,
			Set<String> constantsBefore) {
		if (branches.size() == 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return obj1.toString() + " = " + obj2.toString();
	}
	
	@Override
	public String toStringParen() {
		return "(" + toString() + ")";
	}

	@Override
	public boolean equals(Statement other) {
		if (other instanceof Equality)
		{
			Equality otherEquality = (Equality) other;
			return (obj1 == otherEquality.obj1 && obj2 == otherEquality.obj2);
		}
		else
		{
			return false;
		}
	}

	@Override
	public Set<String> getVariables() {
		Set<String> vars = new LinkedHashSet<String>();
		vars.addAll(obj1.getVariables());
		vars.addAll(obj2.getVariables());
		return vars;
	}

	@Override
	public Set<String> getConstants() {
		Set<String> vars = new LinkedHashSet<String>();
		vars.addAll(obj1.getConstants());
		vars.addAll(obj2.getConstants());
		return vars;
	}

	@Override
	public Binding determineBinding(Statement unbound) {
		// TODO Implement this
		return null;
	}
	
}
