package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.Set;

/**
 * A constant represents a name for exactly one specific object.
 * E.x. john, marie, a, b, c1, c2 etc.
 */
public class Constant extends LogicObject {
	
	private String name;
	
	public Constant(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}

	@Override
	public Set<String> getVariables() {
		return Collections.emptySet();
	}

	@Override
	public Set<String> getConstants() {
		return Collections.singleton(name);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Constant)
		{
			Constant otherConstant = (Constant) other;
			return name.equals(otherConstant.name);
		}
		return false;
	}

	@Override
	public Binding determineBinding(LogicObject unbound) {
		if (unbound instanceof Variable)
		{
			return new Binding(this, (Variable) unbound);
		}
		else if (unbound instanceof Constant)
		{
			if (this.equals(unbound))
			{
				return Binding.EMPTY_BINDING;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

}
