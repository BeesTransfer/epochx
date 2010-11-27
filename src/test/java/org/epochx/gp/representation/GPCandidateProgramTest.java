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
package org.epochx.gp.representation;

import java.util.List;

import org.epochx.epox.*;
import org.epochx.epox.bool.AndFunction;
import org.epochx.representation.*;

/**
 * 
 */
public class GPCandidateProgramTest extends AbstractCandidateProgramTestCase {

	private GPCandidateProgram program;
	private Node node0;
	private Node node1;
	private Node node2;

	@Override
	public CandidateProgram getCandidateProgram() {
		return new GPCandidateProgram(new BooleanLiteral(true), null);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		program = (GPCandidateProgram) getCandidateProgram();
		node1 = new BooleanLiteral(true);
		node2 = new BooleanLiteral(true);
		node0 = new AndFunction((BooleanNode) node1, (BooleanNode) node2);
		program.setRootNode(node0);
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to get
	 * a node at a negative index.
	 */
	public void testGetNthNodeNegativeIndex() {
		try {
			program.getNthNode(-1);
			fail("Exception not thrown for negative index");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to get
	 * a node an index beyond the tree's length - 1.
	 */
	public void testGetNthTooLarge() {
		try {
			program.getNthNode(program.getProgramLength());
			fail("Exception not thrown for index >= length");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that the nth node is correctly returned when n is positive.
	 */
	public void testGetNthNodePositiveIndex() {
		try {
			final Node node = program.getNthNode(2);
			assertSame("2nd node not being returned at index 2", node2, node);
		} catch (final IndexOutOfBoundsException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test that the root node is returned when retrieving nth node of 0.
	 */
	public void testGetNthNodeRoot() {
		final Node node = program.getNthNode(0);
		assertSame("root node not being returned at index 0", node0, node);
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to set
	 * a node at a negative index.
	 */
	public void testSetNthNodeNegativeIndex() {
		try {
			program.setNthNode(-1, new BooleanLiteral(false));
			fail("Exception not thrown for negative index");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to set
	 * a node an index beyond the tree's length - 1.
	 */
	public void testSetNthNodeTooLarge() {
		try {
			program.setNthNode(3, new BooleanLiteral(false));
			fail("Exception not thrown for index >= length");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that the nth node is set correctly when it is positive.
	 */
	public void testSetNthNodePositiveIndex() {
		try {
			final Node node = new BooleanLiteral(false);
			program.setNthNode(2, node);
			assertSame("2nd node not being set at index 2", node,
					program.getNthNode(2));
		} catch (final IndexOutOfBoundsException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test that the nth node is set correctly when n is 0.
	 */
	public void testSetNthNodeRoot() {
		final Node node = new BooleanLiteral(false);
		program.setNthNode(0, node);
		assertSame("root node not being returned at index 0", node,
				program.getRootNode());
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to get
	 * nodes at a negative depth.
	 */
	public void testGetNodesAtDepthNegative() {
		try {
			program.getNodesAtDepth(-1);
			fail("Exception not thrown for negative depth");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that just the root node is returned when requesting nodes at depth
	 * 0.
	 */
	public void testGetNodesAtDepthZero() {
		final List<Node> nodes = program.getNodesAtDepth(0);
		assertEquals("more than one node at depth zero", 1, nodes.size());
		assertSame("root node not returned for depth zero",
				program.getRootNode(), nodes.get(0));
	}

	/**
	 * Test that only the two child nodes are returned from depth one.
	 */
	public void testGetNodesAtDepthPositive() {
		final List<Node> nodes = program.getNodesAtDepth(1);
		assertEquals("more than two nodes found at depth one", 2, nodes.size());
		assertTrue("nodes at depth one not returned", nodes.contains(node1)
				&& nodes.contains(node2));
	}

	/**
	 * Test that an empty list is returned if retrieving nodes at a depth
	 * greater than the programs maximum depth.
	 */
	public void testGetNodesAtDepthTooLarge() {
		try {
			final List<Node> nodes = program.getNodesAtDepth(2);
			assertEquals(
					"empty list not returned for nodes greater than maximum depth in program",
					0, nodes.size());
		} catch (final IndexOutOfBoundsException e) {
			fail("Exception thrown for depth greater than the maximum depth");
		}
	}
}
