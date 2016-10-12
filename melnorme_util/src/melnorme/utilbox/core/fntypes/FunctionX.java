package melnorme.utilbox.core.fntypes;

import java.util.function.Function;

/**
 * Similar to {@link Function}, but allows specifying and exception for the underlying method. 
 */
public interface FunctionX<PARAM, RESULT, EXC extends Exception> {
	
	RESULT apply(PARAM obj) throws EXC;
	
}