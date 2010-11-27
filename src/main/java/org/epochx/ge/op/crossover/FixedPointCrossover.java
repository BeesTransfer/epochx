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
package org.epochx.ge.op.crossover;

import java.util.List;

import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class implements a fixed point crossover on two
 * <code>CandidatePrograms</code>.
 * 
 * <p>
 * The operation is performed on the programs' chromosomes in a similar manner
 * to OnePointCrossover. One random codon position is chosen which is within the
 * length bounds of both parent programs. Then all codons from that point
 * onwards in both programs are exchanged.
 * 
 * <p>
 * Fixed point crossover results in two child programs with chromosomes of equal
 * size to the two parents passed in, thus fixed point crossover prevents
 * chromosome length expanding over a run. However, chromosome length may still
 * change as a result of other operations in the algorithm such as the extension
 * property if used during mapping.
 * 
 * @see OnePointCrossover
 */
public class FixedPointCrossover extends ConfigOperator<GEModel> implements GECrossover {

	/**
	 * Requests an <code>Integer</code> which is the point in the parents'
	 * chromosomes chosen for the fixed point crossover operation.
	 */
	public static final Stat XO_POINT = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests a <code>List&lt;Integer&gt;</code> which is the portion of the
	 * first parent program which is being exchanged into the second parent.
	 */
	public static final Stat XO_CODONS1 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	/**
	 * Requests a <code>List&lt;Integer&gt;</code> which is the portion of the
	 * second parent program which is being exchanged into the first parent.
	 */
	public static final Stat XO_CODONS2 = new AbstractStat(ExpiryEvent.CROSSOVER) {};
	
	// The random number generator in use.
	private RandomNumberGenerator rng;

	/**
	 * Constructs an instance of <code>FixedPointCrossover</code> with the only 
	 * necessary parameter given.
	 * 
	 * @param rng a <code>RandomNumberGenerator</code> used to lead 
	 * non-deterministic behaviour.
	 */
	public FixedPointCrossover(final RandomNumberGenerator rng) {
		this((GEModel) null);
		
		this.rng = rng;
	}
	
	/**
	 * Constructs an instance of <code>FixedPointCrossover</code>.
	 * 
	 * @param model the current controlling model.
	 */
	public FixedPointCrossover(final GEModel model) {
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
	 * Performs a fixed point crossover operation on the specified parent
	 * programs.
	 * 
	 * <p>
	 * The operation is performed on the programs' chromosomes in a similar
	 * manner to OnePointCrossover. One random codon position is chosen which is
	 * within the length bounds of both parent programs. Then all codons from
	 * that point onwards in both programs are exchanged.
	 * 
	 * @param p1 {@inheritDoc}
	 * @param p2 {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public GECandidateProgram[] crossover(final CandidateProgram p1,
			final CandidateProgram p2) {
		final GECandidateProgram parent1 = (GECandidateProgram) p1;
		final GECandidateProgram parent2 = (GECandidateProgram) p2;

		// Pick a point in the shortest parent chromosome.
		int crossoverPoint = 0;
		final int parent1Codons = parent1.getNoCodons();
		final int parent2Codons = parent2.getNoCodons();
		if (parent1Codons < parent2Codons) {
			crossoverPoint = rng.nextInt(parent1Codons);
		} else {
			crossoverPoint = rng.nextInt(parent2Codons);
		}
		
		// Add crossover point to the stats manager.
		Stats.get().addData(XO_POINT, crossoverPoint);

		// Make copies of the parents.
		final GECandidateProgram child1 = (GECandidateProgram) parent1.clone();
		final GECandidateProgram child2 = (GECandidateProgram) parent2.clone();

		final List<Integer> part1 = child1.removeCodons(crossoverPoint,
				child1.getNoCodons());
		final List<Integer> part2 = child2.removeCodons(crossoverPoint,
				child2.getNoCodons());

		// Add codon portions into the stats manager.
		Stats.get().addData(XO_CODONS1, part1);
		Stats.get().addData(XO_CODONS2, part2);
		
		// Swap over the endings at the crossover points.
		child2.appendCodons(part1);
		child1.appendCodons(part2);

		return new GECandidateProgram[]{child1, child2};
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