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
package melnorme.lang.tooling;

import melnorme.lang.tooling.structure.StructureElementKind;

public class ElementLabelInfo {
	
	public final StructureElementKind kind;
	public final CompletionProposalKind proposalKind;
	public final String type;
	public final ElementAttributes elementAttribs;
	
	public ElementLabelInfo(StructureElementKind kind, CompletionProposalKind proposalKind, String type,
			ElementAttributes elementAttribs) {
		this.kind = kind;
		this.proposalKind = proposalKind;
		this.type = type;
		this.elementAttribs = elementAttribs;
	}
	
}