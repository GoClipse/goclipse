package com.googlecode.goclipse.gocode;

import java.io.File;
import java.io.IOException;

import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.gocode.preferences.GocodePreferences;
import com.googlecode.goclipse.gocode.utils.Utils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle.
 */
public class GocodePlugin extends AbstractUIPlugin implements IPropertyChangeListener {
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
    
    if (GocodePreferences.RUN_SERVER_PREF.get()) {
      IPath path = getBestGocodeInstance();
      
      if (path != null) {
        gocodeServer = new GocodeServer(path);
        gocodeServer.startServer();
      }
    }
    
    getPreferenceStore().addPropertyChangeListener(this);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    getPreferenceStore().removePropertyChangeListener(this);
    
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
   * Log the given error message to the Eclipse log.
   */
  public static void logError(String message) {
    getPlugin().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message));
  }
  
  /**
   * Log the given exception to the Eclipse log.
   * 
   * @param t the exception to log
   */
  public static void logError(Throwable t) {
    getPlugin().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, t.getMessage(), t));
  }

  /**
   * Log the given warning message to the Eclipse log.
   */
  public static void logWarning(String message) {
    getPlugin().getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, message));
  }
  
 /**
  * Log the given info message to the Eclipse log.
  */
 public static void logInfo(String message) {
   if (Environment.DEBUG) {
     getPlugin().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
   }
 }
  
  @Override
  public void propertyChange(PropertyChangeEvent event) {
    updateGocodeServer();
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
    String pref = GocodePreferences.GOCODE_PATH_PREF.get();
    
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
  
  protected IPath getGocodeGoRootPath() {
    String goroot = PreferenceConstants.GO_ROOT.get();

    if (goroot != null && goroot.length() > 0) {
      return new Path(goroot).append("bin").append(getExeName());
    } else {
      return null;
    }
  }
  
  protected IPath getGocodeBundledPath() {
    try {
      File pluginDir = FileLocator.getBundleFile(getPlugin().getBundle());
      
      IPath toolsPath = Path.fromOSString(pluginDir.getAbsolutePath()).append("tools");
      
      String name;
      
      if (MiscUtil.OS_IS_WINDOWS) {
        name = "windows";
      } else if (MiscUtil.OS_IS_MAC) {
        name = "darwin";
      } else {
        name = "linux";
      }
      
      name += "_";
      
      if (Utils.is64Bit()) {
        name += "amd64";
      } else {
        name += "386";
      }
      
      toolsPath = toolsPath.append(name).append(getExeName());
      
      if (!MiscUtil.OS_IS_WINDOWS) {
        Utils.ensureExecutable(toolsPath);
      }
      
      return toolsPath;
    } catch (IOException exception) {
      return null;
    }
  }
  
  public IPath getBestGocodeInstance() {
    IPath path = getGocodePrefPath();
    
    if (Utils.pathExists(path)) {
      return path;
    }
    
    path = getGocodeGoBinPath();
    
    if (Utils.pathExists(path)) {
      return path;
    }
    
    path = getGocodeGoRootPath();
    
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
  
  void updateGocodeServer() {
    boolean wantsRun = GocodePreferences.RUN_SERVER_PREF.get();
    IPath path = getBestGocodeInstance();
    
    boolean shouldRun = wantsRun && Utils.pathExists(path);
    
    if (!shouldRun && gocodeServer != null) {
      gocodeServer.stopServer();
      gocodeServer = null;
    } else if (shouldRun && gocodeServer == null) {
      gocodeServer = new GocodeServer(path);
      gocodeServer.startServer();
    } else if (wantsRun && gocodeServer != null) {
      // Check if the path to gocode changed.
      if (!path.equals(gocodeServer.getPath())) {
        gocodeServer.stopServer();
        gocodeServer = null;
        
        gocodeServer = new GocodeServer(path);
        gocodeServer.startServer();
      }
    }
  }
  
  static void winGocodeKill() {
    // shutdown previous gocode instances with command:
    //    TASKKILL /F /IM "gocode.exe"
    try {
      ExternalProcessHelper ph = new ExternalProcessHelper(
    	  new ProcessBuilder("TASKKILL", "/F", "/IM", "\"gocode.exe\""));
      ph.strictAwaitTermination();
      
    } catch (Exception error) {
      GocodePlugin.getPlugin().getLog().log(
          new Status(IStatus.ERROR, GocodePlugin.PLUGIN_ID,
              "Windows taskkill process failed.  Could not kill gocode process."));
    }
  }

}
