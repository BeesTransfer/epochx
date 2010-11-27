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
 * A <code>FunctionNode</code> which performs logical conjunction or the
 * boolean function of AND.
 */
public class AndFunction extends BooleanNode {

	/**
	 * Construct an AndFunction with no children.
	 */
	public AndFunction() {
		this(null, null);
	}

	/**
	 * Construct an AndFunction with two children. When evaluated, if both
	 * children evaluate to true then the result will be true. All other
	 * combinations will return a result of false.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public AndFunction(final BooleanNode child1, final BooleanNode child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating an <code>AndFunction</code> involves combining the evaluation
	 * of the children according to the rules of AND where if both children
	 * evaluate to true then the result will be true. All other combinations
	 * will return a result of false. For performance, this function is 
	 * evaluated lazily. The first child is evaluated first, if it evaluates to 
	 * <code>false</code> then the overall result will always be 
	 * <code>false</code>, so the second child will not be evaluated at all.
	 */
	@Override
	public Boolean evaluate() {
		boolean result = ((Boolean) getChild(0).evaluate()).booleanValue();
		
		if (result) {
			result = ((Boolean) getChild(1).evaluate()).booleanValue();
		}

		return result;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "AND";
	}
}
