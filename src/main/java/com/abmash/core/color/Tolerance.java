package com.abmash.core.color;

public enum Tolerance {
	VERYLOW(0.15),
	LOW(0.35),
	MIDLOW(0.5),
	MIDHIGH(0.65),
	HIGH(0.75),
	VERYHIGH(0.85),
	HUGE(0.95);
	
	private double value;
	
	private Tolerance(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}