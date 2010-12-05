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

import java.math.BigInteger;
import java.util.*;


/**
 * This class provides static utility methods for working with Epox nodes.
 */
public final class NodeUtils {
	
	/**
	 * Counts and returns the number of varieties of node tree that are possible
	 * with a set of nodes to a given depth. The quantity of varieties is 
	 * effectively the size of the search space. The result will grow 
	 * exponentially with depth and out grow a long very quickly, so a 
	 * BigInteger is used here.
	 * 
	 * @param syntax the available nodes.
	 * @param depth the depth of node trees allowable.
	 * @return an int which is the number of varieties of node trees possible.
	 */
	public static BigInteger noVarieties(final List<Node> syntax, final int depth) {
		List<Node> validRootNodes = getValidNodes(syntax, depth);
		List<Node> terminals = getTerminals(validRootNodes);
		List<Node> functions = getFunctions(validRootNodes);
		
		BigInteger varieties = BigInteger.valueOf(terminals.size());
		if (depth > 0) {
			for (Node n: functions) {
				BigInteger noChildCombinations = BigInteger.ONE;
				for (int i=0; i<n.getArity(); i++) {
					noChildCombinations = noChildCombinations.multiply(noVarieties(syntax, depth-1));
				}
				varieties = varieties.add(noChildCombinations);
			}
		}
		
		return varieties;
	}
	
	
	/*
	 * Helper method for noVarieties method which returns a list of those nodes
	 * that are valid with specified depth remaining. This will exclude 
	 * terminals at depth 0. Currently this method pays no attention to data
	 * types but will need to at some point.
	 */
	private static List<Node> getValidNodes(final List<Node> syntax, final int depth) {
		List<Node> validNodes;
		if (depth > 0) {
			validNodes = syntax;
		} else {
			// Count only terminals.
			validNodes = getTerminals(syntax);
		}
		
		return validNodes;
	}
	
	/**
	 * Returns true if the number of node tree varieties is equal to or greater
	 * than the target parameter. Otherwise, false is returned. The noVarieties
	 * method can be used to determine the number of varieties, but often the 
	 * caller is only interested in whether there are sufficient varieties so 
	 * there is no need to calculate the full number.
	 *  
	 * @param syntax the available nodes.
	 * @param depth the depth of node trees allowable.
	 * @param target the number of varieties that is considered sufficient.
	 * @return true if the number of node tree varieties is equal to or greater
	 * than the target parameter, false otherwise.
	 */
	public static boolean isSufficientVarieties(final List<Node> syntax, final int depth, final BigInteger target) {
		List<Node> validRootNodes = getValidNodes(syntax, depth);

		BigInteger varieties = new BigInteger(Integer.toString(validRootNodes.size()));
		for (Node n: validRootNodes) {
			if (n.getArity() > 0) {
				BigInteger noChildCombinations = BigInteger.ONE;
				for (int i=0; i<n.getArity(); i++) {
					noChildCombinations = noChildCombinations.multiply(noVarieties(syntax, depth-1));
				}
				varieties = varieties.add(noChildCombinations);
			}
			
			if (varieties.compareTo(target) >= 0) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns those nodes from the given syntax that have an arity of 0.
	 * @param syntax a List of Nodes.
	 * @return a List of those Node objects that have an arity of 0.
	 */
	public static List<Node> getTerminals(final List<Node> syntax) {
		List<Node> terminals = new ArrayList<Node>(syntax.size());
		for (Node n: syntax) {
			if (n.getArity() == 0) {
				terminals.add(n);
			}
		}
		
		return terminals;
	}
	
	/**
	 * Returns those nodes from the given syntax that have an arity of greater
	 * than 0.
	 * @param syntax a List of Nodes.
	 * @return a List of those Node objects that have an arity of greater than 
	 * 0.
	 */
	public static List<Node> getFunctions(final List<Node> syntax) {
		List<Node> functions = new ArrayList<Node>(syntax.size());
		for (Node n: syntax) {
			if (n.getArity() > 0) {
				functions.add(n);
			}
		}
		
		return functions;
	}
}
