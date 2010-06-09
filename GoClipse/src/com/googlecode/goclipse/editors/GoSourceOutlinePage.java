package com.googlecode.goclipse.editors;

import java.util.Vector;

import javax.swing.JLabel;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

// TODO: Add icons
// TODO: Add focus handlers
// TODO: Add document listeners

public class GoSourceOutlinePage extends ContentOutlinePage {
	private GoSourceEditor fEditor;
	private IDocumentProvider fDocProvider;
	private IEditorInput fInput;

	private ASTNode fRoot;

	public GoSourceOutlinePage(IDocumentProvider documentProvider,
			GoSourceEditor goSourceEditor) {
		super();
		fDocProvider = documentProvider;
		fEditor = goSourceEditor;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);

		TreeViewer viewer = getTreeViewer();
		fRoot = ASTNode.parse("DOCUMENT", "");

		viewer.setContentProvider(new ITreeContentProvider() {
			public void dispose() {
				// Do nothing... everything is in the parent class
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				System.out.println("ITreeContentProvider.inputChanged("
						+ viewer.toString() + ","
						+ ((oldInput != null) ? oldInput.toString() : "(null)")
						+ ","
						+ ((newInput != null) ? newInput.toString() : "(null)")
						+ ")");
				fInput = (IEditorInput) newInput;
				
				if (fEditor.getDocumentProvider() == null) return;
				System.out.println("+++ DocumentProvider exists!");

				if (fEditor.getDocumentProvider().getDocument(fInput) == null) return;
				System.out.println("+++ Document exists!");
				
				fRoot = ASTNode.parse("DOCUMENT", fEditor.getDocumentProvider().getDocument(fInput).get());
			}

			public Object[] getElements(Object inputElement) {
				System.out.println("ITreeContentProvider.getElements("
						+ inputElement.toString() + ")");
				if (fRoot == null)
					return null;
				return new Object[] { fRoot };
			}

			public Object[] getChildren(Object parentElement) {
				System.out.println("ITreeContentProvider.getChildren("
						+ parentElement.toString() + ")");
				if (fRoot == null)
					return null;
				return fRoot.getChildrenOf(parentElement);
			}

			public Object getParent(Object element) {
				System.out.println("ITreeContentProvider.getParent("
						+ element.toString() + ")");
				if (fRoot == null)
					return null;
				return fRoot.getParentOf(element);
			}

			public boolean hasChildren(Object element) {
				System.out.println("ITreeContentProvider.hasChildren("
						+ element.toString() + ")");
				if (fRoot == null)
					return false;
				Object[] children = fRoot.getChildrenOf(element);
				return children != null && children.length > 0;
			}
		});

		viewer.setLabelProvider(new ILabelProvider() {

			public void addListener(ILabelProviderListener listener) {
				System.out.println("ILabelProvider.sddListener("
						+ listener.toString() + ")");
			}

			public void dispose() {
				System.out.println("ILabelProvider.dispose()");
			}

			public boolean isLabelProperty(Object element, String property) {
				System.out.println("ILabelProvider.isLabelProperty("
						+ element.toString() + "," + property + ")");
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
				System.out.println("ILabelProvider.removeListener("
						+ listener.toString() + ")");
			}

			public Image getImage(Object element) {
				System.out.println("ILabelProvider.getImage("
						+ element.toString() + ")");
				return null;
			}

			public String getText(Object element) {
				System.out.println("ILabelProvider.getText("
						+ element.toString() + ")");
				if (!ASTNode.class.equals(element.getClass())) {
					System.out.println(" -- Not an ASTNode");
					return null;
				}
				ASTNode astnode = (ASTNode) element;
				return astnode.getElement();
			}
		});
		viewer.addSelectionChangedListener(this);

		if (fInput != null)
			viewer.setInput(fInput);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		super.selectionChanged(event);
	}

	public void setFocus() {
		// TODO Auto-generated method stub
		super.setFocus();
	}

	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub
		super.setSelection(selection);
	}

	public void setInput(IEditorInput editorInput) {
		fInput = editorInput;
	}

	private static class ASTNode {
		private String fType;
		private ASTNode[] fChildren;
		private String fElement;

		public static ASTNode parse(String type, String text) {
			String[] pieces = tokenize(text);
			Vector<ASTNode> children = new Vector<ASTNode>();
			if (pieces.length == 0)
				new ASTNode("EMPTY", "[EMPTY]", new ASTNode[0]); // shouldn't happen?
			if (pieces.length == 1)
				new ASTNode("TEXT", text, new ASTNode[0]);
			for (int i = 0; i < pieces.length; ++i)
			{
				String piece = pieces[i];
				if (startsWith(piece, "package"))
				{
					children.add(new ASTNode("TEXT", piece, new ASTNode[0]));
				}
				else if (startsWith(piece, "import"))
				{
					int paren = piece.indexOf('(');
					if (paren < 0) continue;
					
					children.add(parse("IMPORT", piece.substring(paren)));
				}
				else if (startsWith(piece, "const"))
				{
					int paren = piece.indexOf('(');
					if (paren < 0) continue;
					
					children.add(parse("CONST", piece.substring(paren)));
				}
				else if (startsWith(piece, "var"))
				{
					int paren = piece.indexOf('(');
					if (paren < 0) continue;
					
					children.add(parse("VAR", piece.substring(paren)));
				}
				else if (startsWith(piece, "func") && i+1 < pieces.length)
				{
					String body = pieces[i+1];
					ASTNode function = parse("FUNCTION", body);
					function.fElement = piece; // function header
					children.add(function);
					++i; // skip the body piece
				}
				else if (startsWith(piece, "if") && i+1 < pieces.length)
				{
					String body = pieces[i+1];
					ASTNode ifstmt = parse("CONDITIONAL", body);
					ifstmt.fElement = piece; // function header
					children.add(ifstmt);
					++i; // skip the body piece
				}
				else if (startsWith(piece, "for") && i+1 < pieces.length)
				{
					String body = pieces[i+1];
					ASTNode loopstmt = parse("LOOP", body);
					loopstmt.fElement = piece; // function header
					children.add(loopstmt);
					++i; // skip the body piece
				}
				else if (startsWith(piece, "type") && i+1 < pieces.length && pieces[i+1].charAt(0) == '{')
				{
					String body = pieces[i+1];
					ASTNode typestmt = parse("TYPE", body);
					typestmt.fElement = piece; // function header
					children.add(typestmt);
					++i; // skip the body piece
				}
				else // text node
					children.add(new ASTNode("TEXT", piece, new ASTNode[0]));
			}
			if (type == "DOCUMENT") text = "[DOCUMENT]";
			else if (type == "IMPORT") text = "[IMPORT]";
			else if (type == "CONST") text = "[CONSTANTS]";
			else if (type == "VAR") text = "[VARIABLES]";
			return new ASTNode(type, text, children.toArray(new ASTNode[0]));
		}

		private static boolean startsWith(String str, String prefix) {
			return str.length() > prefix.length() && str.substring(0, prefix.length()).equals(prefix) && Character.isWhitespace(str.charAt(prefix.length()));
		}

		private static String[] tokenize(String text) {
			Vector<String> pieces = new Vector<String>();
			
			// Trivial case
			if (text.length() == 0) return new String[0];
			
			// Strip down internal blocks
			text = text.trim();
			char first = text.charAt(0);
			char last = text.charAt(text.length()-1);
			if ((first == '{' && last == '}') || (first == '(' && last == ')'))
				text = text.substring(1, text.length()-1).trim();
			
			System.err.println("Tokenizing: " + text);
			
			// Mark the beginning and end of the token
			int start = 0;
			int end = 0;
			
			// Find the tokens
			while (start < text.length()) {
				// Find the start of the token
				while (start < text.length() && Character.isWhitespace(text.charAt(start)))
					++start;
				
				// Reset the end and ignore
				end = start;
				
				// Find the end of the token
				while (end < text.length()) {
					
					char ch = text.charAt(end);
					if (ch == '\r' || ch == '\n')
						break;
					if (ch == '(' || ch == '{') { // find match
						// Only find matching if this brace is in its own token
						if (ch == '{' && end > start) break;
						
						// Nested search (facilitates recursive descent)
						int depth = 1;
						++end;
						if (ch == '(') {
							while (end < text.length() && depth > 0) {
								switch (text.charAt(end)) {
								case '"': end += quoteLength(text, end) - 1; break;
								case '/': end += commentLength(text, end) - 1; break;
								case '(': ++depth; break;
								case ')': --depth; break;
								}
								if (depth > 0) ++end; // we want it to skip only the )
							} // closing paren search
							// This doesn't break so that it stays with its predecessor
						} // open paren -> find closing paren
						else if (ch == '{')
						{
							while (end < text.length() && depth > 0) {
								switch (text.charAt(end)) {
								case '"': end += quoteLength(text, end) - 1; break;
								case '/': end += commentLength(text, end) - 1; break;
								case '{': ++depth; break;
								case '}': --depth; break;
								}
								++end;
							} // closing paren search
							
							// We always want these in their own tokens
							break;
						} // open brace -> find closing brace
					} // groupings
					if (ch == '"')
						end += quoteLength(text, end) - 1;
					else if (ch == '/')
						end += commentLength(text, end) - 1;
					
					// If we didn't break, move on to the next character
					++end;
				} // token end search

				if (end > text.length()) end = text.length();
				System.err.print("- "
						+ Integer.toString(start) + ","
						+ Integer.toString(end) + ": ");
				System.err.println(text.substring(start, end));
				String token = text.substring(start, end);
				pieces.add(token.trim());
				
				// Move on to the next token
				start = end;
			} // token finding
			
			// Return an array
			return pieces.toArray(new String[0]);
		}
		
		// TODO: Optimize
		private static int quoteLength(String text, int begin)
		{
			int end = begin + 1;
			while (end < text.length()) {
				if (text.charAt(end) == text.charAt(begin) && 
						(end > 0 && text.charAt(end-1) != '\\'))
					break;
				++end;
			} // find the end of the string
			if (end > text.length()) end = text.length();
			return end-begin+1;
		}
		
		private static int commentLength(String text, int begin)
		{
			int end = begin + 1;
			if (end >= text.length()) return 1;
			
			switch (text.charAt(end)){
			case '*':
				end = text.indexOf("*/", end);
				if (end < 0)
					end = text.length();
				break;
			case '/':
				end = text.indexOf("\n", end)-1;
				if (end < 0)
					end = text.length();
				break;
			default:
				end = begin;
				break;
			}
			return end-begin+1;
		}

		private ASTNode(String type, String text, ASTNode[] children) {
			fType = type;
			fElement = text;
			fChildren = children;
		}

		public String getElement() {
			return fElement;
		}

		public ASTNode getParentOf(Object element) {
			for (ASTNode child : fChildren) {
				if (child == element)
					return this;
				ASTNode par = child.getParentOf(element);
				if (par == null)
					return par;
			}
			return null;
		}

		public ASTNode[] getChildrenOf(Object parentElement) {
			if (0 == fChildren.length) {
				return null;
			}
			if (this == parentElement)
				return this.getChildren();
			for (ASTNode child : fChildren) {
				ASTNode[] a = child.getChildrenOf(parentElement);
				if (a != null)
					return a;
			}
			return null;
		}

		public String getType() {
			return fType;
		}

		public ASTNode[] getChildren() {
			return fChildren;
		}
		
		public String toString() {
			String str = "ASTNode{Type: \"" + fType + "\", Element: \"" + fElement + "\", Children: " + Integer.toString(fChildren.length) + "}";
			return str;
		}
	}

}
