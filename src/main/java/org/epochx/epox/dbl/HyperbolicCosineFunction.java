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
 * A <code>FunctionNode</code> which performs the hyperbolic trigonometric
 * function of hyperbolic cosine.
 */
public class HyperbolicCosineFunction extends DoubleNode {

	/**
	 * Construct a HyperbolicCosineFunction with no children.
	 */
	public HyperbolicCosineFunction() {
		this(null);
	}

	/**
	 * Construct a HyperbolicCosineFunction with one child. When evaluated, the
	 * child will first be evaluated with hyperbolic cosine performed on the
	 * result.
	 * 
	 * @param child The child which hyperbolic cosine will be performed on.
	 */
	public HyperbolicCosineFunction(final DoubleNode child) {
		super(child);
	}

	/**
	 * Evaluating an <code>HyperbolicCosineFunction</code> involves calculating
	 * the
	 * hyperbolic cosine of the child once it has also been evaluated.
	 */
	@Override
	public Double evaluate() {
		final double c = ((Double) getChild(0).evaluate()).doubleValue();

		return Math.cosh(c);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the HyperbolicCosineFunction which is COSH.
	 */
	@Override
	public String getIdentifier() {
		return "COSH";
	}
}
