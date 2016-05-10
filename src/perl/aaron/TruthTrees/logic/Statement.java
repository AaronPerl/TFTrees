package perl.aaron.TruthTrees.logic;

import java.util.Set;

public abstract class Statement {
	/**
	 * Returns the statement as a string
	 * @return The statement string
	 */
	public abstract String toString();
	
	/**
	 * Determines equality of two Statements
	 * @param other Statement to be checked for equality
	 * @return True if statements are the same (NOT just logically equivalent)
	 */
	public abstract boolean equals(Statement other);
	
	/**
	 * Returns the statement as a string with parenthesis surrounding it
	 * @return The statement string w/ parenthesis
	 */
	public String toStringParen()
	{
		return "("+toString()+")";
	}
	/**
	 * Returns the list of unbound variables in this statement
	 */
	public abstract Set<String> getVariables();
}
