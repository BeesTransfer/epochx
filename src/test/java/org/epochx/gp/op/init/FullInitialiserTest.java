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
public class FullInitialiserTest extends TestCase {

	private FullInitialiser initialiser;
	
	@Override
	protected void setUp() throws Exception {
		initialiser = new FullInitialiser(null, null, -1, -1, false);
		
		// Ensure setup is valid.
		initialiser.setDepth(0);
		initialiser.setRNG(new MersenneTwisterFast());
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new BooleanLiteral(true));
		syntax.add(new NotFunction());
		initialiser.setSyntax(syntax);
		initialiser.setPopSize(1);
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
	 * Tests that an illegal state exception is thrown if the depth parameter is
	 * greater than 0 and no nodes with arity >0 have been included in the 
	 * syntax.
	 */
	public void testGetPopNoFunctions() {
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new BooleanLiteral(true));
		initialiser.setSyntax(syntax);
		initialiser.setDepth(1);
		
		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for depth of 1 with no function nodes");
		} catch (IllegalStateException e) {}
	}
	
	/**
	 * Tests that an illegal state exception is thrown if there are no terminals
	 * in the syntax.
	 */
	public void testGetPopNoTerminals() {
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new NotFunction());
		initialiser.setSyntax(syntax);
		
		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for syntax with no terminal nodes");
		} catch (IllegalStateException e) {}
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
	 * Tests that an illegal state exception is thrown if depth parameter is 
	 * -1.
	 */
	public void testGetPopNegDepth() {
		// Setup initialiser to be valid except for depth.
		initialiser.setDepth(-1);
		
		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for depth being -1");
		} catch (IllegalStateException e) {}
	}
	
	/**
	 * Tests that an illegal state exception is thrown if no random number 
	 * generator is set.
	 */
	public void testGetPopRNGNull() {
		initialiser.setRNG(null);
		
		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for a null RNG");
		} catch (IllegalStateException e) {}
	}
	
	/**
	 * Tests that an illegal state exception is not thrown with valid 
	 * parameters.
	 */
	public void testGetProgramValid() {
		// Population size not needed to be valid.
		initialiser.setPopSize(-1);
		
		try {
			initialiser.getInitialProgram();
		} catch (IllegalStateException e) {
			fail("illegal state exception thrown for valid parameters");
		}
	}
	
	/**
	 * Tests that an illegal state exception is thrown if the depth parameter is
	 * greater than 0 and no nodes with arity >0 have been included in the 
	 * syntax.
	 */
	public void testGetProgramNoFunctions() {
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new BooleanLiteral(true));
		initialiser.setSyntax(syntax);
		initialiser.setDepth(1);
		
		try {
			initialiser.getInitialProgram();
			fail("illegal state exception not thrown for depth of 1 with no function nodes");
		} catch (IllegalStateException e) {}
	}
	
	/**
	 * Tests that an illegal state exception is thrown if there are no terminals
	 * in the syntax.
	 */
	public void testGetProgramNoTerminals() {
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new NotFunction());
		initialiser.setSyntax(syntax);
		
		try {
			initialiser.getInitialProgram();
			fail("illegal state exception not thrown for syntax with no terminal nodes");
		} catch (IllegalStateException e) {}
	}

	/**
	 * Tests that an illegal state exception is thrown if depth parameter is 
	 * -1.
	 */
	public void testGetProgramNegDepth() {
		// Setup initialiser to be valid except for depth.
		initialiser.setDepth(-1);
		
		try {
			initialiser.getInitialProgram();
			fail("illegal state exception not thrown for depth being -1");
		} catch (IllegalStateException e) {}
	}
	
	/**
	 * Tests that an illegal state exception is thrown if no random number 
	 * generator is set.
	 */
	public void testGetProgramRNGNull() {
		initialiser.setRNG(null);
		
		try {
			initialiser.getInitialProgram();
			fail("illegal state exception not thrown for a null RNG");
		} catch (IllegalStateException e) {}
	}
}
