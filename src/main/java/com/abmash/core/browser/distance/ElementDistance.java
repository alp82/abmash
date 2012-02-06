package com.abmash.core.browser.distance;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class ElementDistance {
	
	private Point sourceLocation;
	private Point targetLocation;
	private Dimension sourceSize;
	private Dimension targetSize;
	
	private Double distanceTopLeft;
	private Double distanceTop;
	private Double distanceTopRight;
	private Double distanceLeft;
	private Double distanceCenter;
	private Double distanceRight;
	private Double distanceBottomLeft;
	private Double distanceBottom;
	private Double distanceBottomRight;
	
	public ElementDistance(Point sourceLocation, Point targetLocation, Dimension sourceSize, Dimension targetSize) {
		this.sourceLocation = sourceLocation;
		this.targetLocation = targetLocation;
		this.sourceSize = sourceSize;
		this.targetSize = targetSize;
		computeDistances();
	}
	
	public String toString() {
		String output = "";
		output += " ~ distanceTopLeft:" + distanceTopLeft + "\n";
		output += " ~ distanceTop:" + distanceTop + "\n";
		output += " ~ distanceTopRight:" + distanceTopRight + "\n";
		output += " ~ distanceLeft:" + distanceLeft + "\n";
		output += " ~ distanceCenter:" + distanceCenter + "\n";
		output += " ~ distanceRight:" + distanceRight + "\n";
		output += " ~ distanceBottomLeft:" + distanceBottomLeft + "\n";
		output += " ~ distanceBottom:" + distanceBottom + "\n";
		output += " ~ distanceBottomRight:" + distanceBottomRight;
		return output;
	}
	
	public Double getDistance() {
		return (distanceTopLeft + distanceCenter + distanceBottomRight) / 3;
	}
	
	public Double getDistanceTopLeft() {
		return distanceTopLeft;
	}
	
	public Double getDistanceTop() {
		return distanceTop;
	}
	
	public Double getDistanceTopRight() {
		return distanceTopRight;
	}
	
	public Double getDistanceLeft() {
		return distanceLeft;
	}
	
	public Double getDistanceCenter() {
		return distanceCenter;
	}
	
	public Double getDistanceRight() {
		return distanceRight;
	}
	
	public Double getDistanceBottomLeft() {
		return distanceBottomLeft;
	}
	
	public Double getDistanceBottom() {
		return distanceBottom;
	}
	
	public Double getDistanceBottomRight() {
		return distanceBottomRight;
	}
	
	private void computeDistances() {
		// TODO always center from source element?
		// get coordinates
		Double xLeftSource = (double) sourceLocation.getX();
		Double xCenterSource = (double) sourceLocation.getX() + sourceSize.getWidth() / 2;
		Double xRightSource = (double) sourceLocation.getX() + sourceSize.getWidth();
		Double yTopSource = (double) sourceLocation.getY();
		Double yCenterSource = (double) sourceLocation.getY() + sourceSize.getHeight() / 2;
		Double yBottomSource = (double) sourceLocation.getY() + sourceSize.getHeight();
		
		Double xLeftTarget = (double) targetLocation.getX();
		Double xCenterTarget = (double) targetLocation.getX() + targetSize.getWidth() / 2;
		Double xRightTarget = (double) targetLocation.getX() + targetSize.getWidth();
		Double yTopTarget = (double) targetLocation.getY();
		Double yCenterTarget = (double) targetLocation.getY() + targetSize.getHeight() / 2;
		Double yBottomTarget = (double) targetLocation.getY() + targetSize.getHeight();
		
		// calculate difference of x and y coordinates at the edges and the center of the elements
		Double xDiffLeft = xLeftTarget - xLeftSource;
		Double xDiffCenter = xCenterTarget - xCenterSource;
		Double xDiffRight = xRightTarget - xRightSource;
		Double yDiffCenter = yCenterTarget - yCenterSource;

		// calculate euclidean distances
		distanceTopLeft = euclideanDistance(xDiffLeft, yBottomSource - yTopTarget);
		distanceTop = euclideanDistance(xDiffCenter, yBottomSource - yTopTarget);
		distanceTopRight = euclideanDistance(xDiffRight, yBottomSource - yTopTarget);
		distanceLeft = euclideanDistance(xRightSource - xLeftTarget, yDiffCenter);
		distanceCenter = euclideanDistance(xDiffCenter, yDiffCenter);
		distanceRight = euclideanDistance(xLeftSource - xRightTarget, yDiffCenter);
		distanceBottomLeft = euclideanDistance(xDiffLeft, yTopSource - yBottomTarget);
		distanceBottom = euclideanDistance(xDiffCenter, yTopSource - yBottomTarget);
		distanceBottomRight = euclideanDistance(xDiffRight, yTopSource - yBottomTarget);
	}
	
	private Double euclideanDistance(Double x, Double y) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
}
