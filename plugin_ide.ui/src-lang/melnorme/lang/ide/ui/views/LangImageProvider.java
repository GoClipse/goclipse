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

import melnorme.lang.ide.ui.LangObjImages;
import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.CompletionProposalKind.ProposalKindVisitor;

import org.eclipse.jface.resource.ImageDescriptor;


public abstract class LangImageProvider extends ProposalKindVisitor<ImageDescriptor> {
	
	public ImageDescriptor getImageDescriptor(CompletionProposalKind kind) {
		ImageDescriptor imageDescriptor = switchOnKind(kind);
		if(imageDescriptor == null) {
			return getDefaultImage();
		}
		return imageDescriptor;
	}
	
	protected ImageDescriptor getDefaultImage() {
		return null;
	}
	
	@Override
	protected ImageDescriptor visitVariable() {
		return LangObjImages.VARIABLE.getDescriptor();
	}
	
	@Override
	protected ImageDescriptor visitFunction() {
		return LangObjImages.F_FUNCTION.getDescriptor();
	}
	
	@Override
	protected ImageDescriptor visitConstructor() {
		return LangObjImages.F_CONSTRUCTOR.getDescriptor();
	}
	
	@Override
	protected ImageDescriptor visitClass() {
		return LangObjImages.T_CLASS.getDescriptor();
	}
	@Override
	protected ImageDescriptor visitInterface() {
		return LangObjImages.T_INTERFACE.getDescriptor();
	}
	@Override
	protected ImageDescriptor visitStruct() {
		return LangObjImages.T_STRUCT.getDescriptor();
	}
	@Override
	protected ImageDescriptor visitEnum() {
		return LangObjImages.T_ENUM.getDescriptor();
	}
	
	@Override
	protected ImageDescriptor visitModule() {
		return LangObjImages.MODULE.getDescriptor();
	}
	
	@Override
	protected ImageDescriptor visitKeyword() {
		return null;
	}
	
	@Override
	protected ImageDescriptor visitUnknown() {
		return getDefaultImage();
	}

}