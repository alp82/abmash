package com.abmash.core.htmlquery.condition;


import java.util.TreeSet;

import com.abmash.api.HtmlElement;
import com.abmash.api.HtmlElements;
import com.abmash.core.element.distance.ElementDistanceComparator;
import com.abmash.core.element.distance.ElementDistanceComparator.CalculationType;
import com.abmash.core.element.distance.ElementDistanceComparator.DistanceType;
import com.abmash.core.htmlquery.selector.JQuerySelector;
import com.abmash.core.htmlquery.selector.SelectorGroup;


public class ClosenessCondition extends Condition {
	
	final static int MAX_REFERENCE_ELEMENTS = 10;
	
	/**
	 * Direction for closeness conditions
	 */
	public enum Direction {
		/**
		 * The searched element is close to the/any reference element.
		 */
		CLOSE,
		/**
		 * The searched element is an input field close to the/any reference element.
		 */
		INPUT,
		/**
		 * The searched element is above the/any reference element.
		 */
		ABOVE,
		/**
		 * The searched element is below the/any reference element.
		 */
		BELOW,
		/**
		 * The searched element is left to the/any reference element.
		 */
		LEFT,
		/**
		 * The searched element is right to the/any reference element.
		 */
		RIGHT,
		/**
		 * The searched element is above all reference elements.
		 */
		ABOVE_ALL,
		/**
		 * The searched element is below all reference elements.
		 */
		BELOW_ALL,
		/**
		 * The searched element is left to all reference elements.
		 */
		LEFT_ALL,
		/**
		 * The searched element is right to all reference elements.
		 */
		RIGHT_ALL,
	}
	
	private HtmlElements referenceElements;
	private Direction direction;
	
	// constructors
	
	public ClosenessCondition(HtmlElements referenceElements, Direction direction) {
		this.referenceElements = new HtmlElements();
		int numReferenceElements = 0;
		for (HtmlElement referenceElement: referenceElements) {
			// do not allow more than specific amount of source elements
			if(numReferenceElements + 1 > MAX_REFERENCE_ELEMENTS) break;
			// add reference element (i.e. the parent of inline elements)
			this.referenceElements.add(getNextAncestorBlockElement(referenceElement));
			numReferenceElements++;
		}
		this.direction = direction;
	}

	private HtmlElement getNextAncestorBlockElement(HtmlElement referenceElement) {
		// TODO wrong in most cases (for example http://www.avis.com "Return" datepicker)
		//if(referenceElement.getCssValue("display").equals("inline")) return getNextAncestorBlockElement(referenceElement.getParent());
		return referenceElement;
	}

	// condition

	@Override
	protected void buildSelectors() {
		// jquery closeness selectors
		SelectorGroup group = new SelectorGroup();
		
		switch (direction) {
			case INPUT:
				group.add(new JQuerySelector("find('*:hasLabel(" + referenceElements + ")')"));
				break;
			case ABOVE:
				group.add(new JQuerySelector("find('*:above(" + referenceElements + ")')"));
				break;
			case BELOW:
				group.add(new JQuerySelector("find('*:below(" + referenceElements + ")')"));
				break;
			case LEFT:
				group.add(new JQuerySelector("find('*:leftTo(" + referenceElements + ")')"));
				break;
			case RIGHT:
				group.add(new JQuerySelector("find('*:rightTo(" + referenceElements + ")')"));
				break;
			case ABOVE_ALL:
				group.add(new JQuerySelector("find('*:aboveAll(" + referenceElements + ")')"));
				break;
			case BELOW_ALL:
				group.add(new JQuerySelector("find('*:belowAll(" + referenceElements + ")')"));
				break;
			case LEFT_ALL:
				group.add(new JQuerySelector("find('*:leftToAll(" + referenceElements + ")')"));
				break;
			case RIGHT_ALL:
				group.add(new JQuerySelector("find('*:rightToAll(" + referenceElements + ")')"));
				break;
		}
		
		if(!group.isEmpty()) selectorGroups.add(group);
	}
	
	@Override
	public HtmlElements sortElements(HtmlElements unsortedElements) {
		HtmlElements sortedElements = new HtmlElements();

		DistanceType distanceType;
		CalculationType calculationType = CalculationType.MIN;
		
		switch (direction) {
			case INPUT:
				distanceType = DistanceType.TOPLEFT;
				break;
			case ABOVE_ALL:
				calculationType = CalculationType.AVERAGE;
			case ABOVE:
				distanceType = DistanceType.BOTTOMLEFT;
				break;
			case BELOW_ALL:
				calculationType = CalculationType.AVERAGE;
			case BELOW:
				distanceType = DistanceType.TOPLEFT;
				break;
			case LEFT_ALL:
				calculationType = CalculationType.AVERAGE;
			case LEFT:
				distanceType = DistanceType.RIGHT;
				break;
			case RIGHT_ALL:
				calculationType = CalculationType.AVERAGE;
			case RIGHT:
				distanceType = DistanceType.LEFT;
				break;
			case CLOSE:
			default:
				distanceType = DistanceType.DEFAULT;
				break;
		}
		
		TreeSet<HtmlElement> elementSet = new TreeSet<HtmlElement>(new ElementDistanceComparator(distanceType, calculationType));
		
		// TODO higher weight for reference elements in the beginning of the list
		
//		System.out.println("reference elements: " + referenceElements);
		for (HtmlElement foundElement: unsortedElements) {
			// TODO allow reference elements in result
			if(!referenceElements.contains(foundElement) && elementValid(foundElement)) {
				foundElement.setReferenceElements(referenceElements);
//				System.out.println("ADDED valid found element: " + foundElement);
				// add if element is in allowed location
				elementSet.add(foundElement); // ordered by closeness
			}
		}

		// build sorted element result set
		for (HtmlElement element: elementSet) {
			sortedElements.addAndIgnoreDuplicates(element);
		}
		
		return sortedElements;
	}
	
//	public boolean elementValid(HtmlElement foundElement) {
//		if(!super.elementValid(foundElement)) return false;
//		
//		for (HtmlElement referenceElement: referenceElements) {
//			
////			System.out.println("CLOSENESS: " + foundElement + " vs. " + referenceElement);
////			System.out.println("FOUND location: [" + foundElement.getLocation() + "] + size: [" + foundElement.getSize());
////			System.out.println("REFER location: [" + referenceElement.getLocation() + "] + size: [" + referenceElement.getSize());
////			System.out.println("HORIZ BOUNDS: " + inHorizontalBounds(foundElement, referenceElement));
////			System.out.println("VERTI BOUNDS: " + inVerticalBounds(foundElement, referenceElement));
//			switch (direction) {
//			case INPUT:
//				// only allow elements below or right of the reference element
//				if(below(foundElement, referenceElement) || right(foundElement, referenceElement)) {
//					return true;
//				}
//				break;
//			case ABOVE:
////				System.out.println("ABOVE: " + above(foundElement, referenceElement));
//			case ABOVE_ALL:
//				// only allow elements above the reference element
//				if(above(foundElement, referenceElement) && inHorizontalBounds(foundElement, referenceElement)) {
//					if(direction == Direction.ABOVE) return true;
//				} else if(direction == Direction.ABOVE_ALL) {
//					return false;
//				}
//				break;
//			case BELOW:
////				System.out.println("BELOW: " + below(foundElement, referenceElement));
//			case BELOW_ALL:
//				// only allow elements below the reference element
//				if(below(foundElement, referenceElement) && inHorizontalBounds(foundElement, referenceElement)) {
//					if(direction == Direction.BELOW) return true;
//				} else if(direction == Direction.BELOW_ALL) {
//					return false;
//				}
//				break;
//			case LEFT:
////				System.out.println("CLOSENESS: " + foundElement + " vs. " + referenceElement);
////				System.out.println("LEFTTO: " + left(foundElement, referenceElement));
////				System.out.println("VERTIC BOUNDS: " + inVerticalBounds(foundElement, referenceElement));
//			case LEFT_ALL:
//				// only allow elements in the left of the reference element
//				if(left(foundElement, referenceElement) && inVerticalBounds(foundElement, referenceElement)) { 
//					if(direction == Direction.LEFT) return true;
//				} else if(direction == Direction.LEFT_ALL) {
//					return false;
//				}
//				break;
//			case RIGHT:
////				System.out.println("RIGHTTO: " + right(foundElement, referenceElement));
//			case RIGHT_ALL:
//				// only allow elements in the right of the reference element
//				if(right(foundElement, referenceElement) && inVerticalBounds(foundElement, referenceElement)) {
//					if(direction == Direction.RIGHT) return true;
//				} else if(direction == Direction.RIGHT_ALL) {
//					return false;
//				}
//				break;
//			case CLOSE:
//			default:
//				return true;
//			}
//		}
//		
//		// location is valid if all reference elements were checked
//		boolean isValidLocation = false;
//		if(direction == Direction.ABOVE_ALL || direction == Direction.BELOW_ALL || direction == Direction.LEFT_ALL || direction == Direction.RIGHT_ALL) {
//			isValidLocation = true;
//		}
//
//		return isValidLocation;
//	}
//	
//	private boolean above(HtmlElement foundElement, HtmlElement referenceElement) {
//		Location foundLocation = foundElement.getLocation();
//		Location referenceLocation = referenceElement.getLocation();
//		Size foundSize = foundElement.getSize();
//
//		return foundLocation.getY() + foundSize.getHeight() <= referenceLocation.getY();
//	}
//	
//	private boolean below(HtmlElement foundElement, HtmlElement referenceElement) {
//		Location foundLocation = foundElement.getLocation();
//		Location referenceLocation = referenceElement.getLocation();
//		Size referenceSize = referenceElement.getSize();
//
//		return foundLocation.getY() >= referenceLocation.getY() + referenceSize.getHeight();
//	}
//	
//	private boolean left(HtmlElement foundElement, HtmlElement referenceElement) {
//		Location foundLocation = foundElement.getLocation();
//		Location referenceLocation = referenceElement.getLocation();
//		Size foundSize = foundElement.getSize();
//
//		return foundLocation.getX() + foundSize.getWidth() <= referenceLocation.getX();
//	}
//	
//	private boolean right(HtmlElement foundElement, HtmlElement referenceElement) {
//		Location foundLocation = foundElement.getLocation();
//		Location referenceLocation = referenceElement.getLocation();
//		Size referenceSize = referenceElement.getSize();
//
//		return foundLocation.getX() >= referenceLocation.getX() + referenceSize.getWidth();
//	}
//	
//	private boolean inVerticalBounds(HtmlElement foundElement, HtmlElement referenceElement) {
//		return !above(foundElement, referenceElement) && !below(foundElement, referenceElement);
//	}
//	
//	private boolean inHorizontalBounds(HtmlElement foundElement, HtmlElement referenceElement) {
//		return !left(foundElement, referenceElement) && !right(foundElement, referenceElement);
//	}

	public String toString() {
		return super.toString() + " with direction [" + direction + "] and reference elements [" + referenceElements + "]";
	}

}
