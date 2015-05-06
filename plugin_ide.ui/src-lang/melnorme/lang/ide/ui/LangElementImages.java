/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui;

import melnorme.lang.ide.ui.utils.PluginImagesHelper.ImageHandle;

public interface LangElementImages {

	String CAT_LANG_OBJ = "icons-lang/language_elements";
	
	ImageHandle CA_SNIPPET = LangImages.createManaged(CAT_LANG_OBJ, "CA_Templates.16.gif");
	
	
	ImageHandle ALIAS_ELEMENT = LangImages.createManaged(CAT_LANG_OBJ, "alias_se.png");
	
	ImageHandle ERROR_ELEMENT = LangImages.createManaged(CAT_LANG_OBJ, "error_element.png");
	
	ImageHandle FUNCTION = LangImages.createManaged(CAT_LANG_OBJ, "f_function.png");
	ImageHandle CONSTRUCTOR = LangImages.createManaged(CAT_LANG_OBJ, "f_constructor.png");
	
	ImageHandle MODULE = LangImages.createManaged(CAT_LANG_OBJ, "module.png");
	ImageHandle NAMESPACE = LangImages.createManaged(CAT_LANG_OBJ, "namespace.png");
	ImageHandle PACKAGE = LangImages.createManaged(CAT_LANG_OBJ, "package.png");
	
	ImageHandle T_CLASS = LangImages.createManaged(CAT_LANG_OBJ, "t_class.png");
	ImageHandle T_ENUM = LangImages.createManaged(CAT_LANG_OBJ, "t_enum.png");
	ImageHandle T_INTERFACE = LangImages.createManaged(CAT_LANG_OBJ, "t_interface.png");
	ImageHandle T_NATIVE = LangImages.createManaged(CAT_LANG_OBJ, "t_native.png");
	ImageHandle T_STRUCT = LangImages.createManaged(CAT_LANG_OBJ, "t_struct.png");
	ImageHandle T_TYPE = LangImages.createManaged(CAT_LANG_OBJ, "t_type.png");
//	ImageHandle T_UNION = LangImages.createManaged(CAT_LANG_OBJ, "t_union.gif");
	
	ImageHandle UKKNOWN  = LangImages.createManaged(CAT_LANG_OBJ, "unknown.png");
	
	ImageHandle VARIABLE = LangImages.createManaged(CAT_LANG_OBJ, "variable.png");

}