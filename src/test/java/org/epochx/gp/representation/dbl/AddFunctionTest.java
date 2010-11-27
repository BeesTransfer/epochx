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
package org.epochx.gp.representation.dbl;

import org.epochx.epox.*;
import org.epochx.epox.dbl.AddFunction;
import org.epochx.gp.representation.AbstractDoubleNodeTestCase;

/**
 * 
 */
public class AddFunctionTest extends AbstractDoubleNodeTestCase {

	@Override
	public Node getNode() {
		return new AddFunction();
	}

	public void testEvaluatePositive() {
		final AddFunction node = (AddFunction) getNode();
		final Node[] children = new Node[]{new DoubleLiteral(2.0),
				new DoubleLiteral(3.0)};
		node.setChildren(children);
		final double result = node.evaluate();

		assertEquals("ADD of 2.0 and 3.0 is not 5.0", 5.0, result);
	}

	public void testEvaluateNegative() {
		final AddFunction node = (AddFunction) getNode();
		final Node[] children = new Node[]{new DoubleLiteral(-2.1),
				new DoubleLiteral(-3.9)};
		node.setChildren(children);
		final double result = node.evaluate();

		assertEquals("ADD of -2.1 and -3.9 is not -6.0", -6.0, result);
	}

	public void testEvaluatePositiveNegative() {
		final AddFunction node = (AddFunction) getNode();
		final Node[] children = new Node[]{new DoubleLiteral(-3.5),
				new DoubleLiteral(4.5)};
		node.setChildren(children);
		final double result = node.evaluate();

		assertEquals("ADD of -3.5 and 4.5 is not 1.0", 1.0, result);
	}
}
