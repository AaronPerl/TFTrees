package perl.aaron.TruthTrees.logic;

import java.util.Arrays;


public abstract class BinaryOperator extends LogicalOperator {
	
	public BinaryOperator(Statement a, Statement b)
	{
		Statement[] statementArray = {a,b};
		statements = Arrays.asList(statementArray);
	}
	
}
