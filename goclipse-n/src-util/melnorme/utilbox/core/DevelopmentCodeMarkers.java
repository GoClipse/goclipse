package melnorme.utilbox.core;

/**
 * Utility marker for conditional blocks 
 */
public interface DevelopmentCodeMarkers {
	
	// Marker for commented out buggy code
	public static final boolean BUGS_MODE = false; 
	
	// Marker for non-implemented or non-working functionality
	public static final boolean UNIMPLEMENTED_FUNCTIONALITY = false;
	
}