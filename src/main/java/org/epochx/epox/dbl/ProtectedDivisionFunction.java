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
package org.epochx.epox.dbl;

import org.epochx.epox.DoubleNode;

/**
 * A <code>FunctionNode</code> which performs the arithmetic function of
 * division. The division is protected to avoid the scenario where division
 * by zero is attempted - which is undefined. Division by zero evaluates to
 * zero.
 */
public class ProtectedDivisionFunction extends DoubleNode {

	private final Double protectionValue;

	/**
	 * Construct a ProtectedDivisionFunction with no children. A default
	 * protection value that is returned in the case of divide-by-zero is set
	 * as 0.0.
	 */
	public ProtectedDivisionFunction() {
		this(null, null);
	}

	/**
	 * Construct a ProtectedDivisionFunction with a protection value to assign
	 * during divide-by-zero. This overrides the default protection value of
	 * 0.0.
	 * 
	 * @param protectionValue a double value to return in the case of
	 *        divide-by-zeros.
	 */
	public ProtectedDivisionFunction(final Double protectionValue) {
		this(null, null, protectionValue);
	}

	/**
	 * Construct a ProtectedDivisionFunction with 2 children. When evaluated,
	 * the evaluation of the first child will be divided by the evaluation of
	 * the second. A default protection value that is returned in the case of
	 * divide-by-zero is set as 0.0.
	 * 
	 * @param dividend The first child node - the dividend.
	 * @param divisor The second child node - the divisor.
	 */
	public ProtectedDivisionFunction(final DoubleNode dividend,
			final DoubleNode divisor) {
		this(dividend, divisor, 0.0);
	}

	/**
	 * Construct a ProtectedDivisionFunction with 2 children and a
	 * divide-by-zero protection value. When evaluated, the evaluation of the
	 * first child will be divided by the evaluation of the second. If the
	 * second (divisor) child evaluates to zero, then no division takes place
	 * and the specified protectionValue is returned.
	 * 
	 * @param dividend The first child node - the dividend.
	 * @param divisor The second child node - the divisor.
	 * @param protectionValue a double value to return in the case of
	 *        divide-by-zeros.
	 */
	public ProtectedDivisionFunction(final DoubleNode dividend,
			final DoubleNode divisor, final Double protectionValue) {
		super(dividend, divisor);

		this.protectionValue = protectionValue;
	}

	/**
	 * Evaluating a <code>ProtectedDivisionFunction</code> involves dividing
	 * the result of evaluating both children. If the divisor resolves to zero
	 * then the result returned will be zero to avoid the divide by zero issue.
	 * For performance, this function is evaluated lazily. The divisor child is
	 * evaluated first, if it evaluates to <code>0.0</code> then the first child
	 * will not be evaluated at all.
	 */
	@Override
	public Double evaluate() {
		final double c2 = ((Double) getChild(1).evaluate()).doubleValue();

		if (c2 == 0) {
			return protectionValue;
		} else {
			final double c1 = ((Double) getChild(0).evaluate()).doubleValue();

			return c1 / c2;
		}
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the ProtectedDivisionFunction which is PDIV.
	 */
	@Override
	public String getIdentifier() {
		return "PDIV";
	}
}
