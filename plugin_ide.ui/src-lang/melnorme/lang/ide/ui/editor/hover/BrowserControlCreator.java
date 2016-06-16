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
package melnorme.lang.ide.ui.editor.hover;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.swt.widgets.Shell;

import melnorme.lang.tooling.utils.HTMLHelper;
import melnorme.utilbox.misc.MiscUtil;


@SuppressWarnings("restriction")
public class BrowserControlCreator extends AbstractReusableInformationControlCreator {
	
		protected final String statusFieldText;
		
		public BrowserControlCreator() {
			this(null);
		}
		
		public BrowserControlCreator(String statusFieldText) {
			this.statusFieldText = statusFieldText;
		}
		
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			if(!org.eclipse.jface.internal.text.html.BrowserInformationControl.isAvailable(parent)) {
				return new DefaultInformationControl(parent, true);
			} else {
				return doCreateBrowserInformationControl(parent, JFaceResources.DIALOG_FONT);
			}
		}
		
		protected IInformationControl doCreateBrowserInformationControl(Shell parent, String font) {
			if(statusFieldText == null) {
				return new org.eclipse.jface.internal.text.html.BrowserInformationControl(parent, font, true);
			} else {
				return new org.eclipse.jface.internal.text.html.BrowserInformationControl(parent, font, 
					statusFieldText) {
					@Override
					public IInformationControlCreator getInformationPresenterControlCreator() {
						return getEnrichedInformationPresenterControlCreator();
					}
				};
			}
		}
		
		public IInformationControlCreator getEnrichedInformationPresenterControlCreator() {
			return null;
		}
	
	public static class EnrichableBrowserControlCreator extends BrowserControlCreator {
		
		protected final IInformationControlCreator enrichedInformationControlCreator;
		
		public EnrichableBrowserControlCreator(IInformationControlCreator enrichedInformationControlCreator,
				String statusFieldText) {
			super(statusFieldText);
			assertNotNull(statusFieldText);
			this.enrichedInformationControlCreator = enrichedInformationControlCreator;
		}
		
		@Override
		public IInformationControlCreator getEnrichedInformationPresenterControlCreator() {
			return enrichedInformationControlCreator;
		}
		
		@Override
		public boolean canReuse(IInformationControl control) {
			if(!super.canReuse(control))
				return false;
			
			if(control instanceof IInformationControlExtension4) {
				((IInformationControlExtension4) control).setStatusText(statusFieldText);
			}
			
			return true;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public static String DEFAULT_CSS = MiscUtil.getClassResource(BrowserControlCreator.class, "defaultHoverStyle.css"); 
	
	public static String wrapHTMLBody(String content) {
		return new HTMLHelper().wrapHTMLBody(content, DEFAULT_CSS);
	}
	
}