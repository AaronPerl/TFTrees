package perl.aaron.TruthTrees.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public abstract class BinaryOperator extends LogicalOperator {
	
	public BinaryOperator(Statement a, Statement b)
	{
		Statement[] statementArray = {a,b};
		statements = Arrays.asList(statementArray);
	}
	
	@Override
	public Set<String> getVariables()
	{
		Set<String> union = new LinkedHashSet<String>();
		union.addAll(statements.get(0).getVariables());
		union.addAll(statements.get(1).getVariables());
		return union;
	}
	
	@Override
	public Set<String> getConstants()
	{
		Set<String> union = new LinkedHashSet<String>();
		union.addAll(statements.get(0).getConstants());
		union.addAll(statements.get(1).getConstants());
		return union;
	}
	
}
