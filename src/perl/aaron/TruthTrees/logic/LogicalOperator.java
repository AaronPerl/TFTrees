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

}
