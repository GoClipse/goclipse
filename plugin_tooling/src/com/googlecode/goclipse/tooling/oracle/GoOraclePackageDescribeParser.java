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


import static com.googlecode.goclipse.tooling.oracle.JSONParseHelpers.readOptionalString;
import static com.googlecode.goclipse.tooling.oracle.JSONParseHelpers.readString;
import static melnorme.lang.tooling.structure.StructureElementKind.STRUCT;
import static melnorme.utilbox.core.CoreUtil.list;

import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.ParserErrorTypes;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ParserError;
import melnorme.lang.tooling.structure.AbstractStructureParser;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.lang.tooling.structure.StructureElementKind;
import melnorme.lang.tooling.toolchain.LineColumnPosition;
import melnorme.lang.tooling.toolchain.SourceFileLocation;
import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.lang.utils.parse.LexingUtils;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GoOraclePackageDescribeParser extends AbstractStructureParser {
	
	public GoOraclePackageDescribeParser(Location location, String goSource) {
		super(location, goSource);
	}
	
	public SourceFileStructure parse(ExternalProcessResult result) throws CommonException {
		if(result.exitValue != 0) {
			String errorMsg = result.getStdErrBytes().toString(StringUtil.UTF8);
			return parseErrorMessage(errorMsg);
		}
		
		return parse(result.getStdOutBytes().toString(StringUtil.UTF8));
	}
	
	@Override
	public SourceFileStructure parse(String describeOutput) throws CommonException {
		
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
					int offset1 = o1.getNameSourceRange2() == null ? 0 : o1.getNameSourceRange2().getOffset();
					int offset2 = o2.getNameSourceRange2() == null ? 0 : o2.getNameSourceRange2().getOffset();
					return offset1 - offset2;
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
		
		SourceRange nameSourceRange;
		SourceRange sourceRange;
		
		if(!isSourceElementLocation(elementSourceFileLoc.getFileLocation())) {
			sourceRange = nameSourceRange = null;
		} else {
			sourceRange = nameSourceRange = elementSourceFileLoc.parseSourceRangeFrom1BasedIndex(sourceLinesInfo);
		}
		
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
		
		if(!isSourceElementLocation(elementSourceFileLoc.getFileLocation())) {
			// Fix source range to children range.
			if(children.size() == 0) {
				return null; // Shouldn't even happen
			}
			nameSourceRange = null;
			int startPos = children.get(0).getSourceRange().getStartPos();
			int endPos = children.get(children.size()-1).getSourceRange().getEndPos();
			sourceRange = SourceRange.srStartToEnd(startPos, endPos);
		}
		
		return new StructureElement(name, nameSourceRange, sourceRange, 
			elementKind, elementAttributes, type, children);
	}
	
	protected boolean isSourceElementLocation(Location sourceFileLoc) throws CommonException {
		return location == null || location.equals(sourceFileLoc);
	}
	
	protected int parseIdentifierStart(String source) {
		StringCharSource parser = new StringCharSource(source);
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
	
	protected static final Pattern GO_MESSAGE_LINE_Regex = Pattern.compile(
		"^([^:\\n]*):" + // file
		"(\\d*):((\\d*):)?" +// line:column
//		"( (\\d*):(\\d*))?" + // end line:column
//		"()" + // column-end
		"\\s(.*)$" // error message
	);
	
	public SourceFileStructure parseErrorMessage(String errorMsg) throws CommonException {
		errorMsg = StringUtil.substringUntilLastMatch(errorMsg, "\n");
		errorMsg = StringUtil.trimStart(errorMsg, "oracle: ");
		
		ArrayList2<ParserError> parserProblems = new ArrayList2<>();
		
		if(errorMsg.length() > 2 && errorMsg.charAt(1) == ':') {
			// The path has a Windows driver letter, we need to remove it so it doesn't mess up the regex
			errorMsg = errorMsg.substring(2);
		}
		Matcher matcher = GO_MESSAGE_LINE_Regex.matcher(errorMsg);
		if(!matcher.matches()) {
			throw new CommonException("Error message line format not recognized.");
		}
		
		String lineStr = matcher.group(2);
		String columnStr = matcher.group(4);
		String message = matcher.group(5);
		
		LineColumnPosition lcPost = ToolOutputParseHelper.parseLineColumn(lineStr, columnStr, 1, 1);
		
		int offset = sourceLinesInfo.getValidatedOffset_1(lcPost.line, lcPost.column);
		String source = sourceLinesInfo.getSource();
		int length = 1;
		
		if(offset == source.length()) {
			length = 0;
		} 
		else if(offset < source.length()) {
			length = heuristic_determinTokenLength(offset, source);
		}
		
		SourceRange sr = new SourceRange(offset, length);
		parserProblems.add(new ParserError(ParserErrorTypes.GENERIC_ERROR, sr, message, null));
		
		return new SourceFileStructure(location, list(), parserProblems);
	}
	
	/**
	 * Try to guess the length of the token at given offset, from given source.
	 * Doesn't have to be accurate measurement. (used to get a better range for error msgs)  
	 */
	protected int heuristic_determinTokenLength(int offset, String source) {
		
		StringCharSource charSource = new StringCharSource(source);
		charSource.consume(offset);
		int length = LexingUtils.matchJavaIdentifier(charSource);
		if (length == 0) {
			return 1;
		}
		return length;
	}
	
}