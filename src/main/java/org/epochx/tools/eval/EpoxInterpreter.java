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
package org.epochx.tools.eval;

import org.epochx.epox.*;

/**
 * An <code>EpoxInterpreter</code> provides the facility to evaluate individual
 * Epox expressions. Epox is the lisp like language used by the tree based
 * representation in EpochX. This allows the grammar based representations to
 * evolve programs that are directly equivalent to the tree based system.
 * 
 * <p>
 * The program strings are parsed by the <code>EpoxParser</code> into the same
 * program trees used by the tree GP aspect of EpochX, which can then be
 * evaluated internally. The Epox language is extendable and this interpreter
 * will correctly evaluate any new functions or data-types which have been added
 * to the <code>EpoxParser</code> that is used here.
 * 
 * @see EpoxParser
 */
public class EpoxInterpreter implements Interpreter {

	// The Epox language parser.
	private final EpoxParser parser;

	/**
	 * Constructs a new <code>EpoxInterpreter</code> with a new
	 * <code>EpoxParser</code>.
	 */
	public EpoxInterpreter() {
		parser = new EpoxParser();
	}

	/**
	 * Constructs a new <code>EpoxInterpreter</code> using the given parser to
	 * parse the program strings into Epox program trees for evaluation.
	 * 
	 * @param parser the Epox language parser.
	 */
	public EpoxInterpreter(final EpoxParser parser) {
		this.parser = parser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object eval(final String source, final String[] argNames,
			final Object[] argValues) throws MalformedProgramException {
		if (source == null) {
			return null;
		}

		// Remove any of the old variables.
		parser.clearAvailableVariables();

		for (int i = 0; i < argNames.length; i++) {
			if (argValues[i] instanceof Boolean) {
				parser.addAvailableVariable(new BooleanVariable(argNames[i],
						(Boolean) argValues[i]));
			} else if (argValues[i] instanceof Double) {
				parser.addAvailableVariable(new DoubleVariable(argNames[i],
						(Double) argValues[i]));
			} else {
				throw new MalformedProgramException("Unknown variable type");
			}
		}

		final Node programTree = parser.parse(source);

		return programTree.evaluate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] eval(final String source, final String[] argNames,
			final Object[][] argValues) throws MalformedProgramException {

		final Object[] results = new Object[argValues.length];

		// Call the other eval method for each set of inputs.
		for (int i = 0; i < argValues.length; i++) {
			results[i] = eval(source, argNames, argValues[i]);
		}

		return results;
	}

	/**
	 * Not supported by <code>EpoxInterpreter</code>. Calling will throw an
	 * <code>IllegalStateException</code>.
	 */
	@Override
	public void exec(final String program, final String[] argNames,
			final Object[] argValues) {
		throw new IllegalStateException("method not supported");
	}

	/**
	 * Not supported by <code>EpoxInterpreter</code>. Calling will throw an
	 * <code>IllegalStateException</code>.
	 */
	@Override
	public void exec(final String program, final String[] argNames,
			final Object[][] argValues) {
		throw new IllegalStateException("method not supported");
	}

	/**
	 * Returns the <code>EpoxParser</code> used to parse the program strings.
	 * 
	 * @return the Epox parser
	 */
	public EpoxParser getParser() {
		return parser;
	}
}
