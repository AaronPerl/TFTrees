package perl.aaron.TruthTrees.logic;

import java.util.Set;

/**
 * A LogicObject represents some object in the universe of discourse. This could be a constant, variable or function object.
 */
public abstract class LogicObject {

	public abstract Set<String> getVariables();
	public abstract Set<String> getConstants();
	public abstract boolean equals(Object other);
	public abstract Binding determineBinding(LogicObject unbound);
}
