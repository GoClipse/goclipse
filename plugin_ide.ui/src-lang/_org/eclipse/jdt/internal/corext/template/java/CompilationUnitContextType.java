/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package _org.eclipse.jdt.internal.corext.template.java;

import melnorme.lang.ide.core.ISourceFile;
import melnorme.lang.ide.ui.templates.LangTemplateContextType;

import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.SimpleTemplateVariableResolver;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateVariableResolver;


/**
 * Compilation unit context type.
 */
public abstract class CompilationUnitContextType extends LangTemplateContextType {
	
	/* -----------------  ----------------- */

	public CompilationUnitContextType() {
	}
	
	public CompilationUnitContextType(String id) {
		super(id);
	}
	
	@Override
	protected void addAdditionalResolvers() {
		super.addAdditionalResolvers();
		addResolver(new SurroundWithLineSelection());
		
		addResolver(new File());
	}
	
	/* -----------------  ----------------- */
	
	protected static class File extends TemplateVariableResolver {
		public File() {
			super("file", JavaTemplateMessages.CompilationUnitContextType_variable_description_file);
		}
		@Override
		protected String resolve(TemplateContext context) {
			ISourceFile unit= ((CompilationUnitContext) context).getCompilationUnit();

			return (unit == null) ? null : unit.getLocation().toPath().getFileName().toString();
		}

		@Override
		protected boolean isUnambiguous(TemplateContext context) {
			return resolve(context) != null;
		}
	}


//	protected static class ReturnType extends TemplateVariableResolver {
//	 	public ReturnType() {
//	 	 	super("return_type", JavaTemplateMessages.CompilationUnitContextType_variable_description_return_type);  //$NON-NLS-1$
//	 	}
//	 	@Override
//		protected String resolve(TemplateContext context) {
//			IJavaElement element= ((CompilationUnitContext) context).findEnclosingElement(IJavaElement.METHOD);
//			if (element == null)
//				return null;
//
//			try {
//				return Signature.toString(((IMethod) element).getReturnType());
//			} catch (JavaModelException e) {
//				return null;
//			}
//		}
//	}

//	protected static class PrimaryTypeName extends TemplateVariableResolver {
//		public PrimaryTypeName() {
//			super("primary_type_name", JavaTemplateMessages.CompilationUnitContextType_variable_description_primary_type_name);  //$NON-NLS-1$
//
//		}
//		@Override
//		protected String resolve(TemplateContext context) {
//			ICompilationUnit unit= ((CompilationUnitContext) context).getCompilationUnit();
//			if (unit == null)
//				return null;
//			return JavaCore.removeJavaLikeExtension(unit.getElementName());
//		}
//
//		/*
//		 * @see org.eclipse.jface.text.templates.TemplateVariableResolver#isUnambiguous(org.eclipse.jface.text.templates.TemplateContext)
//		 */
//		@Override
//		protected boolean isUnambiguous(TemplateContext context) {
//			return resolve(context) != null;
//		}
//	}
//
//	protected static class EnclosingJavaElement extends TemplateVariableResolver {
//		protected final int fElementType;
//
//		public EnclosingJavaElement(String name, String description, int elementType) {
//			super(name, description);
//			fElementType= elementType;
//		}
//		@Override
//		protected String resolve(TemplateContext context) {
//			IJavaElement element= ((CompilationUnitContext) context).findEnclosingElement(fElementType);
//			if (element instanceof IType)
//				return JavaElementLabels.getElementLabel(element, JavaElementLabels.T_CONTAINER_QUALIFIED);
//			return (element == null) ? null : element.getElementName();
//		}
//
//		/*
//		 * @see org.eclipse.jface.text.templates.TemplateVariableResolver#isUnambiguous(org.eclipse.jface.text.templates.TemplateContext)
//		 */
//		@Override
//		protected boolean isUnambiguous(TemplateContext context) {
//			return resolve(context) != null;
//		}
//	}
//
//	protected static class Method extends EnclosingJavaElement {
//		public Method() {
//			super("enclosing_method", JavaTemplateMessages.CompilationUnitContextType_variable_description_enclosing_method, IJavaElement.METHOD);  //$NON-NLS-1$
//		}
//	}
//
//	protected static class Type extends EnclosingJavaElement {
//		public Type() {
//			super("enclosing_type", JavaTemplateMessages.CompilationUnitContextType_variable_description_enclosing_type, IJavaElement.TYPE);  //$NON-NLS-1$
//		}
//	}
///*
//	protected static class SuperClass extends EnclosingJavaElement {
//		public Type() {
//			super("super_class", TemplateMessages.getString("JavaContextType.variable.description.type"), IJavaElement.TYPE);
//		}
//	}
//*/
//	protected static class Package extends EnclosingJavaElement {
//		public Package() {
//			super("enclosing_package", JavaTemplateMessages.CompilationUnitContextType_variable_description_enclosing_package, IJavaElement.PACKAGE_FRAGMENT);  //$NON-NLS-1$
//		}
//	}
//
//	protected static class Project extends EnclosingJavaElement {
//		public Project() {
//			super("enclosing_project", JavaTemplateMessages.CompilationUnitContextType_variable_description_enclosing_project, IJavaElement.JAVA_PROJECT);  //$NON-NLS-1$
//		}
//	}
///*
//	protected static class Project2 extends TemplateVariableResolver {
//		public Project2() {
//			super("project", TemplateMessages.getString("JavaContextType.variable.description.project"));
//		}
//		public String evaluate(TemplateContext context) {
//			ICompilationUnit unit= ((JavaContext) context).getUnit();
//			return (unit == null) ? null : unit.getJavaProject().getElementName();
//		}
//	}
//*/
//	protected static class Arguments extends TemplateVariableResolver {
//		public Arguments() {
//			super("enclosing_method_arguments", JavaTemplateMessages.CompilationUnitContextType_variable_description_enclosing_method_arguments);  //$NON-NLS-1$
//		}
//		@Override
//		protected String resolve(TemplateContext context) {
//			IJavaElement element= ((CompilationUnitContext) context).findEnclosingElement(IJavaElement.METHOD);
//			if (element == null)
//				return null;
//
//			IMethod method= (IMethod) element;
//
//			try {
//				String[] arguments= method.getParameterNames();
//				StringBuffer buffer= new StringBuffer();
//
//				for (int i= 0; i < arguments.length; i++) {
//					if (i > 0)
//						buffer.append(", "); //$NON-NLS-1$
//					buffer.append(arguments[i]);
//				}
//
//				return buffer.toString();
//
//			} catch (JavaModelException e) {
//				return null;
//			}
//		}
//	}

/*
	protected static class Line extends TemplateVariableResolver {
		public Line() {
			super("line", TemplateMessages.getString("CompilationUnitContextType.variable.description.line"));
		}
		public String evaluate(TemplateContext context) {
			return ((JavaTemplateContext) context).guessLineNumber();
		}
	}
*/

	/**
	 * The line selection variable determines templates that work on selected lines.
	 * <p>
	 * This class contains additional description that tells about the
	 * 'Source &gt; Surround With > ...' menu.</p>
	 * 
	 * @since 3.7
	 * @see org.eclipse.jface.text.templates.GlobalTemplateVariables.LineSelection
	 */
	protected static class SurroundWithLineSelection extends SimpleTemplateVariableResolver {
	
		/**
		 * Creates a new line selection variable
		 */
		public SurroundWithLineSelection() {
			super(GlobalTemplateVariables.LineSelection.NAME, 
				JavaTemplateMessages.CompilationUnitContextType_variable_description_line_selection);
		}
		@Override
		protected String resolve(TemplateContext context) {
			String selection= context.getVariable(GlobalTemplateVariables.SELECTION);
			if (selection == null)
				return "";
			return selection;
		}
	}
	
}