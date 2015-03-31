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
package org.eclipse.jdt.internal.ui.text.java;

public interface JavaTextMessages {

	String ContentAssistProcessor_computing_proposals = 
			"Computing completion proposals";
	String ContentAssistProcessor_collecting_proposals = 
			"Collecting proposals";
	String ContentAssistProcessor_sorting_proposals = 
			"Sorting";
	
	String ContentAssistProcessor_computing_contexts = 
			"Computing context information";
	String ContentAssistProcessor_collecting_contexts = 
			"Collecting context information";
	String ContentAssistProcessor_sorting_contexts = 
			"Sorting";
	
	String ContentAssistProcessor_empty_message = "No {0}";
	String ContentAssistProcessor_toggle_affordance_update_message = "{1} to show {2}";
	String ContentAssistProcessor_defaultProposalCategory = "Default Proposals";
	String ContentAssistProcessor_toggle_affordance_press_gesture = "Press ''{0}''";
	String ContentAssistProcessor_toggle_affordance_click_gesture = "Click";
	String ContentAssistProcessor_all_disabled_title = "Content Assist Problem";
	String ContentAssistProcessor_all_disabled_message = 
			"Some content proposal kinds have been uninstalled. It is recommended to review the content assist settings.";
	String ContentAssistProcessor_all_disabled_preference_link = 
			"Change the settings on the <a>Advanced Content Assist preference page</a> or click ''{0}'' to restore the default behavior.";
	
}
