package org.kerrin.dungeon.exception;

/**
 * There is a mismatch between hardcore or ironborn states of the objects
 * @author Kerrin
 *
 */
public class DifferentGameStates extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1859129146196436037L;

	public DifferentGameStates() {
		super();
	}
	
	public DifferentGameStates(String obj1, String obj2) {
		super(obj1+" and "+obj2+" should match hardcore and ironborn, but don't");
	}
}
