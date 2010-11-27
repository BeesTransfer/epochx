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

import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class ElitismManagerTest extends TestCase {

	private Model model;
	private ElitismManager elitismManager;
	private List<CandidateProgram> pop;

	@Override
	protected void setUp() throws Exception {
		model = new ModelDummy();
		elitismManager = new ElitismManager(model);
		pop = new ArrayList<CandidateProgram>();
	}

	/**
	 * Tests that the elitism events are fired in the correct order.
	 */
	public void testElitismEventsOrder() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		// Listen for the events.
		Life.get().addElitismListener(new ElitismListener() {

			@Override
			public void onElitismStart() {
				verify.append('1');
			}

			@Override
			public void onElitismEnd() {
				verify.append('3');
			}
		});
		
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> elitismHook(
					final List<CandidateProgram> elites) {
				verify.append('2');
				return elites;
			}
		});

		elitismManager.elitism(pop);

		assertEquals("elitism events were not called in the correct order",
				"123", verify.toString());
	}

	/**
	 * Tests that an exception is thrown when trying to perform elitism on a
	 * null population.
	 */
	public void testElitismPopNull() {
		try {
			elitismManager.elitism(null);
			fail("exception not thrown for elitism on a null population");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown if the program selector is null when
	 * attempting crossover.
	 */
	public void testNoElitesNegative() {
		model.setNoElites(-1);
		Life.get().fireConfigureEvent();

		try {
			elitismManager.elitism(pop);
			fail("illegal state exception not thrown elitism with a negative number of elites");
		} catch (final IllegalStateException e) {
		}
	}
}
