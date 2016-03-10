package com.googlecode.goclipse.tooling.gocode;

import java.util.List;

import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.ops.AbstractToolOutputParser;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;

public class GocodeOutputParser extends AbstractToolOutputParser<ArrayList2<ToolCompletionProposal>> {
	
	public static String lastWord(String source, int offset) {
		for (int n = offset - 1; n >= 0; n--) {
			char c = source.charAt(n);
			
			if (!Character.isJavaIdentifierPart(c)) {
				return source.substring(n + 1, offset);
			}
		}
		
		return "";
	}
	
	protected final int offset;
	protected final String source;
	
	public GocodeOutputParser(int offset, String source) {
		this.offset = offset;
		this.source = source;
	}
	
	@Override
	protected String getToolProcessName() {
		return "gocode";
	}
	
	@Override
	protected ArrayList2<ToolCompletionProposal> parse(StringParseSource parseSource) throws CommonException {
		String prefix = lastWord(source, offset);
		
		String stdout = parseSource.getSource();
		List<String> completions = new ArrayList2<>(GocodeCompletionOperation.LINE_SPLITTER.split(stdout));
		
		ArrayList2<ToolCompletionProposal> baseResults = new ArrayList2<>();
		
		for (String completionEntry : completions) {
			ToolCompletionProposal proposal = handleResult(offset, prefix, completionEntry);
			if(proposal != null) {
				baseResults.add(proposal);
			}
		}
		
		return baseResults;
	}
	
	protected ToolCompletionProposal handleResult(final int offset, String prefix, String completionEntry)
			throws CommonException {
		int firstComma = completionEntry.indexOf(",,");
		int secondComma = completionEntry.indexOf(",,", firstComma + 2);
		
		if (firstComma != -1 && secondComma != -1) {
			String type = completionEntry.substring(0, firstComma);
			
			if ("PANIC".equals(type)) {
				handleParseError(new CommonException("PANIC from gocode - likely go/gocode version mismatch?"));
			}
			
			String identifier = completionEntry.substring(firstComma + 2, secondComma);
			
			if ("PANIC".equals(identifier)) {
				handleParseError(new CommonException("PANIC from gocode - likely go/gocode version mismatch?"));
			}
			
			String spec = completionEntry.substring(secondComma + 2);
			
			return getProposal(offset, prefix, type, identifier, spec);
		}
		return null;
	}
	
	@Override
	protected void handleParseError(CommonException ce) throws CommonException {
		throw ce;
	}
	
	protected ToolCompletionProposal getProposal(final int offset, String prefix, String type, String identifier,
			String spec) {
		String descriptiveString = identifier + " : " + spec;
		// BM: removed used of CodeContext because it is buggy, 
		// see https://github.com/GoClipse/goclipse/issues/112
//			String description = codeContext.getDescriptionForName(identifier).trim();
//			IContextInformation info = new ContextInformation(description, description);
		
		CompletionProposalKind kind = CompletionProposalKind.UNKNOWN;
		EProtection prot = null;
		
		String substr = identifier.substring(prefix.length());
		@SuppressWarnings("unused")
		int replacementLength = identifier.length() - prefix.length();
		
		if (descriptiveString != null && descriptiveString.contains(" : func")) {
			if(identifier.isEmpty() || Character.isLowerCase(identifier.charAt(0))) {
				prot = EProtection.PRIVATE;
			}
			kind = CompletionProposalKind.FUNCTION;
			
			substr = identifier.substring(prefix.length()) + "()";
			replacementLength++;
			
		} else if (descriptiveString != null && descriptiveString.contains(" : interface")) {
			kind = CompletionProposalKind.INTERFACE;
			
		} else if (descriptiveString != null && descriptiveString.contains(" : struct")) {
			kind = CompletionProposalKind.STRUCT;
			
		} else if ("package".equals(type)) {
			kind = CompletionProposalKind.IMPORT;
			
			substr = identifier.substring(prefix.length()) + ".";
			replacementLength++;
		} else {
			if (substr != null && substr.length() > 0 && Character.isLowerCase(substr.charAt(0))) {
				prot = EProtection.PRIVATE;
			}
			kind = CompletionProposalKind.VARIABLE;
		}
		
		// format the output
		descriptiveString = descriptiveString.replace(" : func", " ").replace(" : interface",
				" ").replace(" : struct", " ").replace("(", "( ").replace(")", " )");
		
		ElementAttributes attributes = new ElementAttributes(prot);
		
		return new ToolCompletionProposal(
			offset - prefix.length(), prefix.length(), identifier, descriptiveString, 
			kind, attributes, null, null);
	}
	
}