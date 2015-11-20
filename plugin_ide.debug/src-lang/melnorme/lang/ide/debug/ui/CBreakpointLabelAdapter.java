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
package melnorme.lang.ide.debug.ui;
import static melnorme.utilbox.core.CoreUtil.array;

import java.lang.reflect.Method;

import org.eclipse.cdt.debug.core.model.ICBreakpoint;
import org.eclipse.cdt.debug.core.model.ICBreakpointType;
import org.eclipse.cdt.debug.core.model.ICDynamicPrintf;
import org.eclipse.cdt.debug.core.model.ICEventBreakpoint;
import org.eclipse.cdt.debug.core.model.ICLineBreakpoint;
import org.eclipse.cdt.debug.core.model.ICTracepoint;
import org.eclipse.cdt.debug.core.model.ICWatchpoint;
import org.eclipse.cdt.debug.internal.ui.CDebugModelPresentation;
import org.eclipse.cdt.debug.internal.ui.OverlayImageDescriptor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangImages;
import melnorme.utilbox.misc.ReflectionUtils;


public class CBreakpointLabelAdapter implements IAdapterFactory {
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if(adaptableObject instanceof ICBreakpoint && adapterType == ILabelProvider.class) {
			return (T) new BreakpointLabelProvider();
		}
		return null;
	}
	
	@Override
	public Class<?>[] getAdapterList() {
		return array(ILabelProvider.class);
	}
	
	public static class BreakpointLabelProvider extends CDebugModelPresentation {
		
		protected static Method computeOverlaysMethod;
		
		static {
			try {
				computeOverlaysMethod = CDebugModelPresentation.class.getDeclaredMethod(
					"computeOverlays", ICBreakpoint.class);
				computeOverlaysMethod.setAccessible(true);
			} catch(Exception e) {
				computeOverlaysMethod = null;
			}
		}
		
		@Override
		public String getText(Object element) {
			return null;
		}
		
		@Override
		public Image getImage(Object element) {
			if(element instanceof ICBreakpoint) {
				return getBreakpointImage((ICBreakpoint) element);
			} else {
				return null;
			}
		}
		
		// Override superclass
		@Override
		protected Image getBreakpointImage(ICBreakpoint breakpoint) {
//			// if adapter installed for breakpoint, call the adapter
//			ILabelProvider adapter = Platform.getAdapterManager().getAdapter(breakpoint, ILabelProvider.class);
//			if (adapter!=null) { 
//				Image image = adapter.getImage(breakpoint);
//				if (image!=null) return image;
//			}
			try {
				// Check for ICTracepoint first because they are also ICLineBreakpoint
				if ( breakpoint instanceof ICTracepoint ) {
					return getTracepointImage( (ICTracepoint)breakpoint );
				}
				// Check for ICDynamicPrintf first because they are also ICLineBreakpoint
				if ( breakpoint instanceof ICDynamicPrintf ) {
					return getDynamicPrintfImage( (ICDynamicPrintf)breakpoint );
				}
				if ( breakpoint instanceof ICLineBreakpoint ) {
					// checks if the breakpoint type is a hardware breakpoint,
					// if so, return the hardware breakpoint image
					if( breakpoint instanceof ICBreakpointType) {
						ICBreakpointType breakpointType = (ICBreakpointType) breakpoint;
						if( (breakpointType.getType() & ICBreakpointType.HARDWARE) != 0)
							return getHWBreakpointImage( (ICLineBreakpoint) breakpoint);
					}
					return getLineBreakpointImage( (ICLineBreakpoint)breakpoint );
				}
				if ( breakpoint instanceof ICWatchpoint ) {
					return getWatchpointImage( (ICWatchpoint)breakpoint );
				}
				if ( breakpoint instanceof ICEventBreakpoint ) {
					return getEventBreakpointImage( (ICEventBreakpoint)breakpoint );
				}
				
			}
			catch( CoreException e ) {
			}
			return null;
		}
		
		@Override
		protected Image getLineBreakpointImage(ICLineBreakpoint breakpoint) throws CoreException {
			ImageDescriptor descriptor = null;
			if ( breakpoint.isEnabled() ) {
				descriptor = LangImages.BREAKPOINT_ENABLED;
			}
			else {
				descriptor = LangImages.BREAKPOINT_DISABLED;
			}
			if(computeOverlaysMethod == null) {
				return null;
			}
			
			try {
				
				ImageDescriptor[] computeOverlays = 
						ReflectionUtils.uncheckedInvoke(this, computeOverlaysMethod, breakpoint);
				
				OverlayImageDescriptor imageDescriptor = new OverlayImageDescriptor(
					fDebugImageRegistry.get(descriptor), computeOverlays);
				
				return LangImages.getManagedImage(imageDescriptor );
			} catch(RuntimeException e) {
				LangCore.logError("Error using reflection to bypass CDT private API. ", e);
				return null;
			}
		}
		
	}

}