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
package org.epochx.ge.codon;

import org.epochx.ge.model.GEModel;
import org.epochx.op.ConfigOperator;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Generates codon values randomly between zero and the maximum codon size as
 * specified by the model given as an argument to the constructor.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>random number generator</li>
 * <li>maximum codon size</li>
 * </ul>
 * 
 * <p>
 * If the <code>getModel</code> method returns <code>null</code> then no model
 * is set and whatever static parameters have been set as parameters to the
 * constructor or using the standard accessor methods will be used. If any
 * compulsory parameters remain unset when a new codon is requested, then an 
 * <code>IllegalStateException</code> will be thrown.
 */
public class StandardGenerator extends ConfigOperator<GEModel> implements CodonGenerator {

	// Random number generator.
	private RandomNumberGenerator rng;

	// The maximum integer value of a codon.
	private int maxCodonSize;

	/**
	 * Constructs a <code>StandardGenerator</code>.
	 * 
	 * @param rng
	 * @param maxCodonSize
	 */
	public StandardGenerator(RandomNumberGenerator rng, int maxCodonSize) {
		this(null);
		
		this.rng = rng;
		this.maxCodonSize = maxCodonSize;
	}
	
	/**
	 * Construct a StandardGenerator.
	 * 
	 * @param model the model that controls the run, providing the maximum
	 *        codon size.
	 */
	public StandardGenerator(final GEModel model) {
		super(model);
	}

	/**
	 * Configures this operator with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		rng = getModel().getRNG();
		maxCodonSize = getModel().getMaxCodonSize();
	}

	/**
	 * Generates and returns a new codon value, randomly generated with a
	 * uniform distribution between zero and the maximum codon size.
	 * 
	 * @return the newly generated codon value.
	 */
	@Override
	public int getCodon() {
		if (rng == null) {
			throw new IllegalStateException("random number generator not set");
		} else if (maxCodonSize <= 0) {
			throw new IllegalStateException("max codon size must not be less than 1");
		}
		
		return rng.nextInt(maxCodonSize);
	}
	
	/**
	 * @return the rng
	 */
	public RandomNumberGenerator getRNG() {
		return rng;
	}

	
	/**
	 * @param rng the rng to set
	 */
	public void setRNG(RandomNumberGenerator rng) {
		this.rng = rng;
	}

	
	/**
	 * @return the maxCodonSize
	 */
	public int getMaxCodonSize() {
		return maxCodonSize;
	}

	
	/**
	 * @param maxCodonSize the maxCodonSize to set
	 */
	public void setMaxCodonSize(int maxCodonSize) {
		this.maxCodonSize = maxCodonSize;
	}
	
	
}
