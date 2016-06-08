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
package melnorme.lang.tooling.utils;

public class HTMLEscapeUtil {
	
	public static String escapeToToHTML(String string) {
		String content = string;
		content = content.replace("&", "&amp;");
		content = content.replace("\"", "&quot;");
		content = content.replace("<", "&lt;");
		content = content.replace(">", "&gt;");
		content = content.replace("\n", "<br/>");
		return content;
	}
	
}