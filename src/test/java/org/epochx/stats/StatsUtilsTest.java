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
package org.epochx.stats;

import junit.framework.TestCase;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 */
public class StatsUtilsTest extends TestCase {

	/**
	 * Tests that an exception is thrown for the average of an empty array.
	 */
	public void testAveDoubleEmpty() {
		try {
			StatsUtils.ave(new double[0]);
			fail("exception not thrown for average of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the average of an empty array.
	 */
	public void testAveIntEmpty() {
		try {
			StatsUtils.ave(new int[0]);
			fail("exception not thrown for average of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the standard deviation of an empty
	 * array.
	 */
	public void testStdevDoubleEmpty() {
		try {
			StatsUtils.stdev(new double[0]);
			fail("exception not thrown for standard deviation of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the standard deviation of an empty
	 * array.
	 */
	public void testStdevDoubleEmptyAve() {
		try {
			StatsUtils.stdev(new double[0], 3.0);
			fail("exception not thrown for standard deviation of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the standard deviation of an empty
	 * array.
	 */
	public void testStdevIntEmpty() {
		try {
			StatsUtils.stdev(new int[0]);
			fail("exception not thrown for standard deviation of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the standard deviation of an empty
	 * array.
	 */
	public void testStdevIntEmptyAve() {
		try {
			StatsUtils.stdev(new int[0], 3.0);
			fail("exception not thrown for standard deviation of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the maximum index of an empty
	 * array.
	 */
	public void testMaxIndexDoubleEmpty() {
		try {
			StatsUtils.maxIndex(new double[0]);
			fail("exception not thrown for maximum index of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the value returned from maxIndex is the index for the largest
	 * value.
	 */
	public void testMaxIndexDouble() {
		final double[] values = {0.1, 0.2, 0.3, 0.4};

		assertEquals("maximum index not for the maximum value",
				NumberUtils.max(values), values[StatsUtils.maxIndex(values)]);
	}

	/**
	 * Tests that an exception is thrown for the maximum index of an empty
	 * array.
	 */
	public void testMaxIndexIntEmpty() {
		try {
			StatsUtils.maxIndex(new double[0]);
			fail("exception not thrown for maximum index of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the value returned from maxIndex is the index for the largest
	 * value.
	 */
	public void testMaxIndexInt() {
		final int[] values = {1, 2, 3, 4};

		assertEquals("maximum index not for the maximum value",
				NumberUtils.max(values), values[StatsUtils.maxIndex(values)]);
	}

	/**
	 * Tests that an exception is thrown for the minimum index of an empty
	 * array.
	 */
	public void testMinIndexDoubleEmpty() {
		try {
			StatsUtils.minIndex(new double[0]);
			fail("exception not thrown for minimum index of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the value returned from minIndex is the index for the largest
	 * value.
	 */
	public void testMinIndexDouble() {
		final double[] values = {0.1, 0.2, 0.3, 0.4};

		assertEquals("minimum index not for the minimum value",
				NumberUtils.min(values), values[StatsUtils.minIndex(values)]);
	}

	/**
	 * Tests that an exception is thrown for the minimum index of an empty
	 * array.
	 */
	public void testMinIndexIntEmpty() {
		try {
			StatsUtils.minIndex(new double[0]);
			fail("exception not thrown for minimum index of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that the value returned from minIndex is the index for the largest
	 * value.
	 */
	public void testMinIndexInt() {
		final int[] values = {1, 2, 3, 4};

		assertEquals("minimum index not for the minimum value",
				NumberUtils.min(values), values[StatsUtils.minIndex(values)]);
	}

	/**
	 * Tests that an exception is thrown for the median of an empty array.
	 */
	public void testMedianDoubleEmpty() {
		try {
			StatsUtils.median(new double[0]);
			fail("exception not thrown for median of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the median of an empty array.
	 */
	public void testMedianIntEmpty() {
		try {
			StatsUtils.median(new int[0]);
			fail("exception not thrown for median of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the median of an empty array.
	 */
	public void testCi95DoubleEmpty() {
		try {
			StatsUtils.ci95(new double[0]);
			fail("exception not thrown for 95% confidence interval of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests that an exception is thrown for the median of an empty array.
	 */
	public void testCi95IntEmpty() {
		try {
			StatsUtils.ci95(new int[0]);
			fail("exception not thrown for 95% confidence interval of an empty array");
		} catch (final IllegalArgumentException e) {
		}
	}
}
