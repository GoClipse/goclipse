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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

import melnorme.lang.ide.core.text.DocumentCommand2;
import melnorme.lang.ide.core.text.IAutoEditStrategy2;

public class AutoEditStrategyAdapter implements IAutoEditStrategy {
	
	protected final IAutoEditStrategy2 actualStrategy;

	public AutoEditStrategyAdapter(IAutoEditStrategy2 actualStrategy) {
		super();
		this.actualStrategy = assertNotNull(actualStrategy);
	}
	
	@Override
	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		DocumentCommand2 command2 = new DocumentCommand2(
			command.doit, command.offset, command.length, command.text, command.caretOffset, command.shiftsCaret
		);
		
		actualStrategy.customizeDocumentCommand(document, command2);
		
		command.doit = command2.doit; 
		command.offset = command2.offset;
		command.length = command2.length;
		command.text = command2.text;
		command.caretOffset = command2.caretOffset; 
		command.shiftsCaret = command2.shiftsCaret;
	}
	
}
