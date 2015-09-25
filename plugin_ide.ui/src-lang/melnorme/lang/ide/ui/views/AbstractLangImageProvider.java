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
package melnorme.lang.ide.ui.views;

import melnorme.lang.ide.ui.LangElementImages;
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
		if(kind == null) {
			return null;
		}
		IManagedImage imageDescriptor = kind.switchOnKind(this);
		if(imageDescriptor == null) {
			return getDefaultImage();
		}
		return imageDescriptor;
	}
	
	public static IManagedImage getDefaultImage() {
		return NullManagedImage.INSTANCE;
	}
	
	@Override
	public IManagedImage visitUnknown() {
		return LangElementImages.UNKNOWN;
	}
	
	@Override
	public IManagedImage visitKeyword() {
		return NullManagedImage.INSTANCE;
	}
	
	@Override
	public IManagedImage visitError() {
		return LangElementImages.ERROR_ELEMENT;
	}
	
	@Override
	public IManagedImage visitAlias() {
		return LangElementImages.ALIAS_ELEMENT;
	}
	
	@Override
	public IManagedImage visitModule() {
		return LangElementImages.MODULE;
	}
	
	@Override
	public IManagedImage visitVariable() {
		return LangElementImages.VARIABLE;
	}
	
	@Override
	public IManagedImage visitFunction() {
		return LangElementImages.FUNCTION;
	}
	
	@Override
	public IManagedImage visitConstructor() {
		return LangElementImages.CONSTRUCTOR;
	}
	
	@Override
	public IManagedImage visitClass() {
		return LangElementImages.T_CLASS;
	}
	@Override
	public IManagedImage visitInterface() {
		return LangElementImages.T_INTERFACE;
	}
	@Override
	public IManagedImage visitStruct() {
		return LangElementImages.T_STRUCT;
	}
	@Override
	public IManagedImage visitEnum() {
		return LangElementImages.T_ENUM;
	}
	
	@Override
	public IManagedImage visitNative() {
		return LangElementImages.T_NATIVE;
	}
	
}