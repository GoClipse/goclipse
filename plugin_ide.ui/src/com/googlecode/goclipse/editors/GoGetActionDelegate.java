package com.googlecode.goclipse.editors;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.builder.GoCompiler;

/**
 * 
 */
final public class GoGetActionDelegate implements IEditorActionDelegate {
	
	protected String	name;
	protected GoEditor	editor;
	
	@Override
	public final void selectionChanged(IAction action, ISelection selection) {
		
	}
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		
		if (targetEditor instanceof GoEditor) {
			editor = (GoEditor) targetEditor;
		} else {
			editor = null;
		}
		
		action.setEnabled(editor != null);
	}
	
	@Override
	public final void run(IAction action) {
		if (editor != null) {
			
			IWorkbench wb = PlatformUI.getWorkbench();
			IProgressService ps = wb.getProgressService();
			
			try {
				ps.busyCursorWhile(new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor pm) throws InvocationTargetException {
						GoCompiler compiler = new GoCompiler();
						final IEditorInput input = editor.getEditorInput();
						IFile file = ((IFileEditorInput) input).getFile();
						IProject project = file.getProject();
						try {
							compiler.goGetDependencies(project, pm, file.getLocation().toFile());
						} catch (CoreException ce) {
							throw new InvocationTargetException(ce);
						}
					}
				});
				
			} catch (InvocationTargetException e) {
				Activator.logError(e);
			} catch (InterruptedException e) {
				Activator.logError(e);
			}
		}
	}
}
