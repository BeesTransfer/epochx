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
 * function of cosecant. Cosecant x is equal to 1/sin x.
 */
public class CosecantFunction extends DoubleNode {

	/**
	 * Construct a CosecantFunction with no children.
	 */
	public CosecantFunction() {
		this(null);
	}

	/**
	 * Construct a CosecantFunction with one child. When evaluated, the child
	 * will be evaluated with cosecant performed on the result.
	 * 
	 * @param child The child which cosecant will be performed on.
	 */
	public CosecantFunction(final DoubleNode child) {
		super(child);
	}

	/**
	 * Evaluating a <code>CosecantFunction</code> involves calculating sine of
	 * the
	 * child once it's been evaluated, then dividing 1 by the result.
	 */
	@Override
	public Double evaluate() {
		final double c = ((Double) getChild(0).evaluate()).doubleValue();

		return 1 / Math.sin(c);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the CosecantFunction which is COSEC.
	 */
	@Override
	public String getIdentifier() {
		return "COSEC";
	}
}
