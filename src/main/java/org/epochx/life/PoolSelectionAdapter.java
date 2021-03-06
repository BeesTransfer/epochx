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
package org.epochx.life;


/**
 * Provides an <code>abstract</code> implementation of
 * <code>PoolSelectionListener</code>.
 * 
 * <p>
 * Typical use of this class would be in an anonymous class. For example:
 * 
 * <blockquote>
 * 
 * <pre>
 *     Life.get().addPoolSelectionListener(new PoolSelectionAdapter(){
 *     		public void onPoolSelectionStart() {
 *     			... do something ...
 *     		}
 *     });
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * Creating an anonymous implementation of this class is often preferable to
 * implementing <code>PoolSelectionListener</code> since it avoids the need to
 * implement methods which may be of no interest.
 */
public class PoolSelectionAdapter implements PoolSelectionListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPoolSelectionStart() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPoolSelectionEnd() {
	}

}
