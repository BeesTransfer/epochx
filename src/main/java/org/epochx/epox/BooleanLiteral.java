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
package org.epochx.epox;

public class BooleanLiteral extends BooleanNode {

	private Boolean value;

	public BooleanLiteral(final Boolean value) {
		this.value = value;
	}

	@Override
	public Boolean evaluate() {
		return value;
	}

	@Override
	public String getIdentifier() {
		return Boolean.toString(value);
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		final boolean inst = (obj instanceof BooleanLiteral);

		if (!inst) {
			return false;
		}

		final Boolean objVal = ((BooleanLiteral) obj).value;
		final Boolean thisVal = value;

		if ((objVal == null) ^ (thisVal == null)) {
			return false;
		}

		return (thisVal == objVal) || thisVal.equals(objVal);
	}

	@Override
	public BooleanLiteral clone() {
		final BooleanLiteral clone = (BooleanLiteral) super.clone();

		clone.value = value;

		return clone;
	}
}
