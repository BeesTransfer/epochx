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
 * GP model for the cart centering problem.
 */
public class CartCentering extends GPModel {

	// The rocket force in newtons.
	private static final double F = 1.0;

	// The mass of the cart in kg.
	private static final double m = 2.0;

	// How close to zero the position(m) and velocity(ms^-1) need to get.
	private static final double targetRange = 0.05;

	// The acceleration based upon a = F/m
	private final double a;

	private DoubleVariable varX;
	private DoubleVariable varV;

	private final double[] testPositions;
	private final double[] testVelocities;

	public CartCentering() {
		a = F / m;

		testPositions = new double[20];
		testVelocities = new double[20];

		// Generate random test data in range -0.75 to +0.75.
		for (int i = 0; i < testPositions.length; i++) {
			testPositions[i] = (Math.random() * 1.5) - 0.75;
			testVelocities[i] = (Math.random() * 1.5) - 0.75;
		}

		configure();
	}

	public void configure() {
		// Create variables.
		varX = new DoubleVariable("X");
		varV = new DoubleVariable("V");
	}

	@Override
	public List<Node> getSyntax() {
		// Define function set.
		final List<Node> syntax = new ArrayList<Node>();
		syntax.add(new AddFunction());
		syntax.add(new SubtractFunction());
		syntax.add(new MultiplyFunction());
		syntax.add(new ProtectedDivisionFunction());
		syntax.add(new GreaterThanFunction());

		// Define variables;
		syntax.add(varX);
		syntax.add(varV);

		return syntax;
	}

	@Override
	public double getFitness(final CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;

		// Total time taken for all fitness cases in ms.
		int totalTime = 0;

		/*
		 * EpoxParser<Double> parser = new EpoxParser<Double>();
		 * parser.addAvailableVariable(varV);
		 * parser.addAvailableVariable(varX);
		 * Node<Double> nodeTree =
		 * parser.parse("GT(MUL(-1.0 X) MUL(V ABS(V)))");
		 * program = new CandidateProgram<Double>(nodeTree, this);
		 */

		for (int i = 0; i < testPositions.length; i++) {
			// Set the initial variable values.
			varX.setValue(testPositions[i]);
			varV.setValue(testVelocities[i]);

			/*
			 * Note that we use milliseconds to avoid problems with accuracy of
			 * doubles.
			 */

			// Milliseconds to increment each timestep.
			final int msecs = 20;
			// Max time allowed (ten seconds in millisecs).
			final int maxTime = 10000;
			// Convert timestep to seconds for force calculation.
			final double timestep = (double) msecs / 1000;
			// Amount of time passed (ms).
			int t = 0;

			while (t < maxTime) {
				// Is the cart centered?
				if ((varV.getValue() >= 0 - targetRange)
						&& (varV.getValue() <= 0 + targetRange)
						&& (varX.getValue() >= 0 - targetRange)
						&& (varX.getValue() <= 0 + targetRange)) {
					break;
				}

				// Execute with current position and velocity.
				double u = (Double) program.evaluate();

				// Make 'u' either +1.0 or -1.0 using sign value.
				u = Math.signum(u);

				// Calculate new position and velocity based on u.
				final double v = varV.getValue() + (timestep * (a * u));
				final double x = varX.getValue() + (timestep * varV.getValue());

				// System.out.println(v + " " + x + " " + u);

				// Update the program variables.
				varV.setValue(v);
				varX.setValue(x);

				// Increase time by 0.02 seconds.
				t += msecs;
			}

			totalTime += t;
		}

		// Return total time in seconds.
		return ((double) totalTime) / 1000;
	}

	/*
	 * public static void main(String[] args) {
	 * GPModel model = new CartCentering();
	 * 
	 * model.setPopulationSize(500);
	 * model.setNoGenerations(100);
	 * model.setMutationProbability(0.1);
	 * model.setCrossoverProbability(0.9);
	 * model.setNoRuns(5);
	 * model.setPoolSize(50);
	 * model.setNoElites(50);
	 * model.setMaxProgramDepth(5);
	 * model.setPoolSelector(new TournamentSelector(7));
	 * model.setProgramSelector(new RandomSelector());
	 * model.setCrossover(new UniformPointCrossover());
	 * model.setMutator(new PointMutation(0.1));
	 * 
	 * Controller.run(model);
	 * }
	 */

	public double solution(final double x, final double v) {
		if ((-1.0 * x) > (v * Math.abs(v))) {
			return 1.0;
		} else {
			return -1.0;
		}
	}

}
