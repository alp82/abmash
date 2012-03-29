package com.abmash.core.color;

public enum Tolerance {
	NONE(0.0),
	VERYLOW(0.1),
	LOW(0.2),
	MIDLOW(0.35),
	MIDHIGH(0.5),
	HIGH(0.65),
	VERYHIGH(0.8),
	HUGE(0.9);
	
	private double value;
	
	private Tolerance(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}