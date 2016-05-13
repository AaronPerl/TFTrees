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
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Statement)
		{
			return equals((Statement) other);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
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
	/**
	 * Returns the list of constants in this statement
	 */
	public abstract Set<String> getConstants();
	/**
	 * Attempts to determine a binding that would make the unbound statement equivalent to this one.
	 * @param unbound A statement containing an unbound variable that will be bound
	 * @return The Binding that makes the statements equivalent or null if there is no such binding
	 */
	public abstract Binding determineBinding(Statement unbound);
}
