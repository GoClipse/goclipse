package com.googlecode.goclipse.gocode;

import java.io.File;
import java.io.IOException;

import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.core.GoEnvironmentUtils;

/**
 * The activator class controls the plug-in life cycle.
 */
public class GocodePlugin extends Plugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "com.googlecode.goclipse.gocode"; //$NON-NLS-1$
	
	// The shared instance
	private static GocodePlugin plugin;
	
	public GocodePlugin() {
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		
		plugin = null;
	}
	
	public static GocodePlugin getPlugin() {
		return plugin;
	}
	
	
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
		
		String name = GoEnvironmentUtils.getGO_OS_Default() + "_" + GoEnvironmentUtils.get_GO_ARCH_Default();
		
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
		
		return getDefaultGocodePath();
	}
	
	public IPath getDefaultGocodePath() {
		IPath path;
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
