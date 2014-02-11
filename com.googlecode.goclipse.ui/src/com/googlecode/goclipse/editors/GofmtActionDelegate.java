package com.googlecode.goclipse.editors;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;

import com.googlecode.goclipse.Activator;
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
public class GofmtActionDelegate extends TransformTextAction {
	
    public GofmtActionDelegate() {
    	super("gofmt");
	}

	@Override
	protected String transformText(final String text) throws CoreException {
		final String currentContent = text;

		IPreferenceStore preferenceStore = Activator.getDefault()
				.getPreferenceStore();
		String gofmtPath = preferenceStore
				.getString(PreferenceConstants.FORMATTER_PATH);
		final ExternalCommand gofmtCmd = new ExternalCommand(gofmtPath);
		gofmtCmd.setEnvironment(GoConstants.environment());

		gofmtCmd.setInputFilter(new ProcessOStreamFilter() {
			@Override
			public void setStream(OutputStream outputStream) {
				try {
	        OutputStreamWriter osw = new OutputStreamWriter(outputStream, "UTF-8");
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
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error running gofmt: " + result));
		} else {
			String formattedText = output.getString();
			
			if (!formattedText.equals(currentContent)) {
				return formattedText;
			} else {
				return null;
			}
		}
	}

}
