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

public interface LangObjImages {

	String CAT_LANG_OBJ = "icons-lang/obj";
	String CAT_OBJ = "obj";
	
	ImageHandle CA_SNIPPET = LangImages.createManaged(CAT_LANG_OBJ, "CA_Templates.16.gif");
	
	ImageHandle UKKNOWN  = LangImages.createManaged(CAT_LANG_OBJ, "unknown.gif");
	
	ImageHandle BASIC = LangImages.createManaged(CAT_LANG_OBJ, "basic.png");
	ImageHandle BASIC2 = LangImages.createManaged(CAT_LANG_OBJ, "basic2.gif");
	
	ImageHandle VARIABLE = LangImages.createManaged(CAT_LANG_OBJ, "variable.png");
	
	ImageHandle F_FUNCTION = LangImages.createManaged(CAT_LANG_OBJ, "f_function.png");
	ImageHandle F_CONSTRUCTOR = LangImages.createManaged(CAT_LANG_OBJ, "f_constructor.png");
	
	ImageHandle MODULE = LangImages.createManaged(CAT_LANG_OBJ, "module.gif");
	ImageHandle NAMESPACE = LangImages.createManaged(CAT_LANG_OBJ, "namespace.png");
	ImageHandle PACKAGE = LangImages.createManaged(CAT_LANG_OBJ, "package.png");
	ImageHandle PACKAGE_EMPTY = LangImages.createManaged(CAT_LANG_OBJ, "package_empty.gif");

	ImageHandle T_CLASS = LangImages.createManaged(CAT_LANG_OBJ, "t_class.gif");
	ImageHandle T_ENUM = LangImages.createManaged(CAT_LANG_OBJ, "t_enum.gif");
	ImageHandle T_INTERFACE = LangImages.createManaged(CAT_LANG_OBJ, "t_interface.png");
	ImageHandle T_STRUCT = LangImages.createManaged(CAT_LANG_OBJ, "t_struct.png");
//	ImageHandle T_TYPE = LangImages.createManaged(CAT_LANG_OBJ, "t_type.png");
//	ImageHandle T_UNION = LangImages.createManaged(CAT_LANG_OBJ, "t_union.gif");
	
	ImageHandle ATTRIBUTE = LangImages.createManaged(CAT_LANG_OBJ, "attribute.gif");
	
}