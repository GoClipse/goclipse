package com.googlecode.goclipse.ui.navigator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
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
		if (parentElement instanceof IWorkspaceRoot) {
			IWorkspaceRoot root = (IWorkspaceRoot) parentElement;
			return root.getProjects();
		} else if (parentElement instanceof IProject) {
			IFolder srcFolder = ((IProject)parentElement).getFolder("src");
			return new Object[] { new GoSourceFolder(srcFolder.getFolder("pkg")), new GoSourceFolder(srcFolder.getFolder("cmd")) };
		} else if (parentElement instanceof IGoSourceContainer) {
			IGoSourceContainer srcCont = (IGoSourceContainer) parentElement;
			GoPackage pkg = null;
			IGoSourceFolder sourceFolder = null;
			if (srcCont instanceof GoPackage) {
				pkg = (GoPackage) srcCont;
				sourceFolder = pkg.getGoSource();
			} else if (srcCont instanceof IGoSourceFolder) {
				pkg = null;
				sourceFolder = (IGoSourceFolder) srcCont;
			}

			try {
				List<Object> result = new ArrayList<Object>();
				IFolder baseFolder = srcCont.getFolder();
				baseFolder.refreshLocal(3, new NullProgressMonitor());
				for (IResource res : baseFolder.members()) {
					if (res instanceof IFolder) {
						if (!res.getProjectRelativePath().lastSegment().startsWith("_")){
							IFolder folder = (IFolder) res;
							GoPackage new_package = new GoPackage(sourceFolder,
								pkg, folder);
							result.add(new_package);
						}
					} else {
						result.add(res);
					}
				}

				return result.toArray();
			} catch (CoreException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return NO_CHILDREN;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IGoSourceFolder) {
			return ((IGoSourceFolder) element).getProject();
		} else if (element instanceof GoPackage) {
			GoPackage pkg = (GoPackage) element;
			if (pkg.getParent() != null) {
				return pkg.getParent();
			} else {
				return pkg.getGoSource();
			}
		} else if (element instanceof IResource) {

			
			// FIXME ???? CHECK THE PROJECT ?????
			IResource resource = (IResource) element;
			IContainer parent = resource.getParent();
			if (parent != null && parent.getName().equals("src")) {
				GoSourceFolder sourceFolderResult = new GoSourceFolder(
						(IFolder) resource.getParent());
				
				return sourceFolderResult;
			}

			return parent;
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IProject) {
			return ((IProject) element).isAccessible();
		} else if (element instanceof IGoSourceContainer) {
			return true;
		}
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {

		
		if (input != null) {
			return getChildren(input);
		}

		return null;
	}

	@Override
	public void dispose() {
		stateModel.removePropertyChangeListener(layoutPropertyChangeListener);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;

		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);

		input = newInput;
		
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		final int eventType = event.getType();
		//IResource resource = event.getResource();
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
					try {
						if (!goProjects.contains(project)
								&& project.hasNature(GoNature.NATURE_ID)) {
							goProjects.add(project);
						}
					} catch (CoreException e) {
						e.printStackTrace();
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

	private void findAddedResource(IResourceDelta delta,
			ArrayList<IResource> addedFiles) {

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
	public void restoreState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveState(IMemento aMemento) {
		// TODO Auto-generated method stub

	}

}
