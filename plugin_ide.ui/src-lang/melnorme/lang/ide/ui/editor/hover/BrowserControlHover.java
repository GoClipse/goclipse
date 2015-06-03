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
package melnorme.lang.ide.ui.editor.hover;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;


public abstract class BrowserControlHover extends AbstractLangEditorTextHover {
	
	public BrowserControlHover() {
		super();
	}
	
	@Override
	public final String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return getHoverInfo2(textViewer, hoverRegion);
	}
	
	@Override
	public abstract String getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion);
	
	/* -----------------  ----------------- */
	
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		return createHoverControlCreator(EditorsUI.getTooltipAffordanceString());
	}
	
	public static EnrichableBrowserControlCreator createHoverControlCreator(String tooltipAffordanceString) {
		return createHoverControlCreator(tooltipAffordanceString, new BrowserControlCreator());
	}
	
	public static EnrichableBrowserControlCreator createHoverControlCreator(String tooltipAffordanceString,
			IInformationControlCreator enrichedInformationControlCreator) {
		return new EnrichableBrowserControlCreator(enrichedInformationControlCreator, tooltipAffordanceString);
	}
	
	@SuppressWarnings("restriction")
	public static class BrowserControlCreator extends AbstractReusableInformationControlCreator {
		
		protected final String tooltipAffordanceString;
		
		public BrowserControlCreator() {
			this(null);
		}
		
		public BrowserControlCreator(String tooltipAffordanceString) {
			this.tooltipAffordanceString = tooltipAffordanceString;
		}
		
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			if(org.eclipse.jface.internal.text.html.BrowserInformationControl.isAvailable(parent)) {
				String font = JFaceResources.DIALOG_FONT;
				
				if(tooltipAffordanceString == null) {
					return new org.eclipse.jface.internal.text.html.BrowserInformationControl(parent, font, true);
				} else {
					return new org.eclipse.jface.internal.text.html.BrowserInformationControl(parent, font, 
						tooltipAffordanceString) {
						@Override
						public IInformationControlCreator getInformationPresenterControlCreator() {
							return getEnrichedInformationPresenterControlCreator();
						}
					};
				}
			} else {
				return new DefaultInformationControl(parent, true);
			}
		}
		
		public IInformationControlCreator getEnrichedInformationPresenterControlCreator() {
			return null;
		}
	}
	
	public static class EnrichableBrowserControlCreator extends BrowserControlCreator {
		
		protected final IInformationControlCreator enrichedInformationControlCreator;
		
		public EnrichableBrowserControlCreator(IInformationControlCreator enrichedInformationControlCreator,
				String tooltipAffordanceString) {
			super(tooltipAffordanceString);
			assertNotNull(tooltipAffordanceString);
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
				((IInformationControlExtension4) control).setStatusText(tooltipAffordanceString);
			}
			
			return true;
		}
		
	}
	
}