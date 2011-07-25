package com.googlecode.goclipse.debug.sourceLookup;

import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;

/**
 * 
 * @author devoncarew
 */
public class GoSourceLookupDirector extends AbstractSourceLookupDirector {

	public GoSourceLookupDirector() {

	}

	@Override
	public void initializeParticipants() {
		addParticipants(new ISourceLookupParticipant[] {
			new GoSourceLookupParticipant()
		});
	}

}
