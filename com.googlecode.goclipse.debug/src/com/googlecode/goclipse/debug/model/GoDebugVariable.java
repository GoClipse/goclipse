package com.googlecode.goclipse.debug.model;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.gdb.GdbVariable;

/**
 * 
 * 
 * @author devoncarew
 */
public class GoDebugVariable extends GoDebugElement implements IVariable {
	private GdbVariable variable;
	private SimpleValue value;
	
	public GoDebugVariable(GoDebugTarget target, GdbVariable variable) {
		super(target);
		
		this.variable = variable;
		
		this.value = new SimpleValue(target, variable.getValue());
	}

	@Override
	public void setValue(String expression) throws DebugException {
		throw new DebugException(
				new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID, "unimplemented"));
	}

	@Override
	public void setValue(IValue value) throws DebugException {
		throw new DebugException(
				new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID, "unimplemented"));
	}

	@Override
	public boolean supportsValueModification() {
		return false;
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IValue getValue() throws DebugException {
		// TODO:
		
		return value;
	}

	@Override
	public String getName() throws DebugException {
		return variable.getName();
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		// TODO:
		
		return value.getReferenceTypeName();
	}

	@Override
	public boolean hasValueChanged() throws DebugException {
		// TODO:
		
		return false;
	}

}
