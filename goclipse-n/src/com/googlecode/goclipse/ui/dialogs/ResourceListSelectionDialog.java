/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html Contributors:
 * IBM Corporation - initial API and implementation Sebastian Davids
 * <sdavids@gmx.de> - Fix for bug 19346 - Dialog font should be activated and
 * used by other components.
 *******************************************************************************/
package com.googlecode.goclipse.ui.dialogs;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.StringMatcher;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * Shows a list of resources to the user with a text entry field for a string
 * pattern used to filter the list of resources.
 * <p>
 * ssteel Could not get to the pattern field. Result is this copy of the class.
 * 
 * @since 2.1
 */
@SuppressWarnings("restriction")
public class ResourceListSelectionDialog extends SelectionDialog {

   private static final String DIALOG_SETTINGS_SECTION    = "ResourceListSelectionDialogSettings"; //$NON-NLS-1$

   Text                        pattern;
   String                      startingPattern            = "";
   Table                       resourceNames;
   Table                       folderNames;
   String                      patternString;
   IContainer                  container;
   int                         typeMask;
   private static Collator     collator                   = Collator.getInstance();
   boolean                     gatherResourcesDynamically = true;
   StringMatcher               stringMatcher;
   UpdateFilterThread          updateFilterThread;
   UpdateGatherThread          updateGatherThread;
   ResourceDescriptor[]        descriptors;
   int                         descriptorsSize;
   WorkbenchLabelProvider      labelProvider              = new WorkbenchLabelProvider();
   boolean                     okEnabled                  = false;
   private boolean             showDerived                = false;
   private Button              showDerivedButton;
   private boolean             allowUserToToggleDerived;

   static class ResourceDescriptor implements Comparable<ResourceDescriptor> {
      String    label;
      ArrayList<IResource> resources       = new ArrayList<IResource>();
      boolean   resourcesSorted = true;

      public int compareTo(ResourceDescriptor o) {
         return collator.compare(label, o.label);
      }
   }

   public void setStartingPattern(String str) {
      startingPattern = str;
   }

   class UpdateFilterThread extends Thread {
      boolean stop       = false;
      int     firstMatch = 0;
      int     lastMatch  = descriptorsSize - 1;

      public void run() {
         Display display = resourceNames.getDisplay();
         final int itemIndex[] = { 0 };
         final int itemCount[] = { 0 };
         // Keep track of if the widget got disposed
         // so that we can abort if required
         final boolean[] disposed = { false };
         display.syncExec(new Runnable() {
            public void run() {
               // Be sure the widget still exists
               if (resourceNames.isDisposed()) {
                  disposed[0] = true;
                  return;
               }
               itemCount[0] = resourceNames.getItemCount();
            }
         });

         if (disposed[0]) {
            return;
         }

         int last;
         if ((patternString.indexOf('?') == -1) && (patternString.endsWith("*")) && //$NON-NLS-1$
               (patternString.indexOf('*') == patternString.length() - 1)) {
            // Use a binary search to get first and last match when the pattern
            // string ends with "*" and has no other embedded special
            // characters.
            // For this case, we can be smarter about getting the first and last
            // match since the items are in sorted order.
            firstMatch = getFirstMatch();
            if (firstMatch == -1) {
               firstMatch = 0;
               lastMatch = -1;
            }
            else {
               lastMatch = getLastMatch();
            }
            last = lastMatch;
            for (int i = firstMatch; i <= lastMatch; i++) {
               if (i % 50 == 0) {
                  try {
                     Thread.sleep(10);
                  }
                  catch (InterruptedException e) {
                     // ignore
                  }
               }
               if (stop || resourceNames.isDisposed()) {
                  disposed[0] = true;
                  return;
               }
               final int index = i;
               display.syncExec(new Runnable() {
                  public void run() {
                     if (stop || resourceNames.isDisposed()) {
                        return;
                     }
                     updateItem(index, itemIndex[0], itemCount[0]);
                     itemIndex[0]++;
                  }
               });
            }
         }
         else {
            last = lastMatch;
            boolean setFirstMatch = true;
            for (int i = firstMatch; i <= lastMatch; i++) {
               if (i % 50 == 0) {
                  try {
                     Thread.sleep(10);
                  }
                  catch (InterruptedException e) {
                     // ignore
                  }
               }
               if (stop || resourceNames.isDisposed()) {
                  disposed[0] = true;
                  return;
               }
               final int index = i;
               if (match(descriptors[index].label)) {
                  if (setFirstMatch) {
                     setFirstMatch = false;
                     firstMatch = index;
                  }
                  last = index;
                  display.syncExec(new Runnable() {
                     public void run() {
                        if (stop || resourceNames.isDisposed()) {
                           return;
                        }
                        updateItem(index, itemIndex[0], itemCount[0]);
                        itemIndex[0]++;
                     }
                  });
               }
            }
         }

         if (disposed[0]) {
            return;
         }

         lastMatch = last;
         display.syncExec(new Runnable() {
            public void run() {
               if (resourceNames.isDisposed()) {
                  return;
               }
               itemCount[0] = resourceNames.getItemCount();
               if (itemIndex[0] < itemCount[0]) {
                  resourceNames.setRedraw(false);
                  resourceNames.remove(itemIndex[0], itemCount[0] - 1);
                  resourceNames.setRedraw(true);
               }
               // If no resources, remove remaining folder entries
               if (resourceNames.getItemCount() == 0) {
                  folderNames.removeAll();
                  updateOKState(false);
               }
            }
         });
      }
   }

   class UpdateGatherThread extends Thread {
      boolean stop       = false;
      int     lastMatch  = -1;
      int     firstMatch = 0;
      boolean refilter   = false;

      public void run() {
         Display display = resourceNames.getDisplay();
         final int itemIndex[] = { 0 };
         final int itemCount[] = { 0 };
         // Keep track of if the widget got disposed
         // so that we can abort if required
         final boolean[] disposed = { false };
         display.syncExec(new Runnable() {
            public void run() {
               // Be sure the widget still exists
               if (resourceNames.isDisposed()) {
                  disposed[0] = true;
                  return;
               }
               itemCount[0] = resourceNames.getItemCount();
            }
         });

         if (disposed[0]) {
            return;
         }

         if (!refilter) {
            for (int i = 0; i <= lastMatch; i++) {
               if (i % 50 == 0) {
                  try {
                     Thread.sleep(10);
                  }
                  catch (InterruptedException e) {
                     // ignore
                  }
               }
               if (stop || resourceNames.isDisposed()) {
                  disposed[0] = true;
                  return;
               }
               final int index = i;
               display.syncExec(new Runnable() {
                  public void run() {
                     if (stop || resourceNames.isDisposed()) {
                        return;
                     }
                     updateItem(index, itemIndex[0], itemCount[0]);
                     itemIndex[0]++;
                  }
               });
            }
         }
         else {
            // we're filtering the previous list
            for (int i = firstMatch; i <= lastMatch; i++) {
               if (i % 50 == 0) {
                  try {
                     Thread.sleep(10);
                  }
                  catch (InterruptedException e) {
                     // ignore
                  }
               }
               if (stop || resourceNames.isDisposed()) {
                  disposed[0] = true;
                  return;
               }
               final int index = i;
               if (match(descriptors[index].label)) {
                  display.syncExec(new Runnable() {
                     public void run() {
                        if (stop || resourceNames.isDisposed()) {
                           return;
                        }
                        updateItem(index, itemIndex[0], itemCount[0]);
                        itemIndex[0]++;
                     }
                  });
               }
            }
         }

         if (disposed[0]) {
            return;
         }

         display.syncExec(new Runnable() {
            public void run() {
               if (resourceNames.isDisposed()) {
                  return;
               }
               itemCount[0] = resourceNames.getItemCount();
               if (itemIndex[0] < itemCount[0]) {
                  resourceNames.setRedraw(false);
                  resourceNames.remove(itemIndex[0], itemCount[0] - 1);
                  resourceNames.setRedraw(true);
               }
               // If no resources, remove remaining folder entries
               if (resourceNames.getItemCount() == 0) {
                  folderNames.removeAll();
                  updateOKState(false);
               }
            }
         });
      }
   }

   /**
    * Creates a new instance of the class.
    * 
    * @param parentShell
    *           shell to parent the dialog on
    * @param resources
    *           resources to display in the dialog
    */
   public ResourceListSelectionDialog(Shell parentShell, IResource[] resources) {
      super(parentShell);
      gatherResourcesDynamically = false;
      initDescriptors(resources);
   }

   /**
    * Creates a new instance of the class. When this constructor is used to
    * create the dialog, resources will be gathered dynamically as the pattern
    * string is specified. Only resources of the given types that match the
    * pattern string will be listed. To further filter the matching resources,
    * 
    * @see #select(IResource)
    * @param parentShell
    *           shell to parent the dialog on
    * @param container
    *           container to get resources from
    * @param typeMask
    *           mask containing IResource types to be considered
    */
   public ResourceListSelectionDialog(Shell parentShell, IContainer container, int typeMask) {
      super(parentShell);
      this.container = container;
      this.typeMask = typeMask;
   }

   /**
    * Adjust the pattern string for matching.
    */
   protected String adjustPattern() {
      String text = pattern.getText().trim();
      if (text.endsWith("<")) { //$NON-NLS-1$
         // the < character indicates an exact match search
         return text.substring(0, text.length() - 1);
      }
      if (!text.equals("") && !text.endsWith("*")) { //$NON-NLS-1$ //$NON-NLS-2$
         return text + "*"; //$NON-NLS-1$
      }
      return text;
   }

   /**
    * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
    */
   protected void cancelPressed() {
      setResult(null);
      super.cancelPressed();
   }

   /**
    * @see org.eclipse.jface.window.Window#close()
    */
   public boolean close() {
      boolean result = super.close();
      labelProvider.dispose();
      return result;
   }

   /**
    * @see org.eclipse.jface.window.Window#create()
    */
   public void create() {
      super.create();
      pattern.setFocus();
      if (startingPattern != null) {
         pattern.setText(startingPattern);
         refresh(true);
      }
      getButton(IDialogConstants.OK_ID).setEnabled(okEnabled);
   }

   /**
    * Creates the contents of this dialog, initializes the listener and the
    * update thread.
    * 
    * @param parent
    *           parent to create the dialog widgets in
    */
   protected Control createDialogArea(Composite parent) {

      Composite dialogArea = (Composite) super.createDialogArea(parent);
      Label l = new Label(dialogArea, SWT.NONE);
      l.setText(IDEWorkbenchMessages.ResourceSelectionDialog_label);
      GridData data = new GridData(GridData.FILL_HORIZONTAL);
      l.setLayoutData(data);

      pattern = new Text(dialogArea, SWT.SINGLE | SWT.BORDER);

      pattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      l = new Label(dialogArea, SWT.NONE);
      l.setText(IDEWorkbenchMessages.ResourceSelectionDialog_matching);
      data = new GridData(GridData.FILL_HORIZONTAL);
      l.setLayoutData(data);
      resourceNames = new Table(dialogArea, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
      data = new GridData(GridData.FILL_BOTH);
      data.heightHint = 12 * resourceNames.getItemHeight();
      resourceNames.setLayoutData(data);

      l = new Label(dialogArea, SWT.NONE);
      l.setText(IDEWorkbenchMessages.ResourceSelectionDialog_folders);
      data = new GridData(GridData.FILL_HORIZONTAL);
      l.setLayoutData(data);

      folderNames = new Table(dialogArea, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
      data = new GridData(GridData.FILL_BOTH);
      data.widthHint = 300;
      data.heightHint = 4 * folderNames.getItemHeight();
      folderNames.setLayoutData(data);

      if (gatherResourcesDynamically) {
         updateGatherThread = new UpdateGatherThread();
      }
      else {
         updateFilterThread = new UpdateFilterThread();
      }

      pattern.addKeyListener(new KeyAdapter() {
         public void keyReleased(KeyEvent e) {
            if (e.keyCode == SWT.ARROW_DOWN) {
               resourceNames.setFocus();
            }
         }
      });

      pattern.addModifyListener(new ModifyListener() {
         public void modifyText(ModifyEvent e) {
            refresh(false);
         }
      });

      resourceNames.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent e) {
            updateFolders((ResourceDescriptor) e.item.getData());
         }

         public void widgetDefaultSelected(SelectionEvent e) {
            okPressed();
         }
      });

      folderNames.addSelectionListener(new SelectionAdapter() {
         public void widgetDefaultSelected(SelectionEvent e) {
            okPressed();
         }
      });

      if (getAllowUserToToggleDerived()) {
         showDerivedButton = new Button(dialogArea, SWT.CHECK);
         showDerivedButton.setText(IDEWorkbenchMessages.ResourceSelectionDialog_showDerived);
         showDerivedButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
               setShowDerived(showDerivedButton.getSelection());
               refresh(true);
            }
         });
         showDerivedButton.setSelection(getShowDerived());
      }

      applyDialogFont(dialogArea);
      return dialogArea;
   }

   /**
    * Returns whether to include a "Show derived resources" checkbox in the
    * dialog. The default is <code>false</code>.
    * 
    * @return <code>true</code> to include the checkbox, <code>false</code> to
    *         omit
    * @since 3.1
    */
   public boolean getAllowUserToToggleDerived() {
      return allowUserToToggleDerived;
   }

   /**
    * Sets whether to include a "Show derived resources" checkbox in the dialog.
    * 
    * @param allow
    *           <code>true</code> to include the checkbox, <code>false</code> to
    *           omit
    * @since 3.1
    */
   public void setAllowUserToToggleDerived(boolean allow) {
      allowUserToToggleDerived = allow;
   }

   /**
     */
   private void filterResources(boolean force) {
      String oldPattern = force ? null : patternString;
      patternString = adjustPattern();
      if (!force && patternString.equals(oldPattern)) {
         return;
      }

      updateFilterThread.stop = true;
      stringMatcher = new StringMatcher(patternString, true, false);
      UpdateFilterThread oldThread = updateFilterThread;
      updateFilterThread = new UpdateFilterThread();
      if (patternString.equals("")) { //$NON-NLS-1$
         updateFilterThread.firstMatch = 0;
         updateFilterThread.lastMatch = -1;
         updateFilterThread.start();
         return;
      }

      if (oldPattern != null && (oldPattern.length() != 0) && oldPattern.endsWith("*") && patternString.endsWith("*")) { //$NON-NLS-1$ //$NON-NLS-2$
         int matchLength = oldPattern.length() - 1;
         if (patternString.regionMatches(0, oldPattern, 0, matchLength)) {
            // filter the previous list of items, this is done when the
            // new pattern is a derivative of the old pattern
            updateFilterThread.firstMatch = oldThread.firstMatch;
            updateFilterThread.lastMatch = oldThread.lastMatch;
            updateFilterThread.start();
            return;
         }
      }

      // filter the entire list
      updateFilterThread.firstMatch = 0;
      updateFilterThread.lastMatch = descriptorsSize - 1;
      updateFilterThread.start();
   }

   /**
    * Use a binary search to get the first match for the patternString. This
    * method assumes the patternString does not contain any '?' characters and
    * that it contains only one '*' character at the end of the string.
    */
   private int getFirstMatch() {
      int high = descriptorsSize;
      int low = -1;
      boolean match = false;
      ResourceDescriptor desc = new ResourceDescriptor();
      desc.label = patternString.substring(0, patternString.length() - 1);
      while (high - low > 1) {
         int index = (high + low) / 2;
         String label = descriptors[index].label;
         if (match(label)) {
            high = index;
            match = true;
         }
         else {
            int compare = descriptors[index].compareTo(desc);
            if (compare == -1) {
               low = index;
            }
            else {
               high = index;
            }
         }
      }
      if (match) {
         return high;
      }
      return -1;
   }

   /**
     */
   private void gatherResources(boolean force) {
      String oldPattern = force ? null : patternString;
      patternString = adjustPattern();
      if (!force && patternString.equals(oldPattern)) {
         return;
      }

      updateGatherThread.stop = true;
      updateGatherThread = new UpdateGatherThread();

      if (patternString.equals("")) { //$NON-NLS-1$
         updateGatherThread.start();
         return;
      }
      stringMatcher = new StringMatcher(patternString, true, false);

      if (oldPattern != null && (oldPattern.length() != 0) && oldPattern.endsWith("*") && patternString.endsWith("*")) { //$NON-NLS-1$ //$NON-NLS-2$
         // see if the new pattern is a derivative of the old pattern
         int matchLength = oldPattern.length() - 1;
         if (patternString.regionMatches(0, oldPattern, 0, matchLength)) {
            updateGatherThread.refilter = true;
            updateGatherThread.firstMatch = 0;
            updateGatherThread.lastMatch = descriptorsSize - 1;
            updateGatherThread.start();
            return;
         }
      }

      final ArrayList<IResource> resources = new ArrayList<IResource>();
      BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
         public void run() {
            getMatchingResources(resources);
            IResource resourcesArray[] = new IResource[resources.size()];
            resources.toArray(resourcesArray);
            initDescriptors(resourcesArray);
         }
      });

      updateGatherThread.firstMatch = 0;
      updateGatherThread.lastMatch = descriptorsSize - 1;
      updateGatherThread.start();
   }

   /**
    * Return an image for a resource descriptor.
    * 
    * @param desc
    *           resource descriptor to return image for
    * @return an image for a resource descriptor.
    */
   private Image getImage(ResourceDescriptor desc) {
      IResource r = (IResource) desc.resources.get(0);
      return labelProvider.getImage(r);
   }

   /**
    * Use a binary search to get the last match for the patternString. This
    * method assumes the patternString does not contain any '?' characters and
    * that it contains only one '*' character at the end of the string.
    */
   private int getLastMatch() {
      int high = descriptorsSize;
      int low = -1;
      boolean match = false;
      ResourceDescriptor desc = new ResourceDescriptor();
      desc.label = patternString.substring(0, patternString.length() - 1);
      while (high - low > 1) {
         int index = (high + low) / 2;
         String label = descriptors[index].label;
         if (match(label)) {
            low = index;
            match = true;
         }
         else {
            int compare = descriptors[index].compareTo(desc);
            if (compare == -1) {
               low = index;
            }
            else {
               high = index;
            }
         }
      }
      if (match) {
         return low;
      }
      return -1;
   }

   /**
    * Gather the resources of the specified type that match the current pattern
    * string. Gather the resources using the proxy visitor since this is quicker
    * than getting the entire resource.
    * 
    * @param resources
    *           resources that match
    */
   private void getMatchingResources(final ArrayList<IResource> resources) {
      try {
         container.accept(new IResourceProxyVisitor() {
            public boolean visit(IResourceProxy proxy) {
               // optionally exclude derived resources (bugs 38085 and 81333)
               if (!getShowDerived() && proxy.isDerived()) {
                  return false;
               }
               int type = proxy.getType();
               if ((typeMask & type) != 0) {
                  if (match(proxy.getName())) {
                     IResource res = proxy.requestResource();
                     if (select(res)) {
                        resources.add(res);
                        return true;
                     }
                     return false;
                  }
               }
               if (type == IResource.FILE) {
                  return false;
               }
               return true;
            }
         }, IResource.NONE);
      }
      catch (CoreException e) {
         // ignore
      }
   }

   private Image getParentImage(IResource resource) {
      IResource parent = resource.getParent();
      return labelProvider.getImage(parent);
   }

   private String getParentLabel(IResource resource) {
      IResource parent = resource.getParent();
      String text;
      if (parent.getType() == IResource.ROOT) {
         // Get readable name for workspace root ("Workspace"), without
         // duplicating language-specific string here.
         text = labelProvider.getText(parent);
      }
      else {
         text = parent.getFullPath().makeRelative().toString();
      }
      if (text == null) {
         return ""; //$NON-NLS-1$
      }
      return text;
   }

   /**
    * Returns whether derived resources should be shown in the list. The default
    * is <code>false</code>.
    * 
    * @return <code>true</code> to show derived resources, <code>false</code> to
    *         hide them
    * @since 3.1
    */
   protected boolean getShowDerived() {
      return showDerived;
   }

   /**
    * Sets whether derived resources should be shown in the list.
    * 
    * @param show
    *           <code>true</code> to show derived resources, <code>false</code>
    *           to hide them
    * @since 3.1
    */
   protected void setShowDerived(boolean show) {
      showDerived = show;
   }

   /**
    * Creates a ResourceDescriptor for each IResource, sorts them and removes
    * the duplicated ones.
    * 
    * @param resources
    *           resources to create resource descriptors for
    */
   private void initDescriptors(final IResource resources[]) {
      BusyIndicator.showWhile(null, new Runnable() {
         public void run() {
            descriptors = new ResourceDescriptor[resources.length];
            for (int i = 0; i < resources.length; i++) {
               IResource r = resources[i];
               ResourceDescriptor d = new ResourceDescriptor();
               // TDB: Should use the label provider and compare performance.
               d.label = r.getName();
               d.resources.add(r);
               descriptors[i] = d;
            }
            Arrays.sort(descriptors);
            descriptorsSize = descriptors.length;

            // Merge the resource descriptor with the same label and type.
            int index = 0;
            if (descriptorsSize < 2) {
               return;
            }
            ResourceDescriptor current = descriptors[index];
            IResource currentResource = (IResource) current.resources.get(0);
            for (int i = 1; i < descriptorsSize; i++) {
               ResourceDescriptor next = descriptors[i];
               IResource nextResource = (IResource) next.resources.get(0);
               if (nextResource.getType() == currentResource.getType() && next.label.equals(current.label)) {
                  current.resources.add(nextResource);
                  // If we are merging resources with the same name, into a
                  // single descriptor,
                  // then we must mark the descriptor unsorted so that we will
                  // sort the folder
                  // names.
                  // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=76496
                  current.resourcesSorted = false;
               }
               else {
                  if (current.resources.size() > 1) {
                     current.resourcesSorted = false;
                  }
                  descriptors[index + 1] = descriptors[i];
                  index++;
                  current = descriptors[index];
                  currentResource = (IResource) current.resources.get(0);
               }
            }
            descriptorsSize = index + 1;
         }
      });
   }

   /**
    * Returns true if the label matches the chosen pattern.
    * 
    * @param label
    *           label to match with the current pattern
    * @return true if the label matches the chosen pattern. false otherwise.
    */
   private boolean match(String label) {
      if ((patternString == null) || (patternString.equals("")) || (patternString.equals("*"))) { //$NON-NLS-1$ //$NON-NLS-2$
         return true;
      }
      return stringMatcher.match(label);
   }

   /**
    * The user has selected a resource and the dialog is closing. Set the
    * selected resource as the dialog result.
    */
   protected void okPressed() {
      TableItem items[] = folderNames.getSelection();
      if (items.length == 1) {
         ArrayList<IResource> result = new ArrayList<IResource>();
         result.add((IResource)items[0].getData());
         setResult(result);
      }
      super.okPressed();
   }

   /**
    * Use this method to further filter resources. As resources are gathered, if
    * a resource matches the current pattern string, this method will be called.
    * If this method answers false, the resource will not be included in the
    * list of matches and the resource's children will NOT be considered for
    * matching.
    */
   protected boolean select(IResource resource) {
      return true;
   }

   /**
    * Refreshes the filtered list of resources. Called when the text in the
    * pattern text entry has changed.
    * 
    * @param force
    *           if <code>true</code> a refresh is forced, if <code>false</code>
    *           a refresh only occurs if the pattern has changed
    * @since 3.1
    */
   protected void refresh(boolean force) {
      if (gatherResourcesDynamically) {
         gatherResources(force);
      }
      else {
         filterResources(force);
      }
   }

   /**
    * A new resource has been selected. Change the contents of the folder names
    * list.
    * 
    * @desc resource descriptor of the selected resource
    */
   private void updateFolders(final ResourceDescriptor desc) {
      BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
         public void run() {
            if (!desc.resourcesSorted) {
               // sort the folder names
               Collections.sort(desc.resources, new Comparator<IResource>() {
				@Override
				public int compare(IResource o1, IResource o2) {
                    String s1 = getParentLabel(o1);
                    String s2 = getParentLabel(o2);
                    return collator.compare(s1, s2);
				}
               });
               desc.resourcesSorted = true;
            }
            folderNames.removeAll();
            for (int i = 0; i < desc.resources.size(); i++) {
               TableItem newItem = new TableItem(folderNames, SWT.NONE);
               IResource r = (IResource) desc.resources.get(i);
               newItem.setText(getParentLabel(r));
               newItem.setImage(getParentImage(r));
               newItem.setData(r);
            }
            folderNames.setSelection(0);
         }
      });
   }

   /**
    * Update the specified item with the new info from the resource descriptor.
    * Create a new table item if there is no item.
    * 
    * @param index
    *           index of the resource descriptor
    * @param itemPos
    *           position of the existing item to update
    * @param itemCount
    *           number of items in the resources table widget
    */
   private void updateItem(int index, int itemPos, int itemCount) {
      ResourceDescriptor desc = descriptors[index];
      TableItem item;
      if (itemPos < itemCount) {
         item = resourceNames.getItem(itemPos);
         if (item.getData() != desc) {
            item.setText(desc.label);
            item.setData(desc);
            item.setImage(getImage(desc));
            if (itemPos == 0) {
               resourceNames.setSelection(0);
               updateFolders(desc);
            }
         }
      }
      else {
         item = new TableItem(resourceNames, SWT.NONE);
         item.setText(desc.label);
         item.setData(desc);
         item.setImage(getImage(desc));
         if (itemPos == 0) {
            resourceNames.setSelection(0);
            updateFolders(desc);
         }
      }
      updateOKState(true);
   }

   /**
    * Update the enabled state of the OK button. To be called when the resource
    * list is updated.
    * 
    * @param state
    *           the new enabled state of the button
    */
   protected void updateOKState(boolean state) {
      Button okButton = getButton(IDialogConstants.OK_ID);
      if (okButton != null && !okButton.isDisposed() && state != okEnabled) {
         okButton.setEnabled(state);
         okEnabled = state;
      }
   }

   /*
    * (non-Javadoc)
    * @see org.eclipse.jface.window.Dialog#getDialogBoundsSettings()
    * @since 3.2
    */
   protected IDialogSettings getDialogBoundsSettings() {
      IDialogSettings settings = IDEWorkbenchPlugin.getDefault().getDialogSettings();
      IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION);
      if (section == null) {
         section = settings.addNewSection(DIALOG_SETTINGS_SECTION);
      }
      return section;
   }
}
