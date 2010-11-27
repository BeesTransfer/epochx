package org.epochx.tools.eval;

import java.awt.Point;

import junit.framework.TestCase;

public class GroovyInterpreterTest extends TestCase {

	/**
	 * Tests that the eval method can handle simple expressions.
	 */
	public void testEval() throws MalformedProgramException {
		final Interpreter interpreter = new GroovyInterpreter();

		String expression = "(b1 || false) && (i1 < 4.435)";
		String[] args = {"b1", "i1"};
		Object[] values = {true, 3};

		final Object result = interpreter.eval(expression, args, values);

		assertEquals("evaluation of simple expression incorrect", true, result);
		assertTrue("evaluation of simple expression incorrect",
				result instanceof Boolean);
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
		final Interpreter interpreter = new GroovyInterpreter();

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

	/**
	 * Tests that the exec method can execute sequences of statements.
	 */
	public void testExec() throws MalformedProgramException {
		final Interpreter interpreter = new GroovyInterpreter();

		final Point p = new Point(2, 3);

		final String program = "int x = 25; p.setLocation(x, p.getY() + 1);";
		final String[] args = {"p"};
		final Object[] values = {p};

		interpreter.exec(program, args, values);

		assertEquals("execution of statements had unexpected side-effects",
				new Point(25, 4), p.getLocation());
	}

	/**
	 * Tests that the exec method can execute sequences of statements multiple
	 * times.
	 */
	public void testMultiExec() throws MalformedProgramException {
		final Interpreter interpreter = new GroovyInterpreter();

		final Point p1 = new Point(0, 0);
		final Point p2 = new Point(-10, -10);

		final String program = "int x = p.getX() + 1; int y = p.getY() + 2; p.setLocation(x, y);";
		final String[] args = {"p"};
		final Object[][] values = {{p1}, {p1}, {p2}};

		// Execute the program with p1 twice then p2 once.
		interpreter.exec(program, args, values);

		assertEquals("execution of statements had unexpected side-effects",
				new Point(2, 4), p1.getLocation());
		assertEquals("execution of statements had unexpected side-effects",
				new Point(-9, -8), p2.getLocation());
	}
}
