package perl.aaron.TruthTrees.logic;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniversalQuantifier extends Quantifier {

	public UniversalQuantifier(Variable var, Statement statement) {
		super(var, statement);
	}

	@Override
	public String toString() {
		return "\u2200" + var.toString() + " " + statement.toStringParen();
	}
	
	@Override
	public String toStringParen() {
		return toString();
	}

	@Override
	public boolean verifyDecomposition(List<List<Statement>> branches, Set<String> constants, Set<String> constantsBefore) {
		if (branches.size() != 1) // There should be only 1 branch
			return false;
		Set<String> instantiatedConstants = new LinkedHashSet<String>();
		System.out.println("All constants: " + constants.toString());
		for (Statement s : branches.get(0))
		{
			Binding b = s.determineBinding(statement);
			if (b != null)
			{
				System.out.println("Binding for " + s.toString());
				System.out.println(b);
				
				instantiatedConstants.add(b.getConstant().toString());
			}
			else
			{
				System.out.println("Invalid binding");
			}
		};
		return instantiatedConstants.equals(constants);
	}

}
