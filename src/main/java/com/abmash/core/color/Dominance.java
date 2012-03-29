package com.abmash.core.color;

public enum Dominance {
	VERYLOW(0.1),
	LOW(0.2),
	MIDLOW(0.3),
	MIDHIGH(0.45),
	HIGH(0.6),
	VERYHIGH(0.75),
	HUGE(0.9),
	ABSOLUTE(1.0);
	
	private double value;
	
	private Dominance(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}