package melnorme.utilbox.core;

/**
 * Utility marker for conditional blocks 
 */
public interface DevelopmentCodeMarkers {
	
	// Marker for commented out buggy code
	public static final boolean BUGS_MODE = false; 
	
	// Marker for non-implemented or non-working functionality
	public static final boolean UNIMPLEMENTED_FUNCTIONALITY = false;
	
	/**
	 * Marker interface for test code that has a requirement on external, runtime dependencies,
	 * such a program on the PATH, a system file, an unspecified runtime OSGI dependency, etc.
	 */
	public static interface Tests_HasExternalDependencies {
		
	}
	
}