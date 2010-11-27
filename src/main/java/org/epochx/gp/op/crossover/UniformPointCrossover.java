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
package org.epochx.gp.op.crossover;

import org.epochx.epox.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class implements standard crossover with uniform swap points.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>random number generator</li>
 * </ul>
 * 
 * <p>
 * If the <code>getModel</code> method returns <code>null</code> then no model
 * is set and whatever static parameters have been set as parameters to the
 * constructor or using the standard accessor methods will be used. If any
 * compulsory parameters remain unset when the crossover is performed then an 
 * <code>IllegalStateException</code> will be thrown.
 * 
 * @see KozaCrossover
 */
public class UniformPointCrossover extends ConfigOperator<GPModel> implements GPCrossover {

	/**
	 * Requests an <code>Integer</code> which is the point chosen in the first
	 * parent for the uniform point crossover operation.
	 */
	public static final Stat XO_POINT1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests an <code>Integer</code> which is the point chosen in the second
	 * parent for the uniform point crossover operation.
	 */
	public static final Stat XO_POINT2 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests a <code>Node</code> which is the subtree from the
	 * first parent program which is being exchanged into the second parent.
	 */
	public static final Stat XO_SUBTREE1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests a <code>Node</code> which is the subtree from the
	 * second parent program which is being exchanged into the first parent.
	 */
	public static final Stat XO_SUBTREE2 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	// The random number generator for controlling random behaviour.
	private RandomNumberGenerator rng;

	/**
	 * Constructs a <code>UniformPointCrossover</code> with the only necessary
	 * parameter provided.
	 * 
	 * @param rng a <code>RandomNumberGenerator</code> used to lead 
	 * non-deterministic behaviour.
	 */
	public UniformPointCrossover(final RandomNumberGenerator rng) {
		this((GPModel) null);
		
		this.rng = rng;
	}
	
	/**
	 * Constructs a <code>UniformPointCrossover</code>.
	 * 
	 * @param model the current controlling model.
	 */
	public UniformPointCrossover(final GPModel model) {
		super(model);
	}

	/**
	 * Configures this operator with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		rng = getModel().getRNG();
	}

	/**
	 * GPCrossover the two <code>CandidatePrograms</code> provided as arguments
	 * using uniform swap points. Random crossover points are chosen at random
	 * in both programs, the genetic material at the points are then exchanged.
	 * The resulting programs are returned as new GPCandidateProgram objects.
	 * 
	 * @param p1 The first GPCandidateProgram selected to undergo uniform
	 *        point crossover.
	 * @param p2 The second GPCandidateProgram selected to undergo uniform
	 *        point crossover.
	 */
	@Override
	public GPCandidateProgram[] crossover(final CandidateProgram p1,
			final CandidateProgram p2) {
		final GPCandidateProgram program1 = (GPCandidateProgram) p1;
		final GPCandidateProgram program2 = (GPCandidateProgram) p2;

		// Select swap points.
		final int swapPoint1 = rng.nextInt(program1.getProgramLength());
		final int swapPoint2 = rng.nextInt(program2.getProgramLength());

		// Add crossover points to the stats manager.
		Stats.get().addData(XO_POINT1, swapPoint1);
		Stats.get().addData(XO_POINT2, swapPoint2);
		
		// Get copies of subtrees to swap.
		final Node subtree1 = program1.getNthNode(swapPoint1);// .clone();
		final Node subtree2 = program2.getNthNode(swapPoint2);// .clone();

		// Add subtrees into the stats manager.
		Stats.get().addData(XO_SUBTREE1, subtree1);
		Stats.get().addData(XO_SUBTREE2, subtree2);
		
		// Perform swap.
		program1.setNthNode(swapPoint1, subtree2);
		program2.setNthNode(swapPoint2, subtree1);

		return new GPCandidateProgram[]{program1, program2};
	}
	
	/**
	 * Returns the random number generator that this crossover is using or
	 * <code>null</code> if none has been set.
	 * 
	 * @return the rng the currently set random number generator.
	 */
	public RandomNumberGenerator getRNG() {
		return rng;
	}

	/**
	 * Sets the random number generator to use. If a model has been set then
	 * this parameter will be overwritten with the random number generator from
	 * that model on the next configure event.
	 * 
	 * @param rng the random number generator to set.
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		this.rng = rng;
	}
}
