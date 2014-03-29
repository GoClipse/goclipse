package melnorme.lang.ide.ui.text.util;

import java.util.HashMap;
import java.util.Map;

import melnorme.lang.ide.ui.CodeFormatterConstants;


public enum TabStyle {

	TAB(CodeFormatterConstants.TAB),

	SPACES(CodeFormatterConstants.SPACE),

	MIXED(CodeFormatterConstants.MIXED);

	private final String name;

	private TabStyle(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	private static final Map<String, TabStyle> byName = new HashMap<String, TabStyle>();

	static {
		byName.put(TAB.getName(), TAB);
		byName.put(SPACES.getName(), SPACES);
		byName.put(MIXED.getName(), MIXED);
	}

	public static TabStyle forName(String name) {
		return byName.get(name);
	}

	public static TabStyle forName(String name, TabStyle deflt) {
		final TabStyle result = forName(name);
		return result != null ? result : deflt;
	}

}
