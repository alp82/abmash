package com.abmash.core.jquery;

import java.util.ArrayList;

public class JQueryCommandTODO {
	
	String command = "";
	
	ArrayList<String> filters = new ArrayList<String>();
	
	public JQueryCommandTODO() {
	}
	
	/**
	 * Merges two JQueryCommands
	 * @return self reference
	 */
	public JQueryCommandTODO merge(JQueryCommandTODO cmd) {
		if(cmd instanceof JQueryCommandTODO) {
			command += cmd.getCommand();
		}
		return this;
	}
	
	/**
	 * Reduce the set of matched elements to those that have a descendant that matches the selector or element.
	 * @return self reference
	 */
	public JQueryCommandTODO has(String selector) {
		if(selector instanceof String) {
			command += ".has(" + selector + ")";
		}
		return this;
	}
	
	/**
	 * Gets the immediate parents of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
	 * @return self reference
	 */
	public JQueryCommandTODO parents(String selector) {
		if(!(selector instanceof String)) selector = "";
		command += ".parent(" + selector + ")";
		return this;
	}
	
	/**
	 * @see JQueryCommandTODO#parent(String)
	 * @return self reference
	 */	
	public JQueryCommandTODO parents() {
		return parents(null);
	}
	
	/**
	 * Gets the ancestors of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
	 * @return self reference
	 */
	public JQueryCommandTODO ancestors(String selector) {
		if(!(selector instanceof String)) selector = "";
		command += ".parents(" + selector + ")";
		return this;
	}
	
	/**
	 * @see JQueryCommandTODO#ancestors(String)
	 * @return self reference
	 */	
	public JQueryCommandTODO ancestors() {
		return ancestors(null);
	}
	
	/**
	 * Finds children of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
	 * @return self reference
	 */
	public JQueryCommandTODO children(String selector) {
		if(!(selector instanceof String)) selector = "";
		command += ".children(" + selector + ")";
		return this;
	}
	
	/**
	 * @see JQueryCommandTODO#children(String)
	 * @return self reference
	 */
	public JQueryCommandTODO children() {
		return children(null);
	}
	
	/**
	 * Gets siblings of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
	 * @return self reference
	 */
	public JQueryCommandTODO siblings(String selector) {
		if(!(selector instanceof String)) selector = "";
		command += ".siblings(" + selector + ")";
		return this;
	}
	
	/**
	 * @see JQueryCommandTODO#siblings(String)
	 * @return self reference
	 */
	public JQueryCommandTODO siblings() {
		return siblings(null);
	}
	
	/**
	 * Gets the immediately following sibling of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
	 * @return self reference
	 */
	public JQueryCommandTODO next(String selector) {
		if(!(selector instanceof String)) selector = "";
		command += ".next(" + selector + ")";
		return this;
	}
	
	/**
	 * @see JQueryCommandTODO#next(String)
	 * @return self reference
	 */	
	public JQueryCommandTODO next() {
		return next(null);
	}
	
	/**
	 * Gets the immediately preceding sibling of each element in the current set of matched elements. If a selector is provided it retrieves only elements that match that selector.
	 * @return self reference
	 */
	public JQueryCommandTODO prev(String selector) {
		if(!(selector instanceof String)) selector = "";
		command += ".prev(" + selector + ")";
		return this;
	}
	
	/**
	 * @see JQueryCommandTODO#prev(String)
	 * @return self reference
	 */	
	public JQueryCommandTODO prev() {
		return prev(null);
	}
	
	/**
	 * Builds the jQuery command with the provided start selector. If no selector is provided it selects the 'document.body' element.
	 * @param rootSelector
	 * @return jQuery command
	 */
	public String build(String rootSelector) {
		if(rootSelector instanceof String) {
			command = "jQuery(" + rootSelector + ")" + command;
		} else {
			command = "jQuery" + command;
		}
		return command;
	}
	
	/**
	 * Builds the jQuery command.
	 * @see JQueryCommandTODO#build(String)
	 * @return jQuery command
	 */		
	public String build() {
		return build(null);
	}
	
	
	/**
	 * Gets the pure jQuery command.
	 * @return jQuery command
	 */		
	public String getCommand() {
		return command;
	}

	/**
	 * Returns the jQuery command as string representative
	 * @return jQuery command string
	 */		
	public String toString() {
		return getCommand();
	}
	
}
