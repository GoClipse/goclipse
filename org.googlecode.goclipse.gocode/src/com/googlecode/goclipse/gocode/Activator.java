package com.googlecode.goclipse.gocode;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.builder.ExternalCommand;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.googlecode.goclipse.gocode"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private GoCodeRunner goCodeRunner;
	private String 		 goCodePath;
	private String 		 goCodeDir;

	public String getGoCodePath() {
		return goCodePath;
	}
	
	public String getGoCodeDir() {
		return goCodeDir;
	}

	/**
	 * 
	 * @author steel
	 */
	class GoCodeRunner implements Runnable {
		private ExternalCommand goCodeCommand;

		@Override
		public void run() {

			try {
				ArrayList<String> list = new ArrayList<String>();
				list.add("-s");
				File file;
				file = new File(FileLocator.toFileURL(Platform.getBundle(PLUGIN_ID).getEntry("/")).toURI());
				String arch = System.getProperty("os.arch");
				
				if (System.getProperty("os.name").toLowerCase().contains("windows")) {
					winGocodeKill();					
					goCodeDir     = file.toString() + "\\tools\\win32";
					goCodePath    = file.toString() + "\\tools\\win32\\gocode.exe";
					goCodeCommand = new ExternalCommand(goCodePath);
					goCodeCommand.execute(list);
					
				} else if (System.getProperty("os.name").toLowerCase().contains("os x") && ("x86_64".equals(arch) || "amd64".equals(arch))) {
					goCodeDir     = file.toString() + "/tools/osx64";
					goCodePath    = file.toString() + "/tools/osx64/gocode";
					
				} else if (System.getProperty("os.name").toLowerCase().contains("linux") && ("x86_64".equals(arch) || "amd64".equals(arch))) {
					goCodeDir     = file.toString() + "/tools/linux64";
					goCodePath    = file.toString() + "/tools/linux64/gocode";					
				}
				
				// log the gocode path
				com.googlecode.goclipse.Activator.logInfo(file.toString() + "/tools/linux64");
				com.googlecode.goclipse.Activator.logInfo("ARCH:"+arch);
				com.googlecode.goclipse.Activator.logInfo("OS NAME:"+System.getProperty("os.name"));
				com.googlecode.goclipse.Activator.logInfo("GOCODE PATH:"+goCodePath);

				// make it executable
				File gocode = new File(goCodePath);
				gocode.setExecutable(true);
				
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		

		public void stop() {
			if (goCodeCommand != null) {
				goCodeCommand.destroy();
			}
			

			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				winGocodeKill();	
			}
		}
	}

	/**
	 * The constructor
	 */
	public Activator() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		goCodeRunner = new GoCodeRunner();
		new Thread(goCodeRunner).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		goCodeRunner.stop();		
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/**
	 * 
	 */
	private void winGocodeKill() {
		
		// shutdown previous gocode instances with command:
		//    TASKKILL /F /IM "gocode.exe"
		try{
			ExternalCommand KillCommand = new ExternalCommand("TASKKILL");
			ArrayList<String> killlist = new ArrayList<String>();
			killlist.add("/F");
			killlist.add("/IM");
			killlist.add("\"gocode.exe\"");		
			KillCommand.execute(killlist);
		} 
		catch(Exception error) {
			Activator.getDefault().getLog().log(
					new Status(Status.ERROR, Activator.PLUGIN_ID, "Windows taskkill process failed.  Could not kill gocode process."));
		}
	}

}
