package com.abmash.core.tools;

public class SimpleDataTypeConversion {
	
	public static double longOrDoubleToDouble(Object value) {
		if(!(value instanceof Double) && !(value instanceof Long)) {
			try {
				throw new WrongDataTypeException();
			} catch (WrongDataTypeException e) {
				e.printStackTrace();
			}
		}
		
		if(value instanceof Double) {
			return (Double) value;
		} else {
			return ((Long) value).doubleValue();
		}
	}

}
