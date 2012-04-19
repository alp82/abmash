package com.abmash.core.query;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.abmash.core.color.ColorName;
import com.abmash.core.color.Dominance;
import com.abmash.core.color.Tolerance;

public class ColorOptions {

	private Color color;
	private double tolerance;
	private double dominance;
	
	public ColorOptions(Color color, double tolerance, double dominance) {
		this.color = color;
		this.tolerance = tolerance;
		this.dominance = dominance;
	}
	
	public Color getColor() {
		return color;
	}

	public double getTolerance() {
		return tolerance;
	}

	public double getDominance() {
		return dominance;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public void setDominance(double dominance) {
		this.dominance = dominance;
	}
	
	public void setTolerance(Tolerance tolerance) {
		this.tolerance = tolerance.getValue();
	}

	public void setDominance(Dominance dominance) {
		this.dominance = dominance.getValue();
	}
	
	public Map<String, Integer> getColorAsRGB() {
		HashMap<String, Integer> colorAsRGB = new HashMap<String, Integer>();
		colorAsRGB.put("red", color.getRed());
		colorAsRGB.put("green", color.getGreen());
		colorAsRGB.put("blue", color.getBlue());
		return colorAsRGB;
	}
	
	public String getColorAsJSONString() {
		String colorString = "{";
		colorString += "red:" + color.getRed() + ",";
		colorString += "green:" + color.getGreen() + ",";
		colorString += "blue:" + color.getBlue() + ",";
		colorString += "}";
		return colorString;
	}

	public String buildCommandSelector() {
		String options = "{ " +
			(color != null ? "color: " + getColorAsJSONString() + "," : "") +
			(tolerance > 0 ? "tolerance: " + String.valueOf(tolerance) + "," : "") +
			(dominance > 0 ? "dominance:" + String.valueOf(dominance) + "," : "") +
		" }";
		return options;
	}
	
}
