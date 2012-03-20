
package com.googlecode.goclipse.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;

import com.googlecode.goclipse.Activator;

/**
 * This job checks to see if any projects have been compiler with older versions of the go compiler.
 * If so, it cleans and rebuilds them.
 * 
 * @author devoncarew
 */
public class GoCompilerUpdateJob extends Job {

	public GoCompilerUpdateJob() {
		super("Building Go projects...");
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		List<IProject> dirtyProjects = new ArrayList<IProject>();
		
		GoCompiler compiler = new GoCompiler();
		
		if (compiler.getVersion() != null) {
			try {
				for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
					if (!project.isOpen()) {
						continue;
					}
					
					if (project.getNature(GoNature.NATURE_ID) != null) {
						if (compiler.requiresRebuild(project)) {
							dirtyProjects.add(project);
						}
					}
				}
			} catch (CoreException ce) {
				Activator.logError(ce);
			}
		}
		
		if (dirtyProjects.size() > 0) {
			SubMonitor subMonitor = SubMonitor.convert(monitor, getName(), 100 * dirtyProjects.size());
			
			for (IProject project : dirtyProjects) {
				try {
					project.build(IncrementalProjectBuilder.CLEAN_BUILD, subMonitor.newChild(100));
				} catch (CoreException ce) {
					Activator.logError(ce);
				}
			}
		}
		
		monitor.done();
		
		return Status.OK_STATUS;
	}

}
