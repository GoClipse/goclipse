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
package melnorme.lang.ide.core_text;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

/**
 * A strict damager-repairer ensures that a damage region for a document event always starts 
 * at partition start (or before that)
 */
public class StrictDamagerRepairer extends DefaultDamagerRepairer {
	
	public StrictDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
	}
	
	@Override
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, 
			boolean documentPartitioningChanged) {
		IRegion region = super.getDamageRegion(partition, e, documentPartitioningChanged);
		
		int start = region.getOffset();
		int end = region.getOffset() + region.getLength();
		start = Math.min(start, partition.getOffset());
		
		return new Region(start, end - start);
	}
	
}