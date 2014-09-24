package com.googlecode.goclipse.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

/**
 * A generic status exception.
 * Has an associated message, and optionally an associated exception cause.
 */
public class StatusException extends Exception {
	
	private static final long serialVersionUID = -7324639626503261646L;
	
	public StatusException(String message, Throwable cause) {
		super(assertNotNull(message), cause);
	}
	
}