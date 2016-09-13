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

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;

import melnorme.lang.ide.core.text.format.ILastKeyInfoProvider;


public class VerifyKeyRecorder implements VerifyKeyListener, ILastKeyInfoProvider {
	
	protected KeyCommand lastKeyEvent;
	
	public VerifyKeyRecorder(ITextViewer viewer) {
		if(viewer instanceof ITextViewerExtension) {
			ITextViewerExtension viewerExtension = (ITextViewerExtension) viewer;
			viewerExtension.appendVerifyKeyListener(this);
			// Minor leak issue: we should remove verifyKeyRecorder if viewer is unconfigured
		} else {
			// Ignore
		}
		lastKeyEvent = KeyCommand.OTHER;
	}
	
	@Override
	public KeyCommand getLastPressedKey() {
		return lastKeyEvent;
	}
	
	@Override
	public void verifyKey(VerifyEvent event) {
		lastKeyEvent = toKeyCommand(event);
	}
	
	protected KeyCommand toKeyCommand(VerifyEvent event) {
		switch(event.character) {
		case SWT.BS: return KeyCommand.BACKSPACE; 
		case SWT.DEL: return KeyCommand.DELETE;
		case SWT.CR: return KeyCommand.ENTER;
		default: return KeyCommand.OTHER;
		}
	}

}