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
package org.epochx.ge.representation;

import java.util.*;

import org.apache.commons.lang.ObjectUtils;
import org.epochx.ge.model.GEModel;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.*;

/**
 * A <code>GECandidateProgram</code> encapsulates an individual program within a
 * generation of a GE run.
 * 
 * <p>
 * The main components of a GECandidateProgram are a series of codons, which are
 * the individual's genotype; a source string in someway derived from the codons
 * based upon a grammar, which is the individuals phenotype; and a fitness score
 * calculated by the model that is controlling execution.
 * 
 * <p>
 * To ensure caching can provide maximum performance, the source code and
 * fitness of a program should always be obtained directly from the program
 * instance itself, with a call to <code>getSourceCode()</code> or
 * <code>getFitness()</code>, rather than forcing the model or otherwise to
 * calculate them on each call.
 */
public class GECandidateProgram extends CandidateProgram {

	// The controlling model.
	private GEModel model;

	// The genotype.
	private List<Integer> codons;
	
	// How the codons looked when we last built the phenotype.
	private List<Integer> sourceCacheCodons;
	
	// How the codons looked when we last evaluated the program fitness.
	private List<Integer> fitnessCacheCodons;

	// The phenotype cache.
	private NonTerminalSymbol parseTree;

	// The fitness of the phenotype cache.
	private double fitness;

	// How many of the codons are actually used during mapping.
	private int noActiveCodons;

	/**
	 * Constructs a new program individual with an empty sequence of codons.
	 * 
	 * @param model the controlling model which provides the configuration
	 *        parameters for the run.
	 */
	public GECandidateProgram(final GEModel model) {
		this(new ArrayList<Integer>(), model);
	}

	/**
	 * Constructs a new program individual with the specified codons as the
	 * genotypic chromosome.
	 * 
	 * @param codons the program's initial genotypic chromosome.
	 * @param model the controlling model which provides the configuration
	 *        parameters for the run.
	 */
	public GECandidateProgram(final List<Integer> codons, final GEModel model) {
		this.codons = codons;
		this.model = model;

		parseTree = null;
		fitness = -1;
		
		sourceCacheCodons = null;
		fitnessCacheCodons = null;
	}

	/**
	 * Appends a codon generated with the model's codon generator to the end of
	 * the candidate program's chromosome. All caches will be cleared.
	 */
	public void appendNewCodon() {
		appendCodon(model.getCodonGenerator().getCodon());
	}

	/**
	 * Appends codons generated with the model's codon generator to the end of
	 * the candidate program's chromosome. All caches will be cleared.
	 * 
	 * @param no the number of codons to be generated and appended.
	 */
	public void appendNewCodons(final int no) {
		for (int i = 0; i < no; i++) {
			appendNewCodon();
		}
	}

	/**
	 * Inserts a new codon into the chromosome at the specified index. The
	 * codon currently at that index and all following codons will be shifted
	 * right. The new codon will be generated with the model's codon generator.
	 * All caches will be cleared.
	 * 
	 * @param index index at which the new codon should be inserted.
	 */
	public void insertNewCodon(final int index) {
		codons.add(index, model.getCodonGenerator().getCodon());
	}

	/**
	 * Removes the codon at the specified position in the chromosome. Any
	 * subsequent codons will be shifted left. Returns the codon that was
	 * removed from the chromosome. All caches will be cleared.
	 * 
	 * @param index the index of the codon to be removed.
	 * @return the codon that was removed from the chromosome.
	 */
	public int removeCodon(final int index) {
		return codons.remove(index);
	}

	/**
	 * Removes a sequence of codons from the chromosome, between the specified
	 * indexes. All indexes in the range <code>from &lt;= x &lt; to</code> will
	 * be removed. The removed codons will be returned in a list. All caches
	 * will be cleared.
	 * 
	 * @param from index from which codons will be removed, inclusive of this
	 *        index.
	 * @param to index up to which codons will be remove, exclusive of this
	 *        index.
	 * @return a list of the codons that were removed.
	 */
	public List<Integer> removeCodons(final int from, final int to) {
		final List<Integer> removed = new ArrayList<Integer>();
		for (int i = from; i < to; i++) {
			// As we remove them the list will shrink so we use from not i.
			removed.add(codons.remove(from));
		}

		return removed;
	}

	/**
	 * Appends the specified codon to the program's chromosome. Care should be
	 * taken when using this method as it allows the generation of codons in
	 * any manner regardless of the models response to getCodonGenerator(). All
	 * caches will be cleared.
	 * 
	 * @param newCodon the codon to append to the chromosome.
	 */
	public void appendCodon(final int newCodon) {
		codons.add(newCodon);
	}
		
	/**
	 * Appends multiple codons to the candidate program's chromosome. Care
	 * should be taken when using this method as it allows the generation
	 * of codons in any manner regardless of the models response to
	 * getCodonGenerator(). All caches will be cleared.
	 * 
	 * @param newCodons a list of new codons to append to the chromosome.
	 */
	public void appendCodons(final List<Integer> newCodons) {
		for (int i = 0; i < newCodons.size(); i++) {
			appendCodon(newCodons.get(i));
		}
	}

	/**
	 * Replaces the codon at the specified index with the given codon. All
	 * caches will be cleared.
	 * 
	 * @param index index of the codon to be replaced.
	 * @param newCodon codon to be stored at the specified position.
	 */
	public void setCodon(final int index, final int newCodon) {
		codons.set(index, model.getCodonGenerator().getCodon());
	}

	/**
	 * Sets each codon from the startIndex. If the number of codons takes the
	 * indexes beyond the length of the current codons then the new codons will
	 * be added to the end of the list of codons. However if the first index is
	 * greater than the length of the current set of codons then an
	 * ArrayIndexOutOfBoundsException will be thrown. All caches will be
	 * cleared.
	 * 
	 * @param startIndex the index from which the codons should be replaced or
	 *        otherwise set.
	 * @param newCodons the codons that should be stored at the specified
	 *        positions.
	 * @throws ArrayIndexOutOfBoundsException If the first index is beyond the
	 *         end of the current list of codons.
	 */
	public void setCodons(final int startIndex, final List<Integer> newCodons) {
		// If more than one beyond the end of current codons.
		if (startIndex > codons.size()) {
			throw new ArrayIndexOutOfBoundsException(startIndex);
		}

		for (int i = 0; i < newCodons.size(); i++) {
			final int c = newCodons.get(i);
			if (i + startIndex < codons.size()) {
				codons.set(i + startIndex, c);
			} else {
				appendCodon(c);
			}
		}
	}

	/**
	 * Returns the codon at the specified index.
	 * 
	 * @param index the index at which the codon should be retrieved, between 0
	 *        and getNoCodons()-1.
	 * @return the integer codon found at the specified index.
	 */
	public int getCodon(final int index) {
		return codons.get(index);
	}

	/**
	 * Returns a sequence of codons from the specified <tt>from</tt> index,
	 * inclusive, to the <tt>to</tt> index, exclusive. An
	 * IndexOutOfBoundsException will be thrown if either of the indexes is
	 * outside the range of possible indexes.
	 * 
	 * @param from index from which codons should be returned, inclusive.
	 * @param to index, up to which codons should be returned, exclusive.
	 * @return a list of the codons between the specified indexes.
	 */
	public List<Integer> getCodons(final int from, final int to) {
		return codons.subList(from, to);
	}

	/**
	 * Returns a copy of the full list of codons in this program's chromosome.
	 * Note that because it is a copy that is returned, modifying the returned
	 * list will not modify the GECandidateProgram's list of codons. But also
	 * that calling this method frequently just to obtain the codons would be
	 * bad for performance as it creates a copy of the codons each time.
	 * 
	 * @return a copy of this program's codon string.
	 */
	public List<Integer> getCodons() {
		return codons;
	}

	/**
	 * Returns the number of codons in this program's chromosome.
	 * 
	 * @return the number of codons this program has.
	 */
	public int getNoCodons() {
		return codons.size();
	}

	public int getParseTreeDepth() {
		int depth = 0;

		if (parseTree != null) {
			depth = parseTree.getDepth();
		}

		return depth;
	}

	/**
	 * Returns the fitness of the candidate program. This method should be
	 * called over calling model.getFitness(GEModel) as it may be able to
	 * perform caching. If caching is in use then the fitness will never be
	 * calculated twice for the same codon string.
	 * 
	 * @return the fitness score of this candidate program according to the
	 *         model's getFitness method.
	 */
	@Override
	public double getFitness() {
		// If we're not caching or the cache is out of date.
		if (!model.cacheFitness() || !codons.equals(fitnessCacheCodons)) {
			fitness = model.getFitness(this);
			fitnessCacheCodons = new ArrayList<Integer>(codons);
		}

		return fitness;
	}

	/**
	 * Returns a source code string that is the phenotype to this candidate
	 * program's genotypic codon string. This method should always be called
	 * over calling the mapper directly as it may be able to perform caching.
	 * If caching is in use then the source code will never be generated twice
	 * for the same codon string.
	 * 
	 * <p>
	 * The source code is generated by performing mapping using the mapper
	 * specified in the controlling model.
	 * 
	 * @return the source code of this candidate program.
	 */
	public String getSourceCode() {
		// If we have not already done the mapping then do it now and stash it.
		if (!model.cacheSource() || !codons.equals(sourceCacheCodons)) {
			// May be null still after if the map was invalid.
			parseTree = model.getMapper().map(this);
			// Update the number of codons that were used in mapping.
			noActiveCodons = model.getMapper().getNoMappedCodons();
			// Store the codons as they are now.
			sourceCacheCodons = new ArrayList<Integer>(codons);
		}

		return (parseTree == null) ? null : parseTree.toString();
	}

	/**
	 * Returns the number of codons that are actually active. That is, the
	 * number of codons that are actually used in the mapping process to
	 * convert from chromosome to source code.
	 * 
	 * @return the number of active codons this program has.
	 */
	public int getNoActiveCodons() {
		return noActiveCodons;
	}

	/**
	 * Whether the current program is valid. If the chromosome hasn't been
	 * mapped already then this method will trigger a map operation. A
	 * GECandidateProgram is usually valid if it does not exceed any maximum
	 * depth or length restrictions from the model, however this is controlled
	 * by the mapper in use.
	 * 
	 * @return true if this program is valid, false otherwise.
	 */
	@Override
	public boolean isValid() {
		boolean valid = true;

		final int maxChromosomeLength = model.getMaxChromosomeLength();
		final int maxProgramDepth = model.getMaxDepth();

		if (getSourceCode() == null) {
			valid = false;
		} else if ((maxChromosomeLength != -1)
				&& (codons.size() > maxChromosomeLength)) {
			valid = false;
		} else if ((maxProgramDepth != -1)
				&& (parseTree.getDepth() > maxProgramDepth)) {
			valid = false;
		}

		return valid;
	}

	/**
	 * Create a clone of this GECandidateProgram. The list of codons are copied
	 * as are all caches.
	 * 
	 * @return a copy of this GECandidateProgram instance.
	 */
	@Override
	public CandidateProgram clone() {
		final GECandidateProgram clone = (GECandidateProgram) super.clone();

		// Copy codons.
		clone.codons = new ArrayList<Integer>(codons);

		// If codons are the same then the source and fitness should be the
		// same.
		if (parseTree == null) {
			clone.parseTree = null;
		} else {
			clone.parseTree = (NonTerminalSymbol) parseTree.clone();
		}

		clone.fitness = fitness;
		clone.sourceCacheCodons = new ArrayList<Integer>(sourceCacheCodons);
		clone.fitnessCacheCodons = new ArrayList<Integer>(fitnessCacheCodons);

		// Shallow copy the model.
		clone.model = model;

		return clone;
	}

	/**
	 * Returns a string representation of this program. This will be either the
	 * genotype or the phenotype, depending on whether the program has
	 * undergone mapping or not.
	 */
	@Override
	public String toString() {
		if (codons.equals(sourceCacheCodons) && (parseTree != null)) {
			return parseTree.toString();
		} else {
			return codons.toString();
		}
	}

	/**
	 * Compares the given argument for equivalence to this GECandidateProgram.
	 * Two candidate programs are equal if they have equal genotypes, or if
	 * they have equal (but non-null) phenotypes.
	 * 
	 * @return true if the object is an equivalent candidate program, false
	 *         otherwise.
	 */
	@Override
	public boolean equals(final Object o) {
		final GECandidateProgram prog = (GECandidateProgram) o;

		final Symbol thisParseTree = parseTree;
		final Symbol progParseTree = prog.parseTree;

		if ((thisParseTree == null) && (progParseTree == null)) {
			// Compare genotypes.
			return codons.equals(prog.codons);
		} else {
			// Compare phenotypes.
			return ObjectUtils.equals(thisParseTree, progParseTree);
		}
	}
}
