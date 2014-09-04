package com.googlecode.goclipse.ui.navigator;

import java.util.ArrayList;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;
import org.eclipse.ui.navigator.IExtensionStateModel;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoNature;

/**
 * 
 * @author Mihailo Vasiljevic
 * 
 */
public class GoNavigatorContentProvider implements ITreeContentProvider,
		ICommonContentProvider, IResourceChangeListener {

	public static final Object[] NO_CHILDREN = new Object[0];
	private Object input = null;
	private TreeViewer viewer;

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IProject) {
			IProject project = (IProject) parentElement;
			Object[] result = new Object[2];

			IFolder srcFolder = project.getFolder("src");
			result[0] = new GoSourceFolder(srcFolder);
			
			Environment env = Environment.INSTANCE;

			String goRoot = env.getGoRoot(project);
			result[1] = createLibrary(project, goRoot, "src/pkg");
			
			return result;
		} else if (parentElement instanceof IGoSourceContainer) {
			IGoSourceContainer srcCont = (IGoSourceContainer) parentElement;
			return srcCont.getChildren();
		}
		return NO_CHILDREN;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IWorkspaceRoot) {
			return null;
		} else if (element instanceof IProject) {
			return ((IProject)element).getParent();
		} else if (element instanceof IGoSourceContainer) {
			return ((IGoSourceFolder) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (input != null) {
			return getChildren(input);
		} else {
			return NO_CHILDREN;
		}
	}
	
	private GoExternalFolder createLibrary(IProject root, String libPath, String appendPath) {
		IPath path = Path.fromOSString(libPath).append(appendPath);
		try {
			IFileStore fs = EFS.getStore(path.toFile().toURI());
			return new GoExternalFolder(root, fs, libPath);
		} catch(CoreException e) {
			Activator.logError(e);
			return null;
		}	
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (this.viewer != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		}
		
		this.viewer = (TreeViewer) viewer;

		if (this.viewer != null) {
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		}

		input = newInput;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		final int eventType = event.getType();
		IResourceDelta delta = event.getDelta();
		if (delta != null){
			int kind = delta.getKind();
		
			if (eventType == IResourceChangeEvent.POST_CHANGE
					&& kind == IResourceDelta.CHANGED) {
				ArrayList<IResource> addedResources = new ArrayList<IResource>();
				findAddedResource(delta, addedResources);
				final ArrayList<IProject> goProjects = new ArrayList<IProject>();
				for (IResource addedResource : addedResources) {
					IProject project = addedResource.getProject();
					
					if (project.isOpen()) {
						try {
							if (!goProjects.contains(project) && project.hasNature(GoNature.NATURE_ID)) {
								goProjects.add(project);
							}
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
	
				if (!goProjects.isEmpty()) {
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							for (IProject project : goProjects) {
								viewer.refresh(project, true);
							}
						}
					});
				}
			}
		}
	}

	private void findAddedResource(IResourceDelta delta, ArrayList<IResource> addedFiles) {
		int kind = delta.getKind();
		IResource resource = delta.getResource();
		if (kind == IResourceDelta.ADDED && resource instanceof IResource
				&& !addedFiles.contains(resource)) {
			addedFiles.add(resource);
		}
		for (IResourceDelta child : delta.getAffectedChildren()) {
			findAddedResource(child, addedFiles);
		}
	}

	@SuppressWarnings("unused")
	private boolean flatLayout;
	private IPropertyChangeListener layoutPropertyChangeListener;
	private IExtensionStateModel stateModel;
	public static final String IS_LAYAOUT_FLAT_PROPERTY_NAME = "isLayoutFlat";

	@Override
	public void init(ICommonContentExtensionSite commonContentExtensionSite) {
		stateModel = commonContentExtensionSite.getExtensionStateModel();
		layoutPropertyChangeListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (IS_LAYAOUT_FLAT_PROPERTY_NAME.equals(event.getProperty())) {
					if (event.getNewValue() != null) {
						boolean newValue = ((Boolean) event.getNewValue())
								.booleanValue() ? true : false;
						flatLayout = newValue;
					}
				}
			}
		};
		
		stateModel.addPropertyChangeListener(layoutPropertyChangeListener);

	}

	@Override
	public void dispose() {
		stateModel.removePropertyChangeListener(layoutPropertyChangeListener);
	}

	@Override
	public void restoreState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

}
