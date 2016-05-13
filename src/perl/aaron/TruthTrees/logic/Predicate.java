package perl.aaron.TruthTrees.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Predicate extends Statement {
	private String symbol;
	private List<LogicObject> arguments;

	public Predicate(String symbol, List<LogicObject> arguments)
	{
		this.symbol = symbol;
		this.arguments = new ArrayList<LogicObject>(arguments);
	}
	
	@Override
	public String toString()
	{
		String argString = "";
		for (int i = 0; i < arguments.size(); i++)
		{
			if (i > 0)
				argString += ", ";
			argString += arguments.get(i).toString();
		}
		return symbol + "(" + argString + ")";
	}
	
	public String toStringParen()
	{
		return toString();
	}

	@Override
	public boolean equals(Statement other) {
		if (other instanceof Predicate)
		{
			Predicate otherPredicate = (Predicate) other;
			return symbol.equals(otherPredicate.symbol) && arguments.equals(otherPredicate.arguments);
		}
		return false;
	}

	@Override
	public Set<String> getVariables() {
		Set<String> union = new LinkedHashSet<String>();
		for (LogicObject arg : arguments)
		{
			union.addAll(arg.getVariables());
		}
		return union;
	}
	
	@Override
	public Set<String> getConstants() {
		Set<String> union = new LinkedHashSet<String>();
		for (LogicObject arg : arguments)
		{
			union.addAll(arg.getConstants());
		}
		return union;
	}

	@Override
	public Binding determineBinding(Statement unbound)
	{
		if (unbound.getClass().equals(this.getClass()))
		{
			Predicate unboundPred = (Predicate) unbound;
			if (arguments.size() == unboundPred.arguments.size())
			{
				Binding b = null;
				for (int i = 0; i < arguments.size(); i++)
				{
					Binding curBinding = arguments.get(i).determineBinding(unboundPred.arguments.get(i));
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
