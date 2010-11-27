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
package org.epochx.gp.stats;

import static org.epochx.stats.StatField.GEN_POP;
import static org.epochx.stats.Stats.ExpiryEvent.GENERATION;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.epochx.gp.representation.GPCandidateProgram;
import org.epochx.stats.*;

/**
 * Provides constants to be used as keys to request statistics from the
 * Stats specific to XGP.
 */
public class GPStatField {
	
	/**
	 * Returns an <code>int[]</code> which contains the depths of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_DEPTHS = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			int[] depths = null;

			// Request the population from the stats manager.
			@SuppressWarnings("unchecked")
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			// Get the depths of each program.
			if (pop != null) {
				depths = new int[pop.size()];

				for (int i = 0; i < pop.size(); i++) {
					depths[i] = pop.get(i).getProgramDepth();
				}
			}

			return depths;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the average depth of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_DEPTH_AVE = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double aveDepth = null;

			// Request the population depths from the stats manager.
			final int[] depths = (int[]) Stats.get().getStat(GEN_DEPTHS);

			// Calculate the average depth.
			if (depths != null) {
				aveDepth = StatsUtils.ave(depths);
			}

			return aveDepth;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * depths of all the <code>CandidateProgram</code>s in the
	 * population at the end of the previous generation.
	 */
	public static final Stat GEN_DEPTH_STDEV = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double stdevDepth = null;

			// Request the population depths from the stats manager.
			final int[] depths = (int[]) Stats.get().getStat(GEN_DEPTHS);

			// Calculate the standard deviation of the depths.
			if (depths != null) {
				stdevDepth = StatsUtils.stdev(depths);
			}

			return stdevDepth;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the maximum program depth of
	 * all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_DEPTH_MAX = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer maxDepth = null;

			// Request the population depths from the stats manager.
			final int[] depths = (int[]) Stats.get().getStat(GEN_DEPTHS);

			// Calculate the maximum depth.
			if (depths != null) {
				maxDepth = NumberUtils.max(depths);
			}

			return maxDepth;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the minimum program depth of all
	 * the <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_DEPTH_MIN = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer minDepth = null;

			// Request the population depths from the stats manager.
			final int[] depths = (int[]) Stats.get().getStat(GEN_DEPTHS);

			// Calculate the minimum depth.
			if (depths != null) {
				minDepth = NumberUtils.min(depths);
			}

			return minDepth;
		}
	};

	/**
	 * Returns a <code>double[]</code> which contains the average number of
	 * nodes at each depth of the <code>CandidateProgram</code>s in the
	 * population at the end of the previous generation. The root node of a
	 * <code>CandidateProgram</code> is depth 0.
	 */
	public static final Stat GEN_AVE_NODES_PER_DEPTH = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			double[] aveNodes = null;

			// Request the dependant statistics from the stats manager.
			final int[] depths = (int[]) Stats.get().getStat(GEN_DEPTHS);
			final int maxDepth = (Integer) Stats.get().getStat(GEN_DEPTH_MAX);
			@SuppressWarnings("unchecked")
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			if (depths != null) {
				// Array to fill with average number of nodes at each depth.
				aveNodes = new double[maxDepth];

				// For each depth.
				for (int d = 0; d < maxDepth; d++) {
					// Get number of nodes for each program.
					final double[] noNodes = new double[pop.size()];
					for (int j = 0; j < pop.size(); j++) {
						noNodes[j] = pop.get(j).getNodesAtDepth(d).size();
					}
					aveNodes[d] = StatsUtils.ave(noNodes);
				}
			}

			return aveNodes;
		}
	};

	/**
	 * Returns an <code>int[]</code> which contains the lengths of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_LENGTHS = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			int[] lengths = null;

			// Request the population from the stats manager.
			@SuppressWarnings("unchecked")
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			// Get the lengths of each program.
			if (pop != null) {
				lengths = new int[pop.size()];

				for (int i = 0; i < pop.size(); i++) {
					lengths[i] = pop.get(i).getProgramLength();
				}
			}

			return lengths;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the average length of all the
	 * <code>CandidateProgram</code>s in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_LENGTH_AVE = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double aveLength = null;

			// Request the population lengths from the stats manager.
			final int[] lengths = (int[]) Stats.get().getStat(GEN_LENGTHS);

			// Calculate the average length.
			if (lengths != null) {
				aveLength = StatsUtils.ave(lengths);
			}

			return aveLength;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * lengths of all the <code>CandidateProgram</code>s in the
	 * population at the end of the previous generation.
	 */
	public static final Stat GEN_LENGTH_STDEV = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double stdevLength = null;

			// Request the population lengths from the stats manager.
			final int[] lengths = (int[]) Stats.get().getStat(GEN_LENGTHS);

			// Calculate the standard deviation of the lengths.
			if (lengths != null) {
				stdevLength = StatsUtils.stdev(lengths);
			}

			return stdevLength;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the maximum program length of
	 * all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_LENGTH_MAX = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer maxLength = null;

			// Request the population lengths from the stats manager.
			final int[] lengths = (int[]) Stats.get().getStat(GEN_LENGTHS);

			// Calculate the maximum length.
			if (lengths != null) {
				maxLength = NumberUtils.max(lengths);
			}

			return maxLength;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the minimum program length of
	 * all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_LENGTH_MIN = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer minLength = null;

			// Request the population lengths from the stats manager.
			final int[] lengths = (int[]) Stats.get().getStat(GEN_LENGTHS);

			// Calculate the minimum length.
			if (lengths != null) {
				minLength = NumberUtils.min(lengths);
			}

			return minLength;
		}
	};

	/**
	 * Returns a <code>int[]</code> which contains the number of terminal nodes
	 * in each <code>CandidateProgram</code> in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_NO_TERMINALS = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			int[] noTerminals = null;

			// Request the population from the stats manager.
			@SuppressWarnings("unchecked")
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			// Get the number of terminals of each program.
			if (pop != null) {
				noTerminals = new int[pop.size()];

				for (int i = 0; i < pop.size(); i++) {
					noTerminals[i] = pop.get(i).getNoTerminals();
				}
			}

			return noTerminals;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the average number of terminals
	 * in all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_NO_TERMINALS_AVE = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double aveNoTerminals = null;

			// Request the population's number of terminals from the stats manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_TERMINALS);

			// Calculate the average number of terminals.
			if (noTerminals != null) {
				aveNoTerminals = StatsUtils.ave(noTerminals);
			}

			return aveNoTerminals;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * number of terminals in all the <code>CandidateProgram</code>s in the
	 * population at the end of the previous generation.
	 */
	public static final Stat GEN_NO_TERMINALS_STDEV = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double stdevNoTerminals = null;

			// Request the population's number of terminals from the stats manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_TERMINALS);

			// Calculate the standard deviation of the number of terminals.
			if (noTerminals != null) {
				stdevNoTerminals = StatsUtils.stdev(noTerminals);
			}

			return stdevNoTerminals;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the maximum number of terminals
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_NO_TERMINALS_MAX = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer maxNoTerminals = null;

			// Request the population's number of terminals from the stats manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_TERMINALS);

			// Calculate the maximum number of terminals.
			if (noTerminals != null) {
				maxNoTerminals = NumberUtils.max(noTerminals);
			}

			return maxNoTerminals;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the minimum number of terminals
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_NO_TERMINALS_MIN = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer minNoTerminals = null;

			// Request the population's number of terminals from the stats manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_TERMINALS);

			// Calculate the minimum number of terminals.
			if (noTerminals != null) {
				minNoTerminals = NumberUtils.min(noTerminals);
			}

			return minNoTerminals;
		}
	};

	/**
	 * Returns a <code>int[]</code> which contains the number of unique terminal
	 * nodes in each <code>CandidateProgram</code> in the population at the end
	 * of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_TERMINALS = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			int[] noDistinctTerminals = null;

			// Request the population from the stats manager.
			@SuppressWarnings("unchecked")
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			// Get the number of unique terminals of each program.
			if (pop != null) {
				noDistinctTerminals = new int[pop.size()];

				for (int i = 0; i < pop.size(); i++) {
					noDistinctTerminals[i] = pop.get(i).getNoDistinctTerminals();
				}
			}

			return noDistinctTerminals;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the average number of unique
	 * terminals in all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_TERMINALS_AVE = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double aveNoDistinctTerminals = null;

			// Request the population's number of distinct terminals from the stats
			// manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_TERMINALS);

			// Calculate the average number of unique terminals.
			if (noTerminals != null) {
				aveNoDistinctTerminals = StatsUtils.ave(noTerminals);
			}

			return aveNoDistinctTerminals;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * number of unique terminals in all the <code>CandidateProgram</code>s in
	 * the population at the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_TERMINALS_STDEV = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double stdevNoDistinctTerminals = null;

			// Request the population's number of distinct terminals from the stats
			// manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_TERMINALS);

			// Calculate the standard deviation of the number of unique terminals.
			if (noTerminals != null) {
				stdevNoDistinctTerminals = StatsUtils.stdev(noTerminals);
			}

			return stdevNoDistinctTerminals;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the maximum number of unique
	 * terminals of all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_TERMINALS_MAX = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer maxNoDistinctTerminals = null;

			// Request the population's number of distinct terminals from the stats
			// manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_TERMINALS);

			// Calculate the maximum number of unique terminals.
			if (noTerminals != null) {
				maxNoDistinctTerminals = NumberUtils.max(noTerminals);
			}

			return maxNoDistinctTerminals;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the minimum number of unique
	 * terminals of all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_TERMINALS_MIN = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer minNoDistinctTerminals = null;

			// Request the population's number of distinct terminals from the stats
			// manager.
			final int[] noTerminals = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_TERMINALS);

			// Calculate the minimum number of unique terminals.
			if (noTerminals != null) {
				minNoDistinctTerminals = NumberUtils.min(noTerminals);
			}

			return minNoDistinctTerminals;
		}
	};

	/**
	 * Returns a <code>int[]</code> which contains the number of function nodes
	 * in each <code>CandidateProgram</code> in the population at the end of the
	 * previous generation.
	 */
	public static final Stat GEN_NO_FUNCTIONS = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			int[] noFunctions = null;

			// Request the population from the stats manager.
			@SuppressWarnings("unchecked")
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			// Get the number of functions of each program.
			if (pop != null) {
				noFunctions = new int[pop.size()];

				for (int i = 0; i < pop.size(); i++) {
					noFunctions[i] = pop.get(i).getNoFunctions();
				}
			}

			return noFunctions;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the average number of functions
	 * in all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_NO_FUNCTIONS_AVE = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double aveNoFunctions = null;

			// Request the population's number of functions from the stats manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_FUNCTIONS);

			// Calculate the average number of functions.
			if (noFunctions != null) {
				aveNoFunctions = StatsUtils.ave(noFunctions);
			}

			return aveNoFunctions;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * number of functions in all the <code>CandidateProgram</code>s in the
	 * population at the end of the previous generation.
	 */
	public static final Stat GEN_NO_FUNCTIONS_STDEV = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double stdevNoFunctions = null;

			// Request the population's number of functions from the stats manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_FUNCTIONS);

			// Calculate the standard deviation of the number of functions.
			if (noFunctions != null) {
				stdevNoFunctions = StatsUtils.stdev(noFunctions);
			}

			return stdevNoFunctions;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the maximum number of functions
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_NO_FUNCTIONS_MAX = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer maxNoFunctions = null;

			// Request the population's number of functions from the stats manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_FUNCTIONS);

			// Calculate the maximum number of functions.
			if (noFunctions != null) {
				maxNoFunctions = NumberUtils.max(noFunctions);
			}

			return maxNoFunctions;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the minimum number of functions
	 * of all the <code>CandidateProgram</code>s in the population at the end of
	 * the previous generation.
	 */
	public static final Stat GEN_NO_FUNCTIONS_MIN = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer minNoFunctions = null;

			// Request the population's number of functions from the stats manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_FUNCTIONS);

			// Calculate the minimum number of functions.
			if (noFunctions != null) {
				minNoFunctions = NumberUtils.min(noFunctions);
			}

			return minNoFunctions;
		}
	};

	/**
	 * Returns a <code>int[]</code> which contains the number of unique function
	 * nodes in each <code>CandidateProgram</code> in the population at the end
	 * of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_FUNCTIONS = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			int[] noDistinctFunctions = null;

			// Request the population from the stats manager.
			@SuppressWarnings("unchecked")
			final List<GPCandidateProgram> pop = (List<GPCandidateProgram>) Stats.get().getStat(GEN_POP);

			// Get the number of unique functions of each program.
			if (pop != null) {
				noDistinctFunctions = new int[pop.size()];

				for (int i = 0; i < pop.size(); i++) {
					noDistinctFunctions[i] = pop.get(i).getNoDistinctFunctions();
				}
			}

			return noDistinctFunctions;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the average number of unique
	 * functions in all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_FUNCTIONS_AVE = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double aveNoDistinctFunctions = null;

			// Request the population's number of distinct functions from the stats
			// manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_FUNCTIONS);

			// Calculate the average number of unique functions.
			if (noFunctions != null) {
				aveNoDistinctFunctions = StatsUtils.ave(noFunctions);
			}

			return aveNoDistinctFunctions;
		}
	};

	/**
	 * Returns a <code>Double</code> which is the standard deviation of the
	 * number of unique functions in all the <code>CandidateProgram</code>s in
	 * the population at the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_FUNCTIONS_STDEV = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Double stdevNoDistinctFunctions = null;

			// Request the population's number of distinct functions from the stats
			// manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_FUNCTIONS);

			// Calculate the standard deviation of the number of unique functions.
			if (noFunctions != null) {
				stdevNoDistinctFunctions = StatsUtils.stdev(noFunctions);
			}

			return stdevNoDistinctFunctions;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the maximum number of unique
	 * functions of all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_FUNCTIONS_MAX = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer maxNoDistinctFunctions = null;

			// Request the population's number of distinct functions from the stats
			// manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_FUNCTIONS);

			// Calculate the maximum number of unique functions.
			if (noFunctions != null) {
				maxNoDistinctFunctions = NumberUtils.max(noFunctions);
			}

			return maxNoDistinctFunctions;
		}
	};

	/**
	 * Returns an <code>Integer</code> which is the minimum number of unique
	 * functions of all the <code>CandidateProgram</code>s in the population at
	 * the end of the previous generation.
	 */
	public static final Stat GEN_NO_DISTINCT_FUNCTIONS_MIN = new AbstractStat(GENERATION) {
		@Override
		public Object getStatValue() {
			Integer minNoDistinctFunctions = null;

			// Request the population's number of distinct functions from the stats
			// manager.
			final int[] noFunctions = (int[]) Stats.get().getStat(GEN_NO_DISTINCT_FUNCTIONS);

			// Calculate the minimum number of unique functions.
			if (noFunctions != null) {
				minNoDistinctFunctions = NumberUtils.min(noFunctions);
			}

			return minNoDistinctFunctions;
		}
	};

}
