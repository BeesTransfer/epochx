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
package org.epochx.gp.model;

import java.util.ArrayList;

import org.epochx.core.*;
import org.epochx.epox.Node;
import org.epochx.gp.op.crossover.UniformPointCrossover;
import org.epochx.gp.op.init.FullInitialiser;
import org.epochx.gp.op.mutation.SubtreeMutation;

/**
 * Some of these test check that the default values are correct. It is possible
 * that the default should not be hardcoded here but it means that changing the
 * defaults cannot be taken lightly since it also requires updating the tests.
 * It also has the side-effect that it checks that none of the built in models
 * change the defaults.
 */
public abstract class AbstractGPModelTestCase extends AbstractModelTestCase {

	@Override
	public abstract GPModel getModel();

	/**
	 * Tests that trying to set a negative initial max depth throws an
	 * exception.
	 */
	public void testSetInitialMaxDepthNegative() {
		try {
			getModel().setMaxInitialDepth(-2);
			fail("illegal argument exception not thrown for negative initial max depth");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting an initial max depth of zero is allowed.
	 */
	public void testSetInitialMaxDepthMinusOne() {
		try {
			getModel().setMaxInitialDepth(-1);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for initial max depth of -1");
		}
	}

	/**
	 * Tests that trying to set a negative max depth throws an exception.
	 */
	public void testSetMaxDepthNegative() {
		try {
			getModel().setMaxDepth(-2);
			fail("illegal argument exception not thrown for negative max depth");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that setting a max depth of zero is allowed.
	 */
	public void testSetMaxDepthMinusOne() {
		try {
			getModel().setMaxDepth(0);
		} catch (final IllegalArgumentException e) {
			fail("illegal argument exception thrown for max depth of -1");
		}
	}

	/**
	 * Tests that trying to set a null syntax throws an exception.
	 */
	public void testSetSyntaxNull() {
		try {
			getModel().setSyntax(null);
			fail("illegal argument exception not thrown for syntax of null");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that attempting to execute a run with an empty syntax throws an
	 * exception.
	 */
	public void testRunEmptySyntax() {
		final GPModel model = getModel();
		try {
			// Set low number of generations so won't take too long if failing.
			model.setNoGenerations(1);
			model.setSyntax(new ArrayList<Node>());
			model.run();
			fail("illegal state exception not thrown when running with empty syntax");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that the default max initial depth property is set correctly.
	 */
	public void testMaxInitialDepthDefault() {
		final GPModel model = getModel();

		assertEquals("model's default max initial depth is not 6", 6,
				model.getMaxInitialDepth());
	}

	/**
	 * Tests that the default max depth property is set correctly.
	 */
	public void testMaxDepthDefault() {
		final GPModel model = getModel();

		assertEquals("model's default max depth is not 17", 17,
				model.getMaxDepth());
	}

	/**
	 * Tests that the default initialiser operator is set correctly.
	 */
	public void testInitialiserDefault() {
		final Model model = getModel();

		assertTrue(
				"model's default initialiser is not an instance of full initialiser",
				(model.getInitialiser() instanceof FullInitialiser));
	}

	/**
	 * Tests that the default crossover operator is set correctly.
	 */
	public void testCrossoverDefault() {
		final Model model = getModel();

		assertTrue(
				"model's default crossover is not an instance of uniform point crossover",
				(model.getCrossover() instanceof UniformPointCrossover));
	}

	/**
	 * Tests that the default mutation operator is set correctly.
	 */
	public void testMutationDefault() {
		final Model model = getModel();

		assertTrue(
				"model's default mutation is not an instance of subtree mutation",
				(model.getMutation() instanceof SubtreeMutation));
	}
}
