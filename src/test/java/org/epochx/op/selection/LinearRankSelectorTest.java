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
package org.epochx.op.selection;

import java.util.*;

import junit.framework.TestCase;

import org.epochx.gp.model.GPModelDummy;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.life.Life;
import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class LinearRankSelectorTest extends TestCase {

	private LinearRankSelector selector;
	private GPModelDummy model;
	private List<CandidateProgram> pop;

	@Override
	protected void setUp() throws Exception {
		model = new GPModelDummy();
		selector = new LinearRankSelector(model);
		Life.get().fireConfigureEvent();

		pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(null));
	}

	/**
	 * Tests that an exception is thrown when trying to set a negative
	 * probability gradient.
	 */
	public void testSetGradientNegative() {
		try {
			selector.setGradient(-0.001);
			fail("Illegal argument exception not thrown for a negative gradient");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when trying to set a probability
	 * gradient greater than 1.0.
	 */
	public void testSetGradientGreaterThanOne() {
		try {
			selector.setGradient(1.001);
			fail("Illegal argument exception not thrown for a gradient greater than 1");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is not thrown when setting a probability gradient
	 * of 1.0.
	 */
	public void testSetGradientOne() {
		try {
			selector.setGradient(1.0);
			assertEquals("gradient not equal to 1.0 after being set to 1.0",
					1.0, selector.getGradient());
		} catch (final Exception e) {
			fail("Illegal argument exception thrown for a gradient of 1");
		}
	}

	/**
	 * Tests that an exception is not thrown when setting a probability gradient
	 * of 0.0.
	 */
	public void testSetGradientZero() {
		try {
			selector.setGradient(0.0);
			assertEquals("gradient not equal to 0.0 after being set to 0.0",
					0.0, selector.getGradient());
		} catch (final Exception e) {
			fail("Illegal argument exception thrown for a gradient of 0");
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to select a pool of
	 * size 0.
	 */
	public void testGetPoolSizeZero() {
		try {
			selector.getPool(pop, 0);
			fail("Illegal argument exception not thrown for a pool size of 0");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to select a pool from
	 * a null population.
	 */
	public void testGetPoolPopNull() {
		try {
			selector.getPool(null, 1);
			fail("Illegal argument exception not thrown for a null pop");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to select a pool from
	 * an empty population.
	 */
	public void testGetPoolPopEmpty() {
		try {
			pop.clear();
			selector.getPool(pop, 1);
			fail("Illegal argument exception not thrown for an empty pop");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that pool selection returns a pool of the correct size.
	 */
	public void testGetPoolSize() {
		final int poolSize = 10;
		pop.clear();
		for (int i = 0; i < 20; i++) {
			final GPModelDummy dummy = new GPModelDummy();
			dummy.setCacheFitness(false);
			dummy.setFitness(i);
			pop.add(new GPCandidateProgram(null, dummy));
		}

		final List<CandidateProgram> pool = selector.getPool(pop, poolSize);
		assertEquals("pool returned is not equal to pool size parameter",
				poolSize, pool.size());
	}

	/**
	 * Tests that an exception is thrown when attempting to set a null selection
	 * pool.
	 */
	public void testSetSelectionPoolNull() {
		try {
			selector.setSelectionPool(null);
			fail("Illegal argument exception not thrown for a null pool");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown when attempting to set an empty
	 * selection pool.
	 */
	public void testSetSelectionPoolEmpty() {
		try {
			pop.clear();
			selector.setSelectionPool(pop);
			fail("Illegal argument exception not thrown for an empty pool");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that with a gradient of 1.0, the probability of selecting the best
	 * program is equal to the probability of the worst program.
	 */
	public void testSelectionGradientOne() {
		selector.setGradient(1.0);

		assertTrue("probability of best and worst programs being "
				+ "selected is not equal for a gradient of 1.0",
				selector.getBestProbability(10) == selector
						.getWorstProbability(10));
	}

	/**
	 * Tests that with a gradient of 0.0, the probability of selecting the worst
	 * program is 0.0.
	 */
	public void testSelectionGradientZero() {
		selector.setGradient(0.0);

		assertEquals("probability of worst program being selected is not 0.0",
				0.0, selector.getWorstProbability(10));
	}

	/**
	 * Tests that an exception is thrown when attempting to get a program with
	 * no random number generator set.
	 */
	public void testGetProgramRNGNull() {
		model.setRNG(null);
		Life.get().fireConfigureEvent();
		selector.setSelectionPool(pop);
		try {
			selector.getProgram();
			fail("Illegal state exception not thrown for a null random number generator");
		} catch (final IllegalStateException e) {
		}
	}
}
