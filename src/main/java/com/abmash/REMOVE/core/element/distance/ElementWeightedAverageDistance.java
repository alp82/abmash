package com.abmash.REMOVE.core.element.distance;




import com.abmash.api.HtmlElement;

import java.util.HashMap;


// TODO merge with ElementDistance?
public class ElementWeightedAverageDistance {
	
	/**
	 * The current index for weighted distances 
	 */
	private int index = 0;
	
	/**
	 * All distances to the source elements
	 */
	private HashMap<Integer, Double> distances = new HashMap<Integer, Double>();
	
	/**
	 *  Each distance is weighted to increase the accuracy
	 */
	private HashMap<Integer, Double> weights = new HashMap<Integer, Double>();
	
	/**
	 *  The target elements itself
	 */
	private HtmlElement element;
	
	public ElementWeightedAverageDistance(HtmlElement element) {
		this.element = element;
	}
	
	public HtmlElement getElement() {
		return element;
	}
	
	public void addDistance(Double distance) {
		addWeightedDistance(distance, 1.0);
	}
	
	public void addWeightedDistance(Double distance, Double weight) {
//		browser.log().debug("added weighted distance: " + distance + " * " + weight);
		distances.put(index, distance);
		weights.put(index, weight);
		index++;
	}
	
	public Double calculateWeightedAverageDistance() {
		Double weightedDistance = 0.0;
		Double sumOfWeights = 0.0;
		for (int i = 0; i < index; i++) {
			weightedDistance += distances.get(i) * weights.get(i);
			sumOfWeights += weights.get(i);
		}
		return weightedDistance / sumOfWeights;
	}
}
