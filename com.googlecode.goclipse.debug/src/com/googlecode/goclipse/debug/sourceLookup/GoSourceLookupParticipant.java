package com.googlecode.goclipse.debug.sourceLookup;

import com.googlecode.goclipse.debug.model.GoDebugStackFrame;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;

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
