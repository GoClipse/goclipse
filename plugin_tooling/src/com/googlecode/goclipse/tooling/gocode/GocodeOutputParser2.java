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

import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.toolchain.ops.AbstractToolOperation2;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public abstract class GocodeOutputParser2 extends AbstractToolOperation2<ArrayList2<ToolCompletionProposal>> {
	
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
	protected String getToolProcessName() {
		return "gocode";
	}
	
	@Override
	public ArrayList2<ToolCompletionProposal> parseProcessOutput(StringCharSource parseSource)
			throws CommonException {
		
		String stdout = parseSource.getSource();
		
		if (stdout.startsWith("PANIC")) {
			handleParseError(new CommonException("PANIC from gocode - likely go/gocode version mismatch?"));
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
	
	protected void handleParseError(CommonException ce) throws CommonException {
		throw ce;
	}
	
	protected abstract void logWarning(String message);
	
	protected ToolCompletionProposal parseCompletion(final String completionEntry) throws CommonException {
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
		
		if (spec.startsWith("interface")) {
			kind = CompletionProposalKind.INTERFACE;
		} else 
		if (spec.startsWith("struct")) {
			kind = CompletionProposalKind.STRUCT;
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
		if (kindString.equals("type")) {
			kind = CompletionProposalKind.TYPE_DECL;
			typeLabel = ": " + spec;
		} else
		{
			logWarning("Unknown element kind: " + kindString);
			kind = CompletionProposalKind.UNKNOWN;
		}
		
		EProtection prot = null;
		
		if(kind != CompletionProposalKind.PACKAGE) {
			if (identifier.length() > 0 && Character.isLowerCase(identifier.charAt(0))) {
				prot = EProtection.PRIVATE;
			}
		}
		
		ElementAttributes attributes = new ElementAttributes(prot, flagsSet);
		return new ToolCompletionProposal(
			offset - completionPrefix.length(), completionPrefix.length(), identifier, label, 
			kind, attributes, typeLabel, null, null);
	}
	
}