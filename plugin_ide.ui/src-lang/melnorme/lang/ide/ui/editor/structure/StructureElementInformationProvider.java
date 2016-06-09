/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.structure;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension;

import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.tooling.structure.SourceFileStructure;

public class StructureElementInformationProvider 
	implements IInformationProvider, IInformationProviderExtension 
{
	
	protected final ISourceBufferExt sourceBuffer;
	
	public StructureElementInformationProvider(ISourceBufferExt sourceBuffer) {
		this.sourceBuffer = assertNotNull(sourceBuffer);
	}
	
	@Override
	public IRegion getSubject(ITextViewer textViewer, int offset) {
		return new Region(offset, 0);
	}
	
	@Override
	public String getInformation(ITextViewer textViewer, IRegion subject) {
		return null;
	}
	
	@Override
	public SourceFileStructure getInformation2(ITextViewer textViewer, IRegion subject) {
		return GetUpdatedStructureUIOperation.run_GetUpdatedStructure_UserOperation(sourceBuffer);
	}
	
}