package com.abmash.core.tools;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class DataTypeConversion {
	
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
	
	public static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e1.getValue().compareTo(e2.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}

}
