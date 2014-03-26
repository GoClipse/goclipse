package com.googlecode.goclipse.editors;

import java.util.ResourceBundle;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.googlecode.goclipse.Activator;

public class GoEditor extends TextEditor {
  public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
  public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

  private static final String BUNDLE_ID = "com.googlecode.goclipse.editors.GoEditorMessages";
  
	public static final String EDITOR_CONTEXT = "#GoEditorContext";
	public static final String RULER_CONTEXT = "#GoEditorRulerContext";

  private static ResourceBundle editorResourceBundle = ResourceBundle.getBundle(BUNDLE_ID);

  private IPropertyChangeListener changeListener;
  private DefaultCharacterPairMatcher matcher;
  private EditorImageUpdater imageUpdater;

  private GoEditorOutlinePage outlinePage;

  /**
   * 
   */
  public GoEditor() {
	  
    setSourceViewerConfiguration(new GoEditorSourceViewerConfiguration(this, getPreferenceStore()));

    setKeyBindingScopes(new String[] {"com.googlecode.goclipse.editor"});

    changeListener = new IPropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent event) {
//				SysUtils.debug("Preference Change Event");
//				if (event.getProperty().equals(PreferenceConstants.FIELD_USE_HIGHLIGHTING)) {
//					setSourceViewerConfiguration(new Configuration(colorManager));
//					setDocumentProvider(new DocumentProvider());
//					initializeEditor();
//				}
      }
    };

    Activator.getDefault().getPreferenceStore().addPropertyChangeListener(changeListener);
  }

  @Override
  protected void initializeEditor() {
    super.initializeEditor();

	setEditorContextMenuId(EDITOR_CONTEXT);
	setRulerContextMenuId(RULER_CONTEXT);
  }
  
  @Override
  protected void setTitleImage(Image image) {
    super.setTitleImage(image);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Class required) {
    if (IContentOutlinePage.class.equals(required)) {
      if (outlinePage == null) {
        outlinePage = new GoEditorOutlinePage(getDocumentProvider(), this);
      }

      return outlinePage;
    }

    return super.getAdapter(required);
  }

  private IDocumentProvider createDocumentProvider(IEditorInput input) {
    return new GoDocumentProvider();
  }

  @Override
  protected final void doSetInput(IEditorInput input) throws CoreException {
    setDocumentProvider(createDocumentProvider(input));

    super.doSetInput(input);

    if (input instanceof IFileEditorInput) {
      imageUpdater = new EditorImageUpdater(this);
    }
  }

  @Override
  protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
    super.configureSourceViewerDecorationSupport(support);

    char[] matchChars = {'(', ')', '[', ']', '{', '}'}; //which brackets to match
    matcher = new DefaultCharacterPairMatcher(matchChars, IDocumentExtension3.DEFAULT_PARTITIONING);
    support.setCharacterPairMatcher(matcher);
    support.setMatchingCharacterPainterPreferenceKeys(EDITOR_MATCHING_BRACKETS,
        EDITOR_MATCHING_BRACKETS_COLOR);

    //Enable bracket highlighting in the preference store
    IPreferenceStore store = getPreferenceStore();
    store.setDefault(EDITOR_MATCHING_BRACKETS, true);
    store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, "128,128,128");
  }

  @Override
  protected void createActions() {
    super.createActions();

    IAction action;

    action = new ToggleCommentAction(editorResourceBundle, "ToggleComment.", this);
    action.setActionDefinitionId("com.googlecode.goclipse.actions.ToggleComment");
    setAction("ToggleComment", action);
    markAsStateDependentAction("ToggleComment", true);
    configureToggleCommentAction();
  }

  public DefaultCharacterPairMatcher getPairMatcher() {
    return matcher;
  }

  public String getText() {
    return getSourceViewer().getDocument().get();
  }

  public void replaceText(String newText) {
    ISelection sel = getSelectionProvider().getSelection();
    int topIndex = getSourceViewer().getTopIndex();

    getSourceViewer().getDocument().set(newText);

    if (sel != null) {
      getSelectionProvider().setSelection(sel);
    }

    if (topIndex != -1) {
      getSourceViewer().setTopIndex(topIndex);
    }
  }

  @Override
  public void dispose() {
    Activator.getDefault().getPreferenceStore().removePropertyChangeListener(changeListener);

    if (imageUpdater != null) {
      imageUpdater.dispose();
    }

    super.dispose();
  }

  private void configureToggleCommentAction() {
    IAction action = getAction("ToggleComment");

    if (action instanceof ToggleCommentAction) {
      ISourceViewer sourceViewer = getSourceViewer();
      SourceViewerConfiguration configuration = getSourceViewerConfiguration();
      ((ToggleCommentAction) action).configure(sourceViewer, configuration);
    }
  }

  protected void handleReconcilation(IRegion partition) {
    if (outlinePage != null) {
      outlinePage.handleEditorReconcilation();
    }
  }

	public IProject getCurrentProject() {
		if (getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) getEditorInput();

			IFile file = input.getFile();

			if (file != null) {
				return file.getProject();
			}
		}

		return null;
	}

  @Override
  protected boolean isTabsToSpacesConversionEnabled() {
    return false;
  }

}
