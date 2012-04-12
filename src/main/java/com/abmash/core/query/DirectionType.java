package com.abmash.core.query;

public enum DirectionType {
	/**
	 * The searched element is close to the/any reference element.
	 */
	CLOSE,
	/**
	 * The searched element is an input field close to the/any reference element.
	 */
	INPUT,
	/**
	 * The searched element is a select, checkbox or radio field close to the/any reference element.
	 */
	SELECT,
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
	/**
	 * The searched element is above all reference elements.
	 */
	ABOVE_ALL,
	/**
	 * The searched element is below all reference elements.
	 */
	BELOW_ALL,
	/**
	 * The searched element is left to all reference elements.
	 */
	LEFT_ALL,
	/**
	 * The searched element is right to all reference elements.
	 */
	RIGHT_ALL,
}