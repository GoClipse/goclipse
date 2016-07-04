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
package com.googlecode.goclipse.tooling.gocode;


import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.toolchain.ops.AbstractToolResultParser;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.Pair;
import melnorme.utilbox.misc.StringUtil;

public abstract class GocodeOutputParser2 extends AbstractToolResultParser<ArrayList2<ToolCompletionProposal>> {
	
	protected final int offset;
	protected final String source;
	
	protected final String completionPrefix;
	
	public GocodeOutputParser2(int offset, String source) {
		this.offset = offset;
		this.source = source;
		
		completionPrefix = getWordBeforeOffset(source, offset);
	}
	
	public static String getWordBeforeOffset(String source, int offset) {
		int ix = offset;
		while(ix > 0 && Character.isJavaIdentifierPart(source.charAt(ix-1))){
			ix--;
		}
		return source.substring(ix, offset);
	}
	
	@Override
	protected String getToolName() {
		return "gocode";
	}
	
	@Override
	public ArrayList2<ToolCompletionProposal> parseOutput(StringCharSource parseSource)
			throws OperationSoftFailure {
	
		String stdout = parseSource.getSource();
		
		if (stdout.startsWith("PANIC")) {
			handleParseError(new OperationSoftFailure("PANIC from gocode - likely go/gocode version mismatch?"));
		}
		
		List<String> completions = new ArrayList2<>(GocodeCompletionOperation.LINE_SPLITTER.split(stdout));
		
		ArrayList2<ToolCompletionProposal> baseResults = new ArrayList2<>();
		
		for (String completionEntry : completions) {
			ToolCompletionProposal proposal = parseCompletion(completionEntry);
			if(proposal != null) {
				baseResults.add(proposal);
			}
		}
		
		return baseResults;
	}
	
	protected void handleParseError(OperationSoftFailure ce) throws OperationSoftFailure {
		throw ce;
	}
	
	protected abstract void logWarning(String message);
	
	protected ToolCompletionProposal parseCompletion(final String completionEntry) {
		if(completionEntry.trim().isEmpty()) {
			return null;
		}
		String line = completionEntry;
		
		String kindString = StringUtil.segmentUntilMatch(line, ",,");
		if(kindString == null) {
			logWarning("Invalid gocode completion result line: " + line);
			return null;
		}
		
		line = StringUtil.segmentAfterMatch(line, ",,");
		
		String identifier = StringUtil.segmentUntilMatch(line, ",,");
		if(identifier == null) {
			logWarning("Invalid gocode completion result line: " + completionEntry);
			return null;
		}
		String spec = StringUtil.segmentAfterMatch(line, ",,");
		
		String label = identifier;
		String typeLabel = null;
		
		EnumSet<EAttributeFlag> flagsSet = EnumSet.noneOf(EAttributeFlag.class);
		CompletionProposalKind kind = CompletionProposalKind.UNKNOWN;
		
		String fullReplaceString = identifier;
		Indexable<SourceRange> subRanges = null;
		
		if (kindString.equals("type")) {
			if (spec.equals("interface")) {
				kind = CompletionProposalKind.INTERFACE;
			} else 
			if (spec.equals("struct")) {
				kind = CompletionProposalKind.STRUCT;
			} else 
			if (spec.equals("built-in")) {
				kind = CompletionProposalKind.NATIVE;
			} else {
				kind = CompletionProposalKind.TYPE_DECL;
				typeLabel = ": " + spec;
			}
		} else
		if (kindString.equals("package")) {
			kind = CompletionProposalKind.PACKAGE;
		} else 
		if (kindString.equals("func")) {
			kind = CompletionProposalKind.FUNCTION;
			spec = spec.replaceFirst("^func", "");
			typeLabel = StringUtil.segmentAfterMatch(spec, ")");
			String fnParams = "";
			if(typeLabel != null) {
				fnParams = StringUtil.trimEnd(spec, typeLabel);
				typeLabel = typeLabel.trim();
				
				Pair<String, ArrayList2<SourceRange>> pair = getParamsString(identifier.length(), fnParams);
				String paramsString = pair.getFirst();
				subRanges = pair.getSecond();
				fullReplaceString = identifier + paramsString; 
			}
			label = label + fnParams;
		} else
		if (kindString.equals("var") || kindString.equals("const")) {
			kind = CompletionProposalKind.VARIABLE;
			if(!spec.trim().isEmpty()) {
				typeLabel = ": " + spec;
			}
			if (kindString.equals("const")) {
				flagsSet.add(EAttributeFlag.CONST);
			}
		} else 
		{
			logWarning("Unknown element kind: " + kindString);
			kind = CompletionProposalKind.UNKNOWN;
		}
		
		EProtection prot = null;
		
		if(kind != CompletionProposalKind.PACKAGE && kind != CompletionProposalKind.NATIVE) {
			if (identifier.length() > 0 && Character.isLowerCase(identifier.charAt(0))) {
				prot = EProtection.PRIVATE;
			}
		}
		
		ElementAttributes attributes = new ElementAttributes(prot, flagsSet);
		return new ToolCompletionProposal(
			offset - completionPrefix.length(), completionPrefix.length(), 
			identifier, label, 
			kind, attributes, 
			typeLabel, null, null, 
			fullReplaceString, subRanges);
	}
	
	protected Pair<String, ArrayList2<SourceRange>> getParamsString(int baseOffset, String fnParams) {
		fnParams = fnParams.trim();
		if(!fnParams.startsWith("(") && fnParams.endsWith(")")) {
			return Pair.create("", null);
		}
		String parse = fnParams.substring(1, fnParams.length()-1);
		if(parse.trim().isEmpty()) {
			return Pair.create("()", null);
		}
		
		String[] params = parse.split(Pattern.quote(","));
		
		ArrayList2<SourceRange> subRanges = new ArrayList2<>();
		
		StringBuilder paramsString = new StringBuilder("(");
		baseOffset += 1;
		for(int i = 0; i < params.length; i++) {
			if(i != 0) {
				paramsString.append(", ");
				baseOffset += 2;
			}
			String paramName = StringUtil.substringUntilMatch(params[i].trim(), " ");
			paramsString.append(paramName);
			subRanges.add(new SourceRange(baseOffset, paramName.length()));
			baseOffset += paramName.length();
		}
		paramsString.append(")");
		
		return Pair.create(paramsString.toString(), subRanges);
	}
	
}