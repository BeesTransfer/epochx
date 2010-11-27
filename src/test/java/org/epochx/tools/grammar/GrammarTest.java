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
package org.epochx.tools.grammar;

import junit.framework.TestCase;

import org.apache.commons.lang.ArrayUtils;

public class GrammarTest extends TestCase {

	/**
	 * Tests that a malformed grammar exception is thrown for a grammar with no
	 * rules.
	 */
	public void testEmptyGrammar() {
		try {
			new Grammar("");
			fail("Malformed grammar exception not thrown for a grammar with no rules");
		} catch (final MalformedGrammarException e) {
		}
	}

	/**
	 * Tests that a malformed grammar exception is thrown for a grammar with a
	 * rule that is referred to but is missing.
	 */
	public void testMissingRule() {
		try {
			new Grammar("<rule> ::= abc | <missing>");
			fail("Malformed grammar exception not thrown for a grammar a missing rule");
		} catch (final MalformedGrammarException e) {
		}
	}

	/**
	 * Tests that a malformed grammar exception is thrown for a grammar
	 * including infinitely recursive rules.
	 */
	public void testInfiniteRecursion() {
		final String grammarStr = "<rule1> ::= <rule1> | <rule2> <rule3>\n"
				+ "<rule2> ::= abc | dcd\n"
				+ "<rule3> ::= <rule2> <rule1> | dvd <rule3>\n";

		try {
			new Grammar(grammarStr);
			fail("Malformed grammar exception not thrown for an infinitely recursive grammar");
		} catch (final MalformedGrammarException e) {
		}
	}

	/**
	 * Tests that a malformed grammar exception is not thrown for a grammar
	 * which is heavily recursive but not infinitely so.
	 */
	public void testNonInfiniteRecursion() {
		final String grammarStr = "<rule1> ::= <rule1> | <rule2> <rule3>\n"
				+ "<rule2> ::= abc | dcd\n"
				+ "<rule3> ::= <rule2> <rule1> | dvd\n";

		try {
			new Grammar(grammarStr);
		} catch (final MalformedGrammarException e) {
			fail("Malformed grammar exception thrown for recursive grammar");
		}
	}

	/**
	 * Tests that rules get set recursive correctly.
	 */
	public void testSetRecursive() {
		final String grammarStr = "<rule1> ::= <rule2> | <rule3>\n"
				+ "<rule2> ::= <rule1> <rule3>\n" + "<rule3> ::= abc | def\n";

		try {
			final Grammar g = new Grammar(grammarStr);
			if (!g.getGrammarRule("rule1").isRecursive()) {
				fail("Recursive flag not set for recursive rule");
			}
			if (!g.getGrammarRule("rule2").isRecursive()) {
				fail("Recursive flag not set for recursive rule");
			}
			if (g.getGrammarRule("rule3").isRecursive()) {
				fail("Recursive flag set for non-recursive rule");
			}
		} catch (final MalformedGrammarException ex) {
			fail("Malformed grammar exception thrown for a valid grammar");
		}
	}

	/**
	 * Tests that attributes get set correctly on productions.
	 */
	public void testAttributesSet() {
		final String grammarStr = "<rule1> ::= afg | <rule2> <?k1=v1;k2=3?>\n"
				+ "<rule2> ::= abc | def\n";

		try {
			final Grammar g = new Grammar(grammarStr);
			final GrammarRule rule1 = g.getGrammarRule("rule1");
			final GrammarProduction p = rule1.getProduction(1);
			if (!"v1".equals(p.getAttribute("k1"))) {
				fail("Production attribute key/values not set");
			}
			if (!"3".equals(p.getAttribute("k2"))) {
				fail("Production attribute key/values not set");
			}
		} catch (final MalformedGrammarException ex) {
			fail("Malformed grammar exception thrown for a valid grammar");
		}
	}

	/**
	 * Tests that grammar literals are correctly listed by getGrammarLiterals
	 * method.
	 */
	public void testGrammarLiterals() {
		final String grammarStr = "<rule1> ::= <rule2> | <rule3>\n"
				+ "<rule2> ::= <rule1> dfa\n" + "<rule3> ::= abc | def\n";

		final String[] terminals = {"dfa", "abc", "def"};

		try {
			final Grammar g = new Grammar(grammarStr);
			final GrammarLiteral[] literals = g.getGrammarLiterals().toArray(
					new GrammarLiteral[terminals.length]);
			for (final GrammarLiteral literal: literals) {
				assertTrue("Grammar literals not correctly set",
						ArrayUtils.contains(terminals, literal.getValue()));
			}
			assertSame("Grammar literals not correctly set", terminals.length,
					literals.length);
		} catch (final MalformedGrammarException ex) {
			fail("Malformed grammar exception thrown for a valid grammar");
		}
	}

	/**
	 * Tests that grammar rules are correctly listed by getGrammarRules
	 * method.
	 */
	public void testGrammarRules() {
		final String grammarStr = "<rule1> ::= <rule2> | dfa\n"
				+ "<rule2> ::= <rule1> agd\n" + "<rule3> ::= abc | def\n";

		final String[] ruleNames = {"rule1", "rule2", "rule3"};

		try {
			final Grammar g = new Grammar(grammarStr);
			final GrammarRule[] rules = g.getGrammarRules().toArray(
					new GrammarRule[ruleNames.length]);
			for (final GrammarRule r: rules) {
				assertTrue("Grammar rules not correctly set",
						ArrayUtils.contains(ruleNames, r.getName()));
			}
			assertSame("Grammar rules not correctly set", ruleNames.length,
					rules.length);
		} catch (final MalformedGrammarException ex) {
			fail("Malformed grammar exception thrown for a valid grammar");
		}
	}
}
