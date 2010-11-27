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
package org.epochx.core;

import java.util.*;

import junit.framework.TestCase;

import org.epochx.epox.*;
import org.epochx.gp.model.*;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.life.*;
import org.epochx.op.selection.RandomSelector;
import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class PoolSelectionManagerTest extends TestCase {

	private GPModel model;

	private PoolSelectionManager poolManager;

	private List<CandidateProgram> pop;

	private int count;

	@Override
	protected void setUp() throws Exception {
		model = new GPModelDummy();
		pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(model));
		poolManager = new PoolSelectionManager(model);
	}

	/**
	 * Tests that an exception is thrown if the pool size is equal to 0
	 * if a pool selector is set.
	 */
	public void testPoolSizeZero() {
		// Create a model with a null program selector.
		model.setPoolSize(0);
		model.setPoolSelector(new RandomSelector(model));

		Life.get().fireConfigureEvent();
		try {
			poolManager.getPool(pop);
			fail("illegal state exception not thrown for pool selection with a pool size of zero");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that an exception is thrown if the pool size is less than -1 which
	 * is an invalid pool size, if a pool selector is set. A pool
	 * size of -1 is fine since it indicates no pool selection should take
	 * place.
	 */
	public void testPoolSizeNegative() {
		// Create a model with a null program selector.
		model.setPoolSize(-2);
		model.setPoolSelector(new RandomSelector(model));

		Life.get().fireConfigureEvent();
		try {
			poolManager.getPool(pop);
			fail("illegal state exception not thrown for pool selection with a pool size of -2");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that an exception is not thrown if the pool size is -1, and instead
	 * the given population is returned as the pool.
	 */
	public void testPoolSizeMinusOne() {
		// Create a model with a null program selector.
		model.setPoolSize(-1);
		model.setPoolSelector(new RandomSelector(model));

		Life.get().fireConfigureEvent();
		try {
			final List<CandidateProgram> pool = poolManager.getPool(pop);
			assertSame("returned pool is not same as pop when pool size is -1",
					pop, pool);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("exception thrown for pool selection with a pool size of -1");
		}
	}

	/**
	 * Tests that an exception is not thrown if the pool selector is null, and
	 * instead the given population is returned as the pool.
	 */
	public void testPoolSelectorNull() {
		// Create a model with a null program selector.
		model.setPoolSize(5);
		model.setPoolSelector(null);

		Life.get().fireConfigureEvent();
		try {
			final List<CandidateProgram> pool = poolManager.getPool(pop);
			assertSame(
					"returned pool is not same as pop when pool selector is null",
					pop, pool);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("exception thrown for pool selection with a pool selector of null");
		}
	}

	/**
	 * Tests that an exception is not thrown if the pool size is 1.
	 */
	public void testPoolSizeOne() {
		// Create a model with a null program selector.
		model.setPoolSize(1);
		model.setPoolSelector(new RandomSelector(model));

		Life.get().fireConfigureEvent();
		try {
			poolManager.getPool(pop);
		} catch (final Exception e) {
			e.printStackTrace();
			fail("exception thrown for pool selection with a pool size of 1");
		}
	}

	/**
	 * Tests that the pool selection events are all triggered and in the correct
	 * order.
	 */
	public void testPoolSelectionEventsOrder() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		final List<CandidateProgram> pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(new BooleanLiteral(false), model));
		pop.add(new GPCandidateProgram(new BooleanLiteral(false), model));
		model.getProgramSelector().setSelectionPool(pop);

		// Listen for the crossver.
		Life.get().addPoolSelectionListener(new PoolSelectionListener() {
			@Override
			public void onPoolSelectionStart() {
				verify.append('1');
			}

			@Override
			public void onPoolSelectionEnd() {
				verify.append('3');
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> poolSelectionHook(
					final List<CandidateProgram> pool) {
				verify.append('2');
				return pool;
			}
		});
		Life.get().fireConfigureEvent();
		poolManager.getPool(pop);

		assertEquals(
				"pool selection events were not called in the correct order",
				"123", verify.toString());
	}

	/**
	 * Tests that returning null to the pool selection event will revert the
	 * selection.
	 */
	public void testPoolSelectionEventRevert() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		final List<CandidateProgram> pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(new BooleanLiteral(false), model));
		pop.add(new GPCandidateProgram(new BooleanLiteral(false), model));
		model.getProgramSelector().setSelectionPool(pop);

		count = 0;

		// Listen for the generation.
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> poolSelectionHook(
					final List<CandidateProgram> pool) {
				verify.append('2');
				// Revert 3 times before confirming.
				if (count == 3) {
					return pool;
				} else {
					count++;
				}
				return null;
			}
		});

		Life.get().fireConfigureEvent();
		poolManager.getPool(pop);

		assertEquals("pool selection operation was not correctly reverted",
				"2222", verify.toString());
	}

}
