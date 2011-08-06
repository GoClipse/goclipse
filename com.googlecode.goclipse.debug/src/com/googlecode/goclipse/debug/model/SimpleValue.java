package com.googlecode.goclipse.debug.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class SimpleValue extends GoDebugElement implements IValue {
	private String value;
	
	public SimpleValue(GoDebugTarget target, String value) {
		super(target);
		
		this.value = value;
	}
	
	@Override
	public String getReferenceTypeName() throws DebugException {
		return "string";
	}

	@Override
	public String getValueString() throws DebugException {
		return value;
	}

	@Override
	public boolean isAllocated() throws DebugException {
		return true;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		return new IVariable[0];
	}

	@Override
	public boolean hasVariables() throws DebugException {
		return false;
	}

}
