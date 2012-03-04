package com.abmash.core.element;

/**
 * A copy of java.awt.Point, but with double values
 */
public class Location {

	public double x;
	public double y;

	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Location moveBy(double xOffset, double yOffset) {
		return new Location(x + xOffset, y + yOffset);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Location)) {
			return false;
		}

		Location other = (Location) o;
		return other.x == x && other.y == y;
	}

	@Override
	public int hashCode() {
		// Assuming x,y rarely exceed 4096 pixels, shifting
		// by 12 should provide a good hash value.
		return Integer.valueOf(Double.toString(x * 4096 + y));
	}

	public void move(double newX, double newY) {
		x = newX;
		y = newY;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}

}
