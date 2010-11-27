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

/**
 * A <code>FunctionNode</code> which provides the facility to sequence two
 * instructions - which may be other functions or terminal nodes with actions.
 */
public class Seq2Function extends VoidNode {

	/**
	 * Construct a Seq2Function with no children.
	 */
	public Seq2Function() {
		this(null, null);
	}

	/**
	 * Construct a Seq2Function with two children. When evaluated, the first
	 * child node will be taken first and evaluated, then the second child node
	 * will be taken and evaluated. As such they will have been executed in
	 * sequence.
	 * 
	 * @param child1 The first child node to be executed first in sequence.
	 * @param child2 The second child node to be executed second in sequence.
	 */
	public Seq2Function(final VoidNode child1, final VoidNode child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating a <code>Seq2Function</code> involves evaluating each of the
	 * children in sequence - the first child node, followed by the second
	 * child node.
	 * 
	 * <p>
	 * Each of the children will thus have been evaluated (triggering execution
	 * of actions at the <code>TerminalNodes</code>) and then this method which
	 * must return an Action, returns Action.DO_NOTHING which any functions
	 * higher up in the program tree will execute, but with no effect.
	 * </p>
	 */
	@Override
	public Void evaluate() {
		getChild(0).evaluate();
		getChild(1).evaluate();

		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the Seq2Function which is SEQ2.
	 */
	@Override
	public String getIdentifier() {
		return "SEQ2";
	}
}
