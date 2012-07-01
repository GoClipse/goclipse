
package com.googlecode.goclipse.debug.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;

import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.gdb.GdbFrame;
import com.googlecode.goclipse.debug.gdb.GdbThread;

/**
 * 
 * @author devoncarew
 */
public class GoDebugThread extends GoDebugElement implements IThread {
	private GdbThread gdbThread;
	private List<GoDebugStackFrame> frames;
	
	public GoDebugThread(GoDebugTarget debugTarget, GdbThread gdbThread) {
		super(debugTarget);
		
		this.gdbThread = gdbThread;
	}
	
	@Override
	public boolean canResume() {
		return getDebugTarget().canResume();
	}

	@Override
	public boolean canSuspend() {
		return getDebugTarget().canSuspend();
	}

	@Override
	public boolean isSuspended() {
		return getDebugTarget().isSuspended();
	}

	@Override
	public void resume() throws DebugException {
		getDebugTarget().resume();
	}

	@Override
	public void suspend() throws DebugException {
		getDebugTarget().suspend();
	}

	@Override
	public boolean canStepInto() {
		return true;
	}

	@Override
	public boolean canStepOver() {
		return true;
	}

	@Override
	public boolean canStepReturn() {
		return true;
	}

	@Override
	public boolean isStepping() {
		return false;
	}

	@Override
	public void stepInto() throws DebugException {
		try {
			getConnection().sendStepIn();
		} catch (IOException e) {
			throw new DebugException(
				new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID, "Error stepping", e));
		}
	}

	@Override
	public void stepOver() throws DebugException {
		try {
			getConnection().sendStep();
		} catch (IOException e) {
			throw new DebugException(
				new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID, "Error stepping", e));
		}
	}

	@Override
	public void stepReturn() throws DebugException {
		try {
			getConnection().sendStepOut();
		} catch (IOException e) {
			throw new DebugException(
				new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID, "Error stepping", e));
		}
	}

	@Override
	public boolean canTerminate() {
		return getDebugTarget().canTerminate();
	}

	@Override
	public boolean isTerminated() {
		return getDebugTarget().isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		getDebugTarget().terminate();
	}

	@Override
	public IStackFrame[] getStackFrames() throws DebugException {
		if (frames == null) {
			frames = new ArrayList<GoDebugStackFrame>();
			
			for (GdbFrame gdbFrame : gdbThread.getFrames()) {
				GoDebugStackFrame frame = new GoDebugStackFrame(getTarget(), this, gdbFrame);
				
				frames.add(frame);
			}
		}
		
		return frames.toArray(new IStackFrame[frames.size()]);
	}
	
	@Override
	public boolean hasStackFrames() throws DebugException {
		return getStackFrames().length > 0;
	}

	@Override
	public int getPriority() throws DebugException {
		return 0;
	}

	@Override
	public IStackFrame getTopStackFrame() throws DebugException {
		IStackFrame[] frames = getStackFrames();
		
		return frames.length > 0 ? frames[0] : null;
	}

	@Override
	public String getName() throws DebugException {
		return "gothread-" + gdbThread.getId();
	}

	@Override
	public IBreakpoint[] getBreakpoints() {
		// TODO:
		
		return new IBreakpoint[0];
	}

}
