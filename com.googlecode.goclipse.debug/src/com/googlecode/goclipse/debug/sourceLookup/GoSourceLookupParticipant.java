package com.googlecode.goclipse.debug.sourceLookup;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;

import com.googlecode.goclipse.debug.model.GoDebugStackFrame;

/**
 * 
 * @author devoncarew
 */
public class GoSourceLookupParticipant extends AbstractSourceLookupParticipant {

	public GoSourceLookupParticipant() {

	}

	@Override
	public String getSourceName(Object object) throws CoreException {
		if (object instanceof GoDebugStackFrame) {
			return ((GoDebugStackFrame) object).getSourceName();
		}

		return null;
	}

}
