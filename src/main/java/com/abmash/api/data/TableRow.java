package com.abmash.api.data;
//package com.abmash.api.tabular;
//
//import java.util.HashMap;
//
//import com.abmash.api.HtmlElement;
//
///**
// * Represents a row in a {@link Table}.
// * 
// * @author Alper Ortac
// * @see Table
// */
//public class TableRow {
//	
//	private HtmlElement row = null;
//	private HashMap<String, HtmlElement> cells = new HashMap<String, HtmlElement>();
//	
//	public TableRow(HtmlElement rowElement) {
//		row = rowElement;
//	}
//	
//	/**
//	 * Adds a reference to a table cell element with the specified name, automatically done
//	 * for all table cells when a new {@link Table} instance is created.
//	 * 
//	 * @param columnName reference name for column
//	 * @param element {@link HtmlElement} in that column
//	 */
//	public void addCell(String columnName, HtmlElement element) {
//		cells.put(columnName.toLowerCase(), element);
//	}
//	
//	/**
//	 * Returns the element at the specified column index.
//	 * 
//	 * @param columnIndex index of column, starting at 0
//	 * @return {@link HtmlElement} which is contained in the cell
//	 */
//	public HtmlElement get(int columnIndex) {
//		return cells.get(String.valueOf(columnIndex));
//	}
//	
//	/**
//	 * Returns the visible text in the cell at the specified column index.
//	 * 
//	 * @param columnIndex index of column, starting at 0
//	 * @return visible text in the cell
//	 */
//	public String getText(int columnIndex) {
//		return get(columnIndex).getText();
//	}
//	
//	/**
//	 * Returns the element in the column with the specified name.
//	 * 
//	 * @param columnName name of column, either the visible text label in the table header or its class attribute value
//	 * @return {@link HtmlElement} which is contained in the cell
//	 */
//	public HtmlElement get(String columnName) {
//		String realColumnName = findColumnByName(columnName);
//		return realColumnName instanceof String ? cells.get(realColumnName) : null;
//	}
//	
//	private String findColumnByName(String columnName) {
//		columnName = columnName.toLowerCase();
//		// look for exact text matches
//		for (String index: cells.keySet()) {
//			if(index.equals(columnName)) return index;
//		}
//		// look for partial text matches
//		for (String index: cells.keySet()) {
//			if(index.contains(columnName)) return index;
//		}
//		// no so called column was found
//		return null;
//	}
//
//	/**
//	 * Returns the visible text in the column with the specified name.
//	 * 
//	 * @param columnName name of column, either the visible text label in the table header or its class attribute value
//	 * @return visible text in the cell
//	 */
//	public String getText(String columnName) {
//		return get(columnName).getText();
//	}
//	
//	/**
//	 * Check if row contains the specified text.
//	 * 
//	 * @param query the text to check for
//	 * @return true if row contains the text
//	 */
//	public boolean contains(String query) {
//		return row.getText().contains(query);
//	}
//	
//	/**
//	 * Returns true if this row contains at least one cell.
//	 * 
//	 * @return true if row contains at least one cell
//	 */
//	public boolean hasCells() {
//		return cells.size() > 0;
//	}
//	
//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString() {
//		return cells.toString();
//	}
//
//}
