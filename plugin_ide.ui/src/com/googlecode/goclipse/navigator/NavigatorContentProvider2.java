package com.googlecode.goclipse.navigator;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.MiscUtil.InvalidPathExceptionX;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.navigator.elements.GoPathElement;
import com.googlecode.goclipse.ui.navigator.elements.GoPathEntryElement;
import com.googlecode.goclipse.ui.navigator.elements.GoRootElement;

// TODO: this content provider is hard-coded to show files from GOROOT; we'll probably want this
// to have better knowledge of the GOROOT / GOPATH directories that are in use by the project.

/**
 * A CNF content provider that decorates the the standard resource content provider with a GOROOT
 * node. This shows the IFileStore files in the GOROOT/src directory.
 * 
 * @author devoncarew
 */
public class NavigatorContentProvider2 implements ITreeContentProvider, IPropertyChangeListener {
	
	protected final Object[] NO_CHILDREN = new Object[0];
	
	private Viewer viewer;
	
	public NavigatorContentProvider2() {
		// TODO: we really want to listen for changes to the root directories referenced by the project.
		GoUIPlugin.getPrefStore().addPropertyChangeListener(this);
		GoUIPlugin.getCorePrefStore().addPropertyChangeListener(this);
	}
	
	@Override
	public void dispose() {
		GoUIPlugin.getPrefStore().removePropertyChangeListener(this);
		GoUIPlugin.getCorePrefStore().removePropertyChangeListener(this);
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IProject) {
			return getProjectChildren((IProject) parentElement);
		} else if (parentElement instanceof GoPathElement) {
			GoPathElement pathElement = (GoPathElement) parentElement;
			
			try {
				IFileStore fileStore = EFS.getStore(pathElement.getDirectory().toURI());
				
				return fileStore.childStores(EFS.NONE, null);
			} catch (CoreException exception) {
				return NO_CHILDREN;
			}
		} else if (parentElement instanceof IFileStore) {
			IFileStore file = (IFileStore) parentElement;
			
			try {
				return file.childStores(EFS.NONE, null);
			} catch (CoreException e) {
				return NO_CHILDREN;
			}
		}
		
		return NO_CHILDREN;
	}

	protected Object[] getProjectChildren(IProject project) {
		
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
		
		GoRoot goRoot = goEnvironment.getGoRoot();
		java.nio.file.Path goRootSource;
		try {
			goRootSource = goRoot.getSourceRootLocation();
		} catch (CommonException e) {
			return NO_CHILDREN;
		}
		
		if (goRoot.isEmpty()) {
			return NO_CHILDREN;
		}
		
		ArrayList2<GoPathElement> buildpathChildren = new ArrayList2<>();
		
		buildpathChildren.add(new GoRootElement(goRootSource.toFile()));
		
		GoPath effectiveGoPath = goEnvironment.getGoPath();
		
		for (String goPathEntry : effectiveGoPath.getGoPathEntries()) {
			Path goPathEntryPath;
			try {
				goPathEntryPath = MiscUtil.createPath(goPathEntry);
			} catch (InvalidPathExceptionX e) {
				continue; // TODO: create error element
			}
			if(areEqual(goPathEntryPath, project.getLocation().toFile().toPath())) {
				continue; // Don't add this entry.
			}
			
			buildpathChildren.add(new GoPathEntryElement(goPathEntryPath, project, effectiveGoPath));
		}
		
		return buildpathChildren.toArray();
	}
	
	@Override
	public Object getParent(Object element) {
		if (element instanceof IFileStore) {
			IFileStore file = (IFileStore) element;
			
			// TODO: trim this at the GOROOT directory
			return file.getParent();
		}
		
		return null;
	}
	
	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		updateViewer();
	}
	
	private void updateViewer() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (viewer != null) {
					viewer.refresh();
				}
			}
		});
	}
	
}