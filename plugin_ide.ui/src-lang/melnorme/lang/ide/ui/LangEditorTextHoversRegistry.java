/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui;

import java.util.ArrayList;
import java.util.List;

import _org.eclipse.jdt.internal.ui.text.java.hover.AnnotationHover;
import _org.eclipse.jdt.internal.ui.text.java.hover.ProblemHover;
import melnorme.lang.ide.ui.editor.hover.ILangEditorTextHover;

/**
 * Registry for text hovers.
 */
public class LangEditorTextHoversRegistry {
	
	private static List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications = new ArrayList<>();
	
	static {
		textHoverSpecifications.add(ProblemHover.class);
		LangUIPlugin_Actual.initTextHovers_afterProblemHover(textHoverSpecifications);
		textHoverSpecifications.add(AnnotationHover.class);
	}
	
	public synchronized static List<Class<? extends ILangEditorTextHover<?>>> getTextHoversSpecifications() {
		return new ArrayList<>(textHoverSpecifications);
	}
	
	public synchronized static void addTextHoverSpecToBeggining(Class<? extends ILangEditorTextHover<?>> hoverKlass) {
		textHoverSpecifications.add(0, hoverKlass);
	}
	
	public synchronized static void addTextHoverSpec(int ix, Class<? extends ILangEditorTextHover<?>> hoverKlass) {
		textHoverSpecifications.add(ix, hoverKlass);
	}
	
	public synchronized static boolean removeTextHoverSpec(Class<? extends ILangEditorTextHover<?>> hoverKlass) {
		return textHoverSpecifications.remove(hoverKlass);
	}
	
}