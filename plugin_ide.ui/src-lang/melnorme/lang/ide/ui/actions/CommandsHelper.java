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
package melnorme.lang.ide.ui.actions;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;

public abstract class CommandsHelper {
	
	public CommandsHelper() {
	}
	
	public static CommandContributionItem pushItem(IServiceLocator svLocator, String commandId) {
		return new CommandContributionItem(contribItemParameter(svLocator, commandId));
	}
	
	public static CommandContributionItem pushItem(IServiceLocator svLocator, String commandId, String contribId) {
		return new CommandContributionItem(contribItemParameter(svLocator, commandId, contribId));
	}
	
	public static CommandContributionItemParameter contribItemParameter(IServiceLocator svLocator, String commandId) {
		return new CommandContributionItemParameter(svLocator, 
			commandId, 
			commandId, 
			CommandContributionItem.STYLE_PUSH
		);
	}
	
	public static CommandContributionItemParameter contribItemParameter2(IServiceLocator svLocator, String commandId,
			ImageDescriptor icon) {
		CommandContributionItemParameter cip = new CommandContributionItemParameter(svLocator, 
			commandId, 
			commandId, 
			CommandContributionItem.STYLE_PUSH
		);
		cip.icon = icon;
		return cip;
	}
	
	public static CommandContributionItemParameter contribItemParameter(IServiceLocator svLocator, String commandId,
			String contribId) {
		return new CommandContributionItemParameter(svLocator, 
			contribId, 
			commandId, 
			CommandContributionItem.STYLE_PUSH
		);
	}
	
}