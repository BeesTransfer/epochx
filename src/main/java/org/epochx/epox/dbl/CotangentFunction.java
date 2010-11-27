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
 * A <code>FunctionNode</code> which performs the reciprocal trigonometric
 * function of cotangent. Cotangent x is equal to 1/tan x.
 */
public class CotangentFunction extends DoubleNode {

	/**
	 * Construct a CotangentFunction with no children.
	 */
	public CotangentFunction() {
		this(null);
	}

	/**
	 * Construct a CotangentFunction with one child. When evaluated, the child
	 * will be evaluated with cotangent performed on the result.
	 * 
	 * @param child The child which cotangent will be performed on.
	 */
	public CotangentFunction(final DoubleNode child) {
		super(child);
	}

	/**
	 * Evaluating a <code>CotangentFunction</code> involves calculating tan of
	 * the
	 * child once it's been evaluated, then dividing 1 by the result.
	 */
	@Override
	public Double evaluate() {
		final double c = ((Double) getChild(0).evaluate()).doubleValue();

		return 1 / Math.tan(c);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the CotangentFunction which is COT.
	 */
	@Override
	public String getIdentifier() {
		return "COT";
	}
}
