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
 * A <code>FunctionNode</code> which performs the arithmetic function of
 * squaring,
 * that is - raising to the second power. It is equivalent to the
 * <code>PowerFunction</code> where the second child is the double literal 2.0.
 */
public class SquareFunction extends DoubleNode {

	/**
	 * Construct a SquareFunction with no children.
	 */
	public SquareFunction() {
		this(null);
	}

	/**
	 * Construct a SquareFunction with one child. When evaluated, the child will
	 * be evaluated with the result then raised to the power of 2.
	 * 
	 * @param child The child which will be squared.
	 */
	public SquareFunction(final DoubleNode child) {
		super(child);
	}

	/**
	 * Evaluating a <code>SquareFunction</code> involves evaluating the child
	 * then raising the result to the power of 2.
	 */
	@Override
	public Double evaluate() {
		final double c = ((Double) getChild(0).evaluate()).doubleValue();

		return Math.pow(c, 2);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the SquareFunction which is SQUARE.
	 */
	@Override
	public String getIdentifier() {
		return "SQUARE";
	}
}
