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
package org.epochx.gp.op.mutation;

import java.util.List;

import org.epochx.epox.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.op.init.GrowInitialiser;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class performs a subtree mutation on a <code>GPCandidateProgram</code>.
 * 
 * <p>
 * A mutation point is randomly selected anywhere in the program tree. Then the
 * node at that point is replaced with a newly generated program tree, which is
 * created using a grow strategy.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>random number generator</li>
 * <li>syntax</li>
 * </ul>
 * 
 * <p>
 * If the <code>getModel</code> method returns <code>null</code> then no model
 * is set and whatever static parameters have been set as parameters to the
 * constructor or using the standard accessor methods will be used. If any
 * compulsory parameters remain unset when the mutation is performed then an 
 * <code>IllegalStateException</code> will be thrown.
 */
public class SubtreeMutation extends ConfigOperator<GPModel> implements GPMutation {

	/**
	 * Requests an <code>Integer</code> which is the point which was modified as
	 * a result of the subtree mutation operation.
	 */
	public static final Stat MUT_POINT = new AbstractStat(ExpiryEvent.MUTATION) {};
	
	/**
	 * Requests a <code>Node</code> which is the subtree that was inserted in
	 * the program undergoing subtree mutation.
	 */
	public static final Stat MUT_SUBTREE = new AbstractStat(ExpiryEvent.MUTATION) {};
	
	// Grow initialiser to build our replacement subtrees.
	private final GrowInitialiser grower;
	
	private RandomNumberGenerator rng;
	
	// The maximum depth of the new subtree.
	private int maxSubtreeDepth;

	public SubtreeMutation(final RandomNumberGenerator rng, final List<Node> syntax, final int maxSubtreeDepth) {
		this((GPModel) null, maxSubtreeDepth);
		
		this.rng = rng;
		
		grower.setRNG(rng);
		grower.setSyntax(syntax);
	}
	
	/**
	 * Simple constructor for subtree mutation using a default maximum depth
	 * of 4 for new subtrees.
	 * 
	 * @param model The controlling model which provides any configuration
	 *        parameters for the run.
	 */
	public SubtreeMutation(final GPModel model) {
		// 4 is a slightly arbitrary choice but we had to choose something.
		this(model, 4);
	}

	/**
	 * Subtree mutation constructor with control for the maximum depth of new
	 * subtrees.
	 * 
	 * @param model The controlling model which provides any configuration
	 *        parameters for the run.
	 * @param maxSubtreeDepth The maximum depth of the inserted subtree.
	 */
	public SubtreeMutation(final GPModel model, final int maxSubtreeDepth) {
		super(model);
		
		this.maxSubtreeDepth = maxSubtreeDepth;

		// Don't let this configure itself because it will use the wrong depth.
		grower = new GrowInitialiser(null, null, -1, maxSubtreeDepth, false);
	}

	/*
	 * Configure component with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		rng = getModel().getRNG();
		
		grower.setRNG(rng);
		grower.setSyntax(getModel().getSyntax());
	}

	/**
	 * Perform subtree mutation on the given GPCandidateProgram. A mutation
	 * point
	 * is randomly selected anywhere in the program tree. Then the node at that
	 * point is replaced with a newly generated program tree, which is created
	 * using a grow strategy.
	 * 
	 * @param p The GPCandidateProgram selected to undergo this mutation
	 *        operation.
	 * @return A GPCandidateProgram that was the result of a point mutation on
	 *         the provided GPCandidateProgram.
	 */
	@Override
	public GPCandidateProgram mutate(final CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;

		// Randomly choose a mutation point.
		final int length = program.getProgramLength();
		final int mutationPoint = rng.nextInt(length);

		// Add mutation point into the stats manager.
		Stats.get().addData(MUT_POINT, mutationPoint);
		
		// Grow a new subtree using the GrowInitialiser.
		final Node subtree = grower.getGrownNodeTree(maxSubtreeDepth);

		// Add subtree into the stats manager.
		Stats.get().addData(MUT_SUBTREE, subtree);
		
		// Set the new subtree.
		program.setNthNode(mutationPoint, subtree);

		return program;
	}

	/**
	 * Returns the random number generator that this mutation is using or
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
		
		grower.setRNG(rng);
	}
	
	/**
	 * Returns the maximum depth that the new subtrees being inserted into the 
	 * program may be.
	 *  
	 * @return an int which is the maximum subtree depth that will be generated.
	 */
	public int getMaxSubtreeDepth() {
		return maxSubtreeDepth;
	}
	
	/**
	 * Sets the maximum depth to use for new subtrees being inserted into 
	 * programs by this mutation.
	 * 
	 * @param maxSubtreeDepth the maximum subtree depth to use for new subtrees.
	 */
	public void setMaxSubtreeDepth(int maxSubtreeDepth) {
		this.maxSubtreeDepth = maxSubtreeDepth;
		
		grower.setMaxDepth(maxSubtreeDepth);
	}
}
