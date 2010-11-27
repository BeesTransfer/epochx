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
 * 
 * 
 */
public class AbsoluteFunction extends DoubleNode {

	/**
	 * Construct an AbsoluteFunction with no children.
	 */
	public AbsoluteFunction() {
		this(null);
	}

	/**
	 * 
	 */
	public AbsoluteFunction(final DoubleNode child) {
		super(child);
	}

	/**
	 * 
	 */
	@Override
	public Double evaluate() {
		final double value = (Double) getChild(0).evaluate();

		return Math.abs(value);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AbsoluteFunction which is ABS.
	 */
	@Override
	public String getIdentifier() {
		return "ABS";
	}

}
