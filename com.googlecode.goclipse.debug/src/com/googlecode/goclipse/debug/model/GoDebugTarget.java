package com.googlecode.goclipse.debug.model;

import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.breakpoints.GoBreakpoint;
import com.googlecode.goclipse.debug.gdb.GdbConnection;
import com.googlecode.goclipse.debug.gdb.GdbConnectionListener;
import com.googlecode.goclipse.debug.gdb.GdbContext;
import com.googlecode.goclipse.debug.gdb.GdbEvent;
import com.googlecode.goclipse.debug.gdb.GdbProperties;
import com.googlecode.goclipse.debug.gdb.GdbThread;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author devoncarew
 */
public class GoDebugTarget extends GoDebugElement implements IDebugTarget, GdbConnectionListener {
  private String name;
  private ILaunch launch;

  private IProcess process;
  private GdbConnection connection;

  private boolean suspended;

  private List<GoDebugThread> threads;

  private Map<GoBreakpoint, Integer> bpMap = new HashMap<GoBreakpoint, Integer>();

  public GoDebugTarget(String name, IProcess process, GdbConnection connection) {
    super(null);

    this.name = name;
    this.process = process;
    this.connection = connection;
  }

  public void start(ILaunch launch) throws DebugException {
    this.launch = launch;

    connection.addConnectionListener(this);

    connection.start();

    launch.addDebugTarget(this);

    DebugPlugin.getDefault().addDebugEventListener(new IDebugEventSetListener() {
      @Override
      public void handleDebugEvents(DebugEvent[] events) {
        for (DebugEvent event : events) {
          if (event.getSource() == process) {
            if (event.getKind() == DebugEvent.TERMINATE) {
              GoDebugTarget.this.fireTerminateEvent();
            }
          }
        }
      }
    });

    fireCreationEvent();

    setupBreakpoints();

    try {
      connection.sendRun();
    } catch (IOException e) {
      throw new DebugException(new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID,
          "Error starting target", e));
    }
  }

  @Override
  protected GdbConnection getConnection() {
    return connection;
  }

  private void setupBreakpoints() {
    IBreakpointManager bpManager = DebugPlugin.getDefault().getBreakpointManager();

    for (IBreakpoint breakpoint : bpManager.getBreakpoints(GoDebugPlugin.PLUGIN_ID)) {
      breakpointAdded(breakpoint);
    }

    bpManager.addBreakpointListener(this);
  }

  @Override
  public IDebugTarget getDebugTarget() {
    return this;
  }

  @Override
  public ILaunch getLaunch() {
    return launch;
  }

  @Override
  public boolean canTerminate() {
    return !isTerminated();
  }

  @Override
  public boolean isTerminated() {
    return getProcess().isTerminated();
  }

  @Override
  public void terminate() throws DebugException {
    DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(this);

    try {
      connection.sendQuit();
    } catch (IOException e) {
      throw new DebugException(new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID,
          "Error terminating target", e));
    }
  }

  @Override
  public boolean canResume() {
    return !isTerminated() && isSuspended();
  }

  @Override
  public boolean canSuspend() {
    return !isTerminated() && !isSuspended();
  }

  @Override
  public boolean isSuspended() {
    return suspended;
  }

  @Override
  public void resume() throws DebugException {
    try {
      connection.sendContinue();
    } catch (IOException e) {
      throw new DebugException(new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID,
          "Error continuing", e));
    }
  }

  @Override
  public void suspend() throws DebugException {
    try {
      connection.sendStop();
    } catch (IOException e) {
      throw new DebugException(new Status(IStatus.ERROR, GoDebugPlugin.PLUGIN_ID,
          "Error continuing", e));
    }
  }

  @Override
  public void breakpointAdded(IBreakpoint bp) {
    if (bp instanceof GoBreakpoint) {
      final GoBreakpoint breakpoint = (GoBreakpoint) bp;

      if (breakpoint.isBreakpointEnabled()) {
        try {
          connection.createBreakpoint(breakpoint.getFile(), breakpoint.getLine(),
              new GdbConnection.Callback() {
                @Override
                public void handleResult(GdbEvent event) {
                  GdbProperties props = (GdbProperties) event.getProperty("bkpt");

                  if (props != null) {
                    int bpNumber = props.getPropertyParseInt("number");

                    if (bpNumber != -1) {
                      bpMap.put(breakpoint, bpNumber);
                    }
                  }
                }
              });
        } catch (IOException e) {
          GoDebugPlugin.logError(e);
        }
      }
    }
  }

  @Override
  public void breakpointRemoved(IBreakpoint bp, IMarkerDelta delta) {
    if (bp instanceof GoBreakpoint) {
      GoBreakpoint breakpoint = (GoBreakpoint) bp;

      if (bpMap.containsKey(breakpoint)) {
        int bpNumber = bpMap.get(breakpoint);

        try {
          connection.removeBreakpoint(bpNumber);
        } catch (IOException e) {
          GoDebugPlugin.logError(e);
        }
      }
    }
  }

  @Override
  public void breakpointChanged(IBreakpoint bp, IMarkerDelta delta) {
    // TODO: optimize this

    if (bp instanceof GoBreakpoint) {
      breakpointRemoved(bp, delta);
      breakpointAdded(bp);
    }
  }

  @Override
  public boolean canDisconnect() {
    return false;
  }

  @Override
  public void disconnect() throws DebugException {
    throw new UnsupportedOperationException("disconnect()");
  }

  @Override
  public boolean isDisconnected() {
    return false;
  }

  @Override
  public boolean supportsStorageRetrieval() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IMemoryBlock getMemoryBlock(long startAddress, long length) throws DebugException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IProcess getProcess() {
    return process;
  }

  @Override
  public IThread[] getThreads() throws DebugException {
    if (threads == null) {
      return new IThread[0];
    } else {
      return threads.toArray(new GoDebugThread[threads.size()]);
    }
  }

  @Override
  public boolean hasThreads() throws DebugException {
    return true;
  }

  @Override
  public String getName() {
    return "gdb-" + name;
  }

  @Override
  public boolean supportsBreakpoint(IBreakpoint breakpoint) {
    return true;
  }

  // GdbConnectionListener interface

  @Override
  public void handleFinished() {
    DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(this);

    try {
      connection.sendQuit();
    } catch (IOException e) {
      // ignore

    }

    connection.dispose();
  }

  @Override
  public void handleResumed(GdbEvent event) {
    suspended = false;

    fireResumeEvent(DebugEvent.UNSPECIFIED);
  }

  @Override
  public void handleSuspended(GdbContext context) {
    suspended = true;

    threads = new ArrayList<GoDebugThread>();

    GoDebugThread eventThread = null;

    for (GdbThread gdbThread : connection.getThreads()) {
      GoDebugThread thread = new GoDebugThread(this, gdbThread);

      threads.add(thread);

      if (gdbThread == context.getCurrentThread()) {
        eventThread = thread;
      }
    }

    if (eventThread != null) {
      eventThread.fireSuspendEvent(DebugEvent.BREAKPOINT);
    } else {
      fireSuspendEvent(DebugEvent.UNSPECIFIED);
    }
  }

}
