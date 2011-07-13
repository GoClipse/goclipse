package com.googlecode.goclipse.go.lang.parser;

import com.googlecode.goclipse.model.Function;

public interface FunctionParserListener {
	void onEnterFunction(Function function);
	void onExitFunction(Function function);
}
