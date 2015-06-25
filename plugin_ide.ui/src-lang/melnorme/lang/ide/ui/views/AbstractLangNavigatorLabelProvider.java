/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.IWorkbenchAdapter;

import _org.eclipse.jdt.ui.ProblemsLabelDecorator;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.navigator.BuildTargetElement;
import melnorme.lang.ide.ui.navigator.BuildTargetsContainer;
import melnorme.lang.ide.ui.navigator.NavigatorElementsSwitcher;
import melnorme.util.swt.jface.resources.ImageDescriptorRegistry;
import melnorme.utilbox.collections.ArrayList2;



public abstract class AbstractLangNavigatorLabelProvider extends AbstractLangLabelProvider {
	
	protected final ArrayList2<ILabelDecorator> labelDecorators = new ArrayList2<>();
	protected final ImageDescriptorRegistry registry;

	public AbstractLangNavigatorLabelProvider() {
		super();
		this.registry = init_getImageRegistry();
		this.labelDecorators.add(new ProblemsLabelDecorator(registry));
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
		return getStyledText_switcher().switchElement(element);
	}
	
	protected abstract DefaultGetStyledTextSwitcher getStyledText_switcher();
	
	public static abstract class DefaultGetStyledTextSwitcher implements NavigatorElementsSwitcher<StyledString> {
		
		@Override
		public StyledString visitProject(IProject project) {
			return null;
		}
		
		@Override
		public StyledString visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return new StyledString(buildTargetsElement.getText());
		}
		
		@Override
		public StyledString visitBuildTarget(BuildTargetElement buildTarget) {
			return new StyledString(buildTarget.getTargetName());
		}
		
	}
	
	/* ----------------- image ----------------- */
	
	@Override
	public Image getImage(Object element) {
		Image baseImage = getBaseImage(element);
		return decorateImage(baseImage, element);
	}
	
	public Image getBaseImage(Object element) {
		Image baseImage = getBaseImage_switcher().switchElement(element);
		if(baseImage != null) {
			return baseImage;
		}
		
		if(element instanceof IResource) {
			IResource resource = (IResource) element;
			return registry.get(getWorkbenchImageDescriptor(resource));
		}
		return null;
	}
	
	protected abstract DefaultGetImageSwitcher getBaseImage_switcher();
	
	public static abstract class DefaultGetImageSwitcher implements NavigatorElementsSwitcher<Image> {
		
		@Override
		public Image visitProject(IProject project) {
			return null;
		}
		
		@Override
		public Image visitBuildTargetsElement(BuildTargetsContainer buildTargetsElement) {
			return LangImages.BUILD_TARGETS_ELEM.getImage();
		}
		
		@Override
		public Image visitBuildTarget(BuildTargetElement buildTarget) {
			return LangImages.BUILD_TARGET.getImage();
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