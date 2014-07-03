package melnorme.utilbox.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
	
	/** Marker interface for code that uses reflection to access internal API (private fields, methods, etc.),
	 * Such code is not safe and is likely to break, but in certain circumstances may be the only resort to 
	 * achieve some purpose. The @Deprecated tag is used to generate warnings in IDEs */
	@Retention(RetentionPolicy.CLASS)
	@Deprecated
	public @interface UsesReflectionToAccessInternalAPI {
		
	}
	
}