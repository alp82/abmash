package com.abmash.api.data;
//package com.abmash.api.tabular;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import com.abmash.api.Browser;
//import com.abmash.api.HtmlElement;
//import com.abmash.api.HtmlElements;
//
///**
// * Represents a table on a webpage, use it by calling {@link Browser#getTable(String)} or {@link Browser#getTable(HtmlElement)}.
// * <p>
// * <strong>Example:</strong>
// * <ul>
// * <li><code>Table userTable = browser.getTable("Username")</code> searches for a table which contains the query string
// * <em>Username</em> and returns the table representation of it.</li>
// * <li><code>userTable.getCell(1, 3)</code> returns the {@link HtmlElement} of the cell in the second row and the
// * fourth column (index numbering is starting at 0)</li>
// * <li><code>userTable.getCell(2, "email")</code> returns the {@link HtmlElement} of the cell in the third row (index
// * numbering is starting at 0) and the column labeled <em>email</em></li>
// * <li><code>userTable.getCellText(2, "email")</code> returns the visible text of the element</li>
// * </ul>
// * <p>
// * This class is iterable through all {@link TableRow}s. To get a specific row, use {@link #getRow(int)}.
// * <p>
// * Tables are basically matrices with n rows and m columns. Spoken in HTML source code:
// * <pre>{@code
// * <table>
// *   <tr>
// *     <td>row 1, col 1, cell 1</td>
// *     <td>row 1, col 2, cell 2</td>
// *   </tr>
// *   <tr>
// *     <td>row 2, col 1, cell 3</td>
// *     <td>row 2, col 2, cell 4</td>
// *   </tr>
// * </table>
// * }</pre>
// * <p>
// * Nested tables will be returned as visible text, there is no support for nested table representations yet.
// * 
// * @author Alper Ortac
// * @see TableRow
// */
//public class Table implements Iterable<TableRow> {
//	
//	private Browser browser;
//	private HtmlElement tableElement;
//	
//	private ArrayList<String> columnNames = new ArrayList<String>();
//	private TableRows rows = new TableRows();
//	
//	/**
//	 * Creates a table representation of the specified table element in the current page, use it by calling
//	 * {@link Browser#getTable(HtmlElement)}.
//	 * 
//	 * @param tableElement has to be an {@link HtmlElement} with the tag {@code <table>}
//	 */
//	public Table(HtmlElement tableElement) {
//		if(!tableElement.getTagName().equals("table")) throw new RuntimeException("Element is not a table: " + tableElement);
//		this.browser = tableElement.getBrowser();
//		this.tableElement = tableElement;
//		readRows();
//	}
//	
//	/**
//	 * Creates a table representation of the first table element found which contains the query text, use it by calling
//	 * {@link Browser#getTable(String)}.
//	 * 
//	 * @param browser browser instance which contains the table
//	 * @param query text which is contained in the table (visible text or attribute value)
//	 */
//	public Table(Browser browser, String query) {
//		this(browser.query().isTable().has(query).findFirst());
//	}
//	
//	private void readRows() {
//		HtmlElements rowElements = tableElement.query().xPathSelector(".//tr").find();
//		for (HtmlElement rowElement: rowElements) {
//			TableRow row = new TableRow(rowElement);
//			
//			// table headers
//			try {
//				HtmlElements thElements = rowElement.query().xPathSelector(".//th").find();
//				for (HtmlElement thElement: thElements) {
//					columnNames.add(thElement.getText());
//				}
//			} catch (Exception e) {
//				// no th elements exist
//
//				// table cells
//				int columnIndex = 0;
//				HtmlElements cellElements = rowElement.query().xPathSelector(".//td").find();
//				for (HtmlElement cellElement: cellElements) {
//					// add reference per column index
//					row.addCell(String.valueOf(columnIndex), cellElement);
//
//					// add reference per column name
//					String columnName = columnNames.get(columnIndex).toLowerCase();
//					row.addCell(columnName, cellElement);
//
//					// add reference per column class
//					String columnClass = cellElement.getAttribute("class").toLowerCase();
//					if(!columnClass.equals("") && !columnClass.equals(columnName)) row.addCell(columnClass, cellElement);
//
//					// TODO element.getLabel()
////					System.out.println(" ++ added row '" + columnIndex + "' for column '" + columnName + "': " + cellElement);
//					columnIndex++;
//				}
//			}			
//			
//			if(row.hasCells()) rows.add(row);
//		}
//	}
//	
//	/**
//	 * Iterates through all {@link TableRow}s.
//	 * 
//	 * @see java.lang.Iterable#iterator()
//	 */
//	public Iterator<TableRow> iterator() {
//		return rows.iterator();
//	}
//	
//	/**
//	 * Returns the specified table row.
//	 * <p>
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table productTable = browser.getTable("Product")</code> returns the first table which contains
//	 * the text <em>Product</em></li>
//	 * <li><code>productTable.getRow(1)</code> returns the second row of that table</li>
//	 * </ul>
//	 * 
//	 * @param rowIndex the number of the row to return, starting at 0 - throws an exception if out of bounds
//	 * @return {@link TableRow} of the specified row index
//	 */
//	public TableRow getRow(int rowIndex) {
//		if(rowIndex < 0 || rowIndex >= rows.size()) {
//			throw new RuntimeException("Table row number '" + rowIndex + "' is out of bounds, number of rows is '" + rows.size() +  "'");
//		}
//		
//		return rows.get(rowIndex);
//	}
//	
//	/**
//	 * Returns the {@link HtmlElement} in the specified table cell.
//	 * <p>
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table productTable = browser.getTable("Product")</code> returns the first table which contains
//	 * the text <em>Product</em></li>
//	 * <li><code>productTable.getCell(1,0)</code> returns the cell in the second row and first column of that table</li>
//	 * </ul>
//	 * 
//	 * @param rowIndex the number of the row, starting at 0 - throws an exception if out of bounds
//	 * @param columnIndex the number of the column, starting at 0
//	 * @return {@link HtmlElement} which is in the specified table cell
//	 */
//	public HtmlElement getCell(int rowIndex, int columnIndex) {
//		return getRow(rowIndex).get(columnIndex);
//	}
//	
//	/**
//	 * Returns the visible text in the specified table cell.
//	 * <p>
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table productTable = browser.getTable("Product")</code> returns the first table which contains
//	 * the text <em>Product</em></li>
//	 * <li><code>productTable.getTextFromCell(1,0)</code> returns the visible text in the cell in the
//	 * second row and first column of that table</li>
//	 * </ul>
//	 * 
//	 * @param rowIndex the number of the row, starting at 0 - throws an exception if out of bounds
//	 * @param columnIndex the number of the column, starting at 0
//	 * @return visible text in the specified table cell
//	 */
//	public String getTextFromCell(int rowIndex, int columnIndex) {
//		return getCell(rowIndex, columnIndex).getText();
//	}
//	
//	/**
//	 * Returns the {@link HtmlElement} in the specified table cell.
//	 * <p>
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table productTable = browser.getTable("Product")</code> returns the first table which contains
//	 * the text <em>Product</em></li>
//	 * <li><code>productTable.getCell(1, "Link")</code> returns the table cell in the second row and the
//	 * column labeled <em>Link</em>. The label is usually located in the top row of the table.</li>
//	 * </ul>
//	 * 
//	 * @param rowIndex the number of the row, starting at 0 - throws an exception if out of bounds
//	 * @param columnName label of the column, case-insensitive - label is usually in the table header
//	 * @return {@link HtmlElement} which is in the specified table cell
//	 */
//	public HtmlElement getCell(int rowIndex, String columnName) {
//		return getRow(rowIndex).get(columnName);
//	}
//	
//	/**
//	 * Returns the visible text in the specified table cell.
//	 * <p>
//	 * <strong>Example:</strong>
//	 * <ul>
//	 * <li><code>Table productTable = browser.getTable("Product")</code> returns the first table which contains
//	 * the text <em>Product</em></li>
//	 * <li><code>productTable.getTextFromCell(1, "Link")</code> returns the visible text in the cell in the
//	 * second row and the column labeled <em>Link</em>. The label is usually located in the top row of the table.</li>
//	 * </ul>
//	 * 
//	 * @param rowIndex the number of the row, starting at 0 - throws an exception if out of bounds
//	 * @param columnName label of the column, case-insensitive - label is usually in the table header
//	 * @return visible text in the specified table cell
//	 */
//	public String getTextFromCell(int rowIndex, String columnName) {
//		return getCell(rowIndex, columnName).getText();
//	}
//	
//	/**
//	 * Returns all table rows containing the specified string.
//	 * 
//	 * @param query the string which has to be in a table cell
//	 * @return the table rows containing the query string
//	 * @see TableRows#filterWith(String)
//	 */
//	public TableRows getRowsWith(String query) {
//		return rows.filterWith(query);
//	}
//	
//	/**
//	 * Returns the number of rows in this table.
//	 * 
//	 * @return row count
//	 */
//	public int getRowCount() {
//		return rows.size();
//	}
//	
//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString() {
//		return rows.toString();
//	}
//
//
//}
