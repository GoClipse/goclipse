/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import melnorme.utilbox.core.CommonException;

public class GsonHelper {
	
	public JsonObject getObject(JsonObject jsonObject, String key) throws CommonException {
		JsonElement element = jsonObject.get(key);
		if(element != null && element.isJsonObject()) {
			return element.getAsJsonObject();
		} else {
			throw wrongTypeException(key, "Object");
		}
	}
	
	public String getString(JsonObject jsonObject, String key) throws CommonException {
		JsonElement element = jsonObject.get(key);
		if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
			return element.getAsString();
		} else {
			throw wrongTypeException(key, "String");
		}
	}
	
	public boolean getBoolean(JsonObject jsonObject, String key) throws CommonException {
		JsonElement element = jsonObject.get(key);
		if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
			return element.getAsBoolean();
		} else {
			throw wrongTypeException(key, "boolean");
		}
	}
	
	public Number getNumber(JsonObject jsonObject, String key) throws CommonException {
		JsonElement element = jsonObject.get(key);
		if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
			return element.getAsNumber();
		} else {
			throw wrongTypeException(key, "Number");
		}
	}
	
	public int getInteger(JsonObject jsonObject, String key) throws CommonException {
		Number number = getNumber(jsonObject, key);
		
		int intValue = number.intValue();
		
		// This check is not 100% precise, but good enough for now.
		if((double) intValue != number.doubleValue()) {
			throw CommonException.fromMsgFormat("Number `{0}` cannot be represente as an Integer.", key, number);
		}
		return intValue;
	}
	
	/* ----------------- optional ----------------- */
	
	public boolean isPresent(JsonObject jsonObject, String key) {
		JsonElement jsonElement = jsonObject.get(key);
		return jsonElement != null && !jsonElement.isJsonNull();
	}
	
	public JsonObject getOptionalObject(JsonObject jsonObject, String key) throws CommonException {
		if(!isPresent(jsonObject, key)) {
			return null;
		}
		return getObject(jsonObject, key);
	}
	
	public String getOptionalString(JsonObject jsonObject, String key) throws CommonException {
		return getOptionalString(jsonObject, key, null);
	}
	
	public String getOptionalString(JsonObject jsonObject, String key, String defaultValue) throws CommonException {
		if(!isPresent(jsonObject, key)) {
			return defaultValue;
		}
		return getString(jsonObject, key);
	}
	
	public Number getOptionalNumber(JsonObject jsonObject, String key) throws CommonException {
		if(!isPresent(jsonObject, key)) {
			return null;
		}
		return getNumber(jsonObject, key);
	}
	
	public boolean getOptionalBoolean(JsonObject jsonObject, String key, boolean defaultValue) throws CommonException {
		if(!isPresent(jsonObject, key)) {
			return defaultValue;
		}
		return getBoolean(jsonObject, key);
	}
	
	public int getOptionalInteger(JsonObject jsonObject, String key, int defaultValue) throws CommonException {
		if(!isPresent(jsonObject, key)) {
			return defaultValue;
		}
		return getInteger(jsonObject, key);
	}
	
	protected CommonException wrongTypeException(String key, String expectedType) throws CommonException {
		return CommonException.fromMsgFormat("Member `{0}` is not a {1}.", key, expectedType);
	}
	
}