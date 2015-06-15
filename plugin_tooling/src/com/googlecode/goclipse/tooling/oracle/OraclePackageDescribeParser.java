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


import static melnorme.lang.tooling.structure.StructureElementKind.STRUCT;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolingMessages;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.util.SourceLinesInfo;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.NumberUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OraclePackageDescribeParser extends JSONParseHelpers {
	
	protected final Location location;
	
	protected SourceLinesInfo sourceLineInfo;
	
	public OraclePackageDescribeParser(Location location) {
		this.location = location;
	}
	
	public SourceFileStructure parse(ExternalProcessResult result, String goSource) throws CommonException {
		if(result.exitValue != 0) {
			throw new CommonException(ToolingMessages.TOOLS_ExitedWithNonZeroStatus(result.exitValue));
		}
		
		return parse(result.getStdOutBytes().toString(StringUtil.UTF8), goSource);
	}
	
	public SourceFileStructure parse(String describeOutput, String goSource) throws CommonException {
		
		sourceLineInfo = new SourceLinesInfo(goSource);
		
		ArrayList2<StructureElement> elements;
		try {
			elements = doParseJsonResult(describeOutput);
		} catch(JSONException e) {
			throw new CommonException("Error parsing JSON output: ", e);
		}
		
		return new SourceFileStructure(location, elements, null);
	}
	
	protected ArrayList2<StructureElement> doParseJsonResult(String output) 
			throws JSONException, CommonException {
		JSONObject jsonResult = new JSONObject(output);
		
		JSONObject describe = jsonResult.getJSONObject("describe");
		
		JSONObject packageObj = describe.getJSONObject("package");
		
		JSONArray members = getOptionalJSONArray(packageObj, "members");
		return parseElements(members, false);
	}
	
	protected JSONArray getOptionalJSONArray(JSONObject packageObj, String key) throws JSONException {
		if(!packageObj.has(key)) {
			return null;
		}
		return packageObj.getJSONArray(key);
	}
	
	protected ArrayList2<StructureElement> parseElements(JSONArray members, boolean parsingMethods) 
			throws JSONException, CommonException {
		ArrayList2<StructureElement> elements = new ArrayList2<>();
		
		if(members != null) {
			for(int i = 0; i < members.length(); i++) {
				Object object = members.get(i);
				if(object instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) object;
					elements.add(parseStructureElement(jsonObject, parsingMethods));	
				} else {
					throw new CommonException("'members' element is not a JSONObject: " + object);
				}
			}
		}
		
		return elements;
	}
	
	protected StructureElement parseStructureElement(JSONObject object, boolean parsingMethods) 
			throws JSONException, CommonException {
		String name = readString(object, "name");
		
		String posString = readString(object, "pos");
		SourceRange nameSourceRange = parseSourceRange(posString);
		SourceRange sourceRange  = nameSourceRange;
		
		String type = readOptionalString(object, "type");
		
		String kindString = readOptionalString(object, "kind");
		
		StructureElementKind elementKind;
		if(parsingMethods) {
			elementKind = StructureElementKind.FUNCTION; // Methods
		} else {
			if(kindString == null) {
				throw new CommonException("No `kind` field for element: " + name);
			}
			elementKind = parseKind(kindString, type);
		}
		if(elementKind == STRUCT || elementKind == StructureElementKind.INTERFACE) {
			type = null;
		} else if(elementKind == StructureElementKind.FUNCTION) {
			if(name.startsWith("method ")) {
				name = name.substring("method ".length());
			}
		}
		
		ElementAttributes elementAttributes = new ElementAttributes(EProtection.PUBLIC);
		
		JSONArray methods = getOptionalJSONArray(object, "methods");
		Indexable<StructureElement> children = parseElements(methods, true);
		
		return new StructureElement(name, nameSourceRange, sourceRange, 
			elementKind, elementAttributes, type, children);
	}
	
	protected SourceRange parseSourceRange(String positionString) throws CommonException {
		int i = positionString.length();

		String sourceRangeString = getSourceRangeString(positionString, i);
		if(sourceRangeString == null) {
			throw new CommonException("Source range not available in `" + positionString + "`");
		}
		
		String lineStr = StringUtil.segmentUntilMatch(sourceRangeString, ":");
		String columnStr = StringUtil.segmentAfterMatch(sourceRangeString, ":");
		
		int line = NumberUtil.parsePositiveInt(lineStr);
		int column = NumberUtil.parsePositiveInt(columnStr);
		
		int validatedOffset = sourceLineInfo.getValidatedOffset(line, column);
		int length = sourceLineInfo.getIdentifierAt(validatedOffset);
		
		return new SourceRange(validatedOffset, length);
	}
	
	protected String getSourceRangeString(String posString, int i) {
		int count = 0;
		
		while(--i >= 0) {
			if(posString.charAt(i) == ':') {
				
				if(++count == 2) {
					return posString.substring(i + 1, posString.length());  
				}
			}
		}
		return null;
	}
	
	protected StructureElementKind parseKind(String kind, String type) {
		if(kind == null) {
			return null;
		}
		
		switch (kind.toLowerCase()) {
		case "func": return StructureElementKind.FUNCTION;
		case "var": return StructureElementKind.VARIABLE;
		case "const": return StructureElementKind.CONST;
		case "type": 
			
			if(type.startsWith("struct")) {
				return StructureElementKind.STRUCT;
			}
			if(type.startsWith("interface")) {
				return StructureElementKind.INTERFACE;
			}
			
			return StructureElementKind.TYPE_DECL;
		default:
			return StructureElementKind.VARIABLE;
		}
		
	}
	
}