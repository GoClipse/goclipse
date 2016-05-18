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
package melnorme.lang.tooling.structure;

import melnorme.lang.tooling.common.ParserError;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.Location;

public class SourceFileStructure extends SourceFileStructure_Default {
	
	public SourceFileStructure(Location location, Indexable<StructureElement> children,
			Indexable<ParserError> parserProblems) {
		super(location, children, parserProblems);
	}
	
}
