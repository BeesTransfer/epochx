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
package org.epochx.gp.model;

import java.util.*;

import org.epochx.epox.*;
import org.epochx.epox.bool.*;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.util.BoolUtils;

/**
 * GP model for the majority problems.
 * 
 * <h4>Majority problem</h4>
 * 
 * Given n binary inputValues, a program that solves the majority problem will
 * return true in all circumstances where a majority of the inputValues are true
 * (or 1), and return false whenever there is not a majority of true values.
 */
public class Majority extends GPModel {

	// The names of the inputValues used in the grammar.
	private final BooleanVariable[] variables;

	// The boolean input sequences.
	private final boolean[][] inputValues;

	/**
	 * Constructs a Majority model for the given number of inputs.
	 * 
	 * @param noInputBits the number of inputs the majority problem should be
	 *        for
	 */
	public Majority(final int noInputBits) {
		// Generate the input sequences.
		inputValues = BoolUtils.generateBoolSequences(noInputBits);

		// Define functions.
		final List<Node> syntax = new ArrayList<Node>();
		syntax.add(new IfFunction());
		syntax.add(new AndFunction());
		syntax.add(new OrFunction());
		syntax.add(new NotFunction());

		// Define terminal variables.
		variables = new BooleanVariable[noInputBits];
		for (int i = 0; i < noInputBits; i++) {
			variables[i] = new BooleanVariable("d" + i);
			syntax.add(variables[i]);
		}

		setSyntax(syntax);
	}

	/**
	 * Calculates the fitness score for the given program. The fitness of a
	 * program for the majority problem is calculated by evaluating it
	 * using each of the possible sets of input values. There are
	 * <code>2^noInputBits</code> possible sets of inputs. The fitness of the
	 * program is the quantity of those input sequences that the program
	 * returned an incorrect response for. That is, a fitness value of
	 * <code>0.0</code> indicates the program responded correctly for every
	 * possible set of input values.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */
	@Override
	public double getFitness(final CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;

		double score = 0;

		// Execute on all possible inputs.
		for (final boolean[] in: inputValues) {

			// Set the variables.
			for (int i = 0; i < in.length; i++) {
				variables[i].setValue(in[i]);
			}

			if ((Boolean) program.evaluate() == majorityTrue(in)) {
				score++;
			}
		}

		return inputValues.length - score;
	}

	/*
	 * Calculate what the correct response should be for the given inputs.
	 */
	private boolean majorityTrue(final boolean[] input) {
		int trueCount = 0;

		for (final boolean b: input) {
			if (b) {
				trueCount++;
			}
		}

		return (trueCount >= (input.length / 2));
	}
}
