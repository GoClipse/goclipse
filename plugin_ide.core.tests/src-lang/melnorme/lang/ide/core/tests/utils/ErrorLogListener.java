package melnorme.lang.ide.core.tests.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;

public class ErrorLogListener implements ILogListener {
	
	public ErrorLogListener(boolean register) {
		if(register) {
			Platform.addLogListener(this);
		}
	}
	
	protected boolean errorOccurred = false;
	protected IStatus status;
	
	public static ErrorLogListener createAndInstall() {
		return new ErrorLogListener(true);
	}
	
	@Override
	public synchronized void logging(IStatus status, String plugin) {
		if(status.getSeverity() == IStatus.ERROR && errorOccurred == false) {
			handleErrorStatus(status, plugin);
		}
	}
	
	public void handleErrorStatus(IStatus status, String plugin) {
		errorOccurred = true;
		this.status = status;
		
		System.out.println("!!!>>>>>>>>> Logged error from: " + plugin);
		System.out.println(status);
	}
	
	public synchronized void checkErrors() throws Throwable {
		if(errorOccurred == true) {
			reset();
			throw new CoreException(status);
		}
		assertTrue(errorOccurred == false, "Assertion failed.");
	}
	
	public void uninstall() {
		Platform.removeLogListener(this);
	}
	
	public void checkErrorsAndUninstall() throws Throwable {
		uninstall();
		checkErrors();
	}
	
	public synchronized void reset() {
		errorOccurred = false;
	}
	
}