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
	
	public String getName()
	{
		return name;
	}

	@Override
	public Set<String> getVariables() {
		return Collections.emptySet();
	}

}
