package com.abmash.core.query;

public enum DirectionType {
	/**
	 * The searched element is close to the/any reference element.
	 */
	CLOSETO,
	/**
	 * The searched element is an input field close to the/any reference element.
	 */
	CLOSETOLABEL,
	/**
	 * The searched element is a select, checkbox or radio field close to the/any reference element.
	 */
	CLOSETOCLICKABLELABEL,
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
	LEFTOF,
	/**
	 * The searched element is right to the/any reference element.
	 */
	RIGHTOF,
}