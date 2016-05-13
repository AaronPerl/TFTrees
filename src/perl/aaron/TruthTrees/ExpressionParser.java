package perl.aaron.TruthTrees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import perl.aaron.TruthTrees.logic.*;


public class ExpressionParser {
	
	private static final ArrayList<Set<Character>> operators;
	private static final String negationPattern;
	private static final String variablePattern;
	private static final String constantPattern;
	private static final String quantifiers;
//	private static final Pattern operatorPattern;
	
	static
	{
		operators = new ArrayList<Set<Character>>();
		
		Set<Character> conditionals = new LinkedHashSet<Character>();
		conditionals.add('\u2192');
		conditionals.add('$');
		operators.add(conditionals);
		
		Set<Character> biconditionals = new LinkedHashSet<Character>();
		biconditionals.add('\u2194');
		biconditionals.add('%');
		operators.add(biconditionals);
		
		Set<Character> conjunctions = new LinkedHashSet<Character>();
		conjunctions.add('\u2227');		// conjunction unicode
		conjunctions.add('&');			// conjunction (junction, what's your function?)
		operators.add(conjunctions);
		
		Set<Character> disjunctions = new LinkedHashSet<Character>();
		disjunctions.add('\u2228');		// disjunction unicode
		disjunctions.add('|');			// disjunction
		operators.add(disjunctions);
		
		negationPattern = "[\u00AC~!]";
		variablePattern = "[t-z][0-9]*";
		constantPattern = "[a-zA-Z0-9]+";
		quantifiers = "[\u2200\u2203]";
		
//		operatorString = "";
//		for (char curOperator : operators)
//			operatorString += curOperator;
//		
//		operatorPattern = Pattern.compile("[\\s]*[" + operatorString + "][\\s]*");
	}
	
	private ExpressionParser() {}
	
	private static int findZeroDepth(String searchString, Set<Character> searchChars)
	{
		int depth = 0;
		for (int i = 0; i < searchString.length(); i++)
		{
			char curChar = searchString.charAt(i);
			if (curChar == '(')
				depth++;
			else if (curChar == ')')
			{
				if (depth == 0)
					return -1;
				depth--;
			}
			else if (searchChars.contains(curChar) && depth == 0)
				return i;
		}
		return -1;
	}
	
	private static int findZeroDepth(String searchString, char searchChar)
	{
		Set<Character> tmpSet = new LinkedHashSet<Character>();
		tmpSet.add(searchChar);
		return findZeroDepth(searchString, tmpSet);
	}
	
	private static Vector<String> splitZeroDepth(String searchString, Set<Character> splitChars)
	{
		Vector<String> retVal = new Vector<String>();
		String curString = searchString;
		int findIndex;
		
		while ((findIndex = findZeroDepth(curString, splitChars)) > -1)
		{
			retVal.add(curString.substring(0, findIndex));
			curString = curString.substring(findIndex + 1);
		}
		
		retVal.add(curString);
		
		return retVal;
	}
	
	private static LogicObject parseObject(String objectString)
	{
		if (objectString.matches("[a-z][A-Z0-9]*\\(.+\\)"))
		{
			String symbol = objectString.substring(0, objectString.indexOf('('));
//			System.out.println("Function symbol: " + symbol);
			String argsString =
				objectString.substring(
					objectString.indexOf('(') + 1,
					objectString.length() - 1);
			List<String> argStrings = splitZeroDepth(argsString, Collections.singleton(','));
			List<LogicObject> objs = new ArrayList<LogicObject>();
			for (String curArgString : argStrings)
			{
				objs.add(parseObject(curArgString));
			}
			return new FunctionSymbol(symbol, objs);
		}
		else if (objectString.matches(variablePattern))
		{
//			System.out.println("Variable: " + objectString);
			return new Variable(objectString);
		}
		else if (objectString.matches(constantPattern))
		{
//			System.out.println("Constant: " + objectString);
			return new Constant(objectString);
		}
		else
		{
			return null;
		}
	}
	
	private static Statement recurseStatement(String subExpression)
	{	
		int operatorSet = -1;
		
		for (int i = 0; i < operators.size(); i++)
		{
			Set<Character> curOperatorSet = operators.get(i);
			for (char curOperator : curOperatorSet)
			{
				int curOperatorIndex = findZeroDepth(subExpression, curOperator);
				if (curOperatorIndex > -1)
				{
					if (operatorSet > -1)
						return null;
					operatorSet = i;
					break;
				}
			}
		}
		
//		System.out.println("Operator set = " + operatorSet);
		
		if (operatorSet == -1)
		{
//			System.out.println(subExpression);
			if (subExpression.matches("\\w+") || subExpression.matches("(\\w+)"))
			{
//				System.out.println("Atomic Statement : " + subExpression);
				return new AtomicStatement(subExpression);
			}
			else if (subExpression.startsWith("(") && subExpression.endsWith(")"))
			{
//				System.out.println("Parenthesis around " + subExpression.substring(1,subExpression.length() -1));
				return recurseStatement(
						subExpression.substring(1, subExpression.length() - 1));
			}
			else if (subExpression.matches(negationPattern + ".+"))
			{
//				System.out.println("Negation of " + subExpression.substring(1));
				return new Negation(recurseStatement(subExpression.substring(1)));
			}
			// Equality (not implemented)
//			else if (subExpression.matches(".+=.+"))
//			{
//				int equalsPos = subExpression.indexOf('=');
//				String obj1 = subExpression.substring(0, equalsPos);
//				String obj2 = subExpression.substring(equalsPos + 1);
//				
//				return new Equality(parseObject(obj1), parseObject(obj2));
//			}
			else if (subExpression.matches("[A-Z][a-zA-Z0-9]*(.+)"))
			{
				String symbol = subExpression.substring(0, subExpression.indexOf('('));
//				System.out.println("Predicate symbol: " + symbol);
				String argumentsString =
					subExpression.substring(
						subExpression.indexOf('(') + 1,
						subExpression.length() - 1);
				List<String> objectStrings = splitZeroDepth(argumentsString, Collections.singleton(','));
				List<LogicObject> arguments = new ArrayList<LogicObject>();
				for (String curObjectString : objectStrings)
				{
					LogicObject newObj = parseObject(curObjectString);
					if (newObj == null) return null;
					arguments.add(newObj);
				}
				
				return new Predicate(symbol, arguments);
			}
			// forall [var] ...
			else if (subExpression.matches("\u2200" + variablePattern + ".+"))
			{
				Pattern p = Pattern.compile(variablePattern);
				Matcher m = p.matcher(subExpression.substring(1));
				m.find();
				int start = m.start() + 1;
				int end = m.end() + 1;
				
				String varString = subExpression.substring(start, end);
				String statementString = subExpression.substring(end);
				
				Variable var = (Variable) parseObject(varString);
				Statement statement = recurseStatement(statementString);
				
				if (var == null || statement == null) return null;
				
//				System.out.println("Univeral Quantifier:");
//				System.out.println("\tQuantified Variable: " + var);
//				System.out.println("\tQuantified Statement: " + statement);
				return new UniversalQuantifier(var, statement);						
			}
			// exists [var] ...
			else if (subExpression.matches("\u2203" + variablePattern + ".+"))
			{
				Pattern p = Pattern.compile(variablePattern);
				Matcher m = p.matcher(subExpression.substring(1));
				m.find();
				int start = m.start() + 1;
				int end = m.end() + 1;
				
				String varString = subExpression.substring(start, end);
				String statementString = subExpression.substring(end);
				
				Variable var = (Variable) parseObject(varString);
				Statement statement = recurseStatement(statementString);
				
				if (var == null || statement == null) return null;
				
//				System.out.println("Existential Quantifier:");
//				System.out.println("\tQuantified Variable: " + var);
//				System.out.println("\tQuantified Statement: " + statement);
				return new ExistentialQuantifier(var, statement);						
			}
			return null;
		}
		
		Vector<String> operandStrings = splitZeroDepth(subExpression, operators.get(operatorSet));
		
		Vector<Statement> operands = new Vector<Statement>();
		
		for (String curOperandString : operandStrings)
		{
			Statement curOperand = recurseStatement(curOperandString);
			if (curOperand == null)
				return null;
			operands.add(curOperand);
		}
		
		switch (operatorSet)
		{
		case 0: // conditional
			if (operands.size() != 2)
				return null; // conditional has exactly 2 operands
			return new Conditional(operands.get(0),operands.get(1));
		case 1: // biconditional
			if (operands.size() != 2)
				return null; // biconditional ---
			return new Biconditional(operands.get(0),operands.get(1));
		case 2: // conjunction
			if (operands.size() < 2)
				return null; // conjunction has at least two operands (how did you even manage this?)
			return new Conjunction(operands);
		case 3: // disjunction
			if (operands.size() < 2)
				return null; // disjunction ---
			return new Disjunction(operands);
		}
		
		return null;
	}
	
	public static Statement parseExpression(String expression)
	{
//		if (checkForMismatchedParenthesis(expression))
//			throw new ...
		Statement statement = recurseStatement(expression.replaceAll("\\s", ""));
		if (statement != null)
		{
//			System.out.println("Expression: " + statement.toString());
//			System.out.println("Constants: " + statement.getConstants());
//			System.out.println("Variables: " + statement.getVariables());
			if (statement.getVariables().size() != 0)
			{
				JOptionPane.showMessageDialog(null, "This statement has unbound variables: " +
					statement.getVariables().toString());
			}
		}
		return statement;
	}

}
