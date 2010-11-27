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
package org.epochx.tools.eval;

import junit.framework.TestCase;

/**
 * 
 */
public class JavaInterpreterTest extends TestCase {

	/**
	 * Tests that the eval method can handle simple expressions.
	 */
	public void testEval() throws MalformedProgramException {
		final Interpreter interpreter = new JavaInterpreter();

		String expression = "(b1 || false) && (i1 < 4.435)";
		String[] args = {"b1", "i1"};
		Object[] values = {true, 3};

		assertEquals("evaluation of simple expression incorrect", true,
				interpreter.eval(expression, args, values));
		assertEquals("evaluation of simple expression incorrect", false,
				interpreter.eval('!' + expression, args, values));

		expression = "(4.0 + 2.0) / (3.0 - d1)";
		args = new String[]{"d1"};
		values = new Object[]{1.0};

		assertEquals("evaluation of simple expression incorrect", 3.0,
				interpreter.eval(expression, args, values));
	}

	/**
	 * Tests that the eval method can handle multiple simple expressions.
	 */
	public void testMultiEval() throws MalformedProgramException {
		final Interpreter interpreter = new JavaInterpreter();

		String expression = "(b1 || false) && (i1 < 4.435)";
		String[] args = {"b1", "i1"};
		Object[][] values = {{true, 3}, {false, 4}};

		Object[] result = interpreter.eval(expression, args, values);

		assertEquals("evaluation of simple expression incorrect", true,
				result[0]);
		assertEquals("evaluation of simple expression incorrect", false,
				result[1]);

		expression = "(4.0 + 2.0) / (3.0 - d1)";
		args = new String[]{"d1"};
		values = new Object[][]{{1.0}, {2.0}};

		result = interpreter.eval(expression, args, values);

		assertEquals("evaluation of simple expression incorrect", 3.0,
				result[0]);
		assertEquals("evaluation of simple expression incorrect", 6.0,
				result[1]);
	}

}
