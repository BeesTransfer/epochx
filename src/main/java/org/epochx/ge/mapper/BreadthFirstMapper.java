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
package org.epochx.ge.mapper;

import java.util.*;

import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.tools.grammar.*;

/**
 * <strong>WARNING: This class is not fully implemented. It should NOT currently 
 * be used.</strong>
 * 
 * A breadth first mapper is a twist on the classic depth-first mapper
 * typically used by GE. It maps a GECandidateProgram's chromosome to a
 * String by using each codon in order to choose between productions
 * when constructing a derivation tree in breadth-first order.
 * 
 * <p>
 * It provides facility for wrapping and extending of chromosomes.
 */
public class BreadthFirstMapper implements Mapper {

	// The controlling model.
	private final GEModel model;

	private List<GrammarNode> symbols;

	private GECandidateProgram program;

	// Wrapping and extending are mutually exclusive, they cannot both be true.
	private boolean wrapping;
	private boolean extending;

	private boolean removingUnusedCodons;

	private int maxWraps;

	private final int noMappedCodons;

	/**
	 * Constructs an instance of BreadthFirstMapper.
	 * 
	 * @param model the controlling model providing configuration details such
	 *        as the Grammar.
	 */
	public BreadthFirstMapper(final GEModel model) {
		this.model = model;

		// Default to extending.
		wrapping = false;
		extending = true;

		// Default to removing unused codons.
		removingUnusedCodons = true;

		noMappedCodons = -1;
	}

	/**
	 * Perform the mapping operation.
	 * 
	 * @param program the GECandidateProgram to be mapped to its source.
	 * @return a source string equivalent to the specified GECandidateProgram's
	 *         chromosome after mapping using the model's grammar. Null is
	 *         returned if
	 *         a valid string could not be generated.
	 */
	// @Override
	public String mapToString(final GECandidateProgram program) {
		final Grammar grammar = model.getGrammar();
		symbols = new ArrayList<GrammarNode>();
		final List<Integer> codons = program.getCodons();
		this.program = program;

		symbols.add(grammar.getStartRule());

		int i = 0;
		while (containsNonTerminals(symbols)) {
			if ((i > model.getMaxDepth())
					|| (program.getNoCodons() > model.getMaxChromosomeLength())) {
				return null;
			} else {
				if (!map(codons)) {
					return null;
				}
				i++;
			}
		}

		final StringBuilder buffer = new StringBuilder();
		for (final GrammarNode s: symbols) {
			final GrammarLiteral t = (GrammarLiteral) s;
			buffer.append(t.getValue());
		}

		// Before returning, remove all the unused codons.
		if (removingUnusedCodons) {
			for (int c = 0; c < codons.size(); c++) {
				// Remove one codon from the end, for each one left.
				program.removeCodon(program.getNoCodons() - 1);
			}
		}

		return buffer.toString();
	}

	/*
	 * Helper method which checks whether a list of symbols contains any non
	 * terminal symbols.
	 */
	private boolean containsNonTerminals(final List<GrammarNode> symbolList) {
		for (final GrammarNode s: symbolList) {
			if (s instanceof GrammarRule) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Non-recursive helper method that performs one pass of mapping.
	 */
	private boolean map(final List<Integer> codons) {
		final List<GrammarNode> newSymbols = new ArrayList<GrammarNode>();
		for (final GrammarNode s: symbols) {
			if (s instanceof GrammarRule) {
				// Pick a production to replace it with.
				final GrammarRule nt = (GrammarRule) s;
				final int noProductions = nt.getNoProductions();
				GrammarProduction p;
				if (noProductions == 1) {
					p = nt.getProduction(0);
				} else {
					// If there are no more codons we simply add one.
					if (codons.size() == 0) {
						if (extending) {
							if (program.getNoCodons() < model
									.getMaxChromosomeLength()) {
								final int newCodon = model.getCodonGenerator()
										.getCodon();
								program.appendCodon(newCodon);
								codons.add(newCodon);
							} else {
								return false;
							}
						} else if (wrapping) {
							// Refresh set of codons to start again.
							codons.addAll(program.getCodons());
						} else {
							// We're not wrapping or extending so the chromosome
							// is invalid.
							return false;
						}
					}

					// Pick a production using the next codon.
					final int codon = codons.remove(0);
					p = nt.getProduction(codon % noProductions);
				}
				for (final GrammarNode nextS: p.getGrammarNodes()) {
					newSymbols.add(nextS);
				}
			} else {
				newSymbols.add(s);
			}
		}
		symbols = newSymbols;
		return true;
	}

	/**
	 * Returns whether the wrapping operation is in use. If wrapping is being
	 * used then if there are insufficient codons during mapping then they will
	 * be wrapped, starting back at the first codon.
	 * 
	 * @return true if codons will undergo wrapping, false otherwise.
	 */
	public boolean isWrapping() {
		return wrapping;
	}

	/**
	 * Specifies whether the wrapping operation should be used. If wrapping is
	 * turned on then when insufficient codons are available during mapping,
	 * the chromosome will be wrapped, starting again with the first codon.
	 * 
	 * <p>
	 * Wrapping and extending are mutually exclusive. If one is set to be used
	 * then the other will automatically be turned off.
	 * 
	 * @param wrapping whether wrapping should be used during mapping.
	 */
	public void setWrapping(final boolean wrapping) {
		this.wrapping = wrapping;

		if (wrapping && extending) {
			extending = false;
		}
	}

	/**
	 * Returns whether the extension operation is in use. If extending is being
	 * used then if there are insufficient codons during mapping then new
	 * codons will be generated using the models CodonGenerator and appended to
	 * the end of the program's chromosome. Extension will only occur up to the
	 * maximum chromosome length parameter specified in the model, after which
	 * programs will be considered illegal and the mapper will return null.
	 * 
	 * @return true if codons will undergo extension, false otherwise.
	 */
	public boolean isExtending() {
		return extending;
	}

	/**
	 * Specifies whether the extension operation should be used. If extension
	 * is turned on then when insufficient codons are available during mapping,
	 * the chromosome will be extended with a newly generated codon using the
	 * model's CodonGenerator. If the chromosome has already met the model's
	 * specified maximum length then the mapping operation will be abandoned
	 * and null will be returned to signal an invalid individual.
	 * 
	 * @param extending whether extension should be used during mapping.
	 */
	public void setExtending(final boolean extending) {
		this.extending = extending;

		if (extending && wrapping) {
			wrapping = false;
		}
	}

	/**
	 * Returns whether unused codons at the end of a chromosome are removed
	 * after mapping is complete.
	 * 
	 * @return true if unused codons are currently being removed, false
	 *         otherwise.
	 */
	public boolean isRemovingUnusedCodons() {
		return removingUnusedCodons;
	}

	/**
	 * Specifies whether unused codons at the end of a chromosome should be
	 * removed or not after the mapping operation is completed.
	 * 
	 * @param remove whether unused codons should be removed or not.
	 */
	public void setRemovingUnusedCodons(final boolean remove) {
		removingUnusedCodons = remove;
	}

	/**
	 * Returns the number of codons that were used in mapping the last
	 * GECandidateProgram to be mapped, or -1 if no CandidatePrograms have yet
	 * been mapped. The number of mapped codons constitutes the active portion
	 * of a chromosome and refers to the first n codons of the program.
	 * 
	 * @return the number of codons used during the last mapping process.
	 */
	@Override
	public int getNoMappedCodons() {
		return noMappedCodons;
	}

	/**
	 * This mapper is currently not fully implemented. It should not be used.
	 * This method will just return null.
	 */
	@Override
	public NonTerminalSymbol map(final GECandidateProgram program) {
		return null;
	}
}
