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
package org.epochx.epox.dbl;

import org.epochx.epox.DoubleNode;

/**
 * A <code>FunctionNode</code> which performs the mathematical function of
 * subtraction.
 */
public class SubtractFunction extends DoubleNode {

	/**
	 * Construct an SubtractFunction with no children.
	 */
	public SubtractFunction() {
		this(null, null);
	}

	/**
	 * Construct a SubtractFunction with 2 children. When evaluated, both
	 * children will be evaluated, with the second subtracted from the first.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node, to be subtracted from the first.
	 */
	public SubtractFunction(final DoubleNode child1, final DoubleNode child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating a <code>SubtractFunction</code> involves subtracting the
	 * result of the second child from the first.
	 */
	@Override
	public Double evaluate() {
		final double c1 = ((Double) getChild(0).evaluate()).doubleValue();
		final double c2 = ((Double) getChild(1).evaluate()).doubleValue();

		return c1 - c2;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the SubtractFunction which is SUB.
	 */
	@Override
	public String getIdentifier() {
		return "SUB";
	}
}
