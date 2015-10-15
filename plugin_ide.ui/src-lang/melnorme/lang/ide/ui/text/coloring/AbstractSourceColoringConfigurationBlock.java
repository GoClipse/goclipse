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
import org.eclipse.swt.widgets.Link;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.text.LangDocumentPartitionerSetup;
import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.LangSourceViewer;
import melnorme.lang.ide.ui.preferences.PreferencesMessages;
import melnorme.lang.ide.ui.preferences.common.IPreferencesWidget;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.SimpleSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.coloring.StylingPreferences.OverlayStylingPreferences;
import melnorme.lang.ide.ui.text.coloring.TextStyling.TextStylingData;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.AbstractComponent;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.ColorField;
import melnorme.util.swt.jface.ElementContentProvider2;
import melnorme.util.swt.jface.LabeledTreeElement;
import melnorme.util.swt.jface.LabeledTreeElement.LabeledTreeElementLabelProvider;
import melnorme.util.swt.jface.TreeViewerExt;
import melnorme.util.swt.jface.text.ColorManager2;
import melnorme.utilbox.fields.IDomainField;
import melnorme.utilbox.fields.IFieldValueListener;
import melnorme.utilbox.fields.IProperty;
import melnorme.utilbox.misc.StreamUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.tree.IElement;
import melnorme.utilbox.tree.SimpleTreeElement;
import melnorme.utilbox.tree.TreeVisitor;

/**
 * A configuration component for syntax (and possibly semantic) source highlighting options.
 */
public abstract class AbstractSourceColoringConfigurationBlock extends AbstractComponent 
	implements IPreferencesWidget {
		
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

	public AbstractSourceColoringConfigurationBlock() {
		this.coloringOptionsList = new SourceColoringListRoot();
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
	
	public class SourceColoringElement extends LabeledTreeElement {
		
		protected final ThemedTextStylingPreference stylingPref;
		protected final String prefId;
		protected final IDomainField<TextStyling> temporaryPref;
		
		public SourceColoringElement(String labelText, ThemedTextStylingPreference stylingPref) {
			super(null, null, labelText);
			this.stylingPref = stylingPref;
			this.prefId = stylingPref.getPrefId();
			this.temporaryPref = overlayStylingPrefs.get(prefId);
			this.temporaryPref.setValue(stylingPref.getFieldValue());
		}
		
		public TextStyling getWorkingValue() {
			return temporaryPref.getValue();
		}
		
		public void loadDefaults() {
			temporaryPref.setValue(stylingPref.getDefaultValue());
		}
		
		public void saveToGlobalPreferences() {
			try {
				stylingPref.setInstanceScopeValue(temporaryPref.getValue());
			} catch(BackingStoreException e) {
				// Ignore
				/* FIXME: */
			}
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
	public void loadDefaults() {
		visitColoringItems(item -> item.loadDefaults());
		
		updateComponentFromInput();
	}
	
	@Override
	public boolean saveSettings() {
		visitColoringItems(item -> item.saveToGlobalPreferences());
		return true;
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return glFillDefaults().spacing(0, 5).numColumns(getPreferredLayoutColumns());
	}
	
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
			gdFillDefaults().hint(pc.convertWidthInCharsToPixels(50), SWT.DEFAULT).create());
		
		ControlUtils.createHorizontalSpacer(topControl, 1, pc);
		
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, 
			PreferencesMessages.DLTKEditorPreferencePage_coloring_element, 
			gdFillDefaults().create());
		
		Composite editorComposite = SWTFactoryUtil.createComposite(topControl);
		editorComposite.setLayoutData(
			gdFillDefaults().grab(true, false).create());
		editorComposite.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).margins(0, 0).create());
		
		treeViewer = new TreeViewerExt(editorComposite, SWT.SINGLE | SWT.BORDER);
		treeViewer.setContentProvider(new ElementContentProvider2());
		treeViewer.setLabelProvider(new LabeledTreeElementLabelProvider());
		treeViewer.getTree().setLayoutData(
			gdFillDefaults().hint(pc.convertWidthInCharsToPixels(40), pc.convertHeightInCharsToPixels(10)).create());
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
			gdFillDefaults().create());
		itemEditorComposite.setLayout(GridLayoutFactory.swtDefaults().extendedMargins(5, 20, 5, 5).create());
		
		enableField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_enable);
		enableField.createComponent(itemEditorComposite, gdFillDefaults().create());
		
		colorField = new ColorField(PreferencesMessages.DLTKEditorPreferencePage_color);
		colorField.createComponent(itemEditorComposite, gdFillDefaults().indent(20, 0).create());
		boldCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_bold);
		boldCheckboxField.createComponent(itemEditorComposite, gdFillDefaults().indent(20, 0).create());
		italicCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_italic);
		italicCheckboxField.createComponent(itemEditorComposite, gdFillDefaults().indent(20, 0).create());
		striketroughCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_strikethrough);
		striketroughCheckboxField.createComponent(itemEditorComposite, gdFillDefaults().indent(20, 0).create());
		underlineCheckboxField = new CheckBoxField(PreferencesMessages.DLTKEditorPreferencePage_underline);
		underlineCheckboxField.createComponent(itemEditorComposite, gdFillDefaults().indent(20, 0).create());
		
		enableField.addValueChangedListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isEnabled = enableField.getBooleanFieldValue();
				setColoringEditorControlsEnabled(data.isEnabled);
			}
		});
		
		colorField.addValueChangedListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.rgb = colorField.getFieldValue();
			}
		});
		boldCheckboxField.addValueChangedListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isBold = boldCheckboxField.getBooleanFieldValue();
			}
		});
		italicCheckboxField.addValueChangedListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isItalic = italicCheckboxField.getBooleanFieldValue();
			}
		});
		striketroughCheckboxField.addValueChangedListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isStrikethrough = striketroughCheckboxField.getBooleanFieldValue();
			}
		});
		underlineCheckboxField.addValueChangedListener(new ChangeStylingField() {
			@Override
			protected void changeStylingValue(TextStylingData data) {
				data.isUnderline = underlineCheckboxField.getBooleanFieldValue();
			}
		});
		
		SWTFactoryUtil.createLabel(topControl, SWT.LEFT, 
			PreferencesMessages.DLTKEditorPreferencePage_preview, 
			gdFillDefaults().create());
		
		
		Control previewViewerControl = createPreviewViewer(topControl);
		previewViewerControl.setLayoutData(
			gdFillDefaults().hint(pc.convertWidthInCharsToPixels(50), pc.convertHeightInCharsToPixels(15)).
			grab(true, true).
			create());
	}
	
	@Override
	public void updateComponentFromInput() {
		handleAppearanceColorListSelectionChanged();
	}
	
	protected abstract class ChangeStylingField implements IFieldValueListener {
		@Override
		public void fieldValueChanged() {
			IProperty<TextStyling> field = getSelectedColoringItem().temporaryPref;
			TextStyling newStyling = field.getValue();
			TextStylingData data = newStyling.getData();
			changeStylingValue(data);
			field.setValue(new TextStyling(data));
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
			LangUIPlugin.logInternalError(e);
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
		
		AbstractLangSourceViewerConfiguration configuration = createSimpleSourceViewerConfig(store, colorManager);
		sourceViewer.configure(configuration);
		return sourceViewer;
	}
	
	protected AbstractLangSourceViewerConfiguration createSimpleSourceViewerConfig( 
			IPreferenceStore preferenceStore, ColorManager2 colorManager) {
		return new SimpleSourceViewerConfiguration(preferenceStore, colorManager, overlayStylingPrefs);
	}
	
}