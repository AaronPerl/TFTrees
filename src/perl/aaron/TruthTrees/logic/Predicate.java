package perl.aaron.TruthTrees.logic;

import java.util.List;
import java.util.Set;

public class Predicate extends Statement {
	private String symbol;
	private List<LogicObject> arguments;

	@Override
	public String toString() {
		String argString = "";
		for (int i = 0; i < arguments.size(); i++)
		{
			if (i > 0)
				argString += ", ";
			argString += arguments.get(0).toString();
		}
		return symbol + "(" + argString + ")";
	}

	@Override
	public boolean equals(Statement other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

}
