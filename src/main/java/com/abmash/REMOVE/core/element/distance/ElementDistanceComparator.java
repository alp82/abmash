package com.abmash.REMOVE.core.element.distance;



import com.abmash.REMOVE.core.htmlquery.condition.ElementCondition.ElementType;
import com.abmash.api.HtmlElement;
import com.abmash.core.element.Location;
import com.abmash.core.element.Size;

import java.util.ArrayList;
import java.util.Comparator;

public class ElementDistanceComparator implements Comparator<HtmlElement> {
	
	public enum DistanceType {
		TOPLEFT, TOP, TOPRIGHT,
		LEFT, CENTER, RIGHT,
		BOTTOMLEFT, BOTTOM, BOTTOMRIGHT,
		DEFAULT
	}
	
	
	public enum CalculationType {
		AVERAGE, MIN,
	}
	
	private DistanceType distanceType;
	private CalculationType calculationType;
	
	public ElementDistanceComparator(DistanceType distanceType, CalculationType calculationType) {
		this.distanceType = distanceType;
		this.calculationType = calculationType;
	}

	public int compare(HtmlElement firstElement, HtmlElement secondElement) {
		// get weighted average distances
		ArrayList<ElementWeightedAverageDistance> firstWeightedAverageDistances = getWeightedAverageDistances(firstElement);
		ArrayList<ElementWeightedAverageDistance> secondWeightedAverageDistances = getWeightedAverageDistances(secondElement);

		// TODO give weight on different reference elements?
		Double firstDistance;
		Double secondDistance;
		if(calculationType == CalculationType.MIN) {
			// get minimum distance for all reference elements
			firstDistance = getMinimumDistanceForAllReferenceElements(firstWeightedAverageDistances);
			secondDistance = getMinimumDistanceForAllReferenceElements(secondWeightedAverageDistances);
		} else {
			// get average distance for all reference elements
			firstDistance = getAverageDistanceForAllReferenceElements(firstWeightedAverageDistances);
			secondDistance = getAverageDistanceForAllReferenceElements(secondWeightedAverageDistances);
		}
		
		if(firstDistance <= secondDistance) return -1;
		if(firstDistance > secondDistance) return 1;
		return 0;
	}
	
	private ArrayList<ElementWeightedAverageDistance> getWeightedAverageDistances(HtmlElement element) {
		// hash map for the weighted distances of each target element, identified by their internal id
		ArrayList<ElementWeightedAverageDistance> weightedAverageDistances = new ArrayList<ElementWeightedAverageDistance>();

		for (HtmlElement referenceElement: element.getReferenceElements()) {
			// calculate distance and weight
			Double distance = getDistance(element, referenceElement);
			Double weight = getWeight(referenceElement);
			
			// add distance and weight
			ElementWeightedAverageDistance elementWeightedAverageDistance = new ElementWeightedAverageDistance(element);
			elementWeightedAverageDistance.addWeightedDistance(distance, weight);
			weightedAverageDistances.add(elementWeightedAverageDistance);
		}
		
		return weightedAverageDistances;
	}
	
	private Double getDistance(HtmlElement element, HtmlElement referenceElement) {
		Double distance;
		
		Location location = element.getLocation();
		Location referenceLocation = referenceElement.getLocation();
		Size size = element.getSize();
		Size referenceSize = referenceElement.getSize();
		
		ElementDistance elementDistance = new ElementDistance(referenceLocation, location, referenceSize, size);
		switch (distanceType) {
			case TOPLEFT:
				distance = elementDistance.getDistanceTopLeft();
				break;
			case TOP:
				distance = elementDistance.getDistanceTop();
				break;
			case TOPRIGHT:
				distance = elementDistance.getDistanceTopRight();
				break;
			case LEFT:
				distance = elementDistance.getDistanceLeft();
				break;
			case CENTER:
				distance = elementDistance.getDistanceCenter();
				break;
			case RIGHT:
				distance = elementDistance.getDistanceRight();
				break;
			case BOTTOMLEFT:
				distance = elementDistance.getDistanceBottomLeft();
				break;
			case BOTTOM:
				distance = elementDistance.getDistanceBottom();
				break;
			case BOTTOMRIGHT:
				distance = elementDistance.getDistanceBottomRight();
				break;
			case DEFAULT:
			default:
				distance = elementDistance.getDistance();
				for (ElementType elementType: referenceElement.getTypes()) {
					switch (elementType) {
					case TYPABLE:
						distance = elementDistance.getDistanceTopLeft();
						break;
					}
					// TODO break loop after first match?
				}
				break;
		}

//		System.out.println(distance + " for [" + element + "]");
		return distance;
	}
	
	private Double getWeight(HtmlElement element) {
		Double weight = 1.0;
		
		for (ElementType elementType: element.getTypes()) {
			switch (elementType) {
			case TYPABLE:
			case CHOOSABLE:
			case DATEPICKER:
				if (element.getTagName().equals("label")) {
					weight *= 3;
				}
				// TODO more weight on exact matches
			case IMAGE:
				// TODO font-weight 400 as constant. research if 400 is default everywhere
				String fontWeight;
				if ((fontWeight = element.getCssValue("font-weight")) != null) {
					weight *= Double.parseDouble(fontWeight) / 400;
				}
				if (element.getTagName().equals("strong")) {
					weight *= 2.0;
				}
				if (element.getTagName().equals("li")) {
					weight *= 1.5;
				}
				break;
			default:
				break;
			}		
		}
		
		return weight;
	}
	
	private Double getAverageDistanceForAllReferenceElements(ArrayList<ElementWeightedAverageDistance> weightedAverageDistances) {
		Double distance = 0.0;
		for (ElementWeightedAverageDistance weightedAverageDistance: weightedAverageDistances) {
			distance += weightedAverageDistance.calculateWeightedAverageDistance();
		}
		return distance / weightedAverageDistances.size();
	}
	
	private Double getMinimumDistanceForAllReferenceElements(ArrayList<ElementWeightedAverageDistance> weightedAverageDistances) {
		Double distance = Double.MAX_VALUE;
		for (ElementWeightedAverageDistance weightedAverageDistance: weightedAverageDistances) {
			distance = Math.min(distance, weightedAverageDistance.calculateWeightedAverageDistance());
		}
		return distance;
	}
}
