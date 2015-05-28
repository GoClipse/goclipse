/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package _org.eclipse.jdt.internal.ui.javaeditor;

import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.IMarker;

import org.eclipse.jface.text.source.Annotation;

import org.eclipse.ui.texteditor.MarkerAnnotation;


/**
 * Filters problems based on their types.
 */
public class JavaAnnotationIterator implements Iterator<Annotation> {

	private Iterator<Annotation> fIterator;
	private Annotation fNext;
	private boolean fReturnAllAnnotations;


	/**
	 * Returns a new JavaAnnotationIterator.
	 * @param parent the parent iterator to iterate over annotations
	 * @param returnAllAnnotations whether to return all annotations or just problem annotations
	 */
	public JavaAnnotationIterator(Iterator<Annotation> parent, boolean returnAllAnnotations) {
		fReturnAllAnnotations= returnAllAnnotations;
		fIterator= parent;
		skip();
	}

	private void skip() {
		while (fIterator.hasNext()) {
			Annotation next= fIterator.next();

			if (next.isMarkedDeleted())
				continue;

			if (fReturnAllAnnotations || /*next instanceof IJavaAnnotation ||*/ isProblemMarkerAnnotation(next)) {
				fNext= next;
				return;
			}
		}
		fNext= null;
	}

	private static boolean isProblemMarkerAnnotation(Annotation annotation) {
		if (!(annotation instanceof MarkerAnnotation))
			return false;
		try {
			return(((MarkerAnnotation)annotation).getMarker().isSubtypeOf(IMarker.PROBLEM));
		} catch (CoreException e) {
			return false;
		}
	}

	@Override
	public boolean hasNext() {
		return fNext != null;
	}

	@Override
	public Annotation next() {
		try {
			return fNext;
		} finally {
			skip();
		}
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
