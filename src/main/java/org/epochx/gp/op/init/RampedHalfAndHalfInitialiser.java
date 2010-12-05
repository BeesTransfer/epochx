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
package org.epochx.gp.op.init;

import java.math.BigInteger;
import java.util.*;

import org.epochx.epox.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.stats.Stats.ExpiryEvent;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Initialisation implementation which uses a combination of full and grow
 * initialisers to create an initial population of
 * <code>GPCandidatePrograms</code>.
 * 
 * <p>
 * Depths are equally split between depths from the minimum initial depth
 * attribute up to the maximum initial depth. Initialisation of individuals at
 * each of these depths is then alternated between full and grow initialisers
 * starting with grow.
 * 
 * <p>
 * There will not always be an equal number of programs created to each depth,
 * this will depend on if the population size is exactly divisible by the range
 * of depths (<code>initial maximum depth - initial minimum depth</code>). If
 * the range of depths is greater than the population size then some depths will
 * not occur at all in order to ensure as wide a spread of depths up to the
 * maximum as possible.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>population size</li>
 * <li>maximum initial program initialDepth</li>
 * <li>syntax</li>
 * <li>random number generator</li>
 * </ul>
 * 
 * <p>
 * If the <code>getModel</code> method returns <code>null</code> then no model
 * is set and whatever static parameters have been set as parameters to the
 * constructor or using the standard accessor methods will be used. If any
 * compulsory parameters remain unset when the initialiser is requested to
 * generate new programs, then an <code>IllegalStateException</code> will be
 * thrown.
 * 
 * @see FullInitialiser
 * @see GrowInitialiser
 */
public class RampedHalfAndHalfInitialiser extends ConfigOperator<GPModel> implements GPInitialiser {

	/**
	 * Requests an <code>boolean[]</code> which has one element per program 
	 * initialised by this RH+H initialiser. A value of <code>true</code> 
	 * indicates the program was initialised with grow, and <code>false</code>
	 * indicates full initialisation was used.
	 */
	public static final Stat INIT_GROWN = new AbstractStat(ExpiryEvent.INITIALISATION) {};
	
	// The grow and full instances for doing their share of the work - do not allow access.
	private final GrowInitialiser grow;
	private final FullInitialiser full;

	// Nodes the programs will be constructed from.
	private List<Node> syntax;
	
	// The size of the populations to construct.
	private int popSize;

	// The depth limits of each program tree to generate.
	private int endMaxDepth;
	private int startMaxDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	/**
	 * Constructs a <code>FullInitialiser</code> with all the necessary
	 * parameters given.
	 */
	public RampedHalfAndHalfInitialiser(final RandomNumberGenerator rng,
			final List<Node> syntax, final int popSize,
			final int startMaxDepth, final int endMaxDepth,
			final boolean acceptDuplicates) {
		this(null, startMaxDepth, acceptDuplicates);
		
		this.endMaxDepth = endMaxDepth;
		this.startMaxDepth = startMaxDepth;
		this.popSize = popSize;
		this.syntax = syntax;

		// Set up the grow and full parts.
		grow.setRNG(rng);
		grow.setSyntax(syntax);
		full.setRNG(rng);
		full.setSyntax(syntax);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>GPModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public RampedHalfAndHalfInitialiser(final GPModel model) {
		this(model, 1);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param startMaxDepth the minimum depth from which programs should be
	 *        generated
	 *        to.
	 */
	public RampedHalfAndHalfInitialiser(final GPModel model, final int startMaxDepth) {
		this(model, startMaxDepth, true);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param startMaxDepth the minimum depth from which programs should be
	 *        generated
	 *        to.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public RampedHalfAndHalfInitialiser(final GPModel model,
			final int startMaxDepth, final boolean acceptDuplicates) {
		super(model);

		this.startMaxDepth = startMaxDepth;
		this.acceptDuplicates = acceptDuplicates;

		// Set up the grow and full parts.
		grow = new GrowInitialiser(model);
		full = new FullInitialiser(model);
	}

	/**
	 * Configures this operator with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		popSize = getModel().getPopulationSize();
		endMaxDepth = getModel().getMaxInitialDepth();
		syntax = getModel().getSyntax();
	}

	/**
	 * Generates a population of new <code>CandidatePrograms</code> constructed
	 * from the <code>Nodes</code> in the syntax attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GPCandidateProgram</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>.
	 * Each program will alternately be generated with the
	 * {@link FullInitialiser} and {@link GrowInitialiser}. If the population
	 * size is odd then the extra individual will be initialised using grow.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>CandidatePrograms</code>.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		if (popSize < 1) {
			throw new IllegalStateException("Population size must be 1 or greater");
		} else if (endMaxDepth < startMaxDepth) {
			throw new IllegalStateException("End maximum depth must be greater than the start maximum depth.");
		}
		
		// Create population list to populate.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);

		// Number of programs to create at each depth.
		final int[] programsPerDepth = getProgramsPerDepth();

		// Whether each program was grown or not (full).
		boolean[] grown = new boolean[popSize];
		
		int popIndex = 0;
		boolean growNext = true;
		for (int depth = startMaxDepth; depth <= endMaxDepth; depth++) {
			int noPrograms = programsPerDepth[depth-startMaxDepth];
			
			for (int i = 0; i < noPrograms; i++) {
				// Grow on even numbers, full on odd.
				GPCandidateProgram program;

				do {
					grown[popIndex] = growNext;
					if (growNext) {
						grow.setMaxDepth(depth);
						program = grow.getInitialProgram();
					} else {
						full.setDepth(depth);
						program = full.getInitialProgram();
					}
					// The effect is that if is a duplicate then will use other
					// approach next - this is deliberate because full may have
					// less possible programs for a given depth.
					growNext = !growNext;
				} while (!acceptDuplicates && firstGen.contains(program));

				firstGen.add(program);
				popIndex++;
			}
		}
		
		// Add the grown or full nature of all the programs.
		Stats.get().addData(INIT_GROWN, grown);

		return firstGen;
	}
	
	private int[] getProgramsPerDepth() {
		int noDepths = endMaxDepth - startMaxDepth + 1;
		int[] noPrograms = new int[noDepths];
		
		final int programsPerDepth = popSize / noDepths;
		Arrays.fill(noPrograms, programsPerDepth);
		
		// Remaining programs missed out by rounding.
		final int remainder = popSize % noDepths;
		
		// Add remainders to largest depth. TODO Change to spread?
		noPrograms[noPrograms.length-1] += remainder;
		
		if (!acceptDuplicates) {
			// Can sufficient be created at each depth?
			for (int i=startMaxDepth; i<=endMaxDepth; i++) {
				int target = noPrograms[i - startMaxDepth];
				BigInteger targetBI = BigInteger.valueOf(target);
				if (!NodeUtils.isSufficientVarieties(syntax, i, targetBI)) {
					BigInteger noPossibleBI = NodeUtils.noVarieties(syntax, i);
					
					// Must fit into an int because target was an int.
					int noPossible = noPossibleBI.intValue();
					int shortfall = target - noPossible;
					
					// Move the shortfall to the next depth if there is one.
					if (i+1 <= endMaxDepth) {
						noPrograms[i+1 - startMaxDepth] += shortfall;
						noPrograms[i - startMaxDepth] -= shortfall;
					} else {
						throw new IllegalStateException("Impossible to create sufficient programs inside depth parameters");
					}
				} else {
					break;
				}
			}
		}
		
		return noPrograms;
	}

	/**
	 * Returns whether or not duplicates are currently accepted or rejected from
	 * generated populations.
	 * 
	 * @return <code>true</code> if duplicates are currently accepted in any
	 *         populations generated by the <code>getInitialPopulation</code>
	 *         method and <code>false</code> otherwise
	 */
	public boolean isDuplicatesEnabled() {
		return acceptDuplicates;
	}

	/**
	 * Sets whether duplicates should be allowed in the populations that are
	 * generated, or if they should be discarded.
	 * 
	 * @param acceptDuplicates whether duplicates should be accepted in the
	 *        populations that are constructed.
	 */
	public void setDuplicatesEnabled(boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(final GPModel model) {
		super.setModel(model);

		grow.setModel(model);
		full.setModel(model);
	}

	/**
	 * Sets the random number generator to use. If a model has been set then
	 * this parameter will be overwritten with the random number generator from
	 * that model on the next configure event.
	 * 
	 * @param rng the random number generator to set.
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		grow.setRNG(rng);
		full.setRNG(rng);
	}
	
	/**
	 * Returns a <code>List</code> of the <code>Nodes</code> that form the
	 * syntax of new program generated with this initialiser, or
	 * an empty list if none have been set.
	 * 
	 * @return the types of <code>Node</code> that should be used in
	 *         constructing new programs.
	 */
	public List<Node> getSyntax() {
		return syntax;
	}

	/**
	 * Sets the <code>Nodes</code> that should be used to construct new
	 * programs. If a model has been set then this parameter will be overwritten
	 * with the syntax from that model on the next configure event.
	 * 
	 * @param syntax a <code>List</code> of the types of <code>Node</code> that
	 *        should be used in constructing new programs.
	 */
	public void setSyntax(final List<Node> syntax) {
		this.syntax = syntax;
		
		grow.setSyntax(syntax);
		full.setSyntax(syntax);
	}

	/**
	 * Returns the size of the populations that this initialiser constructs or
	 * <code>-1</code> if none has been set.
	 * 
	 * @return the size of the populations that this initialiser will generate.
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * Sets the size of the populations that this initialiser should construct
	 * on calls to the <code>getInitialPopulation</code> method.
	 * 
	 * @param popSize the size of the populations that should be created by this
	 *        initialiser.
	 */
	public void setPopSize(final int popSize) {
		this.popSize = popSize;
	}

	/**
	 * Returns the final maximum depth that will be used for programs this
	 * initialiser generates. Program depths will have been gradually ramped up
	 * to this value from the start max depth.
	 * 
	 * @return the maximum depth the program trees constructed should be.
	 */
	public int getEndMaxDepth() {
		return endMaxDepth;
	}

	/**
	 * Sets the maximum depth used for program trees this initialiser
	 * constructs. Depths will be ramped up to this maximum depth.
	 * 
	 * @param endMaxDepth the maximum depth of the ramping process.
	 */
	public void setEndMaxDepth(final int endMaxDepth) {
		this.endMaxDepth = endMaxDepth;
	}

	/**
	 * Returns the first maximum depth that will be used for programs this
	 * initialiser generates. Program depths will then be gradually ramped up to
	 * the end max depth.
	 * 
	 * @return the maximum depth used for the first program trees before the 
	 * depth is ramped up to the end max depth.
	 */
	public int getStartMaxDepth() {
		return startMaxDepth;
	}

	/**
	 * Sets the first maximum depth used for program trees this initialiser
	 * constructs. Depths will be ramped up from here to the end maximum depth.
	 * 
	 * @param startMaxDepth the maximum depth to be used for the smallest set of
	 *        program trees.
	 */
	public void setStartMaxDepth(final int startMaxDepth) {
		this.startMaxDepth = startMaxDepth;
	}
}
