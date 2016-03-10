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
package melnorme.lang.ide.ui.editor;

import static melnorme.lang.ide.core.utils.ResourceUtils.getWorkspaceRoot;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.ui.texteditor.MarkerUtilities;

import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.LifecycleObject;

public class ExternalBreakpointWatcher extends LifecycleObject {
	
	protected static final String CBREAKPOINT_MARKER_TYPE = "org.eclipse.cdt.debug.core.cLineBreakpointMarker";
	// ICLineBreakpoint2.REQUESTED_SOURCE_HANDLE
	protected static final String ATTRIBUTE_SOURCE_HANDLE = "requestedSourceHandle";
	
	protected final Location location;
	protected final IDocument document;
	protected final IAnnotationModel annotationModel;
	protected final IAnnotationModelExtension annotationModelExt;
	
	public ExternalBreakpointWatcher(IEditorInput input, IDocument document, IAnnotationModel annotationModel) {
		this.location = EditorUtils.getLocationOrNull(input);
		
		this.document = assertNotNull(document);
		this.annotationModel = assertNotNull(annotationModel);
		
		if(annotationModel instanceof IAnnotationModelExtension) {
			this.annotationModelExt = (IAnnotationModelExtension) annotationModel;
		} else {
			this.annotationModelExt = null;
		}
		
		if(location != null) {
			ResourceUtils.connectResourceListener(resourceListener, this::initializeFromResources, 
				getWorkspaceRoot(), owned);
		}
	}
	
	protected void initializeFromResources() throws CoreException {
		IMarker[] findMarkers = getWorkspaceRoot().findMarkers(CBREAKPOINT_MARKER_TYPE, true, 0);
		for (IMarker marker : findMarkers) {
			addMarkerAnnotation(marker);
		}
	}
	
	protected final IResourceChangeListener resourceListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			IResourceDelta delta = event.getDelta();
			if(delta == null) {
				return;
			}
			
			IResource res = delta.getResource();
			if(res instanceof IWorkspaceRoot && (delta.getFlags() & IResourceDelta.MARKERS) != 0) {
				
				IMarkerDelta[] markerDeltas = delta.getMarkerDeltas();
				for (IMarkerDelta markerDelta : markerDeltas) {
					int kind = markerDelta.getKind();
					
					if(markerDelta.isSubtypeOf(CBREAKPOINT_MARKER_TYPE)) {
						
						if (kind == IResourceDelta.ADDED) {
							addMarkerAnnotation(markerDelta.getMarker());
						}
						
						if (kind == IResourceDelta.REMOVED) {
							removeMarkerAnnotation(markerDelta.getMarker());
						}
						
						if (kind == IResourceDelta.CHANGED) {
							updateMarkerAnnotation(markerDelta.getMarker());
						}
						
					}
				}
			}
		}
	};
	
	protected void addMarkerAnnotation(IMarker marker) {
		try {
			String sourceHandle = marker.getAttribute(ATTRIBUTE_SOURCE_HANDLE, null);
			if(sourceHandle == null) {
				return;
			}
			Location markerLocation = Location.create(sourceHandle);
			if(!markerLocation.equals(location)) {
				return;
			}
		} catch(CommonException e) {
			return;
		}
		
		int markerOffset;
		try {
			markerOffset = getMarkerPosition(marker);
		} catch(BadLocationException e) {
			return;
		}
		
		Position position = new Position(markerOffset, 0);
		Annotation annotation = new MarkerAnnotation(marker);
		
		annotationModel.addAnnotation(annotation, position);
	}
	
	protected int getMarkerPosition(IMarker marker) throws BadLocationException {
		int line = MarkerUtilities.getLineNumber(marker);
		if (line > 0) {
			return document.getLineOffset(line - 1);
		}
		throw new BadLocationException();
	}
	
	protected void removeMarkerAnnotation(IMarker marker) {
		
		Iterator<Annotation> iter = annotationModel.getAnnotationIterator();
		
		for (Annotation ann : (Iterable<Annotation>) () -> iter) {
			if(ann instanceof MarkerAnnotation) {
				MarkerAnnotation markerAnnotation = (MarkerAnnotation) ann;
				if(markerAnnotation.getMarker().equals(marker)) {
					annotationModel.removeAnnotation(markerAnnotation);
					return;
				}
			}
		}
	}
	
	protected void updateMarkerAnnotation(IMarker marker) {
		
		Iterator<Annotation> iter = annotationModel.getAnnotationIterator();
		
		for (Annotation ann : (Iterable<Annotation>) () -> iter) {
			if(ann instanceof MarkerAnnotation) {
				MarkerAnnotation markerAnnotation = (MarkerAnnotation) ann;
				if(markerAnnotation.getMarker().equals(marker)) {
					
					Position position = annotationModel.getPosition(markerAnnotation);
					
					// Trigger a model update.
					annotationModelExt.modifyAnnotationPosition(markerAnnotation, position);
					
					return;
				}
			}
		}
	}
	
}