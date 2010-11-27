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
package org.epochx.representation;

import junit.framework.TestCase;

/**
 * 
 */
public abstract class AbstractCandidateProgramTestCase extends TestCase {

	public abstract CandidateProgram getCandidateProgram();

	public void testCloneEqual() {
		final CandidateProgram p1 = getCandidateProgram();
		final CandidateProgram p2 = p1.clone();

		assertEquals("cloned CandidateProgram is not equal", p1, p2);
		assertEquals("cloned CandidateProgram is not equal", p2, p1);
	}

	public void testCompareTo() {
		final DummyCandidateProgram p1 = new DummyCandidateProgram();
		final DummyCandidateProgram p2 = new DummyCandidateProgram();

		p1.setFitness(0.0);
		p2.setFitness(1.0);

		assertTrue(
				"comparing against worse program does not return a positive integer",
				p1.compareTo(p2) > 0);
		assertTrue(
				"comparing against a better program does not return a negative integer",
				p2.compareTo(p1) < 0);

		p1.setFitness(1.0);
		assertTrue("comparing equally fit programs does not return 0",
				p1.compareTo(p2) == 0);
	}
}
