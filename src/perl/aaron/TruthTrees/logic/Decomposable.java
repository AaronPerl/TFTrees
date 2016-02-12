package perl.aaron.TruthTrees.logic;

import java.util.List;

public interface Decomposable {
	/**
	 * Checks if a list of branches properly decomposes a statement
	 * @param branches A list of branches (each being a list of statements) to be verified
	 * @return True if the branches decompose the statement, false otherwise
	 */
	public abstract boolean verifyDecomposition(List< List<Statement> > branches);
}
