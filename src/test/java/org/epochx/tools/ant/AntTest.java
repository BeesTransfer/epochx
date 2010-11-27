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
package org.epochx.tools.ant;

import java.awt.*;
import java.util.*;
import java.util.List;

import junit.framework.TestCase;

/**
 * 
 */
public class AntTest extends TestCase {

	private Ant ant;
	private AntLandscape landscape;

	@Override
	protected void setUp() throws Exception {
		landscape = new AntLandscape(new Dimension(32, 32));
		ant = new Ant(500, landscape);
	}

	/**
	 * Tests that an ant is reset correctly.
	 */
	public void testReset() {
		ant.setLocation(3, 3);
		ant.setOrientation(Orientation.SOUTH);
		ant.move();
		ant.eatFood();

		ant.reset();
		assertEquals("ant location not reset to 0,0.", ant.getLocation(),
				new Point(0, 0));
		assertEquals("ant timesteps not reset to 0.", ant.getTimesteps(), 0);
		assertEquals("ant food eaten not reset to 0.", ant.getFoodEaten(), 0);
		assertSame("ant orientation not reset to EAST.", ant.getOrientation(),
				Orientation.EAST);
	}

	/**
	 * Tests that an ant turns left from each possible orientation.
	 */
	public void testTurnLeft() {
		final int timesteps = ant.getTimesteps();

		ant.setOrientation(Orientation.EAST);
		ant.turnLeft();
		assertSame("ant turning left from EAST is not NORTH",
				ant.getOrientation(), Orientation.NORTH);

		ant.setOrientation(Orientation.SOUTH);
		ant.turnLeft();
		assertSame("ant turning left from SOUTH is not EAST",
				ant.getOrientation(), Orientation.EAST);

		ant.setOrientation(Orientation.WEST);
		ant.turnLeft();
		assertSame("ant turning left from WEST is not SOUTH",
				ant.getOrientation(), Orientation.SOUTH);

		ant.setOrientation(Orientation.NORTH);
		ant.turnLeft();
		assertSame("ant turning left from NORTH is not WEST",
				ant.getOrientation(), Orientation.WEST);

		assertSame("number of timesteps not incremented for turning left",
				timesteps + 4, ant.getTimesteps());

		final Orientation before = ant.getOrientation();
		ant.setMaxTimeSteps(4);
		ant.turnLeft();
		assertSame(
				"ant can still turn left after reaching the maximum time steps",
				before, ant.getOrientation());
	}

	/**
	 * Tests that an ant turns right from each possible orientation.
	 */
	public void testTurnRight() {
		final int timesteps = ant.getTimesteps();

		ant.setOrientation(Orientation.EAST);
		ant.turnRight();
		assertSame("ant turning right from EAST is not SOUTH",
				ant.getOrientation(), Orientation.SOUTH);

		ant.setOrientation(Orientation.SOUTH);
		ant.turnRight();
		assertSame("ant turning right from SOUTH is not WEST",
				ant.getOrientation(), Orientation.WEST);

		ant.setOrientation(Orientation.WEST);
		ant.turnRight();
		assertSame("ant turning right from WEST is not NORTH",
				ant.getOrientation(), Orientation.NORTH);

		ant.setOrientation(Orientation.NORTH);
		ant.turnRight();
		assertSame("ant turning right from NORTH is not EAST",
				ant.getOrientation(), Orientation.EAST);

		assertSame("number of timesteps not incremented for turning right",
				timesteps + 4, ant.getTimesteps());

		final Orientation before = ant.getOrientation();
		ant.setMaxTimeSteps(4);
		ant.turnRight();
		assertSame(
				"ant can still turn right after reaching the maximum time steps",
				before, ant.getOrientation());
	}

	/**
	 * Tests that an ant moves correctly.
	 */
	public void testMove() {
		final int timesteps = ant.getTimesteps();

		ant.setOrientation(Orientation.WEST);
		ant.setLocation(0, 0);
		ant.move();
		assertEquals("ant moving WEST from 0,0 does not go to 31,0",
				ant.getLocation(), new Point(31, 0));

		ant.setOrientation(Orientation.NORTH);
		ant.setLocation(0, 0);
		ant.move();
		assertEquals("ant moving NORTH from 0,0 does not go to 0,31",
				ant.getLocation(), new Point(0, 31));

		ant.setOrientation(Orientation.EAST);
		ant.setLocation(31, 31);
		ant.move();
		assertEquals("ant moving EAST from 31,31 does not go to 0,31",
				ant.getLocation(), new Point(0, 31));

		ant.setOrientation(Orientation.SOUTH);
		ant.setLocation(31, 31);
		ant.move();
		assertEquals("ant moving SOUTH from 31,31 does not go to 31,0",
				ant.getLocation(), new Point(31, 0));

		assertSame("number of timesteps not incremented for moving",
				timesteps + 4, ant.getTimesteps());

		final Point before = ant.getLocation();
		ant.setMaxTimeSteps(4);
		ant.move();
		assertEquals(
				"ant can still move after reaching the maximum time steps",
				before, ant.getLocation());
	}

	public void testIsFoodAhead() {
		assertFalse("ant reporting food ahead without there being food",
				ant.isFoodAhead());

		final List<Point> food = new ArrayList<Point>();
		food.add(new Point(1, 0));
		landscape.setFoodLocations(food);

		assertTrue("ant does not report food ahead", ant.isFoodAhead());
	}
}
