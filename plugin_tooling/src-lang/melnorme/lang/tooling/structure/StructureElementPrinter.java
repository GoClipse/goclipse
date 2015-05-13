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
package melnorme.lang.tooling.structure;

import melnorme.utilbox.misc.StringUtil;

public class StructureElementPrinter extends StructureElementVisitor {
	
	protected StringBuilder sb = new StringBuilder();
	
	public StructureElementPrinter() {
	}
	
	public String printElement(StructureElement element) {
		level = -1;
		visitTree(element);
		String baseString = sb.toString();
		return baseString.substring(0, baseString.length()-1); // Remove last newline
	}
	
	@Override
	protected boolean visitNode(StructureElement element) {
		sb.append(getIndent());
		if(element instanceof StructureElement) {
			StructureElement ss = (StructureElement) element;
			sb.append(ss.toStringNode());
			sb.append("\n");
			return true;
		}
		sb.append(element);
		sb.append("\n");
		return false;
	}
	
	protected String getIndent() {
		return StringUtil.newFilledString(level, "  ");
	}
	
}