package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FunctionSymbol extends LogicObject {
	
	private String symbol;
	private List<LogicObject> arguments;
	
	public FunctionSymbol(String symbol, List<LogicObject> arguments)
	{
		this.symbol = symbol;
		Collections.copy(this.arguments, arguments);
	}

	@Override
	public Set<String> getVariables() {
		Set<String> allVariables = new LinkedHashSet<String>();
		for (LogicObject obj : arguments)
		{
			allVariables.addAll(obj.getVariables());
		}
		return allVariables;
	}
	
	public String toString()
	{
		String argString = "";
		for (int i = 0; i < arguments.size(); i++)
		{
			if (i > 0)
				argString += ", ";
			argString += arguments.get(0).toString();
		}
		return symbol + "(" + argString + ")";
	}

}
