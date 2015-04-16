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
package melnorme.lang.ide.ui;


/* package */ interface LangUIMessages_Default {
	
	String Op_OpenDefinition_Name = "Open Definition";
	
	
	
	String ContentAssist_Timeout = 
			"Timeout invoking content assist.";
	String ContentAssist_Cancelled = 
			"Cancelled.";
	
	String ContentAssistProcessor_opName = "Content Assist";
	
	String ContentAssistProcessor_emptyDefaultProposals = "No proposals";
	String ContentAssistProcessor_empty_message = "No {0}";
	
	String ContentAssistProcessor_toggle_affordance_press_gesture = "Press ''{2}'' to show {1}";
	String ContentAssistProcessor_toggle_affordance_click_gesture = "Click to show {1}";
	
	
	String ContentAssistProcessor_defaultProposalCategory = "Default Proposals";
	String ContentAssistProcessor_snippetsProposalCategory = "Snippet Proposals";
	
	
	String ContentAssist_additionalInfo_affordance = "Press 'Tab' from proposal table or click for focus";
	String SourceHover_additionalInfo_affordance = ContentAssist_additionalInfo_affordance;
	
}