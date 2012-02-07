package com.googlecode.goclipse.debug.model;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: implement
 * 
 * @author devoncarew
 */
class GoDebugProcess implements IProcess, IAdaptable {
  private GoDebugTarget target;
  private Map<String, String> attributes = new HashMap<String, String>();
  
  public GoDebugProcess(GoDebugTarget target) {
    this.target = target;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Class adapter) {
    if (adapter == ILaunch.class) {
      return getLaunch();
    }

    return null;
  }

  @Override
  public boolean canTerminate() {
    return target.canTerminate();
  }

  @Override
  public boolean isTerminated() {
    return target.isTerminated();
  }

  @Override
  public void terminate() throws DebugException {
    target.terminate();
  }

  @Override
  public String getLabel() {
    return target.getName();
  }

  @Override
  public ILaunch getLaunch() {
    return target.getLaunch();
  }

  @Override
  public IStreamsProxy getStreamsProxy() {
    // TODO Auto-generated method stub
    
    return null;
  }

  @Override
  public void setAttribute(String key, String value) {
    attributes.put(key, value);
  }

  @Override
  public String getAttribute(String key) {
    return attributes.get(key);
  }

  @Override
  public int getExitValue() throws DebugException {
    // TODO Auto-generated method stub
    
    return 0;
  }

  void fireCreationEvent() {
    fireEvent(new DebugEvent(this, DebugEvent.CREATE));
  }

  void fireTerminateEvent() {
    fireEvent(new DebugEvent(this, DebugEvent.TERMINATE));
  }

  private void fireEvent(DebugEvent event) {
    DebugPlugin.getDefault().fireDebugEventSet(new DebugEvent[] {event});
  }

}
