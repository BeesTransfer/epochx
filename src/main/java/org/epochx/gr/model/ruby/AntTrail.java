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
package org.epochx.gr.model.ruby;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.ant.*;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;

/**
 * Abstract XGR model for ant trail problems in the Ruby language. This class
 * provides the general functionality of ant trail models, but does not itself
 * define a trail. Ant trail models for the Ruby language should extend this
 * class.
 */
public abstract class AntTrail extends GRModel {

	/**
	 * The grammar that defines valid solution space.
	 */
	public static final String GRAMMAR_STRING = "<code> ::= <line> | <code> <line>\n"
			+ "<line> ::= <expr> \n"
			+ "<expr> ::= <condition> | <op>\n"
			+ "<condition> ::= \"if ant.isFoodAhead()\" \\n <op> \\n else \\n <op> \\n end \n"
			+ "<op> ::= ant.turnLeft(); | ant.turnRight(); | ant.move();";

	// Ruby interpreter for performing evaluation.
	private final RubyInterpreter interpreter;

	// Ant components.
	private final AntLandscape landscape;
	private final Ant ant;

	// Trail settings.
	private final List<Point> foodLocations;
	private final int allowedTimeSteps;

	/**
	 * Constructs a new AntTrail with the given <code>FOOD_LOCATIONS</code>
	 * which
	 * must all be positioned at points within the given <code>landscapeSize
	 * </code>.
	 * 
	 * @param foodLocations the points on the landscape which will be occupied
	 *        by food
	 * @param landscapeSize the dimensions of the landscape that the ant will
	 *        operate within
	 * @param allowedTimeSteps the number of moves and turns the ant will be
	 *        allowed to collect the food before timing out.
	 */
	public AntTrail(final Point[] foodLocations, final Dimension landscapeSize,
			final int allowedTimeSteps) {
		this.foodLocations = new ArrayList<Point>(Arrays.asList(foodLocations));
		this.allowedTimeSteps = allowedTimeSteps;

		landscape = new AntLandscape(landscapeSize, null);
		ant = new Ant(allowedTimeSteps, landscape);

		setGrammar(new Grammar(GRAMMAR_STRING));

		interpreter = new RubyInterpreter();
	}

	/**
	 * Calculates and returns the fitness of the given program. The fitness of a
	 * program is calculated as the number of food pieces that the ant did not
	 * manage to reach. That is, a fitness of 0.0 means the ant found every food
	 * item.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */

	@Override
	public double getFitness(final CandidateProgram p) {
		final GRCandidateProgram program = (GRCandidateProgram) p;

		// Reset the ant.
		landscape.setFoodLocations(foodLocations);
		ant.reset(allowedTimeSteps, landscape);

		// Construct argument arrays.
		final String[] argNames = {"ant"};
		final Object[] argValues = {ant};

		// Evaluate multiple times until all time moves used.
		while (ant.getTimesteps() < ant.getMaxMoves()) {
			try {
				interpreter.exec(program.getSourceCode(), argNames, argValues);
			} catch (final MalformedProgramException e) {
				// Stop evaluation and give a bad score.
				break;
			}
		}

		// Calculate score.
		return (foodLocations.size() - ant.getFoodEaten());
	}

}
