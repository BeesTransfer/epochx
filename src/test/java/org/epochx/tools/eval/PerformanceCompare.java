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

import java.awt.Point;
import java.util.Arrays;

/**
 * 
 */
public class PerformanceCompare {

	/**
	 * @param args
	 * @throws MalformedProgramException
	 */
	public static void main(final String[] args)
			throws MalformedProgramException {
		final Interpreter[] interpreters = new Interpreter[]{
				// OldGroovyInterpreter.getInstance(),
				new GroovyInterpreter(), new ScriptingInterpreter("groovy")};

		final long[] timeCounts = new long[interpreters.length];

		for (int i = 0; i < 1000 * interpreters.length; i++) {
			final int selected = i % interpreters.length;
			final long time = test(interpreters[selected]);
			if (i >= interpreters.length) {
				timeCounts[selected] += time;
			}
		}

		System.out.println(Arrays.toString(timeCounts));
	}

	private static long test(final Interpreter interpreter)
			throws MalformedProgramException {
		final long start = System.currentTimeMillis();

		for (int i = 0; i < 1; i++) {
			String expression = "(b1 || false) && (i1 < 4.435)";
			String[] args = {"b1", "i1"};
			Object[] values = {true, 3};

			Object result = interpreter.eval(expression, args, values);

			expression = "(b1 || false) && (i1 < 4.435)";
			args = new String[]{"b1", "i1"};
			Object[][] valueSets = new Object[][]{{true, 3}, {false, 4}};

			final Object[] results = interpreter.eval(expression, args,
					valueSets);

			expression = "(4.0 + 2.0) / (3.0 - d1)";
			args = new String[]{"d1"};
			valueSets = new Object[][]{{1.0}, {2.0}};

			result = interpreter.eval(expression, args, valueSets);

			final Point p = new Point(2, 3);

			String program = "int x = 25; p.setLocation(x, p.getY() + 1);";
			args = new String[]{"p"};
			values = new Object[]{p};

			interpreter.exec(program, args, values);

			// System.out.println(p.getX() + ":" + p.getY());

			final Point p1 = new Point(0, 0);
			final Point p2 = new Point(-10, -10);

			program = "int x = p.getX() + 1; int y = p.getY() + 2; p.setLocation(x, y);";
			args = new String[]{"p"};
			valueSets = new Object[][]{{p1}, {p1}, {p2}};

			// Execute the program with p1 twice then p2 once.
			interpreter.exec(program, args, valueSets);

			// System.out.println(p1.getX() + ":" + p1.getY());
			// System.out.println(p2.getX() + ":" + p2.getY());
		}

		return System.currentTimeMillis() - start;
	}
}
