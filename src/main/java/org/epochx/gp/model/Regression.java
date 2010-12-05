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
import org.epochx.epox.dbl.*;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.representation.CandidateProgram;

/**
 * The abstract super class for regression problems in XGR in the Epox language.
 */
public abstract class Regression extends GPModel {

	// The error each point must be within.
	private static final double POINT_ERROR = 0.01;

	// Inputs and associated outputs.
	private final double[] inputs;
	private final double[] outputs;

	// Variables.
	private final DoubleVariable x;

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
		// Create variables.
		x = new DoubleVariable("X");

		// Define function set.
		final List<Node> syntax = new ArrayList<Node>();
		syntax.add(new AddFunction());
		syntax.add(new SubtractFunction());
		syntax.add(new MultiplyFunction());
		syntax.add(new ProtectedDivisionFunction());

		// Define terminal set;
		syntax.add(x);

		setSyntax(syntax);

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
		final GPCandidateProgram program = (GPCandidateProgram) p;

		int noWrong = 0;

		for (int i = 0; i < inputs.length; i++) {
			x.setValue(inputs[i]);
			final double result = (Double) program.evaluate();
			final double error = Math.abs(result - outputs[i]);

			if (error > POINT_ERROR || Double.isNaN(result)) {
				noWrong++;
			}
		}
		
		// How good is this result?
		return noWrong;
	}
	
//	/**
//	 * Calculates the fitness score for the given program. The fitness of a
//	 * program is the sum of error of the programs results compared to the 
//	 * expected result. All programs are evaluated against the same inputs
//	 * which are randomly selected between -1.0 and 1.0. The number of inputs
//	 * can be provided as an argument to the constructor or defaults to 50.
//	 * 
//	 * @param p {@inheritDoc}
//	 * @return the calculated fitness for the given program.
//	 */
//	@Override
//	public double getFitness(final CandidateProgram p) {
//		final GPCandidateProgram program = (GPCandidateProgram) p;
//
//		double errorSum = 0.0;
//
//		for (int i = 0; i < inputs.length; i++) {
//			x.setValue(inputs[i]);
//			final double result = (Double) program.evaluate();
//			final double error = Math.abs(result - outputs[i]);
//
//			if (!Double.isNaN(result)) {
//				errorSum += error;
//			} else {
//				errorSum = Double.POSITIVE_INFINITY;
//			}
//		}
//		
//		// How good is this result?
//		return errorSum;
//	}

	/*
	 * The actual function we are trying to evolve.
	 */
	public abstract double getCorrectResult(final double x);

}
