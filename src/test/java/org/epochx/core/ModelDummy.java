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
package org.epochx.core;

import org.epochx.op.*;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.*;

/**
 * This class overrides Model and removes all validation on all set methods.
 * ModelDummy is intended for testing only, since it allows components to be
 * tested with invalid data.
 */
public class ModelDummy extends Model {

	// Operators.
	private PoolSelector poolSelector;
	private ProgramSelector programSelector;

	private Initialiser initialiser;
	private Crossover crossover;
	private Mutation mutation;

	// Control parameters.
	private RandomNumberGenerator randomNumberGenerator;

	private int noRuns;
	private int noGenerations;
	private int populationSize;
	private int poolSize;
	private int noElites;

	private double terminationFitness;
	private double crossoverProbability;
	private double mutationProbability;
	private double reproductionProbability;

	// Caching.
	private boolean cacheFitness;

	/**
	 * Construct the model with defaults.
	 */
	public ModelDummy() {
		// Control parameters.
		noRuns = 1;
		noGenerations = 1;
		populationSize = 1;
		poolSize = 1;
		noElites = 1;
		terminationFitness = 0.0;
		crossoverProbability = 0.9;
		mutationProbability = 0.1;
		reproductionProbability = 0.0;

		// Operators.
		programSelector = new TournamentSelector(this, 7);
		poolSelector = null;
		randomNumberGenerator = new MersenneTwisterFast();

		// Caching.
		cacheFitness = true;
	}

	@Override
	public double getFitness(final CandidateProgram program) {
		return 0;
	}

	@Override
	public Initialiser getInitialiser() {
		return initialiser;
	}

	@Override
	public void setInitialiser(final Initialiser initialiser) {
		this.initialiser = initialiser;
	}

	@Override
	public Crossover getCrossover() {
		return crossover;
	}

	@Override
	public void setCrossover(final Crossover crossover) {
		this.crossover = crossover;
	}

	@Override
	public Mutation getMutation() {
		return mutation;
	}

	@Override
	public void setMutation(final Mutation mutation) {
		this.mutation = mutation;
	}

	@Override
	public boolean cacheFitness() {
		return cacheFitness;
	}

	@Override
	public void setCacheFitness(final boolean cacheFitness) {
		this.cacheFitness = cacheFitness;
	}

	@Override
	public int getNoRuns() {
		return noRuns;
	}

	@Override
	public void setNoRuns(final int noRuns) {
		this.noRuns = noRuns;
	}

	@Override
	public int getNoGenerations() {
		return noGenerations;
	}

	@Override
	public void setNoGenerations(final int noGenerations) {
		this.noGenerations = noGenerations;
	}

	@Override
	public int getPopulationSize() {
		return populationSize;
	}

	@Override
	public void setPopulationSize(final int populationSize) {
		this.populationSize = populationSize;
	}

	@Override
	public int getPoolSize() {
		return poolSize;
	}

	@Override
	public void setPoolSize(final int poolSize) {
		this.poolSize = poolSize;
	}

	@Override
	public int getNoElites() {
		return noElites;
	}

	@Override
	public void setNoElites(final int noElites) {
		this.noElites = noElites;
	}

	@Override
	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	@Override
	public void setCrossoverProbability(final double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	@Override
	public double getMutationProbability() {
		return mutationProbability;
	}

	@Override
	public void setMutationProbability(final double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	@Override
	public double getReproductionProbability() {
		return reproductionProbability;
	}

	@Override
	public void setReproductionProbability(final double reproductionProbability) {
		this.reproductionProbability = reproductionProbability;
	}

	@Override
	public double getTerminationFitness() {
		return terminationFitness;
	}

	@Override
	public void setTerminationFitness(final double terminationFitness) {
		this.terminationFitness = terminationFitness;
	}

	@Override
	public ProgramSelector getProgramSelector() {
		return programSelector;
	}

	@Override
	public void setProgramSelector(final ProgramSelector programSelector) {
		this.programSelector = programSelector;
	}

	@Override
	public PoolSelector getPoolSelector() {
		return poolSelector;
	}

	@Override
	public void setPoolSelector(final PoolSelector poolSelector) {
		this.poolSelector = poolSelector;
	}

	@Override
	public RandomNumberGenerator getRNG() {
		return randomNumberGenerator;
	}

	@Override
	public void setRNG(final RandomNumberGenerator rng) {
		randomNumberGenerator = rng;
	}

}
