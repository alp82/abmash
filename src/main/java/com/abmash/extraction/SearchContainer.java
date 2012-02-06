package com.abmash.extraction;

import java.util.ArrayList;

public class SearchContainer {
	
	// TODO search queries with AND, OR, NOT etc.
	// TODO "word" is not a good name
	
	private ArrayList<String> queries = new ArrayList<String>();

	public ArrayList<String> getQueries() {
		return queries;
	}

	public void setQueries(ArrayList<String> queries) {
		this.queries = queries;
	}
	
	public void addQuery(String query) {
		queries.add(query);
	}

}
