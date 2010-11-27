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

import javax.script.*;

/**
 * A GroovyInterpreter provides the facility to evaluate individual Groovy
 * expressions and execute multi-line Groovy statements. Groovy language
 * features up to and including version 1.6 are supported.
 * 
 * <p>
 * <code>GroovyInterpreter</code> extends from the <code>ScriptingInterpreter
 * </code>, adding Groovy specific enhancements, including optimized
 * performance.
 */
public class GroovyInterpreter extends ScriptingInterpreter {

	/**
	 * Constructs a GroovyInterpreter.
	 */
	public GroovyInterpreter() {
		super("groovy");
	}

	/**
	 * Evaluates any valid Groovy expression which may optionally contain the
	 * use of any argument named in the <code>argNames</code> array which will
	 * be pre-declared and assigned to the associated value taken from the
	 * <code>argValues</code> array. The result of evaluating the expression
	 * will be returned from this method. The runtime <code>Object</code> return
	 * type will match the type returned by the expression.
	 * 
	 * <p>
	 * This version of the <code>eval</code> method evaluates the expression
	 * multiple times. The variable names remain the same for each evaluation
	 * but for each evaluation the variable values will come from the next array
	 * in the <code>argValues</code> argument. Groovy variables with the
	 * specified names and values are automatically declared and initialised
	 * before the generated code is run. The argument names link up with the
	 * argument value in the same array index, so both arguments must have the
	 * same length.
	 * 
	 * @param expression a valid Groovy expression that is to be evaluated.
	 * @param argNames {@inheritDoc}
	 * @param argValues {@inheritDoc}
	 * @return the return values from evaluating the expression. The runtime
	 *         type of the returned Objects may vary from program to program. If
	 *         the
	 *         program does not return a value then this method will return an
	 *         array of
	 *         nulls.
	 */
	@Override
	public Object[] eval(final String expression, final String[] argNames,
			final Object[][] argValues) {
		final Object[] results = new Object[argValues.length];

		final String code = getEvalCode(expression, argNames);

		final Invocable invocableEngine = (Invocable) getEngine();
		try {
			getEngine().eval(code);

			// Evaluate each argument set.
			for (int i = 0; i < results.length; i++) {
				results[i] = invocableEngine.invokeFunction("expr",
						argValues[i]);
			}
		} catch (final ScriptException ex) {
			ex.printStackTrace();
		} catch (final NoSuchMethodException ex) {
			ex.printStackTrace();
		}

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final String program, final String[] argNames,
			final Object[][] argValues) {
		final String code = getExecCode(program, argNames);

		final Invocable invocableEngine = (Invocable) getEngine();
		try {
			getEngine().eval(code);

			// Evaluate each argument set.
			for (int i = 0; i < argValues.length; i++) {
				invocableEngine.invokeFunction("expr", argValues[i]);
			}
		} catch (final ScriptException ex) {
			ex.printStackTrace();
		} catch (final NoSuchMethodException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * Helper method to the multiple eval.
	 * 
	 * Constructs a string representing source code of a Groovy method
	 * containing a return statement that returns the result of evaluating
	 * the given expression.
	 */
	private String getEvalCode(final String expression, final String[] argNames) {
		final StringBuilder code = new StringBuilder();

		code.append("public Object expr(");
		for (int i = 0; i < argNames.length; i++) {
			if (i > 0) {
				code.append(',');
			}
			code.append("Object ");
			code.append(argNames[i]);
		}
		code.append(") {");

		code.append("return ");
		code.append(expression);
		code.append(';');

		code.append("}");

		return code.toString();
	}

	/*
	 * Helper method to the multiple exec.
	 * 
	 * Constructs a string representing source code of a Groovy method
	 * containing the given program.
	 */
	private String getExecCode(final String program, final String[] argNames) {
		final StringBuffer code = new StringBuffer();

		code.append("public Object expr(");
		for (int i = 0; i < argNames.length; i++) {
			if (i > 0) {
				code.append(',');
			}
			code.append("Object ");
			code.append(argNames[i]);
		}
		code.append(") {");

		code.append(program);

		code.append("}");

		return code.toString();
	}
}
