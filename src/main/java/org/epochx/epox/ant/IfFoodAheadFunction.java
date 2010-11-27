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
package org.epochx.epox.ant;

import org.epochx.epox.VoidNode;
import org.epochx.tools.ant.Ant;

/**
 * A <code>FunctionNode</code> which represents the conditional if-then-else
 * statement typically used in the artificial ant domain. This version of the
 * if statement has the condition predefined as a check for whether the next
 * move in the landscape contains a food item.
 */
public class IfFoodAheadFunction extends VoidNode {

	// The artificial ant the Actions will be controlling.
	private final Ant ant;

	/**
	 * Construct an IfFoodAheadFunction with no children.
	 * 
	 * @param ant the ant which this function will be controlling.
	 */
	public IfFoodAheadFunction(final Ant ant) {
		this(ant, null, null);
	}

	/**
	 * Construct an IfFoodAheadFunction with two children. When evaluated,
	 * if given ant's next location on the provided landscape is a food
	 * location then the first child node will be evaluated and executed,
	 * otherwise the second child node is evaluated and executed.
	 * 
	 * @param ant the ant which this function will be controlling.
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public IfFoodAheadFunction(final Ant ant, final VoidNode child1,
			final VoidNode child2) {
		super(child1, child2);

		this.ant = ant;
	}

	/**
	 * Evaluating an <code>IfFoodAheadFunction</code> involves identifying the
	 * next location the ant would move to on the landscape were it to be moved.
	 * If this position contains a food item then the first child is evaluated
	 * and executed, else the second child is evaluated and executed.
	 * 
	 * <p>
	 * One of the children will thus have been evaluated (triggering execution
	 * of actions at the <code>TerminalNodes</code>) and then this method which
	 * must return an Action, returns Action.DO_NOTHING which any functions
	 * higher up in the program tree will execute, but with no effect.
	 * </p>
	 */
	@Override
	public Void evaluate() {
		if (ant.isFoodAhead()) {
			getChild(0).evaluate();
		} else {
			getChild(1).evaluate();
		}

		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the IfFoodAheadFunction which is
	 *         IF-FOOD-AHEAD.
	 */
	@Override
	public String getIdentifier() {
		return "IF-FOOD-AHEAD";
	}

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}
}
