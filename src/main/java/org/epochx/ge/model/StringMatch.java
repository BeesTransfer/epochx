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

import java.io.*;

import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Grammar;

/**
 * This model needs to undergo some work and is unlikely to function correctly
 * at present.
 */
public class StringMatch extends GEModel {

	public StringMatch() {
		try {
			setGrammar(new Grammar(new File("example-grammars/StringMatch.bnf")));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getFitness(final CandidateProgram p) {
		final GECandidateProgram program = (GECandidateProgram) p;

		final String match = "hello_world";
		final String src = program.getSourceCode();

		if (src == null) {
			return Integer.MAX_VALUE;
		}

		final int srcLength = src.length();
		final int matchLength = match.length();
		int score = 0;

		for (int i = 0; i < matchLength; i++) {
			if ((i < srcLength) && (match.charAt(i) != src.charAt(i))) {
				score++;
			}
		}

		if (srcLength != matchLength) {
			score += Math.abs(matchLength - srcLength);
		}

		return score;
	}

	/*
	 * public static void main(String[] args) {
	 * GEModel model = new StringMatch();
	 * 
	 * DepthFirstMapper mapper = new DepthFirstMapper(model);
	 * mapper.setRemovingUnusedCodons(false);
	 * mapper.setWrapping(false);
	 * mapper.setExtending(false);
	 * 
	 * model.setMapper(mapper);
	 * model.setInitialiser(new FixedLengthInitialiser(model, 30));
	 * model.setMaxChromosomeLength(500);
	 * model.setNoGenerations(1000);
	 * model.setNoElites(20);
	 * model.setPopulationSize(500);
	 * model.setNoRuns(100);
	 * }
	 */
}
