package melnorme.utilbox.core;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;

/**
 * A generic status exception.
 * Has an associated message, and optionally an associated Exception cause.
 */
public class CommonException extends Exception {
	
	private static final long serialVersionUID = -7324639626503261646L;
	
	public CommonException(String message) {
		this(assertNotNull(message), null);
	}
	
	public CommonException(String message, Throwable cause) {
		super(assertNotNull(message), cause);
	}
	
	public StatusException toStatusException(StatusLevel statusLevel) {
		if(this instanceof StatusException) {
			return (StatusException) this;
		} else {
			return new StatusException(statusLevel, getMessage());
		}
	}
	
}