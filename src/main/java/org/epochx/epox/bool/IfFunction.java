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
 * A <code>FunctionNode</code> which represents the conditional if-then-else
 * statement.
 */
public class IfFunction extends BooleanNode {

	/**
	 * Construct an IfFunction with no children.
	 */
	public IfFunction() {
		this(null, null, null);
	}

	/**
	 * Construct an IfFunction with three children. When evaluated, if the first
	 * child evaluates to true then the second child is evaluated and return,
	 * otherwise the third child is evaluated and returned.
	 * 
	 * @param condition The first child node.
	 * @param ifStatement The second child node.
	 * @param elseStatement The third child node.
	 */
	public IfFunction(final BooleanNode condition,
			final BooleanNode ifStatement, final BooleanNode elseStatement) {
		super(condition, ifStatement, elseStatement);
	}

	/**
	 * Evaluating an <code>IfFunction</code> involves evaluating the first
	 * child,
	 * if it evaluates to true then the second child is evaluated as the result.
	 * Otherwise the third child is evaluated and returned.
	 */
	@Override
	public Boolean evaluate() {
		final boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();

		if (c1) {
			return (Boolean) getChild(1).evaluate();
		} else {
			return (Boolean) getChild(2).evaluate();
		}
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the IfFunction which is IF.
	 */
	@Override
	public String getIdentifier() {
		return "IF";
	}
}
