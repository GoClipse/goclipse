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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;

import melnorme.utilbox.core.CommonException;

/**
 * An alternative to {@link JsonParser}, which preservers the statically-checked exception throwing API
 */
public class JsonParserX {
	
	public static JsonReader newReader(String json, boolean lenient) {
		return newReader(new StringReader(json), lenient);
	}
	
	public static JsonReader newReader(Reader reader, boolean lenient) {
		JsonReader jsonReader = new JsonReader(reader);
		jsonReader.setLenient(lenient);
		return jsonReader;
	}
	
	/* -----------------  ----------------- */
	
	public JsonElement parse(String json, boolean lenient) throws IOException, JsonSyntaxExceptionX {
		return parse(new StringReader(json), lenient);
	}
	
	public JsonElement parse(Reader reader, boolean lenient) throws IOException, JsonSyntaxExceptionX {
		JsonReader json = new JsonReader(reader);
		boolean originalLenient = json.isLenient();
		json.setLenient(lenient);
		try {
			return parse(json);
		} finally {
			json.setLenient(originalLenient);
		}
	}
	
	public JsonElement parse(JsonReader reader) throws IOException, JsonSyntaxExceptionX {
		// Based on GSON 2.2.4 code of Streams.parse(json);
		
		try {
			return TypeAdapters.JSON_ELEMENT.read(reader);
		} catch(MalformedJsonException e) {
			throw new JsonSyntaxExceptionX(e);
		} catch(NumberFormatException e) {
			throw new JsonSyntaxExceptionX(e);
		} catch(JsonParseException e) {
			// I don't think JsonParseException is ever thrown from code above, 
			// not with normal JsonReader at least (a subclass might though). 
			// So just in case, sanitize it:
			throw new JsonSyntaxExceptionX(e.getCause());
		}
	}
	
	@SuppressWarnings("serial")
	public static class JsonSyntaxExceptionX extends Exception {
		public JsonSyntaxExceptionX(Throwable cause) {
			super(cause);
		}
	}
	
	/* -----------------  ----------------- */
	
	public JsonElement parse_common(JsonReader reader) throws CommonException {
		try {
			return parse(reader);
		} catch(IOException e) {
			throw new CommonException("Error reading from input: ", e);
		} catch(JsonSyntaxExceptionX e) {
			throw new CommonException("JSON syntax error: ", e);
		}
	}

	/* -----------------  ----------------- */
	
	public JsonObject parseObject(JsonReader reader) throws CommonException {
		JsonElement element = parse_common(reader);
		if(element.isJsonObject()) {
			return element.getAsJsonObject();
		} else {
			throw new CommonException("Parsed JSON element is not a JSON object.");
		}
	}
	
	public JsonObject parseObject(Reader json, boolean lenient) throws CommonException {
		return parseObject(newReader(json, lenient));
	}
	
	public JsonObject parseObject(String json, boolean lenient) throws CommonException {
		return parseObject(newReader(json, lenient));
	}
	
}