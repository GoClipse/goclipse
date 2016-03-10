/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.navigator;

import static melnorme.lang.ide.ui.views.StylerHelpers.fgColor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.model.IWorkbenchAdapter;

import _org.eclipse.jdt.ui.ProblemsLabelDecorator;
import melnorme.lang.ide.core.project_model.view.BundleErrorElement;
import melnorme.lang.ide.core.project_model.view.BundleModelElementKind.BundleModelElementsSwitcher;
import melnorme.lang.ide.core.project_model.view.DependenciesContainer;
import melnorme.lang.ide.core.project_model.view.RawDependencyElement;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.navigator.NavigatorElementsSwitcher;
import melnorme.lang.ide.ui.views.AbstractLangLabelProvider;
import melnorme.util.swt.jface.resources.CompositeImageDescriptorExt.Corner;
import melnorme.util.swt.jface.resources.DecoratedImageDescriptor;
import melnorme.util.swt.jface.resources.ImageDescriptorRegistry;
import melnorme.utilbox.collections.ArrayList2;


public abstract class LangNavigatorLabelProvider extends AbstractLangLabelProvider {
	
	protected final ArrayList2<ILabelDecorator> labelDecorators = new ArrayList2<>();
	protected final ImageDescriptorRegistry registry;

	public LangNavigatorLabelProvider() {
		super();
		this.registry = init_getImageRegistry();
		ProblemsLabelDecorator problemsLabelDecorator = new ProblemsLabelDecorator(registry);
		problemsLabelDecorator.addListener(new ILabelProviderListener() {
			@Override
			public void labelProviderChanged(LabelProviderChangedEvent event) {
				LangNavigatorLabelProvider.this.fireLabelProviderChanged(event);
			}
		});
		this.labelDecorators.add(problemsLabelDecorator);
	}
	
	protected ImageDescriptorRegistry init_getImageRegistry() {
		return LangUIPlugin.getDefault().getImageDescriptorRegistry();
	}
	
	@Override
	public void dispose() {
		for(ILabelDecorator labelDecorator : labelDecorators) {
			labelDecorator.dispose();
		}
	}
	
	/* ----------------- text ----------------- */
	
	@Override
	public StyledString getStyledText(Object element) {
		return getStyledString_switcher().switchElement(element);
	}
	
	protected abstract DefaultGetStyledStringSwitcher getStyledString_switcher();
	
	public static abstract class DefaultGetStyledStringSwitcher implements NavigatorElementsSwitcher<StyledString> {
		
		@Override
		public StyledString visitProject(IProject project) {
			return null;
		}
		
		@Override
		public StyledString visitManifestFile(IFile element) {
			return null;
		}
		
		@Override
		public StyledString visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return new StyledString(buildTargetsElement.getText());
		}
		
		@Override
		public StyledString visitBuildTarget(BuildTargetElement buildTarget) {
			return new StyledString(buildTarget.getTargetDisplayName());
		}
		
	}
	
	public abstract class BundleModelGetStyledStringSwitcher
		implements BundleModelElementsSwitcher<StyledString> {
		
		@Override
		public StyledString visitErrorElement2(BundleErrorElement element) {
			return new StyledString(element.errorDescription);
		}
		
		@Override
		public StyledString visitDepContainer(DependenciesContainer element) {
			return new StyledString("Dependencies");
		}
		
		@Override
		public StyledString visitRawDepElement(RawDependencyElement element) {
			StyledString baseString = new StyledString(element.getElementName());
			appendBundleRef(baseString, null, element.getDependencyRef().getVersion());
			return baseString;
		}
		
	}
	
	protected RGB ANNOTATION_FG = new RGB(120, 120, 200);
	
	protected StyledString appendBundleRef(StyledString baseString, String bundleName, String bundleVersion) {
		String bundleNameString = bundleName == null ? "" :  bundleName + " ";
		String versionString = bundleVersion == null ? "?" : bundleVersion;
		String bundleRefString = " [" + bundleNameString + versionString + "]";
		return baseString.append(bundleRefString, fgColor(ANNOTATION_FG));
	}
	
	/* ----------------- image ----------------- */
	
	@Override
	public Image getImage(Object element) {
		Image baseImage = getBaseImage(element);
		return decorateImage(baseImage, element);
	}
	
	public Image getBaseImage(Object element) {
		ImageDescriptor baseImage = getBaseImage_switcher().switchElement(element);
		if(baseImage != null) {
			return registry.get(baseImage);
		}
		
		if(element instanceof IResource) {
			IResource resource = (IResource) element;
			return registry.get(getWorkbenchImageDescriptor(resource));
		}
		return null;
	}
	
	protected abstract DefaultGetImageSwitcher getBaseImage_switcher();
	
	public static abstract class DefaultGetImageSwitcher implements NavigatorElementsSwitcher<ImageDescriptor> {
		
		@Override
		public ImageDescriptor visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return LangImages.BUILD_TARGETS_ELEM.getDescriptor();
		}
		
		@Override
		public ImageDescriptor visitBuildTarget(BuildTargetElement buildTarget) {
			ImageDescriptor baseImage = LangImages.BUILD_TARGET.getDescriptor();
			if(buildTarget.getBuildTarget().isEnabled()) {
				return new DecoratedImageDescriptor(baseImage, LangImages.OVR_CHECKED, Corner.BOTTOM_RIGHT);
			}
			return baseImage;
		}
		
		@Override
		public ImageDescriptor visitManifestFile(IFile element) {
			return null;
		}
		
	}
	
	public static abstract class BundleModelGetImageSwitcher
			implements BundleModelElementsSwitcher<ImageDescriptor> {
		
		@Override
		public ImageDescriptor visitDepContainer(DependenciesContainer element) {
			return LangImages.NAV_Library;
		}
		
		@Override
		public ImageDescriptor visitRawDepElement(RawDependencyElement element) {
			return LangImages.NAV_Package;
		}
		
		@Override
		public ImageDescriptor visitErrorElement2(BundleErrorElement element) {
			return LangImages.NAV_Error;
		}
		
	}
	
	protected ImageDescriptor getWorkbenchImageDescriptor(IAdaptable adaptable) {
		IWorkbenchAdapter workbenchAdapter= (IWorkbenchAdapter) adaptable.getAdapter(IWorkbenchAdapter.class);
		if (workbenchAdapter == null) {
			return null;
		}
		ImageDescriptor descriptor = workbenchAdapter.getImageDescriptor(adaptable);
		
		return descriptor;
	}
	
	protected Image decorateImage(Image image, Object element) {
		for(ILabelDecorator labelDecorator : labelDecorators) {
			image = labelDecorator.decorateImage(image, element);
		}
		return image;
	}
	
}