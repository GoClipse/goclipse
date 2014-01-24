package melnorme.utilbox.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;


public class NumUtil {

	/** Caps given number betwen given min and max, inclusive. */
	public static int capBetween(int min, int number, int max) {
		assertTrue(min <= max);
		return Math.min(max, Math.max(min, number));
	}

	/** @return the number closest to zero, from given a and b */
	public static int nearestToZero(int a, int b) {
		if(Math.abs(a) < Math.abs(b))
			return a;
		else 
			return b;
	}

	/** @return the number farther to zero, from given a and b */
	public static int fartherFromZero(int a, int b) {
		if(Math.abs(a) > Math.abs(b))
			return a;
		else 
			return b;
	}

}
