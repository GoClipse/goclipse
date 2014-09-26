package com.googlecode.goclipse.gocode;

import java.io.File;
import java.io.IOException;

import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.core.GoEnvironmentPrefUtils;
import com.googlecode.goclipse.core.tools.GocodeServer;

/**
 * The activator class controls the plug-in life cycle.
 */
public class GocodePlugin extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.googlecode.goclipse.gocode"; //$NON-NLS-1$
	
	// The shared instance
	private static GocodePlugin plugin;
	
	private GocodeServer gocodeServer;
	
	/**
	 * The constructor
	 */
	public GocodePlugin() {
		
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		plugin = this;
		
		if (DaemonEnginePreferences.AUTO_START_SERVER.get()) {
			IPath path = getBestGocodeInstance();
			
			if (path != null) {
				gocodeServer = new GocodeServer(path);
				gocodeServer.startServer();
			}
		}
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		
		if (gocodeServer != null) {
			gocodeServer.stopServer();
			gocodeServer = null;
		}
		
		plugin = null;
		
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static GocodePlugin getPlugin() {
		return plugin;
	}
	
	
	/**
	 * @return "gocode" or "gocode.exe"
	 */
	protected String getExeName() {
		return MiscUtil.OS_IS_WINDOWS ? "gocode.exe" : "gocode";
	}
	
	/**
	 * @return the user specified path to Gocode, or null if nothing has been specified
	 */
	protected IPath getGocodePrefPath() {
		String pref = DaemonEnginePreferences.DAEMON_PATH.get();
		
		if (pref == null || pref.length() == 0) {
			return null;
		}
		
		return new Path(pref);
	}
	
	protected IPath getGocodeGoBinPath() {
		String goBinPath = System.getenv("GOBIN");
		
		if (goBinPath != null) {
			return new Path(goBinPath).append(getExeName());
		} else {
			return null;
		}
	}
	
	protected IPath getGocodeBundledPath() {
		File pluginDir;
		try {
			pluginDir = FileLocator.getBundleFile(getPlugin().getBundle());
		} catch (IOException exception) {
			return null;
		}
		
		IPath toolsPath = Path.fromOSString(pluginDir.getAbsolutePath()).append("tools");
		
		String name = "";
		
		name += GoEnvironmentPrefUtils.getGO_OS_Default();
		
		name += "_" + GoEnvironmentPrefUtils.get_GO_ARCH_Default();
		
		toolsPath = toolsPath.append(name).append(getExeName());
		
		if(!toolsPath.toFile().exists()) {
			return null;
		}
		
		if (!MiscUtil.OS_IS_WINDOWS) {
			Utils.doEnsureExecutable(toolsPath);
		}
		
		return toolsPath;
	}
	
	public IPath getBestGocodeInstance() {
		IPath path = getGocodePrefPath();
		
		if (path != null && !path.isEmpty()) {
			return path;
		}
		
		path = getGocodeGoBinPath();
		
		if (Utils.pathExists(path)) {
			return path;
		}
		
		path = getGocodeBundledPath();
		
		if (Utils.pathExists(path)) {
			return path;
		}
		
		// :(
		return null;
	}
	
}

class Utils {
	
	public static boolean pathExists(IPath path) {
		if (path == null || path.isEmpty()) {
			return false;
		}
		
		return path.toFile().exists();
	}
	
	public static void doEnsureExecutable(IPath path) {
		File file = path.toFile();
		
		if (!file.canExecute()) {
			file.setExecutable(true);
		}
	}
	
}
