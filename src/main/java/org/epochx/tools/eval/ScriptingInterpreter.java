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
 * The ScriptingInterpreter provides a generic interpreter for any language
 * which supports the javax.scripting API (JSR 223). Two constructors are
 * provided, one which receives the name of an installed scripting engine or
 * one of its aliases, for example "ruby", "jruby" and "javascript" would be
 * valid engine names. The second constructor receives a script engine directly
 * which makes it possible to easily plug in support for new languages by
 * implementing a ScriptEngine (or otherwise obtaining an implementation).
 * 
 * <p>
 * It is often the case that performance can improved by handling evaluation and
 * execution in a language specific way, so subclasses of this class may be
 * available for some languages. Where available these language specific classes
 * should be used in preference to this general class.
 * 
 * <p>
 * The javax.scripting API was added to Java in version 1.6 and as such this
 * class and any subclasses require a 1.6 compatible JRE.
 * 
 * @see RubyInterpreter
 * @see GroovyInterpreter
 */
public class ScriptingInterpreter implements Interpreter {

	// The language specific scripting engine.
	private final ScriptEngine engine;

	/**
	 * Constructs a ScriptingInterpreter for a named scripting engine. A list
	 * of installed ScriptEngine names can be obtained with the following code:
	 * 
	 * <blockquote><code>
	 * ScriptEngineManager mgr = new ScriptEngineManager();
	 * List<ScriptEngineFactory> factories = mgr.getEngineFactories();
	 * List<String> engineNames = new ArrayList<String>();
	 * for (ScriptEngineFactory factory: factories) {
	 *     engineNames.addAll(factory.getNames());
	 * }
	 * </code></blockquote>
	 */
	public ScriptingInterpreter(final String engineName) {
		final ScriptEngineManager manager = new ScriptEngineManager();

		engine = manager.getEngineByName(engineName);

		if (engine == null) {
			throw new IllegalArgumentException("no engine matching alias "
					+ engineName);
		}
	}

	/**
	 * Constructs a ScriptingInterpreter for the given ScriptEngine.
	 */
	public ScriptingInterpreter(final ScriptEngine engine) {
		this.engine = engine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object eval(final String expression, final String[] argNames,
			final Object[] argValues) throws MalformedProgramException {
		Object result = null;

		// Evaluate.
		try {
			for (int i = 0; i < argNames.length; i++) {
				engine.put(argNames[i], argValues[i]);
			}
			result = engine.eval(expression);
		} catch (final ScriptException e) {
			throw new MalformedProgramException();
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] eval(final String expression, final String[] argNames,
			final Object[][] argValues) throws MalformedProgramException {
		final Object[] results = new Object[argValues.length];

		// Evaluate each argument set.
		for (int i = 0; i < results.length; i++) {
			results[i] = eval(expression, argNames, argValues[i]);
		}

		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final String program, final String[] argNames,
			final Object[] argValues) throws MalformedProgramException {
		eval(program, argNames, argValues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final String program, final String[] argNames,
			final Object[][] argValues) throws MalformedProgramException {
		// Execute each argument set.
		for (int i = 0; i < argValues.length; i++) {
			exec(program, argNames, argValues[i]);
		}
	}

	/**
	 * Returns the scripting engine performing the evaluation and execution.
	 * 
	 * @return the script engine
	 */
	public ScriptEngine getEngine() {
		return engine;
	}
}
