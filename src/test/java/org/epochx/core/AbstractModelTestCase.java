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

import junit.framework.TestCase;

import org.epochx.op.ProgramSelector;
import org.epochx.op.selection.TournamentSelector;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Some of these test check that the default values are correct. It is possible
 * that the default should not be hardcoded here but it means that changing the
 * defaults cannot be taken lightly since it also requires updating the tests.
 */
public abstract class AbstractModelTestCase extends TestCase {

	public abstract Model getModel();

	/**
	 * Tests that the default cache fitness property is set correctly.
	 */
	public void testCacheFitnessDefault() {
		final Model model = getModel();

		assertTrue("model not caching fitness by default", model.cacheFitness());
	}

	/**
	 * Tests that the default number of runs property is set correctly.
	 */
	public void testNoRunsDefault() {
		final Model model = getModel();

		assertEquals("model's default number of runs not 1", 1,
				model.getNoRuns());
	}

	/**
	 * Tests that trying to set a negative number of runs throws an exception.
	 */
	public void testSetNoRunsNegative() {
		try {
			getModel().setNoRuns(-1);
			fail("illegal argument exception not thrown for negative number of runs");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a number of runs of zero is allowed.
	 */
	public void testSetNoRunsZero() {
		try {
			getModel().setNoRuns(0);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for zero number of runs");
		}
	}

	/**
	 * Tests that the default number of generations property is set correctly.
	 */
	public void testNoGenerationsDefault() {
		final Model model = getModel();

		assertEquals("model's default number of generations not 50", 50,
				model.getNoGenerations());
	}

	/**
	 * Tests that trying to set a negative number of generations throws an
	 * exception.
	 */
	public void testSetNoGenerationsNegative() {
		try {
			getModel().setNoGenerations(-1);
			fail("illegal argument exception not thrown for negative number of generations");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a number of generations of zero is allowed.
	 */
	public void testSetNoGenerationsZero() {
		try {
			getModel().setNoGenerations(0);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for zero number of generations");
		}
	}

	/**
	 * Tests that the default population size property is set correctly.
	 */
	public void testPopulationSizeDefault() {
		final Model model = getModel();

		assertEquals("model's default population size is not 100", 100,
				model.getPopulationSize());
	}

	/**
	 * Tests that trying to set population size lower than one throws an
	 * exception.
	 */
	public void testSetPopulationSizeZero() {
		try {
			getModel().setPopulationSize(0);
			fail("illegal argument exception not thrown for zero population size");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting population size of one is allowed.
	 */
	public void testSetPopulationSizePositive() {
		try {
			getModel().setPopulationSize(1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for population size of one");
		}
	}

	/**
	 * Tests that the default pool size property is set correctly.
	 */
	public void testPoolSizeDefault() {
		final Model model = getModel();

		assertEquals("model's default pool size is not 50", 50,
				model.getPoolSize());
	}

	/**
	 * Tests that trying to set a pool size below one throws an exception.
	 */
	public void testSetPoolSizeZero() {
		try {
			getModel().setPoolSize(0);
			fail("illegal argument exception not thrown for zero pool size");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a pool size of one is allowed.
	 */
	public void testSetPoolSizePositive() {
		try {
			getModel().setPoolSize(1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for pool size of one");
		}
	}

	/**
	 * Tests that setting a pool size of minus one is allowed.
	 */
	public void testSetPoolSizeMinusOne() {
		try {
			getModel().setPoolSize(-1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for pool size of minus one");
		}
	}

	/**
	 * Tests that the default number of elites property is set correctly.
	 */
	public void testNoElitesDefault() {
		final Model model = getModel();

		assertEquals("model's default population size is not 10", 10,
				model.getNoElites());
	}

	/**
	 * Tests that trying to set number of elites lower than zero throws an
	 * exception.
	 */
	public void testSetNoElitesNegative() {
		try {
			getModel().setNoElites(-1);
			fail("illegal argument exception not thrown for negative no elites");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a number of elites of zero is allowed.
	 */
	public void testSetNoElitesZero() {
		try {
			getModel().setNoElites(0);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for zero number of elites");
		}
	}

	/**
	 * Tests that the default crossover probability property is set correctly.
	 */
	public void testCrossoverProbabilityDefault() {
		final Model model = getModel();

		assertEquals("model's default population size is not 0.9", 0.9,
				model.getCrossoverProbability());
	}

	/**
	 * Tests that trying to set a crossover probability lower than zero throws
	 * an exception.
	 */
	public void testSetCrossoverProbabilityNegative() {
		try {
			getModel().setCrossoverProbability(-0.001);
			fail("illegal argument exception not thrown for negative crossover probability");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a crossover probability of zero is allowed.
	 */
	public void testSetCrossoverProbabilityZero() {
		try {
			getModel().setCrossoverProbability(0.0);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for crossover probability of 0.0");
		}
	}

	/**
	 * Tests that trying to set a crossover probability greater than 1.0 throws
	 * an exception.
	 */
	public void testSetCrossoverProbabilityTooLarge() {
		try {
			getModel().setCrossoverProbability(1.001);
			fail("illegal argument exception not thrown for crossover probability greater than 1.0");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the default mutation probability property is set correctly.
	 */
	public void testMutationProbabilityDefault() {
		final Model model = getModel();

		assertEquals("model's default population size is not 0.1", 0.1,
				model.getMutationProbability());
	}

	/**
	 * Tests that trying to set a mutation probability lower than zero throws an
	 * exception.
	 */
	public void testSetMutationProbabilityNegative() {
		try {
			getModel().setMutationProbability(-0.001);
			fail("illegal argument exception not thrown for negative mutation probability");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a mutation probability of zero is allowed.
	 */
	public void testSetMutationProbabilityZero() {
		try {
			getModel().setMutationProbability(0.0);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for mutation probability of 0.0");
		}
	}

	/**
	 * Tests that trying to set a mutation probability greater than 1.0 throws
	 * an exception.
	 */
	public void testSetMutationProbabilityTooLarge() {
		try {
			getModel().setMutationProbability(1.001);
			fail("illegal argument exception not thrown for mutation probability greater than 1.0");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the default reproduction probability property is set
	 * correctly.
	 */
	public void testReproductionProbabilityDefault() {
		final Model model = getModel();

		assertEquals("model's default population size is not 0.0", 0.0,
				model.getReproductionProbability());
	}

	/**
	 * Tests that trying to set a reproduction probability lower than zero
	 * throws an exception.
	 */
	public void testSetReproductionProbabilityNegative() {
		try {
			getModel().setReproductionProbability(-0.001);
			fail("illegal argument exception not thrown for negative reproduction probability");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a reproduction probability of zero is allowed.
	 */
	public void testSetReproductionProbabilityZero() {
		try {
			getModel().setReproductionProbability(0.0);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for reproduction probability of 0.0");
		}
	}

	/**
	 * Tests that trying to set a reproduction probability greater than 1.0
	 * throws an exception.
	 */
	public void testSetReproductionProbabilityTooLarge() {
		try {
			getModel().setReproductionProbability(1.001);
			fail("illegal argument exception not thrown for reproduction probability greater than 1.0");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the default termination fitness property is set correctly.
	 */
	public void testTerminationFitnessDefault() {
		final Model model = getModel();

		assertEquals("model's default population size is not 0.0", 0.0,
				model.getTerminationFitness());
	}

	/**
	 * Tests that the default program selector operator is set correctly.
	 */
	public void testProgramSelectorDefault() {
		final Model model = getModel();

		final ProgramSelector programSelector = model.getProgramSelector();

		assertTrue(
				"model's default program selector is not an instance of tournament selector",
				(programSelector instanceof TournamentSelector));

		final int tournamentSize = ((TournamentSelector) programSelector)
				.getTournamentSize();
		assertEquals(
				"model's default program selector's tournament size is not 7",
				7, tournamentSize);
	}

	/**
	 * Tests that the default pool selector operator is unset.
	 */
	public void testPoolSelectorDefault() {
		final Model model = getModel();

		assertNull("model's default program selector is not null",
				model.getPoolSelector());
	}

	/**
	 * Tests that the default random number generator is set correctly.
	 */
	public void testRNGDefault() {
		final Model model = getModel();

		assertTrue(
				"model's default program selector is not an instance of tournament selector",
				(model.getRNG() instanceof RandomNumberGenerator));
	}

	/**
	 * Tests that trying to set a null program selector throws an exception.
	 */
	public void testSetProgramSelectorNull() {
		try {
			getModel().setProgramSelector(null);
			fail("illegal argument exception not thrown for program selector of null");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that trying to set a null random number generator throws an
	 * exception.
	 */
	public void testSetRNGNull() {
		try {
			getModel().setRNG(null);
			fail("illegal argument exception not thrown for random number generator of null");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that trying to set a null initialiser throws an exception.
	 */
	public void testSetInitialiserNull() {
		try {
			getModel().setInitialiser(null);
			fail("illegal argument exception not thrown for initialiser of null");
		} catch (final IllegalArgumentException e) {
		}
	}

}
