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
 * An action to run the gofix command.
 */
public class GofixAction extends TransformTextAction {
	
	public GofixAction() {
		super("Gofix");
	}

	@Override
	protected String transformText(final String text) throws CoreException {
		String goarch = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOARCH);
		
		String goos = Activator.getDefault().getPreferenceStore().getString(
				PreferenceConstants.GOOS);
		
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String gofixPath = preferenceStore.getString(PreferenceConstants.GOROOT) + "/pkg/tool/"+goos+"_"+goarch+"/fix";
		
		final ExternalCommand goFixCmd = new ExternalCommand(gofixPath);
		goFixCmd.setEnvironment(GoConstants.environment());

		goFixCmd.setInputFilter(new ProcessOStreamFilter() {
			@Override
			public void setStream(OutputStream outputStream) {
				OutputStreamWriter osw = new OutputStreamWriter(
						outputStream);
				try {
					osw.append(text);
					osw.flush();
					outputStream.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		});

		StreamAsString output = new StreamAsString();
		goFixCmd.setResultsFilter(output);

		String result = goFixCmd.execute(new ArrayList<String>(), true);
		
		if (result != null) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, result));
		} else {
			String transformedText = output.getString();
			
			if (transformedText.length() > 0 && !transformedText.equals(text)) {
				return transformedText;
			} else {
				return null;
			}
		}
	}

}
