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
package org.epochx.ge.model;

import org.epochx.core.Model;
import org.epochx.ge.codon.*;
import org.epochx.ge.mapper.*;
import org.epochx.ge.op.crossover.OnePointCrossover;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.op.mutation.PointMutation;
import org.epochx.tools.grammar.Grammar;

/**
 * Model implementation for performing Grammatical Evolution.
 */
public abstract class GEModel extends Model {

	// Control parameters.
	private Grammar grammar;
	private Mapper mapper;
	private CodonGenerator codonGenerator;

	private int maxDepth;
	private int maxInitialDepth;
	private int maxCodonSize;
	private final int maxChromosomeLength;

	private boolean cacheSource;

	/**
	 * Construct a GEModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GEModel() {
		// Set default parameter values.
		mapper = new DepthFirstMapper(this);
		codonGenerator = new StandardGenerator(this);
		grammar = null;

		maxDepth = 14;
		maxInitialDepth = 8;
		maxCodonSize = Integer.MAX_VALUE;
		maxChromosomeLength = -1;

		// Caching.
		cacheSource = true;

		// Operators.
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
		setCrossover(new OnePointCrossover(this));
		setMutation(new PointMutation(this));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if (getGrammar() == null) {
			throw new IllegalStateException("no grammar set");
		}

		super.run();
	}

	/**
	 * Returns the grammar instance that determines the structure of the
	 * programs to be evolved. As well as defining the syntax of solutions, the
	 * grammar also essentially determines the function and terminal sets which
	 * are features of tree GP.
	 * 
	 * @return the language grammar that defines the syntax of solutions.
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the grammar that defines the valid syntax of the programs to be
	 * evolves.
	 * 
	 * @param grammar the language grammar to use to define the syntax of
	 *        solutions.
	 */
	public void setGrammar(final Grammar grammar) {
		if (grammar != null) {
			this.grammar = grammar;
		} else {
			throw new IllegalArgumentException("grammar must not be null");
		}

		assert (this.grammar != null);
	}

	/**
	 * Returns the mapper which should be used to perform the mapping from a
	 * chromosome (List of Codons) to a source string with a syntax matching
	 * the grammar.
	 * 
	 * <p>
	 * Defaults to an instance of {@link DepthFirstMapper}.
	 * 
	 * @return a Mapper to be used to map from chromosome to source.
	 */
	public Mapper getMapper() {
		return mapper;
	}

	/**
	 * Overwrites the default mapper used to map chromosomes to source strings.
	 * 
	 * @param mapper the mapper to be used during the mapping operation.
	 */
	public void setMapper(final Mapper mapper) {
		if (mapper != null) {
			this.mapper = mapper;
		} else {
			throw new IllegalArgumentException("mapper must not be null");
		}

		assert (this.mapper != null);
	}

	/**
	 * Returns the CodonGenerator that the system should use for generating any
	 * new Codons.
	 * 
	 * <p>
	 * Defaults to an instance of {@link StandardGenerator}.
	 * 
	 * @return the CodonGenerator to use for generating new codons.
	 */
	public CodonGenerator getCodonGenerator() {
		return codonGenerator;
	}

	/**
	 * Overwrites the default codon generator used to generate new codons
	 * throughout the run.
	 * 
	 * @param codonGenerator the codon generator to be used any time a new
	 *        codon is required.
	 */
	public void setCodonGenerator(final CodonGenerator codonGenerator) {
		if (codonGenerator != null) {
			this.codonGenerator = codonGenerator;
		} else {
			throw new IllegalArgumentException(
					"codonGenerator must not be null");
		}

		assert (this.codonGenerator != null);
	}

	/**
	 * Returns the maximum value of a codon. Codon values are positive integers
	 * from zero to this size.
	 * 
	 * <p>
	 * Defaults to <code>Integer.MAX_VALUE</code>.
	 * 
	 * @return the maximum value of a codon.
	 */
	public int getMaxCodonSize() {
		return maxCodonSize;
	}

	/**
	 * Overwrites the default maximum size of each codon.
	 * 
	 * @param maxCodonSize the maximum size of a codon to replace the current
	 *        maximum with. Must be a positive integer.
	 */
	public void setMaxCodonSize(final int maxCodonSize) {
		if (maxCodonSize >= 0) {
			this.maxCodonSize = maxCodonSize;
		} else {
			throw new IllegalArgumentException(
					"maxCodonSize must be zero or more");
		}

		assert (this.maxCodonSize >= 0);
	}

	/**
	 * Returns the maximum number of codons that should be allowed in a
	 * chromosome. Crossovers or mutations that result in a larger chromosome
	 * size will not be allowed.
	 * 
	 * <p>
	 * Defaults to 100.
	 * 
	 * @return the maximum number of codons to be allowed in a chromosome.
	 */
	public int getMaxChromosomeLength() {
		return maxChromosomeLength;
	}

	/**
	 * Overwrites the default maximum number of codons in a program's
	 * chromosome.
	 * 
	 * @param maxChromosomeLength the maximum number of codons to allow in a
	 *        chromosome.
	 */
	public void setMaxChromosomeLength(final int maxChromosomeLength) {
		if (maxChromosomeLength >= -1) {
			maxCodonSize = maxChromosomeLength;
		} else {
			throw new IllegalArgumentException(
					"maxChromosomeLength must be -1 or more");
		}

		assert (this.maxChromosomeLength >= 0);
	}

	/**
	 * Returns the maximum depth of the derivation trees allowed. Crossovers or
	 * mutations that result in a larger chromosome will not be allowed.
	 * 
	 * <p>
	 * Defaults to 14.
	 * 
	 * @return the maximum depth of derivation trees to allow.
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Overwrites the default maximum allowable depth of a program's derivation
	 * tree.
	 * 
	 * <p>
	 * Max depth of -1 is allowed to indicate no limit.
	 * 
	 * @param maxDepth the maximum depth to allow a program's derivation tree.
	 */
	public void setMaxDepth(final int maxDepth) {
		if ((maxDepth >= 1) || (maxDepth == -1)) {
			this.maxDepth = maxDepth;
		} else {
			throw new IllegalArgumentException(
					"maxDepth must either be -1 or greater than 0");
		}

		assert ((this.maxDepth >= 1) || (this.maxDepth == -1));
	}

	/**
	 * Returns the maximum depth of the derivation trees allowed at
	 * initialisation.
	 * 
	 * <p>
	 * Defaults to 8.
	 * 
	 * @return the maximum depth of derivation trees to allow after
	 *         initialisation.
	 */
	public int getMaxInitialDepth() {
		return maxInitialDepth;
	}

	/**
	 * Overwrites the default maximum allowable depth of a program's derivation
	 * tree after initialisation.
	 * 
	 * Max depth of -1 is allowed to indicate no limit.
	 * 
	 * @param maxInitialDepth the maximum depth to allow a program's derivation 
	 * 			tree after initialisation.
	 */
	public void setMaxInitialDepth(final int maxInitialDepth) {
		if ((maxInitialDepth >= 1) || (maxInitialDepth == -1)) {
			this.maxInitialDepth = maxInitialDepth;
		} else {
			throw new IllegalArgumentException(
					"maxInitialDepth must either be -1 or greater than 0");
		}

		assert ((this.maxInitialDepth >= 1) || (this.maxInitialDepth == -1));
	}

	/**
	 * Whether CandidatePrograms should cache their source code after mapping
	 * to reduce the need for mapping when the codons are unchanged. Caching
	 * the source potentially gives a large performance improvement and is
	 * generally desirable but if the grammar might change during a run then
	 * caching shouldn't be used as the same codons will evaluate to a
	 * different source.
	 * 
	 * <p>
	 * Defaults to <code>true</code>.
	 * 
	 * @return true if the source should be cached after mapping and false
	 *         otherwise.
	 */
	public boolean cacheSource() {
		return cacheSource;
	}

	/**
	 * Overwrites the default setting of whether to cache the source code after
	 * mapping, until the chromosome is changed again.
	 * 
	 * @param cacheSource true if the source should be cached, false otherwise.
	 */
	public void setCacheSource(final boolean cacheSource) {
		this.cacheSource = cacheSource;
	}
}
