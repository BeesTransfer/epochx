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
package org.epochx.epox.bool;

import org.epochx.epox.BooleanNode;

/**
 * A <code>FunctionNode</code> which performs exclusive disjunction, also known
 * as exclusive OR.
 */
public class XorFunction extends BooleanNode {

	/**
	 * Construct an XorFunction with no children.
	 */
	public XorFunction() {
		this(null, null);
	}

	/**
	 * Construct an XorFunction with two children. When evaluated, if either
	 * child evaluates to true (but not both) then the result will be true,
	 * otherwise the result will be false.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public XorFunction(final BooleanNode child1, final BooleanNode child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating an <code>XorFunction</code> involves evaluating both children.
	 * If either child evaluates to true (but not both) then the result will be
	 * true,
	 * otherwise the result will be false.
	 */
	@Override
	public Boolean evaluate() {
		final boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();
		final boolean c2 = ((Boolean) getChild(1).evaluate()).booleanValue();

		return c1 != c2;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the XorFunction which is XOR.
	 */
	@Override
	public String getIdentifier() {
		return "XOR";
	}
}
