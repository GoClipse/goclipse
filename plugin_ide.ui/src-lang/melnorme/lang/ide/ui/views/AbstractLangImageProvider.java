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
import melnorme.lang.tooling.structure.StructureElementKind.StructureElementKindVisitor;
import melnorme.util.swt.jface.IManagedImage;
import melnorme.util.swt.jface.IManagedImage.NullManagedImage;


public abstract class AbstractLangImageProvider implements 
	ProposalKindVisitor<IManagedImage>, 
	StructureElementKindVisitor<IManagedImage> 
{
	
	public IManagedImage getImageDescriptor(CompletionProposalKind kind) {
		IManagedImage imageDescriptor = kind.switchOnKind(this);
		if(imageDescriptor == null) {
			return getDefaultImage();
		}
		return imageDescriptor;
	}
	
	public IManagedImage getDefaultImage() {
		return new NullManagedImage();
	}
	
	@Override
	public IManagedImage visitVariable() {
		return LangObjImages.VARIABLE;
	}
	
	@Override
	public IManagedImage visitFunction() {
		return LangObjImages.F_FUNCTION;
	}
	
	@Override
	public IManagedImage visitConstructor() {
		return LangObjImages.F_CONSTRUCTOR;
	}
	
	@Override
	public IManagedImage visitClass() {
		return LangObjImages.T_CLASS;
	}
	@Override
	public IManagedImage visitInterface() {
		return LangObjImages.T_INTERFACE;
	}
	@Override
	public IManagedImage visitStruct() {
		return LangObjImages.T_STRUCT;
	}
	@Override
	public IManagedImage visitEnum() {
		return LangObjImages.T_ENUM;
	}
	
	@Override
	public IManagedImage visitModule() {
		return LangObjImages.MODULE;
	}
	
	@Override
	public IManagedImage visitKeyword() {
		return new NullManagedImage();
	}
	
	@Override
	public IManagedImage visitUnknown() {
		return getDefaultImage();
	}
	
}