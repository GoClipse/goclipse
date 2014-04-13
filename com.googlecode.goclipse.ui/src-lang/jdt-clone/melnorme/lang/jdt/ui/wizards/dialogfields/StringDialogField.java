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
package melnorme.lang.jdt.ui.wizards.dialogfields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;

/**
 * Dialog field containing a label and a text control.
 */
public class StringDialogField extends DialogField {

	private String fText;
	private Text fTextControl;
	private ModifyListener fModifyListener;
    private IContentAssistProcessor fContentAssistProcessor;

	public StringDialogField() {
		super();
		fText= ""; //$NON-NLS-1$
	}

	
//	public void setContentAssistProcessor(IContentAssistProcessor processor) {
//	    fContentAssistProcessor= processor;
//	    if (fContentAssistProcessor != null && isOkToUse(fTextControl)) {
//	    	ControlContentAssistHelper.createTextContentAssistant(fTextControl, fContentAssistProcessor);
//	    }
//	}

	public IContentAssistProcessor getContentAssistProcessor() {
	    return fContentAssistProcessor;
	}

	// ------- layout helpers

	/*
	 * @see DialogField#doFillIntoGrid
	 */
	@Override
	public Control[] doFillIntoGrid(Composite parent, int nColumns) {
		assertEnoughColumns(nColumns);

		Label label= getLabelControl(parent);
		label.setLayoutData(gridDataForLabel(1));
		Text text= getTextControl(parent);
		text.setLayoutData(gridDataForText(nColumns - 1));

		return new Control[] { label, text };
	}

	/*
	 * @see DialogField#getNumberOfControls
	 */
	@Override
	public int getNumberOfControls() {
		return 2;
	}

	protected static GridData gridDataForText(int span) {
		GridData gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= span;
		return gd;
	}

	// ------- focus methods

	/*
	 * @see DialogField#setFocus
	 */
	@Override
	public boolean setFocus() {
		if (isOkToUse(fTextControl)) {
			fTextControl.setFocus();
			fTextControl.setSelection(0, fTextControl.getText().length());
		}
		return true;
	}

	// ------- ui creation

	/**
	 * Creates or returns the created text control.
	 * @param parent The parent composite or <code>null</code> when the widget has
	 * already been created.
	 * @return the text control
	 */
	public Text getTextControl(Composite parent) {
		if (fTextControl == null) {
			assertCompositeNotNull(parent);
			fModifyListener= new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					doModifyText();
				}
			};

			fTextControl= createTextControl(parent);
			// moved up due to 1GEUNW2
			fTextControl.setText(fText);
			fTextControl.setFont(parent.getFont());
			fTextControl.addModifyListener(fModifyListener);

			fTextControl.setEnabled(isEnabled());
			if (fContentAssistProcessor != null) {
//			    ControlContentAssistHelper.createTextContentAssistant(fTextControl, fContentAssistProcessor);
			}
		}
		return fTextControl;
	}

	/**
	 * Creates and returns a new text control.
	 * 
	 * @param parent the parent
	 * @return the text control
	 * @since 3.6
	 */
	protected Text createTextControl(Composite parent) {
		return new Text(parent, SWT.SINGLE | SWT.BORDER);
	}

	private void doModifyText() {
		if (isOkToUse(fTextControl)) {
			fText= fTextControl.getText();
		}
		dialogFieldChanged();
	}

	// ------ enable / disable management

	/*
	 * @see DialogField#updateEnableState
	 */
	@Override
	protected void updateEnableState() {
		super.updateEnableState();
		if (isOkToUse(fTextControl)) {
			fTextControl.setEnabled(isEnabled());
		}
	}

	// ------ text access

	/**
	 * @return the text, can not be <code>null</code>
	 */
	public String getText() {
		return fText;
	}

	/**
	 * Sets the text. Triggers a dialog-changed event.
	 * @param text the new text
	 */
	public void setText(String text) {
		fText= text;
		if (isOkToUse(fTextControl)) {
			fTextControl.setText(text);
		} else {
			dialogFieldChanged();
		}
	}

	/**
	 * Sets the text without triggering a dialog-changed event.
	 * @param text the new text
	 */
	public void setTextWithoutUpdate(String text) {
		fText= text;
		if (isOkToUse(fTextControl)) {
			fTextControl.removeModifyListener(fModifyListener);
			fTextControl.setText(text);
			fTextControl.addModifyListener(fModifyListener);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField#refresh()
	 */
	@Override
	public void refresh() {
		super.refresh();
		if (isOkToUse(fTextControl)) {
			setTextWithoutUpdate(fText);
		}
	}

}
