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
package org.epochx.ge.representation;

import java.util.*;

import org.epochx.ge.codon.CodonGenerator;

/**
 * This class is not currently in use by XGE.
 */
public class Chromosome implements Cloneable {

	private final CodonGenerator generator;
	private List<Integer> codons;

	// These are mutually exclusive - one or the other or neither.
	private boolean wrapping;
	private boolean extending;

	public Chromosome(final CodonGenerator generator, final List<Integer> codons) {
		this.generator = generator;
		this.codons = codons;

		wrapping = false;
		extending = true;
	}

	public Chromosome(final CodonGenerator generator) {
		this(generator, new ArrayList<Integer>());
	}

	public int getCodon(int index) {
		if (wrapping) {
			index = index % codons.size();
		} else if (index >= codons.size()) {
			return -1;
		}

		return codons.get(index);
	}

	public void setCodon(int index, final int value) {
		if (wrapping) {
			index = index % codons.size();
		}

		modified();
	}

	public int getNoCodons() {
		return codons.size();
	}

	/**
	 * Randomly generate and add a codon to the candidate program.
	 */
	public void appendNewCodon() {
		appendCodon(generator.getCodon());

		modified();
	}

	/**
	 * Randomly generate and add multiple codons to the candidate program.
	 * 
	 * @param no the number of codons to generate.
	 */
	public void appendNewCodons(final int no) {
		for (int i = 0; i < no; i++) {
			appendNewCodon();
		}

		modified();
	}

	public void insertNewCodon(final int index) {
		codons.add(index, generator.getCodon());

		modified();
	}

	public int removeCodon(final int index) {
		final int c = codons.remove(index);

		modified();

		return c;
	}

	public List<Integer> removeCodons(final int from, final int to) {
		final List<Integer> removed = new ArrayList<Integer>();
		for (int i = from; i < to; i++) {
			// As we remove them the list will shrink so we use from not i.
			removed.add(codons.remove(from));
		}

		modified();

		return removed;
	}

	/**
	 * Randomly generate and add a codon to the candidate program.
	 */
	public void appendCodon(final int newCodon) {
		codons.add(newCodon);

		modified();
	}

	/**
	 * Randomly generate and add multiple codons to the candidate program.
	 */
	public void appendCodons(final List<Integer> newCodons) {
		for (int i = 0; i < newCodons.size(); i++) {
			appendCodon(newCodons.get(i));
		}

		modified();
	}

	/**
	 * Sets each codon from the startIndex. If the number of codons takes the
	 * indexes beyond the length of the current codons then the new codons will
	 * be added to the end of the list of codons. However if the first index is
	 * greater than the length of the current set of codons then an
	 * ArrayIndexOutOfBoundsException will be thrown.
	 * 
	 * @throws ArrayIndexOutOfBoundsException If the first index is beyond the
	 *         end of the current list of codons.
	 */
	public void setCodons(final List<Integer> newCodons, final int startIndex) {
		// If more than one beyond the end of current codons.
		if (startIndex > codons.size()) {
			throw new ArrayIndexOutOfBoundsException(startIndex);
		}

		for (int i = 0; i < newCodons.size(); i++) {
			final int c = newCodons.get(i);
			if (i + startIndex < codons.size()) {
				codons.set(i + startIndex, c);
			} else {
				appendCodon(c);
			}
		}

		modified();
	}

	public List<Integer> getCodons(final int from, final int to) {
		return codons.subList(from, to);
	}

	/**
	 * Note that modifying the returned list won't modify the
	 * GECandidateProgram's
	 * list of codons.
	 */
	public List<Integer> getCodons() {
		return new ArrayList<Integer>(codons);
	}

	private void modified() {

	}

	public boolean isWrapping() {
		return wrapping;
	}

	public void setWrapping(final boolean wrap) {
		wrapping = wrap;

		if (wrapping) {
			extending = false;
		}
	}

	public boolean isExtending() {
		return extending;
	}

	public void setExtending(final boolean extending) {
		this.extending = extending;

		if (extending) {
			wrapping = false;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final Chromosome c = (Chromosome) super.clone();

		c.codons = new ArrayList<Integer>(codons);
		c.extending = extending;
		c.wrapping = wrapping;

		return c;
	}

	@Override
	public String toString() {
		return codons.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Chromosome) {
			final Chromosome c = (Chromosome) obj;

			if ((c.extending == extending) && (c.wrapping == wrapping)) {
				return codons.equals(c.getCodons());
			}
		}

		return false;
	}
}
