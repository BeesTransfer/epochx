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
package org.epochx.gp.op.init;

import java.util.*;

import junit.framework.TestCase;

import org.epochx.epox.*;
import org.epochx.epox.bool.NotFunction;
import org.epochx.tools.random.MersenneTwisterFast;


/**
 * 
 */
public class RampedHalfAndHalfInitialiserTest extends TestCase {
	
	private RampedHalfAndHalfInitialiser initialiser;
	
	@Override
	protected void setUp() throws Exception {
		initialiser = new RampedHalfAndHalfInitialiser(null, null, -1, -1, -1, false);
		
		// Ensure setup is valid.
		initialiser.setStartMaxDepth(0);
		initialiser.setEndMaxDepth(1);
		initialiser.setRNG(new MersenneTwisterFast());
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new BooleanLiteral(true));
		syntax.add(new NotFunction());
		initialiser.setSyntax(syntax);
		initialiser.setPopSize(2);
	}
	
	/**
	 * Tests that an illegal state exception is not thrown with valid 
	 * parameters.
	 */
	public void testGetPopValid() {
		try {
			initialiser.getInitialPopulation();
		} catch (IllegalStateException e) {
			fail("illegal state exception thrown for valid parameters");
		}
	}
	
	/**
	 * Tests that an illegal state exception is thrown if population size
	 * parameter is zero.
	 */
	public void testGetPopZeroPopSize() {
		// Setup initialiser to be valid except for pop size.
		initialiser.setPopSize(0);
		
		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for pop size being 0");
		} catch (IllegalStateException e) {}
	}
	
	/**
	 * Tests that an illegal state exception is thrown if the start maximum 
	 * depth is larger than the end maximum depth.
	 */
	public void testGetPopDepthsSequence() {
		initialiser.setStartMaxDepth(4);
		initialiser.setEndMaxDepth(3);
		
		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown when start depth is greater than end depth");
		} catch (IllegalStateException e) {}
	}
}
