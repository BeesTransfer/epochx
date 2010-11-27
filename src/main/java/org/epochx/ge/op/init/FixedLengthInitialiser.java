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
package org.epochx.ge.op.init;

import java.util.*;

import org.epochx.ge.codon.CodonGenerator;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;

/**
 * Initialisation implementation which constructs each program's chromosome to
 * a specified length with a random sequence of codons. All programs generated
 * with this initialiser will have the same length of chromosome. Each codon
 * is generated with the <code>CodonGenerator</code>.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>population size</li>
 * <li>codon generator</code>
 * </ul>
 * 
 * <p>
 * If the <code>getModel</code> method returns <code>null</code> then no model
 * is set and whatever static parameters have been set as parameters to the
 * constructor or using the standard accessor methods will be used. If any
 * compulsory parameters remain unset when the initialiser is requested to
 * generate new programs, then an <code>IllegalStateException</code> will be
 * thrown.
 * 
 * @see FullInitialiser
 * @see GrowInitialiser
 * @see RampedHalfAndHalfInitialiser
 */
public class FixedLengthInitialiser extends ConfigOperator<GEModel> implements GEInitialiser {

	// The generator to provide the new codons.
	private CodonGenerator codonGenerator;

	// The size of the populations to construct.
	private int popSize;

	// The length of every program chromosome to generate.
	private int chromosomeLength;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	/**
	 * Constructs a <code>FixedLengthInitialiser</code> with all the necessary
	 * parameters given.
	 */
	public FixedLengthInitialiser(final CodonGenerator codonGenerator,
			final int popSize, final int chromosomeLength,
			final boolean acceptDuplicates) {
		this(null, chromosomeLength, acceptDuplicates);
		
		this.codonGenerator = codonGenerator;
		this.popSize = popSize;
	}

	/**
	 * Constructs a <code>FixedLengthInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>GEModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param chromosomeLength The initial length that chromosomes should be
	 *        generated to.
	 */
	public FixedLengthInitialiser(final GEModel model,
			final int chromosomeLength) {
		this(model, chromosomeLength, true);
	}

	/**
	 * Constructs a <code>FixedLengthInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events.
	 * 
	 * @param model the <code>GEModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param chromosomeLength The initial length that chromosomes should be
	 *        generated to.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public FixedLengthInitialiser(final GEModel model,
			final int chromosomeLength, final boolean acceptDuplicates) {
		super(model);
		
		this.chromosomeLength = chromosomeLength;
		this.acceptDuplicates = acceptDuplicates;
	}

	/**
	 * Configures this operator with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		popSize = getModel().getPopulationSize();
		codonGenerator = getModel().getCodonGenerator();
	}

	/**
	 * Generates a population of new CandidatePrograms constructed by generating
	 * their chromosomes with the codon generator to the currently set
	 * chromosome length. The size of the population will be equal to the
	 * population size attribute. All programs in the population are only
	 * guarenteed to be unique (as defined by the <code>equals</code> method on
	 * <code>GECandidateProgram</code>) if the <code>isDuplicatesEnabled</code>
	 * method returns <code>true</code>.
	 * 
	 * @return A List of newly generated CandidatePrograms which will form the
	 *         initial population for a GE run.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		if (popSize < 1) {
			throw new IllegalStateException(
					"Population size must be 1 or greater");
		}

		// Initialise population of candidate programs.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		// Build population.
		for (int i = 0; i < popSize; i++) {
			GECandidateProgram candidate;

			do {
				candidate = getInitialProgram();
			} while (!acceptDuplicates && firstGen.contains(candidate));
			
			firstGen.add(candidate);
		}

		// Return starting population.
		return firstGen;
	}

	/**
	 * Constructs a new <code>GECandidateProgram</code> with a chromosome of
	 * length that matches the chromosome length property of this initialiser.
	 * Each codon will be generated with the currently set
	 * <code>CodonGenerator</code>.
	 * 
	 * @return a new <code>GECandidateProgram</code> instance.
	 */
	public GECandidateProgram getInitialProgram() {
		if (chromosomeLength < 1) {
			throw new IllegalStateException("Chromosome length must be 1 or greater");
		} else if (codonGenerator == null) {
			throw new IllegalStateException("Codon generator is not set");
		}

		// Generate the list of codons.
		List<Integer> codons = new ArrayList<Integer>(chromosomeLength);
		for (int i = 0; i < chromosomeLength; i++) {
			codons.add(codonGenerator.getCodon());
		}

		// Construct and return the new program.
		return new GECandidateProgram(codons, getModel());
	}

	/**
	 * Returns the length of chromosome that all new
	 * <code>GECandidateProgram</code> instances will be generated with.
	 * 
	 * @return the fixed length that this initialiser is generating chromosomes
	 *         to.
	 */
	public int getChromosomeLength() {
		return chromosomeLength;
	}

	/**
	 * Sets the length of chromosome for all future
	 * <code>GECandidateProgram</code> constructions.
	 * 
	 * @param chromosomeLength the fixed length to use for the chromosomes of
	 *        generated programs.
	 */
	public void setChromosomeLength(int chromosomeLength) {
		this.chromosomeLength = chromosomeLength;
	}

	/**
	 * Returns whether or not duplicates are currently accepted or rejected from
	 * generated populations.
	 * 
	 * @return <code>true</code> if duplicates are currently accepted in any
	 *         populations generated by the <code>getInitialPopulation</code>
	 *         method and <code>false</code> otherwise
	 */
	public boolean isDuplicatesEnabled() {
		return acceptDuplicates;
	}

	/**
	 * Sets whether duplicates should be allowed in the populations that are
	 * generated, or if they should be discarded.
	 * 
	 * @param acceptDuplicates whether duplicates should be accepted in the
	 *        populations that are constructed.
	 */
	public void setDuplicatesEnabled(final boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}

	/**
	 * Returns the size of the populations that this initialiser constructs or
	 * <code>-1</code> if none has been set.
	 * 
	 * @return the size of the populations that this initialiser will generate.
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * Sets the size of the populations that this initialiser should construct
	 * on calls to the <code>getInitialPopulation</code> method.
	 * 
	 * @param popSize the size of the populations that should be created by this
	 *        initialiser.
	 */
	public void setPopSize(final int popSize) {
		this.popSize = popSize;
	}

	/**
	 * Returns the codon generator that this initialiser is using to provide
	 * new codons for each candidate program that is constructed.
	 * 
	 * @return the codonGenerator the <code>CodonGenerator</code> that is
	 *         providing each new codon.
	 */
	public CodonGenerator getCodonGenerator() {
		return codonGenerator;
	}

	/**
	 * Sets the codon generator that this initialiser should use to obtain new
	 * codons for newly constructed candidate programs.
	 * 
	 * @param codonGenerator the codonGenerator to use in generating new codons
	 *        for the new candidate programs.
	 */
	public void setCodonGenerator(CodonGenerator codonGenerator) {
		this.codonGenerator = codonGenerator;
	}
}
