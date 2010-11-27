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
package org.epochx.gp.representation.bool;

import org.epochx.epox.*;
import org.epochx.epox.bool.NotFunction;
import org.epochx.gp.representation.AbstractBooleanNodeTestCase;

/**
 * 
 */
public class NotFunctionTest extends AbstractBooleanNodeTestCase {

	@Override
	public Node getNode() {
		return new NotFunction();
	}

	public void testEvaluateT() {
		final NotFunction node = (NotFunction) getNode();
		node.setChild(0, new BooleanLiteral(true));
		final boolean result = node.evaluate();

		assertFalse("NOT of true is not false", result);
	}

	public void testEvaluateF() {
		final NotFunction node = (NotFunction) getNode();
		node.setChild(0, new BooleanLiteral(false));
		final boolean result = node.evaluate();

		assertTrue("NOT of false is not true", result);
	}

}
