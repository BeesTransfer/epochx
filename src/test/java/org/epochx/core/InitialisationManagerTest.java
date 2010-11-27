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
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class InitialisationManagerTest extends TestCase {

	private Model model;
	private InitialisationManager initialisationManager;
	private int count;

	@Override
	protected void setUp() throws Exception {
		model = new GPModelDummy();

		// Need to give the model a valid syntax so we can perform
		// initialisations.
		final List<Node> syntax = new ArrayList<Node>();
		syntax.add(new BooleanLiteral(false));
		((GPModel) model).setSyntax(syntax);
		((GPModel) model).setMaxInitialDepth(0);

		initialisationManager = new InitialisationManager(model);
	}

	/**
	 * Tests that an exception is thrown if the initialiser is null.
	 */
	public void testInitialiserNotSet() {
		model.setInitialiser(null);

		try {
			initialisationManager.initialise();
			fail("illegal state exception not thrown initialisation with a null initialiser");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that the initialisation events are all triggered and in the correct
	 * order.
	 */
	public void testInitialisationEventsOrder() {
		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		// Listen for the config events.
		Life.get().addConfigListener(new ConfigListener() {

			@Override
			public void onConfigure() {
				verify.append('1');
			}
		});
		// Listen for the generation.
		Life.get().addGenerationListener(new GenerationListener() {
			@Override
			public void onGenerationStart() {
				verify.append('2');
			}

			@Override
			public void onGenerationEnd() {
				verify.append('7');
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> generationHook(
					final List<CandidateProgram> genPop) {
				verify.append('4');
				return genPop;
			}
		});
		// Listen for the initialisation.
		Life.get().addInitialisationListener(new InitialisationListener() {
			@Override
			public void onInitialisationStart() {
				verify.append('3');
			}

			@Override
			public void onInitialisationEnd() {
				verify.append('6');
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> initialisationHook(
					final List<CandidateProgram> genPop) {
				verify.append('5');
				return genPop;
			}
		});

		initialisationManager.initialise();

		assertEquals(
				"initialisation events were not called in the correct order",
				"1234567", verify.toString());
	}

	/**
	 * Tests that the initialisation events are all triggered and in the correct
	 * order.
	 */
	public void testInitialisationEventRevert() {
		count = 0;

		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		// Listen for the initialisation.
		Life.get().addHook(new AbstractHook() {
					@Override
					public List<CandidateProgram> initialisationHook(
							final List<CandidateProgram> genPop) {
						verify.append('3');
						// Revert 3 times before confirming.
						if (count == 3) {
							return genPop;
						} else {
							count++;
						}
						return null;
					}
				});

		initialisationManager.initialise();

		assertEquals(
				"initialisation not reverted for null return from onInitialisation event",
				"3333", verify.toString());
	}

	/**
	 * Tests that the initialisation events are all triggered and in the correct
	 * order.
	 */
	public void testGenerationEventRevert() {
		count = 0;

		// We add the chars '1', '2', '3' to builder to check order of calls.
		final StringBuilder verify = new StringBuilder();

		// Listen for the initialisation.
		Life.get().addHook(new AbstractHook() {
					@Override
					public List<CandidateProgram> generationHook(
							final List<CandidateProgram> genPop) {
						verify.append('3');
						// Revert 3 times before confirming.
						if (count == 3) {
							return genPop;
						} else {
							count++;
						}
						return null;
					}
				});

		initialisationManager.initialise();

		assertEquals(
				"initialisation not reverted for null return from onGeneration event",
				"3333", verify.toString());
	}
}
