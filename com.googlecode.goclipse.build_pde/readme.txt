In order to create a build of GoClipse:

  Copy sample_build.properties and name it <username>_build.properties. Update the two properties
  in that copied file. build.dir should point to where you want the build output to go; source.dir
  should point to the directory with the GoClipse source in it.
  
  From this directory (com.googlecode.goclipse.build_pde) run ant. It'll load build.xml by default
  and run the default (desktop_build) target.
  
  The first build will take a while as a 3.6 version of Eclipse is downloaded. Subsequent builds will
  be faster (< 1 minute).
  
  The build will ultimately create a zip file containing an update site for the latest version of
  GoClipse. It'll be in your build output directory, and named goclipse-updatesite-vXXX.zip.

When reving the version number of GoClipse:

  Change the version number in goclipse-feature/feature.xml. On the feature's 'Plugins' tab, run the
  command Versions... > Force feature version into plugin manifests. This will update the plugin versions
  to match the feature.
  
  Run a build (com.googlecode.goclipse.build_pde/build.xml).
  
  Unzip the contents of the goclipse-updatesite-vXXX.zip build result into the goclipse-update-site
  directory.
  
  Commit all the feaure.xml, plugin.xml, and update site changes to svn.
