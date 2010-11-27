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
package org.epochx.life;

import java.util.*;

import junit.framework.TestCase;

import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class LifeCycleManagerTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		Life.get().clearListeners();
		Life.get().clearHooks();
	}
	
	/**
	 * Tests that configure event is dispatched correctly.
	 */
	public void testConfigureEvent() {
		final StringBuilder order = new StringBuilder();

		Life.get().addConfigListener(new ConfigListener() {

			@Override
			public void onConfigure() {
				order.append("1");
			}
		});

		Life.get().fireConfigureEvent();

		assertEquals("configure event not dispatched correctly", "1",
				order.toString());
	}

	/**
	 * Tests that run events are dispatched correctly.
	 */
	public void testRunEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addRunListener(new RunListener() {

			@Override
			public void onRunStart() {
				order.append("1");
			}

			@Override
			public void onSuccess() {
				order.append("2");
			}

			@Override
			public void onRunEnd() {
				order.append("3");
			}
		});

		Life.get().fireRunStartEvent();
		Life.get().fireSuccessEvent();
		Life.get().fireRunEndEvent();

		assertEquals("run events not dispatched correctly", "123",
				order.toString());
	}

	/**
	 * Tests that initialisation events are dispatched correctly.
	 */
	public void testInitialisationEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addInitialisationListener(new InitialisationListener() {
			@Override
			public void onInitialisationStart() {
				order.append("1");
			}

			@Override
			public void onInitialisationEnd() {
				order.append("3");
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> initialisationHook(final List<CandidateProgram> pop) {
				order.append("2");
				return pop;
			}
		});

		Life.get().fireInitialisationStartEvent();
		Life.get().runInitialisationHooks(null);
		Life.get().fireInitialisationEndEvent();

		assertEquals("initialisation events not dispatched correctly", "123", order.toString());
	}

	/**
	 * Tests that elitism events are dispatched correctly.
	 */
	public void testElitismEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addElitismListener(new ElitismListener() {

			@Override
			public void onElitismStart() {
				order.append("1");
			}

			@Override
			public void onElitismEnd() {
				order.append("3");
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> elitismHook(final List<CandidateProgram> elites) {
				order.append("2");
				return elites;
			}
		});

		Life.get().fireElitismStartEvent();
		Life.get().runElitismHooks(new ArrayList<CandidateProgram>());
		Life.get().fireElitismEndEvent();

		assertEquals("elitism events not dispatched correctly", "123",
				order.toString());
	}

	/**
	 * Tests that pool selection events are dispatched correctly.
	 */
	public void testPoolSelectionEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addPoolSelectionListener(new PoolSelectionListener() {
			@Override
			public void onPoolSelectionStart() {
				order.append("1");
			}

			@Override
			public void onPoolSelectionEnd() {
				order.append("3");
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> poolSelectionHook(final List<CandidateProgram> pool) {
				order.append("2");
				return pool;
			}
		});

		Life.get().firePoolSelectionStartEvent();
		Life.get().runPoolSelectionHooks(new ArrayList<CandidateProgram>());
		Life.get().firePoolSelectionEndEvent();

		assertEquals("pool selection events not dispatched correctly", "123",
				order.toString());
	}

	/**
	 * Tests that crossover events are dispatched correctly.
	 */
	public void testCrossoverEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addCrossoverListener(new CrossoverListener() {

			@Override
			public void onCrossoverStart() {
				order.append("1");
			}

			@Override
			public void onCrossoverEnd() {
				order.append("3");
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public CandidateProgram[] crossoverHook(
					final CandidateProgram[] parents,
					final CandidateProgram[] children) {
				order.append("2");
				return children;
			}
		});

		Life.get().fireCrossoverStartEvent();
		Life.get().runCrossoverHooks(null, null);
		Life.get().fireCrossoverEndEvent();

		assertEquals("crossover events not dispatched correctly", "123",
				order.toString());
	}

	/**
	 * Tests that mutation events are dispatched correctly.
	 */
	public void testMutationEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addMutationListener(new MutationListener() {

			@Override
			public void onMutationStart() {
				order.append("1");
			}

			@Override
			public void onMutationEnd() {
				order.append("3");
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public CandidateProgram mutationHook(final CandidateProgram parent,
					final CandidateProgram child) {
				order.append("2");
				return child;
			}
		});
		
		Life.get().fireMutationStartEvent();
		Life.get().runMutationHooks(null, null);
		Life.get().fireMutationEndEvent();

		assertEquals("mutation events not dispatched correctly", "123",
				order.toString());
	}

	/**
	 * Tests that reproduction events are dispatched correctly.
	 */
	public void testReproductionEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addReproductionListener(new ReproductionListener() {
			@Override
			public void onReproductionStart() {
				order.append("1");
			}

			@Override
			public void onReproductionEnd() {
				order.append("3");
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public CandidateProgram reproductionHook(
					final CandidateProgram program) {
				order.append("2");
				return program;
			}
		});

		Life.get().fireReproductionStartEvent();
		Life.get().runReproductionHooks(null);
		Life.get().fireReproductionEndEvent();

		assertEquals("reproduction events not dispatched correctly", "123",
				order.toString());
	}

	/**
	 * Tests that generation events are dispatched correctly.
	 */
	public void testGenerationEvents() {
		final StringBuilder order = new StringBuilder();

		Life.get().addGenerationListener(new GenerationListener() {

			@Override
			public void onGenerationStart() {
				order.append("1");
			}

			@Override
			public void onGenerationEnd() {
				order.append("3");
			}
		});
		Life.get().addHook(new AbstractHook() {
			@Override
			public List<CandidateProgram> generationHook(final List<CandidateProgram> pop) {
				order.append("2");
				return pop;
			}
		});
		
		Life.get().fireGenerationStartEvent();
		Life.get().runGenerationHooks(null);
		Life.get().fireGenerationEndEvent();

		assertEquals("generation events not dispatched correctly", "123",
				order.toString());
	}
}
