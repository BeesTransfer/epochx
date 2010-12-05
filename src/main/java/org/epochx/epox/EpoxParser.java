/*
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.epox;

import java.util.*;
import java.util.regex.Pattern;

import org.epochx.epox.ant.*;
import org.epochx.epox.bool.*;
import org.epochx.epox.dbl.*;
import org.epochx.tools.ant.Ant;
import org.epochx.tools.eval.MalformedProgramException;

/**
 * The function parser can parse a nested function into a node tree. It can
 * only parse those functions that it knows about.
 */
public class EpoxParser {

	// This map is to contain only simple functions that require no additional
	// info.
	private final Map<String, Class<?>> simpleFunctions;

	// List of variables to be used, any variables found, not provided will be
	// created.
	private List<Node> variables;

	// An ant which we may need to use in some function creations - lazily
	// created.
	private Ant ant;

	/*
	 * These regexps come directly from JavaDoc for Double.valueOf(String) to
	 * check the validity of a double without having to rely on catching number
	 * format exceptions.
	 */
	private final String Digits = "(\\p{Digit}+)";
	private final String HexDigits = "(\\p{XDigit}+)";
	private final String Exp = "[eE][+-]?" + Digits;
	private final String fpRegex = ("[\\x00-\\x20]*" + "[+-]?(" + "NaN|"
			+ "Infinity|" + "((("
			+ Digits
			+ "(\\.)?("
			+ Digits
			+ "?)("
			+ Exp
			+ ")?)|"
			+ "(\\.("
			+ Digits
			+ ")("
			+ Exp
			+ ")?)|"
			+ "(("
			+ "(0[xX]"
			+ HexDigits
			+ "(\\.)?)|"
			+ "(0[xX]"
			+ HexDigits
			+ "?(\\.)"
			+ HexDigits
			+ ")"
			+ ")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");

	public EpoxParser() {
		variables = new ArrayList<Node>();
		simpleFunctions = new HashMap<String, Class<?>>();

		// Insert the Boolean functions.
		simpleFunctions.put("AND", AndFunction.class);
		simpleFunctions.put("IFF", IfAndOnlyIfFunction.class);
		simpleFunctions.put("IF", IfFunction.class);
		simpleFunctions.put("IMPLIES", ImpliesFunction.class);
		simpleFunctions.put("NAND", NandFunction.class);
		simpleFunctions.put("NOR", NorFunction.class);
		simpleFunctions.put("NOT", NotFunction.class);
		simpleFunctions.put("OR", OrFunction.class);
		simpleFunctions.put("XOR", XorFunction.class);

		// Insert the Double functions.
		simpleFunctions.put("ABS", AbsoluteFunction.class);
		simpleFunctions.put("ADD", AddFunction.class);
		simpleFunctions.put("ACOS", ArcCosineFunction.class);
		simpleFunctions.put("ASIN", ArcSineFunction.class);
		simpleFunctions.put("ATAN", ArcTangentFunction.class);
		simpleFunctions.put("CVP", CoefficientPowerFunction.class);
		simpleFunctions.put("COSEC", CosecantFunction.class);
		simpleFunctions.put("COS", CosineFunction.class);
		simpleFunctions.put("COT", CotangentFunction.class);
		simpleFunctions.put("CUBE", CubeFunction.class);
		simpleFunctions.put("EXP", ExponentialFunction.class);
		simpleFunctions.put("FACTORIAL", FactorialFunction.class);
		simpleFunctions.put("GT", GreaterThanFunction.class);
		simpleFunctions.put("COSH", HyperbolicCosineFunction.class);
		simpleFunctions.put("SINH", HyperbolicSineFunction.class);
		simpleFunctions.put("TANH", HyperbolicTangentFunction.class);
		simpleFunctions.put("INV", InvertFunction.class);
		simpleFunctions.put("LOG-10", Log10Function.class);
		simpleFunctions.put("LN", LogFunction.class);
		simpleFunctions.put("LT", LessThanFunction.class);
		simpleFunctions.put("MAX", MaxFunction.class);
		simpleFunctions.put("MIN", MinFunction.class);
		simpleFunctions.put("MOD", ModuloFunction.class);
		simpleFunctions.put("MUL", MultiplyFunction.class);
		simpleFunctions.put("POW", PowerFunction.class);
		simpleFunctions.put("PDIV", ProtectedDivisionFunction.class);
		simpleFunctions.put("SEC", SecantFunction.class);
		simpleFunctions.put("SGN", SignumFunction.class);
		simpleFunctions.put("SIN", SineFunction.class);
		simpleFunctions.put("SQUARE", SquareFunction.class);
		simpleFunctions.put("SQRT", SquareRootFunction.class);
		simpleFunctions.put("SUB", SubtractFunction.class);
		simpleFunctions.put("TAN", TangentFunction.class);

		// Insert the Action functions.
		simpleFunctions.put("SEQ2", Seq2Function.class);
		simpleFunctions.put("SEQ3", Seq3Function.class);
		simpleFunctions.put("SEQ4", Seq4Function.class);
	}

	/*
	 * We do lazy initialisation, so if the object hasn't been initialised yet,
	 * we do it now.
	 */
	private Node initialiseFunction(final String name) throws MalformedProgramException {
		// The function node we're going to create.
		Node node = null;

		// Attempt to find the function in the list of simple functions.
		final Class<?> functionClass = simpleFunctions.get(name);

		if (functionClass != null) {
			// Instantiate the function node.
			try {
				node = (Node) functionClass.newInstance();
			} catch (final InstantiationException e) {
				assert false;
			} catch (final IllegalAccessException e) {
				assert false;
			}
		} else if (name.equals("IF-FOOD-AHEAD")) {
			node = new IfFoodAheadFunction(ant);
		} else if (name.equals("MOVE")) {
			node = new AntMoveAction(ant);
		} else if (name.equals("TURN-LEFT")) {
			node = new AntTurnLeftAction(ant);
		} else if (name.equals("TURN-RIGHT")) {
			node = new AntTurnRightAction(ant);
		} else if (name.equals("SKIP")) {
			node = new AntSkipAction(ant);
		} else {
			// Function unknown - error.
			throw new MalformedProgramException("Unknown function " + name + " encountered");
		}

		return node;
	}

	public Node parse(final String source) throws MalformedProgramException {
		if (source == null) {
			return null;
		}

		// Locate the first bracket (straight after function name).
		final int openingBracket = source.indexOf('(');

		String identifier = null;
		List<String> args = null;

		boolean terminal = false;
		
		// If there is no bracket then it must be a terminal.
		if (openingBracket == -1) {
			identifier = source;
			args = new ArrayList<String>();
			terminal = true;
		} else {
			// Get the name of the function.
			identifier = source.substring(0, openingBracket);

			// Locate the closing bracket.
			final int closingBracket = source.lastIndexOf(')');

			// Get the comma separated arguments - without the surrounding
			// brackets.
			final String argumentStr = source.substring(openingBracket + 1,
					closingBracket);

			// Separate the arguments.
			args = splitArguments(argumentStr);
		}

		Node node;
		if (terminal) {
			node = parseTerminal(identifier);
		} else {
			node = initialiseFunction(identifier);
		}

		// Check the arities match.
		if ((node == null) || (node.getArity() != args.size())) {
			throw new MalformedProgramException();
		} else {
			for (int i = 0; i < args.size(); i++) {
				node.setChild(i, parse(args.get(i)));
			}
		}

		return node;
	}

	/**
	 * Parses the given string as a terminal. It will be treated as either a
	 * variable or a literal value depending upon its contents. If it is a
	 * parseable numeric value then it will be considered to be a terminal with
	 * a Double value. if it contains in any case the values "true" or "false"
	 * then it will be treated as a terminal with a Boolean value. All other
	 * terminals will be treated as Variables with a label equal to the
	 * argument string.
	 * 
	 * @param terminalStr
	 * @return
	 */
	private Node parseTerminal(final String terminalStr) {
		Node node = parseBooleanLiteral(terminalStr);
			
		if (node == null) {
			node = parseVariable(terminalStr);
		}
		
		if (node == null && Pattern.matches(fpRegex, terminalStr)) {
			node = new DoubleLiteral(Double.valueOf(terminalStr));
		}
		
		return node;
	}
	
	private BooleanLiteral parseBooleanLiteral(final String literalStr) {
		if (literalStr.equalsIgnoreCase("true") || literalStr.equalsIgnoreCase("false")) {
			return new BooleanLiteral(Boolean.valueOf(literalStr));
		} else {
			return null;
		}
	}
	
	private Node parseVariable(final String variableStr) {
		Node variable = null;
		for (final Node v: variables) {
			if (v.getIdentifier().equals(variableStr)) {
				variable = v;
				break;
			}
		}
		return variable;
	}

	public void addSimpleFunction(final String name,
			final Class<? extends Node> functionClass) {
		simpleFunctions.put(name, functionClass);
	}

	public void setAvailableVariables(final List<Node> variables) {
		this.variables = variables;
	}

	public void addAvailableVariable(final Node variable) {
		variables.add(variable);
	}

	public void clearAvailableVariables() {
		variables.clear();
	}

	/**
	 * Before parsing an Ant program, the parser needs to have access to an ant
	 * object.
	 * 
	 * @param ant
	 */
	public void setAnt(final Ant ant) {
		this.ant = ant;
	}

	private List<String> splitArguments(String argStr) {
		int depth = 0;

		final List<String> args = new ArrayList<String>(5);
		StringBuilder buffer = new StringBuilder();

		argStr = argStr.trim();

		for (int i = 0; i < argStr.length(); i++) {
			final char c = argStr.charAt(i);

			if (c == '(') {
				depth++;
			} else if (c == ')') {
				depth--;
			}
			if (((c == ' ') || (c == ',')) && (depth == 0)) {
				args.add(buffer.toString());
				buffer = new StringBuilder();
				while (argStr.charAt(i+1) == ' ') {
					i++;
				}
			} else {
				buffer.append(c);
			}
		}

		if (buffer.length() > 0) {
			args.add(buffer.toString());
		}

		return args;
	}

	/*
	 * public static void main(String[] args) {
	 * EpoxParser parser = new EpoxParser();
	 * 
	 * //System.out.println(parser.parse("IF(ADD(1,false),NOT(true),false)").
	 * toString());
	 * //Node programTree = parser.parse("XOR(D1 XOR(NOT(XOR(D0 D3)) D2))");
	 * 
	 * parser.addAvailableVariable(new BooleanVariable("d0", false));
	 * 
	 * Node programTree = parser.parse("XOR(OR(d0,d0),NOT(d0))");
	 * System.out.println(programTree.toString());
	 * System.out.println(programTree.evaluate());
	 * }
	 */
}
