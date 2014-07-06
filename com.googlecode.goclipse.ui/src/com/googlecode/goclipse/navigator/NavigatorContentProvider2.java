package com.googlecode.goclipse.navigator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.go.lang.GoPathElement;
import com.googlecode.goclipse.go.lang.GoPathType;
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.ui.GoUIPlugin;

// TODO: this content provider is hard-coded to show files from GOROOT; we'll probably want this
// to have better knowledge of the GOROOT / GOPATH directories that are in use by the project.

/**
 * A CNF content provider that decorates the the standard resource content provider with a GOROOT node. This shows the
 * IFileStore files in the GOROOT/src directory.
 * 
 * @author devoncarew
 */
public class NavigatorContentProvider2 implements ITreeContentProvider, IPropertyChangeListener, IResourceChangeListener {
	private final Object[] NO_CHILDREN = new Object[0];

	private Viewer viewer;

	public NavigatorContentProvider2() {
		// TODO: we really want to listen for changes to the root directories referenced by the project.
		GoUIPlugin.getPrefStore().addPropertyChangeListener(this);
		GoUIPlugin.getCorePrefStore().addPropertyChangeListener(this);
		GoCore.getWorkspace().addResourceChangeListener(this);
	}

	@Override
	public void dispose() {
		GoUIPlugin.getPrefStore().removePropertyChangeListener(this);
		GoUIPlugin.getCorePrefStore().removePropertyChangeListener(this);
		GoCore.getWorkspace().removeResourceChangeListener(this);
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
			if (!isGoRootSet()) {
				return NO_CHILDREN;

			} else {
				List<GoPathElement> goPathList = new ArrayList<GoPathElement>();
				appendProjectGoPaths(goPathList, (IProject) parentElement);
				return goPathList.toArray();
			}
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

	private void appendProjectGoPaths(List<GoPathElement> goPathList, IProject project) {
		goPathList.add(new GoPathElement(GoPathType.Preference, GoConstants.GOROOT, getGoRootSrcPkgFolder()));
		File goRoorSrcPath = getGoRootSrcFolder();
		String[] gopaths = Environment.INSTANCE.getOSEnvGoPath(project);
		this.addProjectGoPath(GoPathType.OS_Env, gopaths, goPathList, project, goRoorSrcPath);
		gopaths = Environment.INSTANCE.getPrefGoPath(project);
		this.addProjectGoPath(GoPathType.Preference, gopaths, goPathList, project, goRoorSrcPath);

		addProjectGoPath(goPathList, project);
	}

	/**
	 * append GoPath to the list and remove extra goRoot setting
	 * 
	 * @param osEnv
	 * @param gopaths
	 * @param goPathList
	 * @param project
	 * @param goRoorSrcPath
	 */
	private void addProjectGoPath(GoPathType type, String[] gopaths, List<GoPathElement> goPathList, IProject project,
			File goRoorSrcPath) {
		if (gopaths != null && gopaths.length > 0) {
			for (int i = 0; i < gopaths.length; i++) {
				File srcFolder = Path.fromOSString(gopaths[i]).append("src").toFile();

				if (!srcFolder.exists()) {
					try {
						srcFolder.mkdirs();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				boolean isGoRootSrc = srcFolder.getAbsolutePath().equalsIgnoreCase(goRoorSrcPath.getAbsolutePath());
				if (!isGoRootSrc) {
					GoPathElement ele = new GoPathElement(type, srcFolder.getParent(), srcFolder);
					goPathList.add(ele);
				}
			}
		}
	}

	private void addProjectGoPath(List<GoPathElement> goPathList, IProject cproject) {
		try {
			List<IProject> projects = Environment.INSTANCE.getProjectDependencies(cproject);
			for (IProject project : projects) {
				File srcFolder = Path.fromOSString(project.getLocation().toString()).append("src").toFile();
				GoPathElement ele = new GoPathElement(GoPathType.Project, project.getLocation().toOSString(), srcFolder);
				goPathList.add(ele);
			}
		} catch (Exception e) {

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

	private boolean isGoRootSet() {
		String goRoot = GoCore.getPreferences().getString(PreferenceConstants.GOROOT);

		return !"".equals(goRoot);
	}

	protected File getGoRootSrcPkgFolder() {
		String goRoot = GoCore.getPreferences().getString(PreferenceConstants.GOROOT);

		File srcFolder = Path.fromOSString(goRoot).append("src/pkg").toFile();

		return srcFolder;
	}

	protected File getGoRootSrcFolder() {
		String goRoot = GoCore.getPreferences().getString(PreferenceConstants.GOROOT);
		File srcFolder = Path.fromOSString(goRoot).append("src").toFile();
		return srcFolder;
	}

	/**
	 * @return File representing the external GOPATH
	 */
	protected File[] getGoPathSrcFolder(IProject project) {

		try {

			String[] goPath = Environment.INSTANCE.getGoPath(project);

			if ("".equals(goPath[0])) {
				return null;
			}

			File[] files = new File[goPath.length];

			for (int i = 0; i < goPath.length; i++) {
				String path = goPath[i];
				File srcFolder = Path.fromOSString(path).append("src").toFile();

				if (!srcFolder.exists()) {

					try {
						srcFolder.mkdirs();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				files[i] = srcFolder;

			}

			return files;

		} catch (Exception e) {
			return null;
		}
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

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if(IResourceChangeEvent.POST_CHANGE == event.getType()){
			updateViewer();
		}
	}


}
