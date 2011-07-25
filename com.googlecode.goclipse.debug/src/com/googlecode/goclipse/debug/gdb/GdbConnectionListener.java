package com.googlecode.goclipse.debug.gdb;

/**
 * 
 * @author devoncarew
 */
public interface GdbConnectionListener {

	public void handleFinished();

	public void handleResumed(GdbEvent event);

	public void handleSuspended(GdbContext context);

}
