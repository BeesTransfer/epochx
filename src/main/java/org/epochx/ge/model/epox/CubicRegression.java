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
package org.epochx.ge.model.epox;

/**
 * XGE model for a cubic symbolic regression problem in the Epox language.
 * 
 * <p>
 * The target program is the function: x + x^2 + x^3
 */
public class CubicRegression extends Regression {

	/**
	 * Constructs an instance of the CubicRegression model with 50 input
	 * points.
	 */
	public CubicRegression() {
		super();
	}

	/**
	 * Constructs an instance of the CubicRegression model.
	 */
	public CubicRegression(final int noPoints) {
		super(noPoints);
	}

	/**
	 * The actual function we are trying to evolve.
	 */
	@Override
	public double getCorrectResult(final double x) {
		return x + x * x + x * x * x;
	}
}
