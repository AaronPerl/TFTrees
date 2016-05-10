package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Variable extends LogicObject {
	
	private String name;
	
	public Variable(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
	
	@Override
	public Set<String> getVariables()
	{
		return Collections.singleton(name);
	}

}
