package org.openhab.binding.helios.internal;

import org.openhab.core.types.Type;

/**
 * This class represents a variable of the Helios modbus.
 * 
 * @author Bernhard Bauer
 * @since 1.8.0
 */
public class HeliosVariable implements Comparable<HeliosVariable> {
	
	/**
	 * Read access
	 */
	public static final int ACCESS_R = 1;
	
	/**
	 * Write access
	 */
	public static final int ACCESS_W = 2;
	
	/**
	 * Read and write access
	 */
	public static final int ACCESS_RW = 3;
	
	
	/**
	 * Integer type
	 */
	public static final int TYPE_INTEGER = 1;
	
	/**
	 * Float type
	 */
	public static final int TYPE_FLOAT = 2;
	
	/**
	 * String type
	 */
	public static final int TYPE_STRING = 3;
	
	
	/**
	 * The variable number
	 */
	private int variable;
	
	/**
	 * The access to the variable
	 */
	private int access;
	
	/**
	 * The length of the variable (number of chars)
	 */
	private int length;
	
	/**
	 * The register count for this variable
	 */
	private int count;
	
	/**
	 * The variable type
	 */
	private int type;
	
	/**
	 * The minimal value (or null if not applicable)
	 */
	private Object minVal;
	
	/**
	 * The maximum value (or null if not applicable)
	 */
	private Object maxVal;

	/**
	 * Contructor to set the member variables 
	 * @param variable The variable's number
	 * @param access Access possibilities
	 * @param length Length
	 * @param count Count
	 */
	public HeliosVariable(int variable, int access, int length, int count, int type) {
		this.variable = variable;
		this.access = access;
		this.length = length;
		this.count = count;
		this.type = type;
		this.minVal = null;
		this.maxVal = null;
	}
	
	/**
	 * Contructor to set the member variables
	 * @param variable The variable's number
	 * @param access Access possibilities
	 * @param length Length
	 * @param count Count
	 * @param minVal Minimum value
	 * @param maxVal Maximum value
	 */
	public HeliosVariable(int variable, int access, int length, int count, int type, int minVal, int maxVal) {
		this(variable, access, length, count, type);
		this.minVal = new Integer(minVal);
		this.maxVal = new Integer(maxVal);
	}
	
	/**
	 * Contructor to set the member variables
	 * @param variable The variable's number
	 * @param access Access possibilities
	 * @param length Length
	 * @param count Count
	 * @param minVal Minimum value
	 * @param maxVal Maximum value
	 */
	public HeliosVariable(int variable, int access, int length, int count, int type, int minVal, long maxVal) {
		this(variable, access, length, count, type);
		this.minVal = new Integer(minVal);
		this.maxVal = new Long(maxVal);
	}
	
	/**
	 * Contructor to set the member variables
	 * @param variable The variable's number
	 * @param access Access possibilities
	 * @param length Length
	 * @param count Count
	 * @param minVal Minimum value
	 * @param maxVal Maximum value
	 */
	public HeliosVariable(int variable, int access, int length, int count, int type, double minVal, double maxVal) {
		this(variable, access, length, count, type);
		this.minVal = new Double(minVal);
		this.maxVal = new Double(maxVal);
	}
	
	/**
	 * Getter for variable
	 * @return variable
	 */
	public int getVariable() {
		return this.variable;
	}
	
	/**
	 * Returns a formatted string representation for the variable
	 * 
	 * @return String The string representation for the variable (e.g. 'v00020' for variable number 20)
	 */
	public String getVariableString() {
		String v = Integer.toString(this.variable);
		while (v.length() < 5) v = '0' + v;
		v = 'v' + v;
		return v;
	}
	
	/**
	 * Getter for access
	 * @return access
	 */
	public int getAccess() {
		return this.access;
	}
	
	/**
	 * Getter for length
	 * @return length
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Getter for count
	 * @return count
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * Getter for the type
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Getter for minimum value
	 * @return minimum value
	 */
	public Object getMinVal() {
		return this.minVal;
	}
	
	/**
	 * Getter for maximum value
	 * @return maximum value
	 */
	public Object getMaxVal() {
		return this.maxVal;
	}
	
	public int compareTo(HeliosVariable v) {
		if (this.getVariable() < v.getVariable()) return -1;
		else if (this.getVariable() == v.getVariable()) return 0;
		else return 1;
	}
}
