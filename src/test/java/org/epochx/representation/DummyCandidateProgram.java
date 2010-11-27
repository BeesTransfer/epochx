package org.epochx.representation;

public class DummyCandidateProgram extends CandidateProgram {

	private double fitness;

	private boolean valid;

	public void setFitness(final double fitness) {
		this.fitness = fitness;
	}

	@Override
	public double getFitness() {
		return fitness;
	}

	public void setValid(final boolean valid) {
		this.valid = valid;
	}

	@Override
	public boolean isValid() {
		return valid;
	}
}
