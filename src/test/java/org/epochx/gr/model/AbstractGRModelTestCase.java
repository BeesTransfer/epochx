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
package org.epochx.gr.model;

import org.epochx.core.*;
import org.epochx.gr.op.crossover.WhighamCrossover;
import org.epochx.gr.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.gr.op.mutation.WhighamMutation;

/**
 * Some of these test check that the default values are correct. It is possible
 * that the default should not be hardcoded here but it means that changing the
 * defaults cannot be taken lightly since it also requires updating the tests.
 * It also has the side-effect that it checks that none of the built in models
 * change the defaults.
 */
public abstract class AbstractGRModelTestCase extends AbstractModelTestCase {

	@Override
	public abstract GRModel getModel();

	/**
	 * Tests that trying to set a null grammar throws an exception.
	 */
	public void testSetGrammarNull() {
		try {
			getModel().setGrammar(null);
			fail("illegal argument exception not thrown for null grammar");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that trying to set a initial max depth of zero throws an exception.
	 */
	public void testSetInitialMaxDepthZero() {
		try {
			getModel().setMaxInitialDepth(0);
			fail("illegal argument exception not thrown for initial max depth of zero");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting an initial max depth of zero is allowed.
	 */
	public void testSetInitialMaxDepthPositive() {
		try {
			getModel().setMaxInitialDepth(1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for initial max depth of 1");
		}
	}

	/**
	 * Tests that trying to set an initial max depth of minus one does not throw
	 * an exception.
	 */
	public void testSetInitialMaxDepthMinusOne() {
		try {
			getModel().setMaxInitialDepth(-1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for initial max depth of -1");
		}
	}

	/**
	 * Tests that trying to set a zero max depth throws an exception.
	 */
	public void testSetMaxDepthZero() {
		try {
			getModel().setMaxDepth(0);
			fail("illegal argument exception not thrown for max depth of zero");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a max depth of zero is allowed.
	 */
	public void testSetMaxDepthPositive() {
		try {
			getModel().setMaxDepth(1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for max depth of 1");
		}
	}

	/**
	 * Tests that trying to set an max depth of minus one does not throw an
	 * exception.
	 */
	public void testSetMaxDepthMinusOne() {
		try {
			getModel().setMaxDepth(-1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for initial max depth of -1");
		}
	}

	/**
	 * Tests that the default max initial depth property is set correctly.
	 */
	public void testMaxInitialDepthDefault() {
		final GRModel model = getModel();

		assertEquals("model's default max initial depth is not 8", 8,
				model.getMaxInitialDepth());
	}

	/**
	 * Tests that the default max depth property is set correctly.
	 */
	public void testMaxDepthDefault() {
		final GRModel model = getModel();

		assertEquals("model's default max depth is not 14", 14,
				model.getMaxDepth());
	}

	/**
	 * Tests that the default initialiser operator is set correctly.
	 */
	public void testInitialiserDefault() {
		final Model model = getModel();

		assertTrue(
				"model's default initialiser is not an instance of ramped half-and-half",
				(model.getInitialiser() instanceof RampedHalfAndHalfInitialiser));
	}

	/**
	 * Tests that the default crossover operator is set correctly.
	 */
	public void testCrossoverDefault() {
		final Model model = getModel();

		assertTrue(
				"model's default crossover is not an instance of whigham crossover",
				(model.getCrossover() instanceof WhighamCrossover));
	}

	/**
	 * Tests that the default mutation operator is set correctly.
	 */
	public void testMutationDefault() {
		final Model model = getModel();

		assertTrue(
				"model's default mutation is not an instance of whigham mutation",
				(model.getMutation() instanceof WhighamMutation));
	}
}
