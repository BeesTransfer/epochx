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
package org.epochx.gp.representation;

import junit.framework.TestCase;

import org.epochx.epox.EpoxParser;
import org.epochx.tools.eval.MalformedProgramException;


/**
 * 
 */
public class EpoxParserTest extends TestCase {

	private EpoxParser parser;
	
	@Override
	protected void setUp() throws Exception {
		parser = new EpoxParser();
	}
	
	/**
	 * Tests that function arguments separated with commas are parsed correctly.
	 */
	public void testParseCommas() {
		try {
			parser.parse("ADD(3.0,2.1)");
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown for comma separated args");
		}
	}
	
	/**
	 * Tests that function arguments separated with spaces are parsed correctly.
	 */
	public void testParseSpaces() {
		try {
			parser.parse("ADD(3.0 2.1)");
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown for comma separated args");
		}
	}
	
	/**
	 * Tests that function arguments separated with a comma AND multiple spaces 
	 * are parsed correctly.
	 */
	public void testParseCommaAndSpace() {
		try {
			parser.parse("ADD(3.0,  2.1)");
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown for comma separated args");
		}
	}
}
