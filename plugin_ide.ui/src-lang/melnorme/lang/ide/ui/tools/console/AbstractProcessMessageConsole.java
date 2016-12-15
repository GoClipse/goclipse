/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.tools.console;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IOConsole;

import melnorme.lang.ide.ui.tools.console.ToolsConsole.IOConsoleOutputStreamExt;
import melnorme.util.swt.jface.text.ColorManager;

public abstract class AbstractProcessMessageConsole extends IOConsole {
	
	public static class ProcessMessageConsole extends AbstractProcessMessageConsole {
		protected ProcessMessageConsole(String name, ImageDescriptor imageDescriptor) {
			super(name, imageDescriptor);
			postToUI_initOutputStreamColors();
		}
	}
	
	public final IOConsoleOutputStreamExt stdOut;
	public final IOConsoleOutputStreamExt stdErr;
	
	public volatile boolean disposed;
	
	/**
	 * Note: subclasse must call {@link #postToUI_initOutputStreamColors()} after all members
	 * have been initialized.
	 */
	protected AbstractProcessMessageConsole(String name, ImageDescriptor imageDescriptor) {
		super(name, imageDescriptor);
		
		stdOut = new IOConsoleOutputStreamExt(newOutputStream());
		stdErr = new IOConsoleOutputStreamExt(newOutputStream());
		stdErr.console().setActivateOnWrite(true);
	}
	
	protected void postToUI_initOutputStreamColors() {
		// BM: it's not clear to me if a Color can be created outside UI thread, so do asyncExec
		// I would think one cant, but some Platform code (ProcessConsole) does freely create Color instances
		// on the UI thread, so maybe the asyncExec is not necessary.
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				ui_initStreamColors();
			}
		});
	}
	
	/** Initialize stream colors. This method is only called in the UI thread. */
	protected void ui_initStreamColors() {
		assertTrue(disposed == false);
	}
	
	/**
	 * Dispose this class, by running the actual disposing to the UI thread.
	 */
	@Override
	protected final void dispose() {
		// Disposing in UI thread is one way to solve certain concurrency issues arising with the use of this class. 
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				disposed = true;
				disposeDo();
			}
		});
	}
	
	protected void disposeDo() {
		assertTrue(Display.getCurrent() != null);
		super.dispose(); // run actual dispose code
	}
	
	protected ISharedTextColors getColorManager() {
		return ColorManager.getDefault();
	}
	
}