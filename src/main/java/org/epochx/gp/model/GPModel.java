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
package org.epochx.gp.model;

import java.util.List;

import org.epochx.core.Model;
import org.epochx.epox.Node;
import org.epochx.gp.op.crossover.UniformPointCrossover;
import org.epochx.gp.op.init.FullInitialiser;
import org.epochx.gp.op.mutation.SubtreeMutation;

/**
 * Model implementation for performing tree-based genetic programming
 * evolutionary runs.
 */
public abstract class GPModel extends Model {

	// Control parameters.
	private List<Node> syntax;

	private int maxInitialDepth;
	private int maxProgramDepth;

	/**
	 * Constructs a <code>GPModel</code> with a set of sensible defaults. See
	 * the appropriate accessor method for information of each default value.
	 */
	public GPModel() {
		// Set default parameter values.
		maxInitialDepth = 6;
		maxProgramDepth = 17;

		// Operators.
		setInitialiser(new FullInitialiser(this));
		setCrossover(new UniformPointCrossover(this));
		setMutation(new SubtreeMutation(this));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		if ((syntax == null) || syntax.isEmpty()) {
			throw new IllegalStateException("no syntax set");
		}

		super.run();
	}

	/**
	 * Retrieves the maximum depth of CandidatePrograms allowed in the
	 * population after initialisation. The exact way in which the
	 * implementation ensures this depth is kept to may vary.
	 * 
	 * <p>
	 * Defaults to 6.
	 * 
	 * @return the maximum depth of CandidatePrograms to be allowed in the
	 *         population after initialisation.
	 */
	public int getMaxInitialDepth() {
		return maxInitialDepth;
	}

	/**
	 * Overwrites the default max program tree depth allowed after
	 * initialisation is performed.
	 * 
	 * <p>
	 * Max depth of -1 is allowed to indicate no limit.
	 * 
	 * @param maxInitialDepth the new max program tree depth to use.
	 */
	public void setMaxInitialDepth(final int maxInitialDepth) {
		if (maxInitialDepth >= -1) {
			this.maxInitialDepth = maxInitialDepth;
		} else {
			throw new IllegalArgumentException(
					"maxInitialDepth must be -1 or greater");
		}

		assert (this.maxInitialDepth >= -1);
	}

	/**
	 * Retrieves the maximum depth of CandidatePrograms allowed in the
	 * population after undergoing genetic operators. The exact way in which
	 * CandidatePrograms deeper than this limit are dealt with may vary, but
	 * they will not be allowed to remain into the next generation unaltered.
	 * 
	 * <p>
	 * Defaults to 17.
	 * 
	 * @return the maximum depth of CandidatePrograms to be allowed in the
	 *         population after genetic operators.
	 */
	public int getMaxDepth() {
		return maxProgramDepth;
	}

	/**
	 * Overwrites the default max program tree depth allowed after genetic
	 * operators are performed.
	 * 
	 * <p>
	 * Max depth of -1 is allowed to indicate no limit.
	 * 
	 * @param maxDepth the new max program tree depth to use.
	 */
	public void setMaxDepth(final int maxDepth) {
		if (maxDepth >= -1) {
			maxProgramDepth = maxDepth;
		} else {
			throw new IllegalArgumentException(
					"maxProgramDepth must be -1 or greater");
		}

		assert (maxProgramDepth >= -1);
	}

	/**
	 * Retrieves the full set of syntax, that is terminals AND function nodes.
	 * 
	 * @return the full syntax for use in building node trees.
	 */
	public List<Node> getSyntax() {
		return syntax;
	}

	/**
	 * Sets the syntax to use, that is a complete set of the terminals and
	 * function nodes.
	 * 
	 * @param syntax a list of the functions and terminals.
	 */
	public void setSyntax(final List<Node> syntax) {
		if (syntax != null) {
			this.syntax = syntax;
		} else {
			throw new IllegalArgumentException("syntax must not be null");
		}

		assert (this.syntax != null);
	}
}
