package com.googlecode.goclipse.core.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.builder.MarkerUtilities;
import com.googlecode.goclipse.builder.StreamAsLines;
import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.go.lang.lexer.Lexer;
import com.googlecode.goclipse.go.lang.lexer.Tokenizer;
import com.googlecode.goclipse.go.lang.model.Import;
import com.googlecode.goclipse.go.lang.parser.ImportParser;
import com.googlecode.goclipse.tooling.GoCommandConstants;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

// BM: This class was extracted from GoCompiler
public class GoGetOperation {

	protected final IProject project;
	protected final GoEnvironment goEnv;
	
	public GoGetOperation(IProject project) {
		this.project = project;
		this.goEnv = GoProjectEnvironment.getGoEnvironment(project);
	}
	
	/**
	 * TODO this needs to be centralized into a common index...
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected List<Import> getImports(File file) throws IOException {
		Lexer 		  lexer        = new Lexer();
		Tokenizer 	  tokenizer    = new Tokenizer(lexer);
		ImportParser  importParser = new ImportParser(tokenizer, file);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = "";
		StringBuilder builder = new StringBuilder();
		while( (temp = reader.readLine()) != null ) {
			builder.append(temp);
			builder.append("\n");
		}
		
		reader.close();
		lexer.scan(builder.toString());
		List<Import> imports = importParser.getImports();
		
		return imports;
	}

	/**
	 * @param target
	 */
	public void goGetDependencies(IProgressMonitor monitor, java.io.File target) throws CoreException {
		
		final IPath projectLocation = project.getLocation();
		final String compilerPath   = GoEnvironmentPrefs.COMPILER_PATH.get();
		final IFile  file           = project.getFile(target.getAbsolutePath().replace(projectLocation.toOSString(), ""));
			
			/**
			 * TODO Allow the user to set the go get locations
			 * manually.
			 */
			
			List<Import> imports;
			try {
				imports = getImports(target);
			} catch (IOException ioe) {
				throw GoCore.createCoreException("Error during getImports", ioe);
			}
			List<String> cmd        = new ArrayList2<String>();
			List<Import> extImports = new ArrayList2<Import>();
			
			monitor.beginTask("Importing external libraries for "+file.getName()+":", 5);
			
			for (Import imp: imports) {
				if (imp.getName().startsWith("code.google.com") ||
					imp.getName().startsWith("github.com")      ||
					imp.getName().startsWith("bitbucket.org")   ||
					imp.getName().startsWith("launchpad.net")   ||
					imp.getName().contains(".git")              ||
					imp.getName().contains(".svn")              ||
					imp.getName().contains(".hg")               ||
					imp.getName().contains(".bzr")              ){
					
					cmd.add(imp.getName());
					extImports.add(imp);
				}
			}
			
			monitor.worked(1);
			
			//String[] cmd     = { compilerPath, GoConstants.GO_GET_COMMAND, "-u" };
			cmd.add(0, "-u");
			cmd.add(0, "-fix");
			cmd.add(0, GoCommandConstants.GO_GET_COMMAND);
			cmd.add(0, compilerPath);
			
			monitor.worked(3);
			
			ExternalProcessResult processResult = GoToolManager.getDefault().runBuildTool(goEnv, monitor, target.getParentFile(), 
				cmd);
			
			StreamAsLines sal = new StreamAsLines(processResult);
			
			boolean exMsg = true;
			
			try {
	            project.deleteMarkers(LangCore_Actual.BUILD_PROBLEM_ID, false, IResource.DEPTH_ZERO);
            } catch (CoreException e1) {
	            Activator.logError(e1);
            }
			
			MarkerUtilities.deleteFileMarkers(file);
			if (sal.getLines().size() > 0) {
				
				for (String line : sal.getLines()) {
					if (line.startsWith("package")) {
						String impt = line.substring(0,line.indexOf(" -"));
						impt = impt.replaceFirst("package ", "");
						for (Import i:extImports) {
							if (i.path.equals(impt)) {
								MarkerUtilities.addMarker(file, i.getLine(), line.substring(line.indexOf(" -")+2), IMarker.SEVERITY_ERROR);
							}
						}
						
					} else if (line.contains(".go:")) {
						try {
							String[] split 	    = line.split(":");
							String   path 	    = "GOPATH/"+split[0].substring(split[0].indexOf("/src/")+5);
							IFile    extfile    = project.getFile(path);
							int      lineNumber = Integer.parseInt(split[1]);
							String   msg 		= split[3];
														
							if(extfile!=null && extfile.exists()){
								MarkerUtilities.addMarker(extfile, lineNumber, msg, IMarker.SEVERITY_ERROR);
								
							} else if (exMsg) {
								exMsg = false;
								MarkerUtilities.addMarker(file, "There are problems with the external imports in this file.\n" +
										                        "You may want to attempt to resolve them outside of eclipse.\n" +
										                        "Here is the GOPATH to use: \n\t"+goEnv.getGoPathString());
							}
							
						} catch (Exception e){
							Activator.logError(e);
						}
					}
				}
			}
			
			monitor.worked(1);
			
	}
	
	
}
