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
 * A <code>FunctionNode</code> which performs the trigonometric function of
 * sine.
 */
public class SineFunction extends DoubleNode {

	/**
	 * Construct a SineFunction with no children.
	 */
	public SineFunction() {
		this(null);
	}

	/**
	 * Construct a SineFunction with one child. When evaluated, the child
	 * will be evaluated with sine performed on the result.
	 * 
	 * @param child The child which sine will be performed on.
	 */
	public SineFunction(final DoubleNode child) {
		super(child);
	}

	/**
	 * Evaluating a <code>SineFunction</code> involves evaluating the child
	 * then calculating the sine of the result.
	 */
	@Override
	public Double evaluate() {
		final double c = ((Double) getChild(0).evaluate()).doubleValue();

		return Math.sin(c);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the SineFunction which is SIN.
	 */
	@Override
	public String getIdentifier() {
		return "SIN";
	}
}
