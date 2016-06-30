/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.coloring;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.osgi.service.event.Event;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.text.LangDocumentPartitionerSetup;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.ThemeHelper.ThemeChangeListener;
import melnorme.lang.ide.ui.editor.LangSourceViewer;
import melnorme.lang.ide.ui.preferences.PreferencesMessages;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock2;
import melnorme.lang.ide.ui.preferences.common.IPreferencesEditor;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.ide.ui.text.SimpleSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.coloring.StylingPreferences.OverlayStylingPreferences;
import melnorme.lang.ide.ui.text.coloring.TextStyling.TextStylingData;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.ColorField;
import melnorme.util.swt.jface.ElementContentProvider2;
import melnorme.util.swt.jface.LabeledTreeElement;
import melnorme.util.swt.jface.LabeledTreeElement.LabeledTreeElementLabelProvider;
import melnorme.util.swt.jface.TreeViewerExt;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.FieldValueListener.FieldChangeListener;
import melnorme.utilbox.fields.IField;
import melnorme.utilbox.fields.IProperty;
import melnorme.utilbox.misc.StreamUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.tree.IElement;
import melnorme.utilbox.tree.SimpleTreeElement;
import melnorme.utilbox.tree.TreeVisitor;

/**
 * A configuration component for syntax (and possibly semantic) source highlighting options.
 */
public abstract class AbstractSourceColoringConfigurationBlock extends AbstractPreferencesBlock2 {
		
	protected final SourceColoringListRoot coloringOptionsList;
	
	protected final OverlayStylingPreferences overlayStylingPrefs = new OverlayStylingPreferences(
		EditorSettings_Actual.getStylingPreferences()); 
	
	protected TreeViewerExt treeViewer;
	
	protected CheckBoxField enableField;
	protected ColorField colorField;
	protected CheckBoxField boldCheckboxField;
	protected CheckBoxField italicCheckboxField;
	protected CheckBoxField striketroughCheckboxField;
	protected CheckBoxField underlineCheckboxField;

	public AbstractSourceColoringConfigurationBlock(PreferencesPageContext prefContext) {
		super(prefContext);
		this.coloringOptionsList = new SourceColoringListRoot();
		
		visitColoringItems(item -> prefContext.addPrefElement(item));
		
		prefContext.addPrefElement(new IPreferencesEditor() {
			@Override
			public void loadDefaults() {
				updateWidgetFromInput();
			}
			@Override
			public void doSaveSettings() {
			}
		});
	}
	
	protected void visitColoringItems(Consumer<SourceColoringElement> consumer) {
		new TreeVisitor() {
			@Override
			protected boolean enterNode(IElement node) {
				if(SourceColoringElement.class.isInstance(node)) {
					SourceColoringElement item = (SourceColoringElement) node;
					consumer.accept(item);
				}
				return true;
			}
		}.traverse(coloringOptionsList);
	}
	
	public static class SourceColoringCategory extends LabeledTreeElement {
		
		public SourceColoringCategory(String labelText, IElement[] children) {
			super(null, children, labelText);
		}
		
	}
	
	public class SourceColoringElement extends LabeledTreeElement implements IPreferencesEditor {
		
		protected final ThemedTextStylingPreference stylingPref;
		protected final String prefId;
		protected final IField<TextStyling> temporaryPref;
		
		public SourceColoringElement(String labelText, ThemedTextStylingPreference stylingPref) {
			super(null, null, labelText);
			this.stylingPref = stylingPref;
			this.prefId = stylingPref.getPrefId();
			this.temporaryPref = overlayStylingPrefs.get(prefId);
			loadFromPrefs();
		}
		
		public void loadFromPrefs() {
			this.temporaryPref.set(stylingPref.getFieldValue());
		}
		
		public TextStyling getWorkingValue() {
			return temporaryPref.get();
		}
		
		@Override
		public void loadDefaults() {
			temporaryPref.set(stylingPref.getDefaultValue());
		}
		
		@Override
		public void doSaveSettings() throws CommonException {
			stylingPref.setInstanceScopeValue(temporaryPref.get());
		}
		
	}
	
	public class SourceColoringListRoot extends SimpleTreeElement {
		public SourceColoringListRoot() {
			super(null, createTreeElements());
			assertTrue(children != null);
		}
	}
	
	protected abstract LabeledTreeElement[] createTreeElements();
	
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(final Composite topControl) {
		PixelConverter pc = new PixelConverter(topControl);
		
		Link openPrefsLink = ControlUtils.createOpenPreferencesDialogLinkedText(topControl, 
			PreferencesMessages.DLTKEditorColoringConfigurationBlock_link);
		openPrefsLink.setLayoutData(
			gdfFillDefaults().hint(pc.convertWidthInCharsToPixels(50), SWT.DEFAULT).create());
		
		ControlUtils.createHorizontalSpacer(topControl, 1, pc);
		
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, 
			PreferencesMessages.DLTKEditorPreferencePage_coloring_element, 
			gdfFillDefaults().create());
		
		Composite editorComposite = SWTFactoryUtil.createComposite(topControl);
		editorComposite.setLayoutData(
			gdfFillDefaults().grab(true, false).create());
		editorComposite.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).create());
		
		treeViewer = new TreeViewerExt(editorComposite, SWT.SINGLE | SWT.BORDER);
		treeViewer.setContentProvider(new ElementContentProvider2());
		treeViewer.setLabelProvider(new LabeledTreeElementLabelProvider());
		treeViewer.getTree().setLayoutData(
			gdfFillDefaults().hint(pc.convertWidthInCharsToPixels(40), pc.convertHeightInCharsToPixels(10)).create());
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleAppearanceColorListSelectionChanged();
			}
		});
		treeViewer.setInput(coloringOptionsList);
		treeViewer.setExpandedElements(coloringOptionsList.getChildren());
		
		
		Composite itemEditorComposite = new Composite(editorComposite, SWT.NONE);
		itemEditorComposite.setLayoutData(
			gdfFillDefaults().create());
		itemEditorComposite.setLayout(GridLayoutFactory.swtDefaults().extendedMargins(5, 20, 5, 5).create());
		
		enableField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_enable);
		enableField.createComponent(itemEditorComposite, gdfFillDefaults().create());
		
		colorField = new ColorField(PreferencesMessages.DLTKEditorPreferencePage_color);
		colorField.createComponent(itemEditorComposite, gdfFillDefaults().indent(20, 0).create());
		boldCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_bold);
		boldCheckboxField.createComponent(itemEditorComposite, gdfFillDefaults().indent(20, 0).create());
		italicCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_italic);
		italicCheckboxField.createComponent(itemEditorComposite, gdfFillDefaults().indent(20, 0).create());
		striketroughCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_strikethrough);
		striketroughCheckboxField.createComponent(itemEditorComposite, gdfFillDefaults().indent(20, 0).create());
		underlineCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_underline);
		underlineCheckboxField.createComponent(itemEditorComposite, gdfFillDefaults().indent(20, 0).create());
		
		enableField.addChangeListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isEnabled = enableField.getBooleanFieldValue();
				setColoringEditorControlsEnabled(data.isEnabled);
			}
		});
		
		colorField.addChangeListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.rgb = colorField.getFieldValue();
			}
		});
		boldCheckboxField.addChangeListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isBold = boldCheckboxField.getBooleanFieldValue();
			}
		});
		italicCheckboxField.addChangeListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isItalic = italicCheckboxField.getBooleanFieldValue();
			}
		});
		striketroughCheckboxField.addChangeListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isStrikethrough = striketroughCheckboxField.getBooleanFieldValue();
			}
		});
		underlineCheckboxField.addChangeListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isUnderline = underlineCheckboxField.getBooleanFieldValue();
			}
		});
		
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, 
			PreferencesMessages.DLTKEditorPreferencePage_preview, 
			gdfFillDefaults().create());
		
		
		Control previewViewerControl = createPreviewViewer(topControl);
		previewViewerControl.setLayoutData(
			gdfFillDefaults().hint(pc.convertWidthInCharsToPixels(50), pc.convertHeightInCharsToPixels(15)).
			grab(true, true).
			create());
		
		Display display = topControl.getShell().getDisplay();
		ThemeChangeListener themeChangeListener = new ThemeChangeListener() {
			@Override
			public void handleEvent(Event event) {
				// Reload prefs for new theme. use asyncExec because ThemeChangeListener order is not guaranteed.
				display.asyncExec(() -> { 
					visitColoringItems((item) -> item.loadFromPrefs());
					updateWidgetFromInput();
				});
			}
		};
		topControl.addDisposeListener((e) -> themeChangeListener.close()); 
	}
	
	@Override
	public void updateWidgetFromInput() {
		handleAppearanceColorListSelectionChanged();
	}
	
	protected abstract class ChangeStylingField implements FieldChangeListener {
		@Override
		public void fieldValueChanged() {
			IProperty<TextStyling> field = getSelectedColoringItem().temporaryPref;
			TextStyling newStyling = field.get();
			TextStylingData data = newStyling.getData();
			changeStylingValue(data);
			field.set(new TextStyling(data));
		}
		
		protected abstract void changeStylingValue(TextStylingData data);
	}
	
	public SourceColoringElement getSelectedColoringItem() {
		Object element = treeViewer.getSelectionFirstElement();
		
		if(element instanceof SourceColoringElement) {
			return (SourceColoringElement) element; 
		}
		return null;
	}
	
	protected void handleAppearanceColorListSelectionChanged() {
		SourceColoringElement selectedItem = getSelectedColoringItem();
		if(selectedItem == null) {
			enableField.setEnabled(false);
			setColoringEditorControlsEnabled(false);
		} else {
			enableField.setEnabled(false);
			enableField.setFieldValue(selectedItem.getWorkingValue().isEnabled);
			colorField.setFieldValue(selectedItem.getWorkingValue().rgb);
			boldCheckboxField.setFieldValue(selectedItem.getWorkingValue().isBold);
			italicCheckboxField.setFieldValue(selectedItem.getWorkingValue().isItalic);
			striketroughCheckboxField.setFieldValue(selectedItem.getWorkingValue().isStrikethrough);
			underlineCheckboxField.setFieldValue(selectedItem.getWorkingValue().isUnderline);
		}
	}
	
	protected void setColoringEditorControlsEnabled(boolean enabled) {
		boldCheckboxField.setEnabled(enabled);
		colorField.setEnabled(enabled);
		italicCheckboxField.setEnabled(enabled);
		striketroughCheckboxField.setEnabled(enabled);
		underlineCheckboxField.setEnabled(enabled);
	}
	
	/* ----------------- Preview viewer ----------------- */
	
	protected Control createPreviewViewer(Composite topControl) {
		IPreferenceStore store = LangUIPlugin.getInstance().getCombinedPreferenceStore();
		ProjectionViewer fPreviewViewer = this.createPreviewViewer(topControl, false, 
			SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER, store);
		fPreviewViewer.setEditable(false);
		
		IDocument document = new Document(getPreviewContent());
		LangDocumentPartitionerSetup.getInstance().setup(document);
		fPreviewViewer.setDocument(document);
		return fPreviewViewer.getControl();
	}
	
	protected String getPreviewContent() {
		InputStream is = getPreviewContentAsStream();
		try {
			return StreamUtil.readAllBytesFromStream(is).toString(StringUtil.UTF8);
		} catch (IOException e) {
			LangCore.logInternalError(e);
			return "<INTERNAL ERROR: COULD NOT READ PREVIEW FILE";
		}
	}
	
	protected abstract InputStream getPreviewContentAsStream();
	
	protected ProjectionViewer createPreviewViewer(Composite parent, boolean showAnnotationsOverview,
			int styles, IPreferenceStore store) {
		LangSourceViewer sourceViewer = new LangSourceViewer(parent, null, null,
			showAnnotationsOverview, styles);
		
		ColorManager2 colorManager = new ColorManager2();
		sourceViewer.addOwned(colorManager);
		
		SimpleSourceViewerConfiguration configuration = createSimpleSourceViewerConfig(store, colorManager);
		sourceViewer.configure(configuration);
		return sourceViewer;
	}
	
	protected SimpleSourceViewerConfiguration createSimpleSourceViewerConfig( 
			IPreferenceStore preferenceStore, ColorManager2 _colorManager) {
		return new SimpleSourceViewerConfiguration(preferenceStore) {
			@Override
			protected ColorManager2 init_ColorManager() {
				return _colorManager;
			}
			
			@Override
			protected StylingPreferences init_StylePreferences() {
				return overlayStylingPrefs;
			}
		};
	}
	
}