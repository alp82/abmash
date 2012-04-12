package com.abmash.core.color;

public enum Tolerance {
	VERYLOW(0.2),
	LOW(0.4),
	MIDLOW(0.55),
	MIDHIGH(0.7),
	HIGH(0.8),
	VERYHIGH(0.9),
	HUGE(0.95);
	
	private double value;
	
	private Tolerance(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}