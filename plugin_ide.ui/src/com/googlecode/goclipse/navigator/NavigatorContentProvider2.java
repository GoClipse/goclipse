package com.googlecode.goclipse.navigator;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.File;

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
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;
import com.googlecode.goclipse.ui.GoUIPlugin;

// TODO: this content provider is hard-coded to show files from GOROOT; we'll probably want this
// to have better knowledge of the GOROOT / GOPATH directories that are in use by the project.

/**
 * A CNF content provider that decorates the the standard resource content provider with a GOROOT
 * node. This shows the IFileStore files in the GOROOT/src directory.
 * 
 * @author devoncarew
 */
public class NavigatorContentProvider2 implements ITreeContentProvider, IPropertyChangeListener {
	private final Object[] NO_CHILDREN = new Object[0];
	
	protected static final String GOROOT_Name = "GOROOT";
	
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
			getProjectChildren((IProject) parentElement);
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
		
		GoRoot goRoot = GoProjectEnvironment.getEffectiveGoRoot(project);
		java.nio.file.Path goRootSource;
		try {
			goRootSource = goRoot.getSourceRootLocation();
		} catch (CommonException e) {
			return NO_CHILDREN;
		}
		
		if (goRoot.isEmpty()) {
			return NO_CHILDREN;
		}
		
		GoPath goPath2 = GoProjectEnvironment.getEffectiveGoPath(project);
		File[] goPath = getGoPathSrcFolder(goPath2);
		
		if (goPath != null && goPath.length > 0) {
			
			// populate the go paths
			if (goPath.length == 1) {
				return array(
					new GoPathElement(GOROOT_Name, goRootSource.toFile()),
					new GoPathElement(goPath[0].getParent(), goPath[0])
				);
				
			} else {
				GoPathElement[] gpe = new GoPathElement[goPath.length+1];
				gpe[0] = new GoPathElement(GOROOT_Name, goRootSource.toFile());
				
				for (int i = 0; i < goPath.length; i++){
					gpe[i+1] = new GoPathElement(goPath[i].getParent(), goPath[i]);
				}
				return gpe;
			}
		} else {
			return array(new GoPathElement(GOROOT_Name, goRootSource.toFile()));
		}
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
	
	protected File[] getGoPathSrcFolder(GoPath goPath) {
		
		int size = goPath.getGoPathElements().size();
		
		File[] files = new File[size];
		
		for (int i = 0; i < size; i++) {
			String goPathEntry = goPath.getGoPathElements().get(i);
			
			File srcFolder;
			try {
				srcFolder = MiscUtil.createPath(goPathEntry).resolve("src").toFile();
			} catch (InvalidPathExceptionX e) {
				return null; // FIXME: create error element
			}
			files[i] = srcFolder;
		}
		
		return files;
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