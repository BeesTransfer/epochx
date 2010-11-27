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
import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class ReproductionManagerTest extends TestCase {

	private Model model;

	private ReproductionManager reproductionManager;

	private int count;

	@Override
	protected void setUp() throws Exception {
		model = new GPModelDummy();

		reproductionManager = new ReproductionManager(model);
	}

	/**
	 * Tests that an exception is thrown if the program selector is null when
	 * attempting reproduction.
	 */
	public void testProgramSelectorNotSet() {
		model.setProgramSelector(null);

		try {
			reproductionManager.reproduce();
			fail("illegal state exception not thrown when performing reproduction with a null program selector");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that the reproduction events are all triggered and in the correct
	 * order.
	 */
	public void testReproductionEventsOrder() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		final List<CandidateProgram> pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(new BooleanLiteral(false),
				(GPModel) model));
		model.getProgramSelector().setSelectionPool(pop);

		// Listen for the crossver.
		Life.get().addReproductionListener(new ReproductionListener() {
			@Override
			public void onReproductionStart() {
				verify.append('1');
			}

			@Override
			public void onReproductionEnd() {
				verify.append('3');
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public CandidateProgram reproductionHook(final CandidateProgram program) {
				verify.append('2');
				return program;
			}
		});
		
		Life.get().fireConfigureEvent();
		reproductionManager.reproduce();

		assertEquals(
				"reproduction events were not called in the correct order",
				"123", verify.toString());
	}

	/**
	 * Tests that returning null to the pool selection event will revert the
	 * selection.
	 */
	public void testReproductionEventRevert() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		final List<CandidateProgram> pop = new ArrayList<CandidateProgram>();
		pop.add(new GPCandidateProgram(new BooleanLiteral(false),
				(GPModel) model));
		model.getProgramSelector().setSelectionPool(pop);

		count = 0;

		// Listen for the generation.
		Life.get().addHook(new AbstractHook() {
					@Override
					public CandidateProgram reproductionHook(final CandidateProgram program) {
						verify.append('2');
						// Revert 3 times before confirming.
						if (count == 3) {
							return program;
						} else {
							count++;
						}
						return null;
					}
				});

		Life.get().fireConfigureEvent();
		reproductionManager.reproduce();

		assertEquals("reproduction operation was not correctly reverted",
				"2222", verify.toString());
	}
}
