package perl.aaron.TruthTrees.logic;

public class AtomicStatement extends Statement {
	private String _symbol;
	/**
	 * Creates an atomic statement with a given symbol
	 * @param symbol The character representing the statement
	 */
	public AtomicStatement(String symbol)
	{
		_symbol = symbol;
	}
	/**
	 * Returns the statement's symbol
	 * @return The character representing the statement
	 */
	public String getSymbol()
	{
		return _symbol;
	}
	public String toString()
	{
		return _symbol;
	}
	public String toStringParen()
	{
		return _symbol;
	}
	public boolean equals(Object other)
	{
		if (!(other instanceof AtomicStatement))
			return false;
		AtomicStatement otherAS = (AtomicStatement) other;
		return (otherAS.getSymbol().equals(_symbol));
	}
	public boolean equals(Statement other) {
		if (!(other instanceof AtomicStatement))
			return false;
		return ((AtomicStatement)other).getSymbol().equals(_symbol);
	}
}
