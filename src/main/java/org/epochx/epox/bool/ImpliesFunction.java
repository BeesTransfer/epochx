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
 * A <code>FunctionNode</code> which performs logical implication.
 */
public class ImpliesFunction extends BooleanNode {

	/**
	 * Construct an ImpliesFunction with no children.
	 */
	public ImpliesFunction() {
		this(null, null);
	}

	/**
	 * Construct an ImpliesFunction with two children. When evaluated, if the
	 * first child evaluates to true and the second child evaluates to false
	 * then the result will be false. All other combinations give a result of
	 * true.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public ImpliesFunction(final BooleanNode child1, final BooleanNode child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating an <code>ImpliesFunction</code> involves combining the
	 * evaluation of the children according to the rules of IMPLIES where if the
	 * first child evaluates to true and the second child evaluates to false
	 * then the result will be false. All other combinations give a result of
	 * true. For performance, this function is  evaluated lazily. The first 
	 * child is evaluated first, if it evaluates to <code>false</code> then the 
	 * overall result will always be <code>true</code>, so the second child will
	 * not be evaluated at all.
	 */
	@Override
	public Boolean evaluate() {
		boolean result = ((Boolean) getChild(0).evaluate()).booleanValue();
		
		if (result) {
			result = !((Boolean) getChild(1).evaluate()).booleanValue();
		}

		return !result;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the ImpliesFunction which is IMPLIES.
	 */
	@Override
	public String getIdentifier() {
		return "IMPLIES";
	}
}
