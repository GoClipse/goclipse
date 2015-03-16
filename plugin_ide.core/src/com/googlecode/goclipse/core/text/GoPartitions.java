/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.text;

import org.eclipse.jface.text.IDocument;

public interface GoPartitions {
	
	String PARTITIONING_ID = "___go_partioning";
	
	String COMMENT = "__comment";
	String STRING = "__string";
	String MULTILINE_STRING = "__multiline_string";
	
	public static final String[] PARTITION_TYPES = new String[] { 
		IDocument.DEFAULT_CONTENT_TYPE, 
		COMMENT, 
		STRING,
		MULTILINE_STRING 
	};
	
}