/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package _org.eclipse.jdt.internal.ui.text;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;

/**
 * A reconciling strategy consisting of a sequence of internal reconciling strategies.
 * By default, all requests are passed on to the contained strategies.
 *
 * @since 3.0
 */
public class CompositeReconcilingStrategy  implements IReconcilingStrategy, IReconcilingStrategyExtension {

	/** The list of internal reconciling strategies. */
	protected final IReconcilingStrategy[] fStrategies;

	/**
	 * Creates a new, empty composite reconciling strategy.
	 */
	public CompositeReconcilingStrategy(IReconcilingStrategy... strategies) {
		fStrategies= strategies;
	}

	/**
	 * Returns the previously set stratgies or <code>null</code>.
	 *
	 * @return the contained strategies or <code>null</code>
	 */
	public IReconcilingStrategy[] getReconcilingStrategies() {
		return fStrategies;
	}

	@Override
	public void setDocument(IDocument document) {
		if (fStrategies == null)
			return;

		for (int i= 0; i < fStrategies.length; i++)
			fStrategies[i].setDocument(document);
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		if (fStrategies == null)
			return;

		for (int i= 0; i < fStrategies.length; i++)
			fStrategies[i].reconcile(dirtyRegion, subRegion);
	}

	@Override
	public void reconcile(IRegion partition) {
		if (fStrategies == null)
			return;

		for (int i= 0; i < fStrategies.length; i++)
			fStrategies[i].reconcile(partition);
	}

	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
		if (fStrategies == null)
			return;

		for (int i=0; i < fStrategies.length; i++) {
			if (fStrategies[i] instanceof IReconcilingStrategyExtension) {
				IReconcilingStrategyExtension extension= (IReconcilingStrategyExtension) fStrategies[i];
				extension.setProgressMonitor(monitor);
			}
		}
	}

	@Override
	public void initialReconcile() {
		if (fStrategies == null)
			return;

		for (int i=0; i < fStrategies.length; i++) {
			if (fStrategies[i] instanceof IReconcilingStrategyExtension) {
				IReconcilingStrategyExtension extension= (IReconcilingStrategyExtension) fStrategies[i];
				extension.initialReconcile();
			}
		}
	}
}
