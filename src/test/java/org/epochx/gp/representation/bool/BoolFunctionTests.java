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

import junit.framework.*;

/**
 * 
 */
public class BoolFunctionTests {

	public static Test suite() {
		final TestSuite suite = new TestSuite(
				"Test for org.epochx.gp.representation.bool");
		// $JUnit-BEGIN$
		suite.addTestSuite(NotFunctionTest.class);
		suite.addTestSuite(IfFunctionTest.class);
		suite.addTestSuite(IfAndOnlyIfFunctionTest.class);
		suite.addTestSuite(XorFunctionTest.class);
		suite.addTestSuite(AndFunctionTest.class);
		suite.addTestSuite(NandFunctionTest.class);
		suite.addTestSuite(NorFunctionTest.class);
		suite.addTestSuite(ImpliesFunctionTest.class);
		suite.addTestSuite(OrFunctionTest.class);
		// $JUnit-END$
		return suite;
	}

}
