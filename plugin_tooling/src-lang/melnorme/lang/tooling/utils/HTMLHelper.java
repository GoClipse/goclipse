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

import melnorme.utilbox.misc.MiscUtil;

public class HTMLHelper {
	
	public static String DEFAULT_CSS = MiscUtil.getClassResource(HTMLHelper.class, "defaultHoverStyle.css"); 
	
	public static String escapeToToHTML(String string) {
		String content = string;
		content = content.replace("&", "&amp;");
		content = content.replace("\"", "&quot;");
		content = content.replace("<", "&lt;");
		content = content.replace(">", "&gt;");
		content = content.replace("\n", "<br/>");
		return content;
	}
	
	public String wrapHTMLBody(String content) {
		return wrapHTMLBody(content, DEFAULT_CSS);
	}
	
	public String wrapHTMLBody(String content, String styleSheet) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		appendStyleSheet(sb, styleSheet);
		sb.append("<body>");
		sb.append(content);
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	
	public void appendStyleSheet(StringBuilder sb, String styleSheet) {
		if(styleSheet == null)
			return;
		
		sb.append("<head><style type=\"text/css\">");
		sb.append(styleSheet);
		sb.append("</style></head>");
	}
	
	public static void getHTMLColor(StringBuilder buffer, int red, int green, int blue) {
		buffer.append('#');
		buffer.append(toHexString(red));
		buffer.append(toHexString(green));
		buffer.append(toHexString(blue));
	}
	
	public static String toHexString(int value) {
		String hexString = Integer.toHexString(value);
		if(hexString.length() == 1) {
			return "0" + hexString;
		}
		return hexString;
	}
	
}