/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.oracle;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.NumberUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONParseHelpers {
	
	public JSONParseHelpers() {
		super();
	}
	
	public static String readOptionalString(JSONObject jsonObject, String key) throws JSONException, CommonException {
		if(!jsonObject.has(key)) {
			return null;
		}
		return readString(jsonObject, key);
	}
	
	public static String readString(JSONObject jsonObject, String key) throws JSONException, CommonException {
		Object stringValue = jsonObject.get(key);
		if(stringValue instanceof String) {
			return (String) stringValue;
		} else {
			throw new CommonException("Member "+key+" not a String.");
		}
	}
	
	public static int parsePositiveInt(String optString) throws CommonException {
		int integer = NumberUtil.parseInt(optString);
		if(integer < 0) {
			throw new CommonException("Integer is not positive: " + optString);
		}
		return integer;
	}
	
}