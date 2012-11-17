package com.googlecode.goclipse.gocode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.ExternalCommand;
import com.googlecode.goclipse.gocode.utils.Utils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle.
 */
public class GocodePlugin extends AbstractUIPlugin implements IPropertyChangeListener {
  // The plug-in ID
  public static final String PLUGIN_ID = "com.googlecode.goclipse.gocode"; //$NON-NLS-1$

  public static final String RUN_SERVER_PREF = "com.googlecode.goclipse.gocode.server";
  public static final String GOCODE_PATH_PREF = "com.googlecode.goclipse.gocode.path";
  
  public static final boolean USE_TCP = true;
  
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
    
    if (getRunServer()) {
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
    return Utils.isWindows() ? "gocode.exe" : "gocode";
  }
  
  /**
   * @return the user specified path to Gocode, or null if nothing has been specified
   */
  protected IPath getGocodePrefPath() {
    String pref = getPreferenceStore().getString(GOCODE_PATH_PREF);
    
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
    String goroot = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.GOROOT);

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
      
      if (Utils.isWindows()) {
        return toolsPath.append("win32").append(getExeName());
      } else if (Utils.isMacOS() && Utils.is64Bit()) {
        IPath exePath = toolsPath.append("osx64").append(getExeName());
        
        Utils.ensureExecutable(exePath);
        
        return exePath;
      } else if (Utils.isLinux() && Utils.is64Bit()) {
        IPath exePath = toolsPath.append("linux64").append(getExeName());
        
        Utils.ensureExecutable(exePath);
        
        return exePath;
      } else {
        return null;
      }
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
  
  private boolean getRunServer() {
    return getPreferenceStore().getBoolean(RUN_SERVER_PREF);
  }
  
  void updateGocodeServer() {
    boolean wantsRun = getRunServer();
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
      ExternalCommand KillCommand = new ExternalCommand("TASKKILL");
      ArrayList<String> killlist = new ArrayList<String>();
      killlist.add("/F");
      killlist.add("/IM");
      killlist.add("\"gocode.exe\"");
      KillCommand.execute(killlist);
    } catch (Exception error) {
      GocodePlugin.getPlugin().getLog().log(
          new Status(IStatus.ERROR, GocodePlugin.PLUGIN_ID,
              "Windows taskkill process failed.  Could not kill gocode process."));
    }
  }

}
