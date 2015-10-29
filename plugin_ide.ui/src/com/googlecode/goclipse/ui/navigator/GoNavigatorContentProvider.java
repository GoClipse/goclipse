package com.googlecode.goclipse.ui.navigator;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Display;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoPath;
import com.googlecode.goclipse.tooling.env.GoRoot;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.navigator.elements.GoPathElement;
import com.googlecode.goclipse.ui.navigator.elements.GoPathEntryElement;
import com.googlecode.goclipse.ui.navigator.elements.GoRootElement;

import melnorme.lang.ide.ui.views.AbstractNavigatorContentProvider;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;

// TODO: this content provider is hard-coded to show files from GOROOT; we'll probably want this
// to have better knowledge of the GOROOT / GOPATH directories that are in use by the project.

/**
 * A CNF content provider that decorates the the standard resource content provider with a GOROOT
 * node. This shows the IFileStore files in the GOROOT/src directory.
 * 
 * @author devoncarew
 */
public class GoNavigatorContentProvider extends AbstractNavigatorContentProvider {
	
	protected final Object[] NO_CHILDREN = new Object[0];
	
	public GoNavigatorContentProvider() {
		// TODO: we really want to listen for changes to the root directories referenced by the project.
		GoUIPlugin.getPrefStore().addPropertyChangeListener(propListener);
		GoUIPlugin.getCorePrefStore().addPropertyChangeListener(propListener);
	}
	
	@Override
	public void dispose() {
		GoUIPlugin.getPrefStore().removePropertyChangeListener(propListener);
		GoUIPlugin.getCorePrefStore().removePropertyChangeListener(propListener);
		
		super.dispose();
	}
	
	protected final IPropertyChangeListener propListener = (pce) -> { updateViewer(); };
	
	protected void updateViewer() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if(viewer != null) {
					viewer.refresh();
				}
			}
		});
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected LangNavigatorSwitcher_HasChildren hasChildren_switcher() {
		return new LangNavigatorSwitcher_HasChildren() {
			
			@Override
			public Boolean visitGoPathElement(GoPathElement goPathElement) {
				return true;
			}
			
			@Override
			public Boolean visitFileStoreElement(IFileStore fileStore) {
				return getChildren(fileStore).length > 0;
			}
			
		};
	}
	
	@Override
	protected LangNavigatorSwitcher_GetChildren getChildren_switcher() {
		return new LangNavigatorSwitcher_GetChildren() {
			@Override
			public void addFirstProjectChildren(IProject project, ArrayList2<Object> projectChildren) {
				projectChildren.addElements(getProjectGoPathChildren(project));
			}
			
			@Override
			public Object[] visitGoPathElement(GoPathElement goPathElement) {
				try {
					IFileStore fileStore = EFS.getStore(goPathElement.getDirectory().toURI());
					return fileStore.childStores(EFS.NONE, null);
				} catch (CoreException exception) {
					return NO_CHILDREN;
				}
			}
			
			@Override
			public Object[] visitFileStoreElement(IFileStore fileStore) {
				try {
					return fileStore.childStores(EFS.NONE, null);
				} catch (CoreException e) {
					return NO_CHILDREN;
				}
			}
		};
	}
	
	@Override
	protected void addBuildTargetsContainer(IProject project, ArrayList2<Object> projectChildren) {
		super.addBuildTargetsContainer(project, projectChildren);
	}
	
	@Override
	protected LangNavigatorSwitcher_GetParent getParent_switcher() {
		return new LangNavigatorSwitcher_GetParent() {
			
			@Override
			public Object visitGoPathElement(GoPathElement goPathElement) {
				return null; // TODO: project parent
			}
			
			@Override
			public Object visitFileStoreElement(IFileStore fileStore) {
				// TODO: trim this at the GOROOT directory
				return fileStore.getParent();
			}
		};
	}
	
	protected Object[] getProjectGoPathChildren(IProject project) {
		
		GoEnvironment goEnvironment = GoProjectEnvironment.getGoEnvironment(project);
		
		GoRoot goRoot = goEnvironment.getGoRoot();
		Location goRootSource;
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
			} catch (CommonException e) {
				continue; // TODO: create error element
			}
			if(areEqual(goPathEntryPath, project.getLocation().toFile().toPath())) {
				continue; // Don't add this entry.
			}
			
			try {
				buildpathChildren.add(new GoPathEntryElement(goPathEntryPath, project, effectiveGoPath));
			} catch (CommonException e) {
				// Don't add any entry.
			}
		}
		
		return buildpathChildren.toArray();
	}
	
}