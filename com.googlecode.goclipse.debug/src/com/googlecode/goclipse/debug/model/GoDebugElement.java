package com.googlecode.goclipse.debug.model;

import org.eclipse.debug.core.model.DebugElement;

import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.gdb.GdbConnection;

/**
 * 
 * @author devoncarew
 */
public abstract class GoDebugElement extends DebugElement {

	public GoDebugElement(GoDebugTarget target) {
		super(target);
	}

	@Override
	public String getModelIdentifier() {
		return GoDebugPlugin.PLUGIN_ID;
	}
	
	protected GoDebugTarget getTarget() {
		return (GoDebugTarget)getDebugTarget();
	}
	
	protected GdbConnection getConnection() {
		return getTarget().getConnection();
	}
	
}
