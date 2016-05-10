package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FunctionSymbol extends LogicObject {
	
	private List<LogicObject> arguments;
	
	public FunctionSymbol(List<LogicObject> arguments)
	{
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

}
