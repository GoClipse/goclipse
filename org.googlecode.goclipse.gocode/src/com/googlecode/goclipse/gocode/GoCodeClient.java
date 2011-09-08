package com.googlecode.goclipse.gocode;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.builder.ProcessOStreamFilter;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * 
 */
public class GoCodeClient {

	private String error;
	private String path;
	private String goarch; 
	private String goos;   
	private String goroot; 
	private String exeName;
	
	public GoCodeClient() {}
	
	/**
	 * 
	 */
	public void buildGoCodePath(){
		goarch  = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOARCH);		
		goos    = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOOS);		
		goroot  = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOROOT);		
		exeName = "gocode" + Environment.INSTANCE.getExecutableExtension();
		path    = Path.fromOSString(goroot).append("bin").append(exeName).toOSString();
	}
	
	/**
	 * 
	 * @return
	 */
	public ExternalCommand buildGoCodeCommand(){
		if(path==null){
			buildGoCodePath();
		}
		
		ExternalCommand goCodeCommand;
		if(com.googlecode.goclipse.gocode.Activator.getDefault().getGoCodePath() == null){
			goCodeCommand = new ExternalCommand(path);
			goCodeCommand.setWorkingFolder(Path.fromOSString(goroot).append("bin").toOSString());
		} else {
			goCodeCommand = new ExternalCommand(com.googlecode.goclipse.gocode.Activator.getDefault().getGoCodePath());
			goCodeCommand.setWorkingFolder(com.googlecode.goclipse.gocode.Activator.getDefault().getGoCodeDir());
		}
		
		return goCodeCommand;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param bufferText
	 * @param offset
	 * @return
	 */
	public List<String> getCompletions(String fileName, final String bufferText, int offset) {
		
		ExternalCommand goCodeCommand = buildGoCodeCommand();

		if (!goCodeCommand.commandExists()) {
			// Caveat: environment variables aren't always available. We only use GOBIN if GOROOT/bin
			// doesn't contain the gocode command.
			String goBinPath = System.getenv("GOBIN");
			
			if (goBinPath != null) {
				ExternalCommand command = new ExternalCommand(Path.fromOSString(goBinPath).append(exeName).toOSString());				
				goCodeCommand.setWorkingFolder(Path.fromOSString(goBinPath).toOSString());
				
				if (command.commandExists()) {
					goCodeCommand = command;
				}
			}
		}
		
		goCodeCommand.setTimeout(100);
		
		// set the package path for the current project
		List<String> parameters = new LinkedList<String>();
		parameters.add("set");
		parameters.add("lib-path");
				
		String pkgPath = 
			Environment.INSTANCE.getAbsoluteProjectPath()+
			"/"+Environment.INSTANCE.getPkgOutputFolder().toOSString();
		
		String rootPath = goroot+"/pkg/"+goos+"_"+goarch;
		
		File pkgDir = new File(goroot+"/pkg/");
		if(pkgDir.isDirectory() && pkgDir.listFiles().length>0 ){
		    rootPath = pkgDir.listFiles()[0].getAbsolutePath();
		}
		
		// remove drive letters
		if(rootPath.contains(":")) {
		    rootPath = rootPath.replaceFirst("[A-Z]:", "");
		}
		
		if(pkgPath.contains(":")) {
		    pkgPath = pkgPath.replaceFirst("[A-Z]:", "");
        }
		
		parameters.add( (rootPath+":"+pkgPath).replace("\\", "/") );
		goCodeCommand.execute(parameters);
				
		ExternalCommand command = buildGoCodeCommand();
		
		StreamAsLines output = new StreamAsLines();
		command.setResultsFilter(output);
		command.setInputFilter(new ProcessOStreamFilter() {
			@Override
			public void setStream(OutputStream outputStream) {
				OutputStreamWriter osw = new OutputStreamWriter(outputStream);
				try {
					osw.append(bufferText);
					osw.flush();
					outputStream.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		});

		parameters = new LinkedList<String>();
//		parameters.add("-sock=tcp");
		parameters.add("-f=csv");
		parameters.add("autocomplete");
		parameters.add(fileName);
		parameters.add(""+offset);
		error = command.execute(parameters, true);
		if (error != null) {
			String out = output.getLinesAsString();
			
			Activator.getDefault().getLog().log(
					new Status(Status.ERROR, Activator.PLUGIN_ID, out == null ? error : error + ": " + out));
		}
		
		return output.getLines();
	}

	public String getError() {
		return error;
	}

}
