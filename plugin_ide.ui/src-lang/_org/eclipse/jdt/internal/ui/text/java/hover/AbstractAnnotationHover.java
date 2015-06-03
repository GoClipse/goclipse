/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package _org.eclipse.jdt.internal.ui.text.java.hover;

import java.util.Iterator;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension2;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;

import _org.eclipse.jdt.internal.ui.javaeditor.JavaAnnotationIterator;


/**
 * Abstract super class for annotation hovers.
 *
 * @since 3.0
 */
public abstract class AbstractAnnotationHover extends AbstractJavaEditorTextHover {

	/**
	 * An annotation info contains information about an {@link Annotation}
	 * It's used as input for the {@link AbstractAnnotationHover.AnnotationInformationControl}
	 *
	 * @since 3.4
	 */
	protected static class AnnotationInfo {
		public final Annotation annotation;
		public final Position position;
		public final ITextViewer viewer;

		public AnnotationInfo(Annotation annotation, Position position, ITextViewer textViewer) {
			this.annotation= annotation;
			this.position= position;
			this.viewer= textViewer;
		}

		/**
		 * Create completion proposals which can resolve the given annotation at
		 * the given position. Returns an empty array if no such proposals exist.
		 *
		 * @return the proposals or an empty array
		 */
		public ICompletionProposal[] getCompletionProposals() {
			return new ICompletionProposal[0];
		}

		/**
		 * Adds actions to the given toolbar.
		 *
		 * @param manager the toolbar manager to add actions to
		 * @param infoControl the information control
		 */
		public void fillToolBar(ToolBarManager manager, IInformationControl infoControl) {
//			ConfigureAnnotationsAction configureAnnotationsAction= new ConfigureAnnotationsAction(annotation, infoControl);
//			manager.add(configureAnnotationsAction);
		}
	}

	/**
	 * The annotation information control shows informations about a given
	 * {@link AbstractAnnotationHover.AnnotationInfo}. It can also show a toolbar
	 * and a list of {@link ICompletionProposal}s.
	 *
	 * @since 3.4
	 */
	private static class AnnotationInformationControl extends AbstractInformationControl implements IInformationControlExtension2 {

		private final DefaultMarkerAnnotationAccess fMarkerAnnotationAccess;
		private Control fFocusControl;
		private AnnotationInfo fInput;
		private Composite fParent;

		public AnnotationInformationControl(Shell parentShell, String statusFieldText) {
			super(parentShell, statusFieldText);

			fMarkerAnnotationAccess= new DefaultMarkerAnnotationAccess();
			create();
		}

		public AnnotationInformationControl(Shell parentShell, ToolBarManager toolBarManager) {
			super(parentShell, toolBarManager);

			fMarkerAnnotationAccess= new DefaultMarkerAnnotationAccess();
			create();
		}

		/*
		 * @see org.eclipse.jface.text.IInformationControl#setInformation(java.lang.String)
		 */
		@Override
		public void setInformation(String information) {
			//replaced by IInformationControlExtension2#setInput
		}

		/*
		 * @see org.eclipse.jface.text.IInformationControlExtension2#setInput(java.lang.Object)
		 */
		@Override
		public void setInput(Object input) {
			Assert.isLegal(input instanceof AnnotationInfo);
			fInput= (AnnotationInfo)input;
			disposeDeferredCreatedContent();
			deferredCreateContent();
		}

		/*
		 * @see org.eclipse.jface.text.IInformationControlExtension#hasContents()
		 */
		@Override
		public boolean hasContents() {
			return fInput != null;
		}

		private AnnotationInfo getAnnotationInfo() {
			return fInput;
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractAnnotationHover.AbstractInformationControl#setFocus()
		 */
		@Override
		public void setFocus() {
			super.setFocus();
			if (fFocusControl != null)
				fFocusControl.setFocus();
		}

		/*
		 * @see org.eclipse.jface.text.AbstractInformationControl#setVisible(boolean)
		 */
		@Override
		public final void setVisible(boolean visible) {
			if (!visible)
				disposeDeferredCreatedContent();
			super.setVisible(visible);
		}

		protected void disposeDeferredCreatedContent() {
			Control[] children= fParent.getChildren();
			for (int i= 0; i < children.length; i++) {
				children[i].dispose();
			}
			ToolBarManager toolBarManager= getToolBarManager();
			if (toolBarManager != null)
				toolBarManager.removeAll();
		}

		/*
		 * @see org.eclipse.jface.text.AbstractInformationControl#createContent(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected void createContent(Composite parent) {
			fParent= parent;
			GridLayout layout= new GridLayout(1, false);
			layout.verticalSpacing= 0;
			layout.marginWidth= 0;
			layout.marginHeight= 0;
			fParent.setLayout(layout);
		}

		/*
		 * @see org.eclipse.jface.text.AbstractInformationControl#computeSizeHint()
		 */
		@Override
		public Point computeSizeHint() {
			Point preferedSize= getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

			Point constrains= getSizeConstraints();
			if (constrains == null)
				return preferedSize;

			int trimWidth= getShell().computeTrim(0, 0, 0, 0).width;
			Point constrainedSize= getShell().computeSize(constrains.x - trimWidth, SWT.DEFAULT, true);

			int width= Math.min(preferedSize.x, constrainedSize.x);
			int height= Math.max(preferedSize.y, constrainedSize.y);

			return new Point(width, height);
		}

		/**
		 * Fills the toolbar actions, if a toolbar is available. This
		 * is called after the input has been set.
		 */
		protected void fillToolbar() {
			ToolBarManager toolBarManager= getToolBarManager();
			if (toolBarManager == null)
				return;
			fInput.fillToolBar(toolBarManager, this);
			toolBarManager.update(true);
		}

		/**
		 * Create content of the hover. This is called after
		 * the input has been set.
		 */
		protected void deferredCreateContent() {
			fillToolbar();

			createAnnotationInformation(fParent, getAnnotationInfo().annotation);
			setColorAndFont(fParent, fParent.getForeground(), fParent.getBackground(), JFaceResources.getDialogFont());

//			ICompletionProposal[] proposals= getAnnotationInfo().getCompletionProposals();
//			if (proposals.length > 0)
//				createCompletionProposalsControl(fParent, proposals);

			fParent.layout(true);
		}

		private void setColorAndFont(Control control, Color foreground, Color background, Font font) {
			control.setForeground(foreground);
			control.setBackground(background);
			control.setFont(font);

			if (control instanceof Composite) {
				Control[] children= ((Composite) control).getChildren();
				for (int i= 0; i < children.length; i++) {
					setColorAndFont(children[i], foreground, background, font);
				}
			}
		}

		private void createAnnotationInformation(Composite parent, final Annotation annotation) {
			Composite composite= new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			GridLayout layout= new GridLayout(2, false);
			layout.marginHeight= 2;
			layout.marginWidth= 2;
			layout.horizontalSpacing= 0;
			composite.setLayout(layout);

			final Canvas canvas= new Canvas(composite, SWT.NO_FOCUS);
			GridData gridData= new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
			gridData.widthHint= 17;
			gridData.heightHint= 16;
			canvas.setLayoutData(gridData);
			canvas.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					e.gc.setFont(null);
					fMarkerAnnotationAccess.paint(annotation, e.gc, canvas, new Rectangle(0, 0, 16, 16));
				}
			});

			StyledText text= new StyledText(composite, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
			GridData data= new GridData(SWT.FILL, SWT.FILL, true, true);
			text.setLayoutData(data);
			String annotationText= annotation.getText();
			if (annotationText != null)
				text.setText(annotationText);
		}

//		private void createCompletionProposalsControl(Composite parent, ICompletionProposal[] proposals) {
//			Composite composite= new Composite(parent, SWT.NONE);
//			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//			GridLayout layout2= new GridLayout(1, false);
//			layout2.marginHeight= 0;
//			layout2.marginWidth= 0;
//			layout2.verticalSpacing= 2;
//			composite.setLayout(layout2);
//
//			Label separator= new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
//			GridData gridData= new GridData(SWT.FILL, SWT.CENTER, true, false);
//			separator.setLayoutData(gridData);
//
//			Label quickFixLabel= new Label(composite, SWT.NONE);
//			GridData layoutData= new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
//			layoutData.horizontalIndent= 4;
//			quickFixLabel.setLayoutData(layoutData);
//			String text;
//			if (proposals.length == 1) {
//				text= JavaHoverMessages.AbstractAnnotationHover_message_singleQuickFix;
//			} else {
//				text= Messages.format(JavaHoverMessages.AbstractAnnotationHover_message_multipleQuickFix, new Object[] { String.valueOf(proposals.length) });
//			}
//			quickFixLabel.setText(text);
//
//			setColorAndFont(composite, parent.getForeground(), parent.getBackground(), JFaceResources.getDialogFont());
//			createCompletionProposalsList(composite, proposals);
//		}
//
//		private void createCompletionProposalsList(Composite parent, ICompletionProposal[] proposals) {
//			final ScrolledComposite scrolledComposite= new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
//			GridData gridData= new GridData(SWT.FILL, SWT.FILL, true, true);
//			scrolledComposite.setLayoutData(gridData);
//			scrolledComposite.setExpandVertical(false);
//			scrolledComposite.setExpandHorizontal(false);
//
//			Composite composite= new Composite(scrolledComposite, SWT.NONE);
//			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//			GridLayout layout= new GridLayout(2, false);
//			layout.marginLeft= 5;
//			layout.verticalSpacing= 2;
//			composite.setLayout(layout);
//			
//			List<Link> list= new ArrayList<Link>();
//			for (int i= 0; i < proposals.length; i++) {
//				list.add(createCompletionProposalLink(composite, proposals[i], 1));// Original link for single fix, hence pass 1 for count
//
//				if (proposals[i] instanceof FixCorrectionProposal) {
//					FixCorrectionProposal proposal= (FixCorrectionProposal)proposals[i];
//					int count= proposal.computeNumberOfFixesForCleanUp(proposal.getCleanUp());
//					if (count > 1) {
//						list.add(createCompletionProposalLink(composite, proposals[i], count));
//					}
//				}
//			}
//			final Link[] links= list.toArray(new Link[list.size()]);
//
//			scrolledComposite.setContent(composite);
//			setColorAndFont(scrolledComposite, parent.getForeground(), parent.getBackground(), JFaceResources.getDialogFont());
//
//			Point contentSize= composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//			composite.setSize(contentSize);
//
//			Point constraints= getSizeConstraints();
//			if (constraints != null && contentSize.x < constraints.x) {
//				ScrollBar horizontalBar= scrolledComposite.getHorizontalBar();
//
//				int scrollBarHeight;
//				if (horizontalBar == null) {
//					Point scrollSize= scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//					scrollBarHeight= scrollSize.y - contentSize.y;
//				} else {
//					scrollBarHeight= horizontalBar.getSize().y;
//				}
//				gridData.heightHint= contentSize.y - scrollBarHeight;
//			}
//
//			fFocusControl= links[0];
//			for (int i= 0; i < links.length; i++) {
//				final int index= i;
//				final Link link= links[index];
//				link.addKeyListener(new KeyListener() {
//					@Override
//					public void keyPressed(KeyEvent e) {
//						switch (e.keyCode) {
//							case SWT.ARROW_DOWN:
//								if (index + 1 < links.length) {
//									links[index + 1].setFocus();
//								}
//								break;
//							case SWT.ARROW_UP:
//								if (index > 0) {
//									links[index - 1].setFocus();
//								}
//								break;
//							default:
//								break;
//						}
//					}
//
//					@Override
//					public void keyReleased(KeyEvent e) {
//					}
//				});
//
//				link.addFocusListener(new FocusListener() {
//					@Override
//					public void focusGained(FocusEvent e) {
//						int currentPosition= scrolledComposite.getOrigin().y;
//						int hight= scrolledComposite.getSize().y;
//						int linkPosition= link.getLocation().y;
//
//						if (linkPosition < currentPosition) {
//							if (linkPosition < 10)
//								linkPosition= 0;
//
//							scrolledComposite.setOrigin(0, linkPosition);
//						} else if (linkPosition + 20 > currentPosition + hight) {
//							scrolledComposite.setOrigin(0, linkPosition - hight + link.getSize().y);
//						}
//					}
//
//					@Override
//					public void focusLost(FocusEvent e) {
//					}
//				});
//			}
//		}
//
//		private Link createCompletionProposalLink(Composite parent, final ICompletionProposal proposal, int count) {
//			final boolean isMultiFix= count > 1;
//			if (isMultiFix) {
//				new Label(parent, SWT.NONE); // spacer to fill image cell
//				parent= new Composite(parent, SWT.NONE); // indented composite for multi-fix
//				GridLayout layout= new GridLayout(2, false);
//				layout.marginWidth= 0;
//				layout.marginHeight= 0;
//				parent.setLayout(layout);
//			}
//			
//			Label proposalImage= new Label(parent, SWT.NONE);
//			proposalImage.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
//			Image image= isMultiFix ? JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_MULTI_FIX) : proposal.getImage();
//			if (image != null) {
//				proposalImage.setImage(image);
//
//				proposalImage.addMouseListener(new MouseListener() {
//
//					@Override
//					public void mouseDoubleClick(MouseEvent e) {
//					}
//
//					@Override
//					public void mouseDown(MouseEvent e) {
//					}
//
//					@Override
//					public void mouseUp(MouseEvent e) {
//						if (e.button == 1) {
//							apply(proposal, fInput.viewer, fInput.position.offset, isMultiFix);
//						}
//					}
//
//				});
//			}
//
//			Link proposalLink= new Link(parent, SWT.NONE);
//			GridData layoutData= new GridData(SWT.FILL, SWT.CENTER, true, false);
//			String linkText;
//			if (isMultiFix) {
//				linkText= Messages.format(JavaHoverMessages.AbstractAnnotationHover_multifix_variable_description, new Integer(count));
//			} else {
//				linkText= proposal.getDisplayString();
//			}
//			proposalLink.setText("<a>" + linkText + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
//			proposalLink.setLayoutData(layoutData);
//			proposalLink.addSelectionListener(new SelectionAdapter() {
//				/*
//				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
//				 */
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					apply(proposal, fInput.viewer, fInput.position.offset, isMultiFix);
//				}
//			});
//			return proposalLink;
//		}
//
//		protected void apply(ICompletionProposal p, ITextViewer viewer, int offset, boolean isMultiFix) {
//			//Focus needs to be in the text viewer, otherwise linked mode does not work
//			dispose();
//
//			IRewriteTarget target= null;
//			try {
//				IDocument document= viewer.getDocument();
//
//				if (viewer instanceof ITextViewerExtension) {
//					ITextViewerExtension extension= (ITextViewerExtension) viewer;
//					target= extension.getRewriteTarget();
//				}
//
//				if (target != null)
//					target.beginCompoundChange();
//
//				if (p instanceof ICompletionProposalExtension2) {
//					ICompletionProposalExtension2 e= (ICompletionProposalExtension2) p;
//					e.apply(viewer, (char) 0, isMultiFix ? SWT.CONTROL : SWT.NONE, offset);
//				} else if (p instanceof ICompletionProposalExtension) {
//					ICompletionProposalExtension e= (ICompletionProposalExtension) p;
//					e.apply(document, (char) 0, offset);
//				} else {
//					p.apply(document);
//				}
//
//				Point selection= p.getSelection(document);
//				if (selection != null) {
//					viewer.setSelectedRange(selection.x, selection.y);
//					viewer.revealRange(selection.x, selection.y);
//				}
//			} finally {
//				if (target != null)
//					target.endCompoundChange();
//			}
//		}
	}

	/**
	 * Presenter control creator.
	 *
	 * @since 3.4
	 */
	private static final class PresenterControlCreator extends AbstractReusableInformationControlCreator {
		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.widgets.Shell)
		 */
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			return new AnnotationInformationControl(parent, new ToolBarManager(SWT.FLAT));
		}
	}


	/**
	 * Hover control creator.
	 *
	 * @since 3.4
	 */
	private static final class HoverControlCreator extends AbstractReusableInformationControlCreator {
		private final IInformationControlCreator fPresenterControlCreator;

		public HoverControlCreator(IInformationControlCreator presenterControlCreator) {
			fPresenterControlCreator= presenterControlCreator;
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.widgets.Shell)
		 */
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			return new AnnotationInformationControl(parent, EditorsUI.getTooltipAffordanceString()) {
				/*
				 * @see org.eclipse.jface.text.IInformationControlExtension5#getInformationPresenterControlCreator()
				 */
				@Override
				public IInformationControlCreator getInformationPresenterControlCreator() {
					return fPresenterControlCreator;
				}
			};
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#canReuse(org.eclipse.jface.text.IInformationControl)
		 */
		@Override
		public boolean canReuse(IInformationControl control) {
			if (!super.canReuse(control))
				return false;

			if (control instanceof IInformationControlExtension4)
				((IInformationControlExtension4) control).setStatusText(EditorsUI.getTooltipAffordanceString());

			return true;
		}
	}

	private final IPreferenceStore fStore= LangUIPlugin.getDefault().getCombinedPreferenceStore();
	private final DefaultMarkerAnnotationAccess fAnnotationAccess= new DefaultMarkerAnnotationAccess();
	private final boolean fAllAnnotations;

	/**
	 * The hover control creator.
	 *
	 * @since 3.4
	 */
	private IInformationControlCreator fHoverControlCreator;
	/**
	 * The presentation control creator.
	 *
	 * @since 3.4
	 */
	private IInformationControlCreator fPresenterControlCreator;


	public AbstractAnnotationHover(boolean allAnnotations) {
		fAllAnnotations= allAnnotations;
	}

	/**
	 * @deprecated As of 3.4, replaced by {@link ITextHoverExtension2#getHoverInfo2(ITextViewer, IRegion)}
	 * @see org.eclipse.jface.text.ITextHover#getHoverInfo(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 */
	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return null;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractJavaEditorTextHover#getHoverInfo2(org.eclipse.jface.text.ITextViewer, org.eclipse.jface.text.IRegion)
	 * @since 3.4
	 */
	@Override
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		IPath path;
		IAnnotationModel model;
		if (textViewer instanceof ISourceViewer) {
			path= null;
			model= ((ISourceViewer)textViewer).getAnnotationModel();
		} else {
			// Get annotation model from file buffer manager
			path= getEditorInputPath();
			model= getAnnotationModel(path);
		}
		if (model == null)
			return null;

		try {
			Iterator<Annotation> parent;
			if (model instanceof IAnnotationModelExtension2)
				parent= ((IAnnotationModelExtension2)model).getAnnotationIterator(hoverRegion.getOffset(), hoverRegion.getLength(), true, true);
			else
				parent= model.getAnnotationIterator();
			Iterator<Annotation> e= new JavaAnnotationIterator(parent, fAllAnnotations);

			int layer= -1;
			Annotation annotation= null;
			Position position= null;
			while (e.hasNext()) {
				Annotation a= e.next();

				AnnotationPreference preference= getAnnotationPreference(a);
				if (preference == null || !(preference.getTextPreferenceKey() != null && fStore.getBoolean(preference.getTextPreferenceKey()) || (preference.getHighlightPreferenceKey() != null && fStore.getBoolean(preference.getHighlightPreferenceKey()))))
					continue;

				Position p= model.getPosition(a);

				int l= fAnnotationAccess.getLayer(a);

				if (l > layer && p != null && p.overlapsWith(hoverRegion.getOffset(), hoverRegion.getLength())) {
					String msg= a.getText();
					if (msg != null && msg.trim().length() > 0) {
						layer= l;
						annotation= a;
						position= p;
					}
				}
			}
			if (layer > -1)
				return createAnnotationInfo(annotation, position, textViewer);

		} finally {
			try {
				if (path != null) {
					ITextFileBufferManager manager= FileBuffers.getTextFileBufferManager();
					manager.disconnect(path, LocationKind.NORMALIZE, null);
				}
			} catch (CoreException ex) {
				LangUIPlugin.logStatus(ex);
			}
		}

		return null;
	}

	protected AnnotationInfo createAnnotationInfo(Annotation annotation, Position position, ITextViewer textViewer) {
		return new AnnotationInfo(annotation, position, textViewer);
	}

	/*
	 * @see ITextHoverExtension#getHoverControlCreator()
	 * @since 3.4
	 */
	@Override
	public IInformationControlCreator getHoverControlCreator() {
		if (fHoverControlCreator == null)
			fHoverControlCreator= new HoverControlCreator(getInformationPresenterControlCreator());
		return fHoverControlCreator;
	}

	public IInformationControlCreator getInformationPresenterControlCreator() {
		if (fPresenterControlCreator == null)
			fPresenterControlCreator= new PresenterControlCreator();
		return fPresenterControlCreator;
	}

	private IPath getEditorInputPath() {
		if (getEditor() == null)
			return null;

		IEditorInput input= getEditor().getEditorInput();
		if (input instanceof IStorageEditorInput) {
			try {
				return ((IStorageEditorInput)input).getStorage().getFullPath();
			} catch (CoreException ex) {
				LangUIPlugin.logStatus(ex);
			}
		}
		return null;
	}

	protected IAnnotationModel getAnnotationModel(IPath path) {
		if (path == null)
			return null;

		ITextFileBufferManager manager= FileBuffers.getTextFileBufferManager();
		try {
			manager.connect(path, LocationKind.NORMALIZE, null);
		} catch (CoreException ex) {
			LangUIPlugin.logStatus(ex);
			return null;
		}

		IAnnotationModel model= null;
		try {
			model= manager.getTextFileBuffer(path, LocationKind.NORMALIZE).getAnnotationModel();
			return model;
		} finally {
			if (model == null) {
				try {
					manager.disconnect(path, LocationKind.NORMALIZE, null);
				} catch (CoreException ex) {
					LangUIPlugin.logStatus(ex);
				}
			}
		}
	}

	/**
	 * Returns the annotation preference for the given annotation.
	 *
	 * @param annotation the annotation
	 * @return the annotation preference or <code>null</code> if none
	 */
	private static AnnotationPreference getAnnotationPreference(Annotation annotation) {

		if (annotation.isMarkedDeleted())
			return null;
		return EditorsUI.getAnnotationPreferenceLookup().getAnnotationPreference(annotation);
	}
}
