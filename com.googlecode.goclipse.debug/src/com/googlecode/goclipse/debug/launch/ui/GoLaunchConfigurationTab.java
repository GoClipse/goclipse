package com.googlecode.goclipse.debug.launch.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.debug.GoDebugPlugin;
import com.googlecode.goclipse.debug.launch.BuildConfiguration;

/**
 * @author steel
 */
public class GoLaunchConfigurationTab extends AbstractLaunchConfigurationTab {
  private GoLaunchConfigurationTabComposite composite;
  private WorkingDirectoryBlock workingDirectoryBlock;

  private String errorMessage = null;

  public GoLaunchConfigurationTab() {
    workingDirectoryBlock = new GoWorkingDirectoryBlock();
  }

  @Override
  public boolean canSave() {
    return validate();
  }

  /**
   * @return
   */
  protected boolean validate() {
    // run the gauntlet
    try {
      if (composite == null) {
        return false;
      }

      String project = composite.getProject();
      if (project == null) {
        errorMessage = "The user must set a valid Go project.";
        composite.setMainConfigEnabled(false);
        return false;
      }

      IProject iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(project);

      if (iProject == null) {
        errorMessage = "The project folder given could not be found.";
        composite.setMainConfigEnabled(false);
        return false;
      }

      if (iProject.getNature(GoNature.NATURE_ID) == null) {
        errorMessage = "The project does not have a Go Nature assigned to it.  "
            + "Please reference valid Go project.";
        composite.setMainConfigEnabled(false);
        return false;
      }

      composite.setMainConfigEnabled(true);
      IResource mainfile = null;

      try {
        mainfile = iProject.findMember(composite.getMainFile());
      } catch (Exception e) {
        GoDebugPlugin.logError(e);
      }

      if (mainfile == null) {
        errorMessage = "The main file could not be found.  "
            + "Please make sure the given path is correct and is relative to the project.";
        return false;
      }

    } catch (CoreException e) {
      GoDebugPlugin.logError(e);
      
      errorMessage = "A problem has occurred while attempting to validate the fields in this configuration.";
    }
    
    errorMessage = null;
    
    return true;
  }

  @Override
  public void createControl(Composite parent) {
    composite = new GoLaunchConfigurationTabComposite(parent, this, SWT.NULL);
    composite.setLaunchConfigurationDialog(getLaunchConfigurationDialog());

    workingDirectoryBlock.createControl(composite);

    setControl(composite);
  }

  @Override
  public void dispose() {
    if (composite != null) {
      composite.dispose();
    }
  }

  @Override
  public Control getControl() {
    return composite;
  }

  @Override
  public String getErrorMessage() {
    if (errorMessage != null) {
      return errorMessage;
    } else {
      return workingDirectoryBlock.getErrorMessage();
    }
  }

  @Override
  public Image getImage() {
    return GoDebugPlugin.getImage("icons/go-icon16.png");
  }

  @Override
  public String getMessage() {
    return "Configure a Go application to run.";
  }

  @Override
  protected Button createPushButton(Composite parent, String label, Image image) {
    return super.createPushButton(parent, label, image);
  }

  @Override
  public String getName() {
    return "Main";
  }

  @Override
  public void initializeFrom(ILaunchConfiguration configuration) {
    try {
      String projectname = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, "");
      String mainfile = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_MAIN, "");
      String buildconfig = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_BUILD_CONFIG,
          "");
      String programargs = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_ARGS, "");

      composite.setProject(projectname);
      composite.setMainFile(mainfile);
      composite.setBuildConfig(buildconfig != null && !buildconfig.equals("")
          ? BuildConfiguration.get(buildconfig) : BuildConfiguration.DEBUG);
      composite.setProgramArgs(programargs);

      workingDirectoryBlock.initializeFrom(configuration);
    } catch (CoreException e) {
      GoDebugPlugin.logError(e);
    }
  }

  @Override
  public boolean isValid(ILaunchConfiguration launchConfig) {
    return validate() && workingDirectoryBlock.isValid(launchConfig);
  }

  @Override
  public void performApply(ILaunchConfigurationWorkingCopy configuration) {
    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, composite.getProject());
    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_MAIN, composite.getMainFile());
    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_BUILD_CONFIG,
        composite.getBuildConfiguration().toString());
    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_ARGS, composite.getProgramArgs());
    configuration.setAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT, true);
    configuration.setAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING, "UTF-8");
    workingDirectoryBlock.performApply(configuration);
  }

  @Override
  public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, "enter a project here...");

    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_MAIN, "");
    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_BUILD_CONFIG,
        BuildConfiguration.RELEASE.toString());
    configuration.setAttribute(GoConstants.GO_CONF_ATTRIBUTE_ARGS, "");

    workingDirectoryBlock.setDefaults(configuration);
  }

  @Override
  public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
    super.setLaunchConfigurationDialog(dialog);

    if (composite != null) {
      composite.setLaunchConfigurationDialog(dialog);
    }

    workingDirectoryBlock.setLaunchConfigurationDialog(dialog);
  }

}
