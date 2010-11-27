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
package org.epochx.ge.model.java;

import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;

/**
 * The abstract super class for regression problems in XGE in the Java language.
 * 
 * <p>
 * The grammar used is:
 * 
 * <blockquote><code>
 * &lt;expr&gt; ::= ( &lt;expr&gt; &lt;op&gt; &lt;expr&gt; ) | &lt;terminal&gt;
 * &lt;op&gt; ::= + | - | *
 * &lt;terminal&gt; ::= X
 * </code></blockquote>
 */
public abstract class Regression extends GEModel {

	/**
	 * The grammar that defines valid solution space.
	 */
	public static final String GRAMMAR_STRING = "<expr> ::= ( <expr> <op> <expr> ) | <terminal>\n"
			+ "<op>   ::= + | - | * \n" + "<terminal>  ::= X \n";

	// The error each point must be within.
	private static final double POINT_ERROR = 0.01;

	// Java interpreter for performing evaluation.
	private final JavaInterpreter interpreter;

	// Inputs and associated outputs.
	private final double[] inputs;
	private final double[] outputs;

	/**
	 * Constructs an instance of the abstract Regression model with 50 input
	 * points.
	 */
	public Regression() {
		this(50);
	}

	/**
	 * Constructs an instance of the abstract Regression model.
	 */
	public Regression(final int noPoints) {
		setGrammar(new Grammar(GRAMMAR_STRING));

		interpreter = new JavaInterpreter();

		// Generate the random inputs and the correct outputs.
		inputs = new double[noPoints];
		outputs = new double[inputs.length];
		for (int i = 0; i < inputs.length; i++) {
			inputs[i] = (getRNG().nextDouble() * 2) - 1.0;
			outputs[i] = getCorrectResult(inputs[i]);
		}
	}

	/**
	 * Calculates the fitness score for the given program. The fitness of a
	 * program is the number of inputs the program incorrectly calculates the
	 * output for. The output must be within 0.01 of the correct result to be
	 * considered correct. All programs are evaluated against the same inputs
	 * which are randomly selected between -1.0 and 1.0. The number of inputs
	 * can be provided as an argument to the constructor or defaults to 50.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */
	@Override
	public double getFitness(final CandidateProgram p) {
		final GECandidateProgram program = (GECandidateProgram) p;

		int noWrong = 0;

		for (int i = 0; i < inputs.length; i++) {
			Double result = null;
			try {
				result = (Double) interpreter.eval(program.getSourceCode(),
						new String[]{"X"}, new Double[]{inputs[i]});
			} catch (final MalformedProgramException e) {
				// This should not ever happen unless user changes grammar.
				assert false;
			}

			if (result == null) {
				noWrong++;
			} else if (Math.abs(result - outputs[i]) > POINT_ERROR) {
				noWrong++;
			}
		}

		// How good is this result?
		return noWrong;
	}

	/*
	 * The actual function we are trying to evolve.
	 */
	public abstract double getCorrectResult(final double x);

}
