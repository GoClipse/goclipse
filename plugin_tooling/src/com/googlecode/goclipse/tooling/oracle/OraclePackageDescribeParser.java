/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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

import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolingMessages;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.ops.SourceFileLocation;
import melnorme.lang.tooling.ops.util.SourceLinesInfo;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.lang.utils.parse.LexingUtils;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

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
					StructureElement element = parseStructureElement(jsonObject, parsingMethods);
					if(element == null) {
						continue; // Can happen for external elements
					}
					elements.add(element);	
				} else {
					throw new CommonException("'members' element is not a JSONObject: " + object);
				}
			}
		}
		
		Collections.sort(elements, new Comparator<StructureElement>() {
			@Override
			public int compare(StructureElement o1, StructureElement o2) {
				SourceRange sr1 = o1.getSourceRange();
				SourceRange sr2 = o2.getSourceRange();
				int cmp = sr1.getOffset() - sr2.getOffset();
				if(cmp == 0) {
					return o1.getNameSourceRange().getOffset() - o2.getNameSourceRange().getOffset();
				}
				return cmp;
			}
		});
		
		return elements;
	}
	
	protected StructureElement parseStructureElement(JSONObject object, boolean parsingMethods) 
			throws JSONException, CommonException {
		String name = readString(object, "name");
		
		String posString = readString(object, "pos");
		SourceFileLocation elementSourceFileLoc = SourceFileLocation.parseSourceRange(posString, ':');
		if(!isSourceElementLocation(elementSourceFileLoc.getFileLocation())) {
			return null;
		}
		SourceRange nameSourceRange = elementSourceFileLoc.parseSourceRangeFrom1BasedIndex(sourceLineInfo);
		SourceRange sourceRange  = nameSourceRange;
		
		String type = readOptionalString(object, "type");
		
		String kindString = readOptionalString(object, "kind");
		
		StructureElementKind elementKind;
		if(parsingMethods) {
			elementKind = StructureElementKind.METHOD;
		} else {
			if(kindString == null) {
				throw new CommonException("No `kind` field for element: " + name);
			}
			elementKind = parseKind(kindString, type);
		}
		if(elementKind == STRUCT || elementKind == StructureElementKind.INTERFACE) {
			type = null;
		} else if(elementKind == StructureElementKind.METHOD) {
			if(name.startsWith("method ")) {
				name = name.substring("method ".length());
				if(name.startsWith("(")) {
					String fullName = StringUtil.segmentAfterMatch(name, ")");
					if(fullName != null) {
						fullName = fullName.trim();
						int idLength = parseIdentifierStart(fullName);
						if(idLength > 0) {
							name = fullName.substring(0, idLength);
							type = "func" + fullName.substring(idLength);
						}
					}
				}
			}
		}
		
		if(name.length() == 0) {
			throw new CommonException("No name provided");
		}
		
		EProtection protection = EProtection.PUBLIC;
		
		if(!parsingMethods && Character.isLowerCase(name.charAt(0))) {
			protection = EProtection.PRIVATE;
		}
		
		ElementAttributes elementAttributes = new ElementAttributes(protection);
		
		JSONArray methods = getOptionalJSONArray(object, "methods");
		Indexable<StructureElement> children = parseElements(methods, true);
		
		return new StructureElement(name, nameSourceRange, sourceRange, 
			elementKind, elementAttributes, type, children);
	}
	
	protected boolean isSourceElementLocation(Location sourceFileLoc) throws CommonException {
		return location == null || location.equals(sourceFileLoc);
	}
	
	protected int parseIdentifierStart(String source) {
		StringParseSource parser = new StringParseSource(source);
		return LexingUtils.matchJavaIdentifier(parser);
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