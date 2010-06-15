package com.googlecode.goclipse.editors;

import org.eclipse.jface.text.IInformationControl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

public class ContentAssistInformationControl implements IInformationControl{
   /** The control's shell */
   private Shell shell;
   private Browser browser;
   public ContentAssistInformationControl(Shell parent, int style) {
      shell = new Shell(parent, style|SWT.TOOL | SWT.ON_TOP | SWT.RESIZE |SWT.SELECTED |SWT.FOCUSED);
      GridLayout layout= new GridLayout(1, false);
      layout.marginHeight= 0;
      layout.marginWidth= 0;
      layout.verticalSpacing= 0;
      shell.setLayout(layout);
      parent.addDisposeListener(new DisposeListener() {
         
         @Override
         public void widgetDisposed(DisposeEvent e) {
            System.out.println("BBBBB");
            
            
         }
      });
      shell.addDisposeListener(new DisposeListener() {
         
         @Override
         public void widgetDisposed(DisposeEvent e) {
            System.out.println("AAAAAAA");
            
         }
      });
      shell.addFocusListener(new FocusListener() {
         
         @Override
         public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void focusGained(FocusEvent e) {
            System.out.println("LLL");
            
         }
      });
      browser = new Browser(shell, style);
      browser.addFocusListener(new FocusListener() {
         
         @Override
         public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void focusGained(FocusEvent e) {
            System.out.println("PPP");
            setFocus();
            
         }
      });
      browser.addMouseListener(new MouseListener() {
         
         @Override
         public void mouseUp(MouseEvent e) {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public void mouseDown(MouseEvent e) {
            System.out.println();
            
         }
         
         @Override
         public void mouseDoubleClick(MouseEvent e) {
            // TODO Auto-generated method stub
            
         }
      });
      browser.setSize(400, 400);
      GridData data = new GridData(GridData.FILL_BOTH);
      browser.setLayoutData(data);
   }

   @Override
   public void addDisposeListener(DisposeListener listener) {
      shell.addDisposeListener(listener);
   }

   @Override
   public void addFocusListener(FocusListener listener) {
      System.out.println("ss");
      
   }

   @Override
   public Point computeSizeHint() {
      System.out.println("ses");
      return shell.computeSize(400, 400);
   }

   @Override
   public void dispose() {
      shell.dispose();
   }

   @Override
   public boolean isFocusControl() {
      System.out.println("xss");
      return shell.getDisplay().getActiveShell() == shell;
   }

   @Override
   public void removeDisposeListener(DisposeListener listener) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void removeFocusListener(FocusListener listener) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void setBackgroundColor(Color background) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void setFocus() {
      System.out.println("LX");
      boolean focusTaken= browser.setFocus();
      if (!focusTaken)
         browser.forceFocus();
   }

   @Override
   public void setForegroundColor(Color foreground) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void setInformation(String information) {
      browser.setText(information);
   }

   @Override
   public void setLocation(Point location) {
      shell.setLocation(location);
   }

   @Override
   public void setSize(int width, int height) {
      shell.setSize(width, height);
   }

   @Override
   public void setSizeConstraints(int maxWidth, int maxHeight) {
      shell.setSize(maxWidth, maxHeight);
   }

   @Override
   public void setVisible(boolean visible) {
      shell.setVisible(visible);
   }

}
