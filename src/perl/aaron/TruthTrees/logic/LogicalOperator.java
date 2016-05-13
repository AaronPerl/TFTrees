package perl.aaron.TruthTrees.logic;

import java.util.Collections;
import java.util.List;

public abstract class LogicalOperator extends Statement implements Decomposable {
	
	protected List<Statement> statements;
	
	/**
	 * Returns the list of connected operands
	 * @return
	 */
	public List<Statement> getOperands() {
		return Collections.unmodifiableList(statements);
	}

	@Override
	public Binding determineBinding(Statement unbound)
	{
		if (unbound.getClass().equals(this.getClass()))
		{
			LogicalOperator unboundOp = (LogicalOperator) unbound;
			if (unboundOp.statements.size() == statements.size())
			{
				Binding b = null;
				for (int i = 0; i < statements.size(); i++)
				{
					Binding curBinding = statements.get(i).determineBinding(unboundOp.statements.get(i));
					if (curBinding == null)
					{
						System.out.println("Invalid binding between\n\t" + statements.get(i).toString() + "\n\t" + unbound.toString());
						return null;
					}
					if (b == null || b.equals(Binding.EMPTY_BINDING))
					{
						b = curBinding;
					}
					else if ((!b.equals(curBinding) && !b.equals(Binding.EMPTY_BINDING)) || curBinding == null)
					{
						System.out.println("Different bindings: " + b.toString() + ", " + curBinding.toString());
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
