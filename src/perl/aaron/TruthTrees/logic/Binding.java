package perl.aaron.TruthTrees.logic;

/**
 * A class that represents a binding of a given variable to a given constant
 */
public class Binding {
	public static final Binding EMPTY_BINDING = new Binding(new Constant(""), new Variable(""));
	private Constant constant;
	private Variable variable;
	
	/**
	 * Constructs a given binding of the given variable with the given constant
	 * @param constant The constant to bind to the variable 
	 * @param variable The variable to be bound
	 */
	public Binding(Constant constant, Variable variable)
	{
		this.constant = constant;
		this.variable = variable;
	}
	
	public Constant getConstant()
	{
		return constant;
	}
	
	public Variable getVariable()
	{
		return variable;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Binding)
		{
			Binding otherB = (Binding) other;
			return (constant.equals(otherB.constant) && variable.equals(otherB.variable));
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public String toString()
	{
		return "Binding: " + variable.toString() + " binds to " + constant.toString();
	}

}
