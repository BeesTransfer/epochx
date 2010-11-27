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

import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.tools.grammar.NonTerminalSymbol;

/**
 * A mapper's job is to convert a GECandidateProgram into a String sequence.
 * Conventionally in GE the mapping process uses the chromosome of the
 * GECandidateProgram and a guide production choices in a grammar.
 * 
 * <p>
 * The method of mapping is the responsibility of the Mapper and as such options
 * such as wrapping of codons or not are down to the Mapper.
 */
public interface Mapper {

	/**
	 * Map the given GECandidateProgram to a parse tree.
	 * 
	 * @param program the program to be converted to a parse tree.
	 * @return a Symbol which is the root node of a valid parse tree, or null
	 *         if no valid parse tree could be created from the
	 *         GECandidateProgram.
	 */
	public NonTerminalSymbol map(GECandidateProgram program);

	/**
	 * Should return the number of codons that were used in mapping the last
	 * GECandidateProgram to be mapped, or -1 if no CandidatePrograms have yet
	 * been mapped. The number of mapped codons constitutes the active portion
	 * of a chromosome. Typically this will refer to the first n codons, but
	 * that is dependent upon the mapper implementation in use.
	 * 
	 * @return the number of codons used during the last mapping process.
	 */
	public int getNoMappedCodons();

}
