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
package melnorme.utilbox.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.function.Consumer;

import melnorme.utilbox.collections.Indexable;

/**
 * Helper for more complex toString printing.
 */
public class ToStringHelper {
	
	public interface ToString {
		
		@Override
		public String toString();
		
		default String defaultToString() {
			ToStringHelper sh = new ToStringHelper();
			toString(sh);
			return sh.getString();
		}
		
		void toString(ToStringHelper sh);
		
	}
	
	public static abstract class DefaultToString implements ToString {
		@Override
		public String toString() {
			return defaultToString();
		}
	}
	
	/* -----------------  ----------------- */	
	
	
	protected final String newline;
	protected final String indentationUnit = "  ";
	protected final StringBuilder sb;
	
	protected int indentation = 0;
	protected boolean afterNewline = false;
	protected boolean preventNextIndentation;
	
	public ToStringHelper(String newline, StringBuilder sb) {
		this.newline = newline;
		this.sb = sb;
	}
	
	public ToStringHelper() {
		this("\n", new StringBuilder());
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
	
	public String getString() {
		return sb.toString();
	}
	
	public String getIndentationUnit() {
		return indentationUnit;
	}
	
	public String getNewline() {
		return newline;
	}
	
	public void writeIndentation() {
		if(preventNextIndentation) {
			preventNextIndentation = false;
			return;
		}
		append(getIndentationString());
	}
	
	public String getIndentationString() {
		return StringUtil.newFilledString(indentation, getIndentationUnit());
	}
	
	public void append(String string) {
		this.afterNewline = false;
		sb.append(string);
	}
	
	public void writeNewline() {
		if(!afterNewline) {
			append(getNewline());
		}
		this.afterNewline = true;
	}
	
	/* -----------------  ----------------- */
	
	public void writeElement(Object element) {
		doWriteElement(element);
	}
	
	protected void doWriteElement(Object element) {
		if(element == null) {
			return;
		}
		
		innerWriteElem(element);
		writeNewline();
	}
	
	public void innerWriteElem(Object obj) {
		if(obj == null) {
			return;
		}
		if(obj instanceof ToString) {
			ToString toString = (ToString) obj;
			toString.toString(this);
		}
		else if(obj instanceof Iterable<?>) {
			Iterable<?> iterable = (Iterable<?>) obj;
			listToString("[", iterable, "]").toString(this);
		} 
		else {
			writeIndentation();
			sb.append(obj);
		}
	}
	
	public void writeElementWithPrefix(String prefix, Object element) {
		writeElementWithPrefix(prefix, element, false);
	}
	
	public void writeElementWithPrefix(String prefix, Object element, boolean ignoreNull) {
		if(ignoreNull && element == null) {
			return;
		}
		append(getIndentationString());
		append(prefix);
		
		if(element == null) {
			element = "null";
		}
		this.preventNextIndentation = true;
		doWriteElement(element);
	}
	
	/* -----------------  ----------------- */	
	
	public void writeList(Indexable<?> list) {
		writeList("[", list, "]");
	}
	
	public void writeList(String prefix, Indexable<?> indexable, String suffix) {
		if(indexable == null) {
			return;
		}
		
		listToString(prefix, indexable, suffix).toString(this);
	}
	
	public void writeBlock(String prefix, Consumer<ToStringHelper> blockWriter, String suffix) {
		assertNotNull(blockWriter);
		
		writeElement(prefix);
		
		indentation++;
		blockWriter.accept(this);
		indentation--;
		
		//writeNewline();
		append(getIndentationString());
		append(suffix);
		writeNewline();
	}
	
	public ToString listToString(String prefix, Iterable<?> indexable, String suffix) {
		ToString listToString = new ToString() {
			@Override
			public void toString(ToStringHelper sh) {
				writeBlock(prefix, (sh2) -> {
					for (Object object : indexable) {
						sh.writeElement(object);
					}
				}, suffix);
			}
		};
		return listToString;
	}
	
}