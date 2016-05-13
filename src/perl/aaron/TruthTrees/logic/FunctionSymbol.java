package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
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
		this.arguments = new ArrayList<LogicObject>(arguments);
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

	@Override
	public Set<String> getConstants() {
		Set<String> allConstants = new LinkedHashSet<String>();
		for (LogicObject obj : arguments)
		{
			allConstants.addAll(obj.getConstants());
		}
		if (getVariables().size() == 0)
			allConstants.add(toString());
		return allConstants;
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

	@Override
	public boolean equals(Object other) {
		if (other instanceof FunctionSymbol)
		{
			FunctionSymbol otherFunction = (FunctionSymbol) other;
			return (otherFunction.symbol == this.symbol) && arguments.equals(otherFunction.arguments);
		}
		return false;
	}

	@Override
	public Binding determineBinding(LogicObject unbound) {
		if (unbound instanceof FunctionSymbol)
		{
			FunctionSymbol unboundFunc = (FunctionSymbol) unbound;
			if (arguments.size() == unboundFunc.arguments.size())
			{
				Binding b = null;
				for (int i = 0; i < arguments.size(); i++)
				{
					Binding curBinding = arguments.get(i).determineBinding(unboundFunc.arguments.get(i));
					if (curBinding == null)
					{
						return null;
					}
					if (b == null || b.equals(Binding.EMPTY_BINDING))
					{
						b = curBinding;
					}
					else if ((b != curBinding && !b.equals(Binding.EMPTY_BINDING)) || curBinding == null)
					{
						return null;
					}
				}
				return b;
			}
			else return null;
			
		}
		else return null;
	}

}
