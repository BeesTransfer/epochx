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
import org.epochx.life.ConfigListener;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class implements a Koza style crossover operation on two
 * <code>CandidatePrograms</code>.
 * 
 * <p>
 * Koza crossover performs a one point exchange with the choice of a swap point
 * being either a function or terminal node with assigned probabilities. This
 * class provides a constructor for specifying a probability of selecting a
 * function swap point. The default constructor uses the typical rates of 90%
 * function node swap point and 10% terminal node swap points.
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
 * @see UniformPointCrossover
 */
public class KozaCrossover extends ConfigOperator<GPModel> implements GPCrossover, ConfigListener {

	/**
	 * Requests an <code>Integer</code> which is the point chosen in the first
	 * parent for the koza crossover operation.
	 */
	public static final Stat XO_POINT1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests an <code>Integer</code> which is the point chosen in the second
	 * parent for the koza crossover operation.
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
	
	// The probability of choosing a function node as the swap point.
	private double functionSwapProbability;

	// The random number generator for controlling random behaviour.
	private RandomNumberGenerator rng;

	/**
	 * Constructs a <code>KozaCrossover</code>. The probability of a
	 * function node being selected as the swap point will default to 90%.
	 * 
	 * @param rng
	 */
	public KozaCrossover(final RandomNumberGenerator rng) {
		this((GPModel) null);
		
		this.rng = rng;
	}
	
	/**
	 * Constructs a <code>KozaCrossover</code>.
	 * 
	 * @param rng a random number generator.
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public KozaCrossover(final RandomNumberGenerator rng, final double functionSwapProbability) {
		this((GPModel) null, functionSwapProbability);
		
		this.rng = rng;
	}
	
	/**
	 * Default constructor for Koza standard crossover. The probability of a
	 * function node being selected as the swap point will default to 90%.
	 */
	public KozaCrossover(final GPModel model) {
		this(model, 0.9);
	}

	/**
	 * Construct an instance of Koza crossover.
	 * 
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public KozaCrossover(final GPModel model, final double functionSwapProbability) {
		super(model);
		
		this.functionSwapProbability = functionSwapProbability;
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
	 * using Koza crossover. The crossover points will be chosen from the
	 * function or terminal sets with the probability assigned at construction
	 * or the default value of 90% function node.
	 * 
	 * @param p1 The first GPCandidateProgram selected to undergo Koza
	 *        crossover.
	 * @param p2 The second GPCandidateProgram selected to undergo Koza
	 *        crossover.
	 */
	@Override
	public GPCandidateProgram[] crossover(final CandidateProgram p1,
			final CandidateProgram p2) {
		final GPCandidateProgram program1 = (GPCandidateProgram) p1;
		final GPCandidateProgram program2 = (GPCandidateProgram) p2;

		// Get swap points.
		final int swapPoint1 = getCrossoverPoint(program1);
		final int swapPoint2 = getCrossoverPoint(program2);

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

	/*
	 * Choose the crossover point for the given GPCandidateProgram with respect
	 * to the probabilities assigned for function and terminal node points.
	 */
	private int getCrossoverPoint(final GPCandidateProgram program) {
		// Calculate numbers of terminal and function nodes.
		final int length = program.getProgramLength();
		final int noTerminals = program.getNoTerminals();
		final int noFunctions = length - noTerminals;

		// Randomly decide whether to use a function or terminal node point.
		if ((noFunctions > 0) && (rng.nextDouble() < functionSwapProbability)) {
			// Randomly select a function node from the function set.
			final int f = rng.nextInt(noFunctions);
			final int nthFunctionNode = program.getRootNode().getNthFunctionNodeIndex(f);

			return nthFunctionNode;
		} else {
			// Randomly select a terminal node from the terminal set.
			final int t = rng.nextInt(noTerminals);
			final int nthTerminalNode = program.getRootNode().getNthTerminalNodeIndex(t);

			return nthTerminalNode;
		}
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
	
	/**
	 * Returns the currently set probability of a function being selected as the
	 * swap point.
	 * 
	 * @return a value between 0.0 and 1.0 which indicates the probability of a 
	 * function node being selected as a swap point instead of a terminal.
	 */
	public double getFunctionSwapProbability() {
		return functionSwapProbability;
	}
	
	/**
	 * Sets the probability of choosing a function node as the swap point rather
	 * than a terminal node. 
	 * @param functionSwapProbability a value between 0.0 and 1.0 for the 
	 * probability of choosing a function node to swap.
	 */
	public void setFunctionSwapProbability(double functionSwapProbability) {
		this.functionSwapProbability = functionSwapProbability;
	}
}
