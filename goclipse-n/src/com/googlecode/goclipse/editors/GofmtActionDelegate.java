package com.googlecode.goclipse.editors;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.builder.ProcessOStreamFilter;
import com.googlecode.goclipse.builder.StreamAsString;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * 
 * @author steel
 *
 */
public class GofmtActionDelegate implements IEditorActionDelegate {
	
	private IEditorPart editorPart;

    public GofmtActionDelegate() {
	}

	@Override
	public void run(IAction action) {
		GoEditor editor;
		if (editorPart instanceof GoEditor) {
			editor = (GoEditor)editorPart;
		} else {
			editor = null;
		}
		if (editor != null) {
			Object obj = editor.getAdapter(Control.class);
			if (obj != null && obj instanceof StyledText) {
				StyledText st = (StyledText)obj;
				final String currentContent = st.getText();

				IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
				String gofmtPath = preferenceStore.getString(PreferenceConstants.FORMATTER_PATH);
				final ExternalCommand gofmtCmd = new ExternalCommand(gofmtPath);
				gofmtCmd.setEnvironment(GoConstants.environment());

				gofmtCmd.setInputFilter(new ProcessOStreamFilter() {
					@Override
					public void setStream(OutputStream outputStream) {
						OutputStreamWriter osw = new OutputStreamWriter(outputStream);
						try {
							osw.append(currentContent);
							osw.flush();
							outputStream.close();
						} catch (IOException e) {
							// do nothing
						}
					}
				});

				StreamAsString output = new StreamAsString();
				gofmtCmd.setResultsFilter(output);
	
				String result = gofmtCmd.execute(new ArrayList<String>(), true);
				
				if (result != null) {
					SysUtils.displayError(editor.getSite().getShell(), "Error Running Gofmt", result);
				} else {
					String formattedText = output.getString();
					if (!formattedText.equals(currentContent)) {
						st.setText(formattedText);
					}
				}
			}			
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editorPart = targetEditor;
	}

}
