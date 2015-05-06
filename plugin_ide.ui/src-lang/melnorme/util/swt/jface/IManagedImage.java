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
package melnorme.util.swt.jface;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * An image stored in an imageRegistry. 
 * Methods can return null.
 */
public interface IManagedImage {
	
	ImageRegistry getImageRegistry();
	
	ImageDescriptor getDescriptor();
	
	Image getImage();
	
	
	public static class NullManagedImage implements IManagedImage {
		
		public static final NullManagedImage INSTANCE = new NullManagedImage();
		
		@Override
		public ImageRegistry getImageRegistry() {
			return null;
		}
		
		@Override
		public ImageDescriptor getDescriptor() {
			return null;
		}
		
		@Override
		public Image getImage() {
			return null;
		}
		
	}
	
}