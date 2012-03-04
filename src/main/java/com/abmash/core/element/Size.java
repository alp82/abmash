package com.abmash.core.element;

/**
 * Similar to java.awt.Point, but with double values
 */
public class Size {

	  public final double width;
	  public final double height;

	  public Size(double width, double height) {
	    this.width = width;
	    this.height = height;
	  }

	  public double getWidth() {
	    return width;
	  }

	  public double getHeight() {
	    return height;
	  }

	  @Override
	  public boolean equals(Object o) {
	    if (!(o instanceof Size)) {
	      return false;
	    }

	    Size other = (Size) o;
	    return other.width == width && other.height == height;
	  }

	  @Override
	  public int hashCode() {
	    // Assuming height, width, rarely exceed 4096 pixels, shifting
	    // by 12 should provide a good hash value.
		  return Integer.valueOf(Double.toString(width * 4096 + height));
	  }

	  @Override
	  public String toString() {
	    return String.format("(%d, %d)", width, height);
	  }
}
