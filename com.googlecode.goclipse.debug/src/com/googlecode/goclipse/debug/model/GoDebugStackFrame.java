package com.googlecode.goclipse.debug.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.gdb.GdbFrame;
import com.googlecode.goclipse.debug.gdb.GdbVariable;

/**
 * 
 * @author devoncarew
 */
public class GoDebugStackFrame extends GoDebugElement implements IStackFrame {
	private GoDebugThread thread;
	private GdbFrame frame;
	
	private IVariable[] variables;
	
	protected GoDebugStackFrame(GoDebugTarget target, GoDebugThread thread, GdbFrame frame) {
		super(target);
		
		this.thread = thread;
		this.frame = frame;
	}
	
	@Override
	public boolean canStepInto() {
		return thread.canStepInto();
	}

	@Override
	public boolean canStepOver() {
		return thread.canStepOver();
	}

	@Override
	public boolean canStepReturn() {
		return thread.canStepReturn();
	}

	@Override
	public boolean isStepping() {
		return thread.isStepping();
	}

	@Override
	public void stepInto() throws DebugException {
		thread.stepInto();
	}

	@Override
	public void stepOver() throws DebugException {
		thread.stepOver();
	}

	@Override
	public void stepReturn() throws DebugException {
		thread.stepReturn();
	}

	@Override
	public boolean canResume() {
		return getTarget().canResume();
	}

	@Override
	public boolean canSuspend() {
		return getTarget().canSuspend();
	}

	@Override
	public boolean isSuspended() {
		return getTarget().isSuspended();
	}

	@Override
	public void resume() throws DebugException {
		getTarget().resume();
	}

	@Override
	public void suspend() throws DebugException {
		getTarget().suspend();
	}

	@Override
	public boolean canTerminate() {
		return getTarget().canTerminate();
	}

	@Override
	public boolean isTerminated() {
		return getTarget().isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		getTarget().terminate();
	}

	@Override
	public IThread getThread() {
		return thread;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		if (variables == null) {
			variables = getVariablesImpl();
		}
		
		return variables;
	}
	
	private IVariable[] getVariablesImpl() throws DebugException {
		try {
			List<IVariable> result = new ArrayList<IVariable>();
			
			for (GdbVariable var : frame.getArguments()) {
				result.add(new GoDebugVariable(getTarget(), var));
			}
			
      for (GdbVariable var : frame.getLocals()) {
        result.add(new GoDebugVariable(getTarget(), var));
      }
      
			return result.toArray(new IVariable[result.size()]);
		} catch (IOException ioe) {
			throw new DebugException(new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID, ioe.getMessage(), ioe));
		}
	}

	@Override
	public boolean hasVariables() throws DebugException {
		return true;
	}

	@Override
	public int getLineNumber() throws DebugException {
		return frame.getLine();
	}

	@Override
	public int getCharStart() throws DebugException {
		return -1;
	}

	@Override
	public int getCharEnd() throws DebugException {
		return -1;
	}

	@Override
	public String getName() throws DebugException {
		return frame.getName();
	}

	@Override
	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		return new IRegisterGroup[0];
	}

	@Override
	public boolean hasRegisterGroups() throws DebugException {
		return false;
	}

	public String getSourceName() {
		return frame.getFile();
	}

}
