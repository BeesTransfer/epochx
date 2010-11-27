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
import org.epochx.op.Initialiser;
import org.epochx.representation.CandidateProgram;

/**
 * 
 */
public class RunManagerTest extends TestCase {

	private Model model;
	private RunManager runManager;

	@Override
	protected void setUp() throws Exception {
		model = new Model() {

			@Override
			public double getFitness(final CandidateProgram program) {
				return 0;
			}
		};
		model.setPopulationSize(1);
		model.setNoGenerations(0);
		model.setCrossoverProbability(0.0);
		model.setMutationProbability(0.0);
		model.setNoElites(0);
		model.setInitialiser(new Initialiser() {

			@Override
			public List<CandidateProgram> getInitialPopulation() {
				final List<CandidateProgram> pop = new ArrayList<CandidateProgram>();
				pop.add(new CandidateProgram() {

					@Override
					public boolean isValid() {
						return true;
					}

					@Override
					public double getFitness() {
						return 0;
					}
				});
				return pop;
			}
		});
		runManager = new RunManager(model);
	}

	/**
	 * Tests that the generation events are all triggered and in the correct
	 * order.
	 */
	public void testRunEventsOrder() {
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
		Life.get().addRunListener(new RunAdapter() {

			@Override
			public void onRunStart() {
				verify.append('2');
			}

			@Override
			public void onRunEnd() {
				verify.append('3');
			}
		});

		runManager.run(1);

		assertEquals("run events were not called in the correct order", "1213",
				verify.toString());
	}

}
