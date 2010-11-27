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

import java.util.*;

import org.epochx.epox.*;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.op.ConfigOperator;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Initialisation implementation which randomly grows program trees down to a
 * maximum depth.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>population size</li>
 * <li>maximum initial program depth</li>
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
 * @see RampedHalfAndHalfInitialiser
 */
public class GrowInitialiser extends ConfigOperator<GPModel> implements GPInitialiser {

	private RandomNumberGenerator rng;

	// The language to construct the trees from.
	private List<Node> syntax;
	private List<Node> terminals;
	private List<Node> functions;

	// The size of the populations to construct.
	private int popSize;

	// The maximum depth of each program tree to generate.
	private int maxDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	/**
	 * Constructs a <code>GrowInitialiser</code> with all the necessary
	 * parameters given.
	 */
	public GrowInitialiser(final RandomNumberGenerator rng, 
			final List<Node> syntax, final int popSize,
			final int maxDepth, final boolean acceptDuplicates) {
		this(null, acceptDuplicates);
		
		this.rng = rng;
		this.syntax = syntax;
		this.popSize = popSize;
		this.maxDepth = maxDepth;

		updateSyntax();
	}
	
	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events. Duplicate programs are allowed in the populations that are
	 * constructed.
	 * 
	 * @param model the <code>GPModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public GrowInitialiser(final GPModel model) {
		this(model, true);
	}

	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events.
	 * 
	 * @param model the <code>GPModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public GrowInitialiser(final GPModel model, final boolean acceptDuplicates) {
		super(model);
		
		terminals = new ArrayList<Node>();
		functions = new ArrayList<Node>();
		
		this.acceptDuplicates = acceptDuplicates;
	}

	/**
	 * Configures this operator with parameters from the model.
	 */
	@Override
	public void onConfigure() {
		rng = getModel().getRNG();
		popSize = getModel().getPopulationSize();
		maxDepth = getModel().getMaxInitialDepth();

		// Only update the syntax if it has changed.
		final List<Node> newSyntax = getModel().getSyntax();
		if (!newSyntax.equals(syntax)) {
			syntax = newSyntax;
			updateSyntax();
		}
	}

	/*
	 * Updates the terminals and functions lists from the syntax.
	 */
	private void updateSyntax() {
		terminals.clear();
		functions.clear();

		if (syntax != null) {
			for (final Node n: syntax) {
				if (n.getArity() == 0) {
					terminals.add(n);
				} else {
					functions.add(n);
				}
			}
		}
	}
	
	/**
	 * Generates a population of new <code>GPCandidatePrograms</code>
	 * constructed
	 * from the <code>Nodes</code> in the syntax attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GPCandidateProgram</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>.
	 * Each program will have a node tree with a depth at most equal to the
	 * value of the maximum depth attribute.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>GPCandidatePrograms</code>.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		if (popSize < 1) {
			throw new IllegalStateException(
					"Population size must be 1 or greater");
		}
		
		// Create population list to be populated.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		// Create and add new programs to the population.
		for (int i = 0; i < popSize; i++) {
			GPCandidateProgram candidate;

			do {
				// Grow a new node tree.
				candidate = getInitialProgram();
			} while (!acceptDuplicates && firstGen.contains(candidate));

			// Must be unique - add to the new population.
			firstGen.add(candidate);
		}

		return firstGen;
	}

	/**
	 * Grows a new node tree and returns it within a
	 * <code>GPCandidateProgram</code>. The nodes that form the tree will be
	 * randomly selected from the nodes provided as the syntax attribute.
	 * 
	 * @return a new <code>GPCandidateProgram</code> instance.
	 */
	public GPCandidateProgram getInitialProgram() {
		final Node root = getGrownNodeTree(maxDepth);

		return new GPCandidateProgram(root, getModel());
	}

	/**
	 * Builds a grown node tree with a maximum depth as given. The nodes that
	 * form the tree will be randomly selected from the nodes provided as the
	 * syntax attribute.
	 * 
	 * @param maxDepth The maximum depth of the node tree to be grown, where
	 *        the depth is the number of nodes from the root.
	 * @return The root node of a randomly generated node tree.
	 */
	public Node getGrownNodeTree(final int maxDepth) {
		if (rng == null) {
			throw new IllegalStateException(
					"No random number generator has been set");
		} else if (maxDepth < 0) {
			throw new IllegalStateException("Depth must be 0 or greater");
		} else if (terminals.isEmpty()) {
			throw new IllegalStateException(
					"Syntax must include nodes with arity of 0");
		}
		
		Node root;
		if (maxDepth == 0) {
			// Randomly choose a terminal node as our root.
			final int randomIndex = rng.nextInt(terminals.size());
			root = terminals.get(randomIndex).clone();
		} else {
			// Randomly choose a root node.
			final int randomIndex = rng.nextInt(syntax.size());
			root = syntax.get(randomIndex).clone();
	
			// Populate the root node with grown children with maximum depth-1.
			this.fillChildren(root, 0, maxDepth);
		}

		return root;
	}

	/*
	 * Helper method for the getGrownNodeTree method. Recursively fills the
	 * children of a node, to construct a grown tree down to at most a depth of
	 * maxDepth.
	 */
	private void fillChildren(final Node currentNode, final int currentDepth,
			final int maxDepth) {
		final int arity = currentNode.getArity();
		if (arity > 0) {
			if (currentDepth < maxDepth - 1) {
				// Not near the maximum depth yet, use functions OR terminals.
				for (int i = 0; i < arity; i++) {
					final int randomIndex = rng.nextInt(syntax.size());
					final Node child = syntax.get(randomIndex).clone();

					currentNode.setChild(i, child);
					this.fillChildren(child, (currentDepth + 1), maxDepth);
				}
			} else {
				// At maximum depth-1, fill children with terminals.
				for (int i = 0; i < arity; i++) {
					final int randomIndex = rng.nextInt(terminals.size());
					final Node child = terminals.get(randomIndex).clone();

					currentNode.setChild(i, child);
				}
			}
		}
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
	public void setDuplicatesEnabled(final boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}
	
	/**
	 * Returns the random number generator that this initialiser is using or
	 * <code>null</code> if none has been set.
	 * 
	 * @return the rng the currently set random number generator.
	 */
	public RandomNumberGenerator getRNG() {
		return rng;
	}

	/**
	 * Sets the random number generator to use. If a model has been set then
	 * this parameter will be overwritten with the random number generator from
	 * that model on the next configure event.
	 * 
	 * @param rng the random number generator to set.
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		this.rng = rng;
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
		
		updateSyntax();
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
	 * Returns the depth that every program generated by this initialiser should
	 * have.
	 * 
	 * @return the maximum depth the program trees constructed should be.
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Sets the depth that the program trees this initialiser constructs should
	 * be.
	 * 
	 * @param maxDepth the maximum depth of all new program trees.
	 */
	public void setMaxDepth(final int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
