/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text;

import melnorme.lang.ide.ui.editor.hover.AbstractDocDisplayInfoSupplier;
import melnorme.lang.tooling.LANG_SPECIFIC;
import melnorme.lang.tooling.common.ISourceBuffer;
import melnorme.utilbox.core.CommonException;

@LANG_SPECIFIC
public class DocDisplayInfoSupplier extends AbstractDocDisplayInfoSupplier {
	
	public DocDisplayInfoSupplier(ISourceBuffer sourceBuffer, int offset) {
		super(sourceBuffer, offset);
	}
	
	@Override
	protected OpenDocumentationOperation getOpenDocumentationOperation(ISourceBuffer sourceBuffer, int offset) {
		// TODO: LANG DocDisplayInfoSupplier
		return new OpenDocumentationOperation("Get Documentation", (om) -> {  
			throw new CommonException("NOT IMPLEMENTED"); });
	}
	
}