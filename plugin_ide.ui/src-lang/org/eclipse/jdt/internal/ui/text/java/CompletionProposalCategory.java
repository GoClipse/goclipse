/*******************************************************************************
 * Copyright (c) 2005, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Paul Fullbright <paul.fullbright@oracle.com> - content assist category enablement - http://bugs.eclipse.org/345213
 *     Marcel Bruch <bruch@cs.tu-darmstadt.de> - [content assist] Allow to re-sort proposals - https://bugs.eclipse.org/bugs/show_bug.cgi?id=350991
 *     Lars Vogel  <lars.vogel@gmail.com> - convert to foreach loop - https://bugs.eclipse.org/bugs/show_bug.cgi?id=406478
 *******************************************************************************/
package org.eclipse.jdt.internal.ui.text.java;

import java.util.ArrayList;
import java.util.List;

import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.action.LegacyActionTools;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
//import org.eclipse.core.expressions.EvaluationContext;
//import org.eclipse.core.expressions.EvaluationResult;
//import org.eclipse.core.expressions.Expression;
//import org.eclipse.core.expressions.ExpressionConverter;
//import org.eclipse.core.expressions.ExpressionTagNames;
//import org.eclipse.jdt.core.IJavaProject;


/**
 * Describes a category extension to the "javaCompletionProposalComputer" extension point.
 *
 * @since 3.2
 */
public final class CompletionProposalCategory {
//	/** The extension schema name of the icon attribute. */
//	private static final String ICON= "icon"; //$NON-NLS-1$

	private final String fId;
	private final String fName;
//	private final IConfigurationElement fElement;
	/** The image descriptor for this category, or <code>null</code> if none specified. */
	private final ImageDescriptor fImage;
	
//	/** The enablement expression for this category, or <code>null</code> if none specified. */
//	private final Expression fEnablementExpression;

	private boolean fIsSeparateCommand= true;
	private boolean fIsEnabled= true;
	private boolean fIsIncluded= true;
//	private final CompletionProposalComputerRegistry fRegistry;

	private int fSortOrder= 0xffff - 1;
	private String fLastError= null;

	/**
	 * Flag indicating whether any completion engine associated with this category requests
	 * resorting of its proposals after filtering is triggered. Filtering is, e.g., triggered when a
	 * user continues typing with an open completion window.
	 * 
	 * @since 3.8
	 */
	private boolean fNeedsSortingAfterFiltering;
	
	protected final List<CompletionProposalComputerDescriptor> computers;

//	CompletionProposalCategory(IConfigurationElement element, CompletionProposalComputerRegistry registry) throws CoreException {
//		fElement= element;
//		fRegistry= registry;
//		IExtension parent= (IExtension) element.getParent();
//		fId= parent.getUniqueIdentifier();
//		checkNotNull(fId, "id"); //$NON-NLS-1$
//		String name= parent.getLabel();
//		if (name == null)
//			fName= fId;
//		else
//			fName= name;
//		
//		IConfigurationElement[] children= fElement.getChildren(ExpressionTagNames.ENABLEMENT);
//		if (children.length == 1) {
//			ExpressionConverter parser= ExpressionConverter.getDefault();
//			fEnablementExpression = parser.perform(children[0]);
//		}
//		else {
//			fEnablementExpression = null;
//		}
//		
//		String icon= element.getAttribute(ICON);
//		ImageDescriptor img= null;
//		if (icon != null) {
//			Bundle bundle= getBundle();
//			if (bundle != null) {
//				Path path= new Path(icon);
//				URL url= FileLocator.find(bundle, path, null);
//				img= ImageDescriptor.createFromURL(url);
//			}
//		}
//		fImage= img;
//
//	}

	CompletionProposalCategory(String id, String name, ArrayList2<CompletionProposalComputerDescriptor> computers) {
//		fRegistry= registry;
		fId= id;
		fName= name;
		this.computers = computers;
//		fElement= null;
//		fEnablementExpression = null;
		fImage= null;
	}
	
	/**
	 * Returns the identifier of the described extension.
	 */
	public String getId() {
		return fId;
	}
	
	public String getName() {
		return fName;
	}
	
	protected List<CompletionProposalComputerDescriptor> getComputers() {
		return computers;
	}
	
	@SuppressWarnings("unused")
	protected List<CompletionProposalComputerDescriptor> getComputers(String partition) {
		return getComputers();
	}
	
	/**
	 * @return the error message from the computers in this category
	 */
	public String getErrorMessage() {
		return fLastError;
	}
	
//	private Bundle getBundle() {
//		String namespace= fElement.getDeclaringExtension().getContributor().getName();
//		Bundle bundle= Platform.getBundle(namespace);
//		return bundle;
//	}

//	/**
//	 * Checks that the given attribute value is not <code>null</code>.
//	 *
//	 * @param value the element to be checked
//	 * @param attribute the attribute
//	 * @throws CoreException if <code>value</code> is <code>null</code>
//	 */
//	private void checkNotNull(Object value, String attribute) throws CoreException {
//		if (value == null) {
//			Object[] args= { getId(), fElement.getContributor().getName(), attribute };
//			String message= Messages.format(
//				JavaTextMessages.CompletionProposalComputerDescriptor_illegal_attribute_message, args);
//			IStatus status= new Status(IStatus.WARNING, JavaPlugin.getPluginId(), IStatus.OK, message, null);
//			throw new CoreException(status);
//		}
//	}

//	/**
//	 * Returns the enablement element of the described extension.
//	 * 
//	 * @return the enablement expression or <code>null</code> if it is not specified
//	 * @since 3.8.1
//	 */
//	Expression getEnablementExpression() {
//		return fEnablementExpression;
//	}

	/**
	 * Returns the name of the described extension
	 * without mnemonic hint in order to be displayed
	 * in a message.
	 *
	 * @return Returns the name
	 */
	public String getDisplayName() {
		return LegacyActionTools.removeMnemonics(fName);
	}

	/**
	 * Returns the image descriptor of the described category.
	 *
	 * @return the image descriptor of the described category
	 */
	public ImageDescriptor getImageDescriptor() {
		return fImage;
	}

	/**
	 * Sets the separate command state of the category.
	 *
	 * @param enabled the new enabled state.
	 */
	public void setSeparateCommand(boolean enabled) {
		fIsSeparateCommand= enabled;
	}

	/**
	 * @return the enablement state of the category
	 */
	public boolean isSeparateCommand() {
		return fIsSeparateCommand;
	}

	/**
	 * @param included the included
	 */
	public void setIncluded(boolean included) {
		fIsIncluded= included;
	}

	/**
	 * @return included
	 */
	public boolean isIncluded() {
		return fIsIncluded;
	}

	public boolean isEnabled() {
		return fIsEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		fIsEnabled= isEnabled;
	}

//	/**
//	 * Returns <code>true</code> if the category contains any computers, <code>false</code>
//	 * otherwise.
//	 *
//	 * @return <code>true</code> if the category contains any computers, <code>false</code>
//	 *         otherwise
//	 */
//	public boolean hasComputers() {
//		List<CompletionProposalComputerDescriptor> descriptors= fRegistry.getProposalComputerDescriptors();
//		for (CompletionProposalComputerDescriptor desc : descriptors) {
//			if (desc.getCategory() == this)
//				return true;
//		}
//		return false;
//	}
//
//	/**
//	 * Returns <code>true</code> if the category contains any computers in the given partition, <code>false</code>
//	 * otherwise.
//	 *
//	 * @param partition the partition
//	 * @return <code>true</code> if the category contains any computers, <code>false</code>
//	 *         otherwise
//	 */
//	public boolean hasComputers(String partition) {
//		List<CompletionProposalComputerDescriptor> descriptors= fRegistry.getProposalComputerDescriptors(partition);
//		for (CompletionProposalComputerDescriptor desc : descriptors) {
//			if (desc.getCategory() == this)
//				return true;
//		}
//		return false;
//	}

	/**
	 * @return sortOrder
	 */
	public int getSortOrder() {
		return fSortOrder;
	}

	/**
	 * @param sortOrder the sortOrder
	 */
	public void setSortOrder(int sortOrder) {
		fSortOrder= sortOrder;
	}
	
//	/**
//	 * Determines if the project matches any enablement expression defined on the extension.
//	 * 
//	 * @param javaProject the Java project against which to test the enablement expression, can be
//	 *            <code>null</code>
//	 * @return <code>true</code> if any enablement expression matches the given project or if the
//	 *         project is <code>null</code> or no enablement expression is specified,
//	 *         <code>false</code> otherwise
//	 * @since 3.8
//	 */
//	public boolean matches(IJavaProject javaProject) {
//		if (fEnablementExpression == null) {
//			return true;
//		}
//		
//		if (javaProject == null) {
//			return false;
//		}
//		
//		try {
//			EvaluationContext evalContext= new EvaluationContext(null, javaProject);
//			evalContext.addVariable("project", javaProject); //$NON-NLS-1$
//			return fEnablementExpression.evaluate(evalContext) == EvaluationResult.TRUE;
//		} catch (CoreException e) {
//			JavaPlugin.log(e);
//		}
//		
//		return false;
//	}

	/**
	 * Safely computes completion proposals of all computers of this category through their
	 * extension. If an extension is disabled, throws an exception or otherwise does not adhere to
	 * the contract described in {@link IJavaCompletionProposalComputer}, it is disabled.
	 *
	 * @param context the invocation context passed on to the extension
	 * @param partition the partition type where to invocation occurred
	 * @param monitor the progress monitor passed on to the extension
	 * @return the list of computed completion proposals (element type:
	 *         {@link org.eclipse.jface.text.contentassist.ICompletionProposal})
	 */
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, 
		String partition, SubProgressMonitor monitor) {
		fLastError= null;
		List<ICompletionProposal> result= new ArrayList<ICompletionProposal>();
		List<CompletionProposalComputerDescriptor> descriptors= getComputers(partition);
		for (CompletionProposalComputerDescriptor desc : descriptors) {
			result.addAll(desc.computeCompletionProposals(context, monitor));
			
			if (fLastError == null && desc.getErrorMessage() != null)
				fLastError= desc.getErrorMessage();
		}
		return result;
	}
	
	/**
	 * Safely computes context information objects of all computers of this category through their
	 * extension. If an extension is disabled, throws an exception or otherwise does not adhere to
	 * the contract described in {@link IJavaCompletionProposalComputer}, it is disabled.
	 *
	 * @param context the invocation context passed on to the extension
	 * @param partition the partition type where to invocation occurred
	 * @param monitor the progress monitor passed on to the extension
	 * @return the list of computed context information objects (element type:
	 *         {@link org.eclipse.jface.text.contentassist.IContextInformation})
	 */
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, 
		String partition, SubProgressMonitor monitor) {
		fLastError= null;
		List<IContextInformation> result= new ArrayList<IContextInformation>();
		List<CompletionProposalComputerDescriptor> descriptors= getComputers(partition);
		for (CompletionProposalComputerDescriptor desc : descriptors) {
			if((isIncluded() || isSeparateCommand()))
				result.addAll(desc.computeContextInformation(context, monitor));
			if (fLastError == null)
				fLastError= desc.getErrorMessage();
		}
		return result;
	}

	/**
	 * Notifies the computers in this category of a proposal computation session start.
	 */
	public void sessionStarted() {
		List<CompletionProposalComputerDescriptor> descriptors= getComputers();
		for (CompletionProposalComputerDescriptor desc : descriptors) {
			desc.sessionStarted();
			fNeedsSortingAfterFiltering= fNeedsSortingAfterFiltering || desc.isSortingAfterFilteringNeeded();
			if (fLastError == null)
				fLastError= desc.getErrorMessage();
		}
	}

	/**
	 * Notifies the computers in this category of a proposal computation session end.
	 */
	public void sessionEnded() {
		fNeedsSortingAfterFiltering= false;
		List<CompletionProposalComputerDescriptor> descriptors= getComputers();
		for (CompletionProposalComputerDescriptor desc : descriptors) {
			desc.sessionEnded();
			if (fLastError == null)
				fLastError= desc.getErrorMessage();
		}
	}

	/**
	 * Returns whether any completion proposal computer associated with this category requires
	 * proposals to be sorted again after filtering.
	 * 
	 * @return <code>true</code> if any completion proposal computer in this category requires
	 *         proposals to be sorted.
	 */
	public boolean isSortingAfterFilteringNeeded() {
		return fNeedsSortingAfterFiltering;
	}
}
