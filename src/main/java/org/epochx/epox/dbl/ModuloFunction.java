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
 * A <code>FunctionNode</code> which performs the modulo operation, that is
 * it finds the remainder of division.
 */
public class ModuloFunction extends DoubleNode {

	/**
	 * Construct a ModuloFunction with no children.
	 */
	public ModuloFunction() {
		this(null, null);
	}

	/**
	 * Construct a ModuloFunction with two children. When evaluated, the modulo
	 * of the evaluated children will be calculated.
	 * 
	 * @param child1 The first child node - the dividend.
	 * @param child2 The second child node - the divisor.
	 */
	public ModuloFunction(final DoubleNode child1, final DoubleNode child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating a <code>ModuloFunction</code> involves dividing the evaluated
	 * first child, by the second child with the result being the remainder. If
	 * the second child evaluates to <code>0</code>, then the result will be 
	 * whatever the first child evaluated to.
	 */
	@Override
	public Double evaluate() {
		final double c1 = ((Double) getChild(0).evaluate()).doubleValue();
		final double c2 = ((Double) getChild(1).evaluate()).doubleValue();

		if (c2 == 0) {
			return c1;
		} else {
			return c1 % c2;
		}
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the ModuloFunction which is MOD.
	 */
	@Override
	public String getIdentifier() {
		return "MOD";
	}
}
