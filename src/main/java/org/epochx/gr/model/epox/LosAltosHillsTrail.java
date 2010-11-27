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
package org.epochx.gr.model.epox;

import java.awt.*;

/**
 * XGR model for the Los Altos Hills ant trail problem in the Epox language.
 */
public class LosAltosHillsTrail extends AntTrail {

	/**
	 * The points in the landscape that will be occupied by food.
	 */
	public static final Point[] FOOD_LOCATIONS = {new Point(1, 0),
			new Point(2, 0), new Point(3, 0), new Point(3, 1), new Point(3, 2),
			new Point(3, 3), new Point(3, 4), new Point(3, 5), new Point(4, 5),
			new Point(5, 5), new Point(6, 5), new Point(8, 5), new Point(9, 5),
			new Point(10, 5), new Point(11, 5), new Point(12, 5),
			new Point(12, 6), new Point(12, 7), new Point(12, 8),
			new Point(12, 9), new Point(12, 11), new Point(12, 12),
			new Point(12, 13), new Point(12, 14), new Point(12, 17),
			new Point(12, 18), new Point(12, 19), new Point(12, 20),
			new Point(12, 21), new Point(12, 22), new Point(12, 23),
			new Point(11, 24), new Point(10, 24), new Point(9, 24),
			new Point(8, 24), new Point(7, 24), new Point(4, 24),
			new Point(3, 24), new Point(1, 25), new Point(1, 26),
			new Point(1, 27), new Point(1, 28), new Point(1, 29),
			new Point(1, 30), new Point(1, 31), new Point(2, 33),
			new Point(3, 33), new Point(4, 33), new Point(5, 33),
			new Point(7, 32), new Point(7, 31), new Point(8, 30),
			new Point(9, 30), new Point(10, 30), new Point(11, 30),
			new Point(12, 30), new Point(17, 30), new Point(18, 30),
			new Point(20, 29), new Point(20, 28), new Point(20, 27),
			new Point(20, 26), new Point(20, 25), new Point(20, 24),
			new Point(20, 21), new Point(20, 20), new Point(20, 19),
			new Point(20, 18), new Point(21, 15), new Point(24, 14),
			new Point(24, 13), new Point(24, 10), new Point(24, 9),
			new Point(24, 8), new Point(24, 7), new Point(25, 5),
			new Point(26, 5), new Point(27, 5), new Point(28, 5),
			new Point(29, 5), new Point(30, 5), new Point(31, 5),
			new Point(32, 5), new Point(33, 5), new Point(34, 5),
			new Point(35, 5), new Point(36, 5), new Point(38, 4),
			new Point(38, 3), new Point(39, 2), new Point(40, 2),
			new Point(41, 2), new Point(43, 3), new Point(43, 4),
			new Point(43, 6), new Point(43, 9), new Point(43, 12),
			new Point(42, 14), new Point(41, 14), new Point(40, 14),
			new Point(37, 15), new Point(38, 18), new Point(41, 19),
			new Point(40, 22), new Point(37, 23), new Point(37, 24),
			new Point(37, 25), new Point(37, 26), new Point(37, 27),
			new Point(37, 28), new Point(37, 29), new Point(37, 30),
			new Point(37, 31), new Point(37, 32), new Point(37, 33),
			new Point(37, 34), new Point(35, 34), new Point(35, 35),
			new Point(35, 36), new Point(35, 37), new Point(35, 38),
			new Point(35, 39), new Point(35, 40), new Point(35, 41),
			new Point(35, 42), new Point(34, 42), new Point(33, 42),
			new Point(32, 42), new Point(31, 42), new Point(30, 42),
			new Point(30, 44), new Point(30, 45), new Point(30, 46),
			new Point(30, 47), new Point(30, 48), new Point(30, 49),
			new Point(28, 50), new Point(28, 51), new Point(28, 52),
			new Point(28, 53), new Point(28, 54), new Point(28, 55),
			new Point(28, 56), new Point(27, 56), new Point(26, 56),
			new Point(25, 56), new Point(24, 56), new Point(23, 56),
			new Point(22, 58), new Point(22, 59), new Point(22, 60),
			new Point(22, 61), new Point(22, 62), new Point(22, 63),
			new Point(22, 64), new Point(22, 65), new Point(22, 66)};

	/**
	 * Constructs the ant trail with the necessary food locations on an ant
	 * landscape of dimensions 100 x 100. The ant is set to 3000 allowed
	 * timesteps.
	 */
	public LosAltosHillsTrail() {
		super(FOOD_LOCATIONS, new Dimension(100, 100), 3000);
	}
}
