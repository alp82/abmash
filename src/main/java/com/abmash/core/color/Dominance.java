package com.abmash.core.color;

public enum Dominance {
	VERYLOW(0.05),
	LOW(0.1),
	MIDLOW(0.2),
	MIDHIGH(0.3),
	HIGH(0.45),
	VERYHIGH(0.6),
	HUGE(0.75);
	
	private double value;
	
	private Dominance(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}