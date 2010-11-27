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
package org.epochx.ge.op.mutation;

import java.util.*;

import org.epochx.ge.codon.CodonGenerator;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class performs a simple point mutation on a
 * <code>GECandidateProgram</code>.
 * 
 * <p>
 * Each codon in the program's chromosome is considered for mutation, with the
 * probability of that codon being mutated given as an argument to the
 * PointMutation constructor. If the codon does undergo mutation then a
 * replacement codon is generated using the CodonGenerator specified in the
 * model.
 */
public class PointMutation extends ConfigOperator<GEModel> implements GEMutation {

	/**
	 * Requests a <code>List&lt;Integer&gt;</code> which is a list of the points
	 * modified as a result of the point mutation operation.
	 */
	public static final Stat MUT_POINTS = new AbstractStat(ExpiryEvent.MUTATION) {};
	
	// The probability each codon has of being mutated in a selected program.
	private double pointProbability;

	private RandomNumberGenerator rng;
	private CodonGenerator codonGenerator;

	/**
	 * Constructs a <code>PointMutation</code> with all the necessary
	 * parameters given.
	 */
	public PointMutation(final CodonGenerator codonGenerator, 
			final RandomNumberGenerator rng, final double pointProbability) {
		this(null, pointProbability);
		
		this.codonGenerator = codonGenerator;
		this.rng = rng;
	}
	
	/**
	 * Construct a point mutation with a default point probability of 0.01. It
	 * is generally recommended that the
	 * <code>PointMutation(GEModel, double)</code> constructor is used instead.
	 * 
	 * @param model The current controlling model. Parameters such as the codon
	 *        generator to use will come from here.
	 */
	public PointMutation(final GEModel model) {
		this(model, 0.01);
	}

	/**
	 * Construct a point mutation with user specified point probability.
	 * 
	 * @param model The current controlling model. Parameters such as the codon
	 *        generator to use will come from here.
	 * @param pointProbability The probability each codon in a selected program
	 *        has of undergoing a mutation. 1.0 would result in all nodes
	 *        being changed, and 0.0 would mean no codons were changed. A
	 *        typical value would be 0.01.
	 */
	public PointMutation(final GEModel model, final double pointProbability) {
		super(model);
		
		this.pointProbability = pointProbability;
	}

	/*
	 * Configures this operator with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		rng = getModel().getRNG();
		codonGenerator = getModel().getCodonGenerator();
	}

	/**
	 * Perform point mutation on the given GECandidateProgram. Each codon in the
	 * program's chromosome is considered in turn, with each having the given
	 * probability of actually being exchanged. Given that a codon is chosen
	 * then a new codon is generated using the model's CodonGenerator to
	 * replace it.
	 * 
	 * @param p The GECandidateProgram selected to undergo this mutation
	 *        operation.
	 * @return A GECandidateProgram that was the result of a point mutation on
	 *         the provided GECandidateProgram.
	 */
	@Override
	public GECandidateProgram mutate(final CandidateProgram p) {
		final GECandidateProgram program = (GECandidateProgram) p;

		final int noCodons = program.getNoCodons();

		List<Integer> points = new ArrayList<Integer>();
		
		for (int i = 0; i < noCodons; i++) {
			// Perform a point mutation at the ith node, pointProbability% of
			// time.
			if (rng.nextDouble() < pointProbability) {
				final int c = codonGenerator.getCodon();
				program.setCodon(i, c);
				points.add(i);
			}
		}
		
		// Add mutation points into the stats manager.
		Stats.get().addData(MUT_POINTS, points);

		return program;
	}

	/**
	 * Returns the currently set probability of each codon undergoing mutation.
	 * 
	 * @return a value between <code>0.0</code> and <code>1.0</code> inclusive
	 *         which is the probability that a codon will undergo mutation.
	 */
	public double getPointProbability() {
		return pointProbability;
	}

	/**
	 * Sets the probability that each codon considered undergoes mutation.
	 * 
	 * @param pointProbability The probability each codon in a selected program
	 *        has of undergoing a mutation. 1.0 would result in all nodes
	 *        being changed, and 0.0 would mean no codons were changed. A
	 *        typical value would be 0.01.
	 */
	public void setPointProbability(double pointProbability) {
		this.pointProbability = pointProbability;
	}
	
	/**
	 * Returns the random number generator that this initialiser is using or
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
	 * Returns the codon generator that this mutation is using to provide
	 * new codons.
	 * 
	 * @return the codonGenerator the <code>CodonGenerator</code> that is
	 *         providing each new codon.
	 */
	public CodonGenerator getCodonGenerator() {
		return codonGenerator;
	}

	/**
	 * Sets the codon generator that this mutation should use to obtain new
	 * codons.
	 * 
	 * @param codonGenerator the codonGenerator to use in generating new codons
	 *        for the new candidate programs.
	 */
	public void setCodonGenerator(final CodonGenerator codonGenerator) {
		this.codonGenerator = codonGenerator;
	}
}
