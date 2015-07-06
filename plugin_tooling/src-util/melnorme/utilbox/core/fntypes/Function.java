package melnorme.utilbox.core.fntypes;

/**
 * @deprecated Use Java 8 utils instead
 */
@Deprecated
public interface Function<PARAM, RESULT> extends java.util.function.Function<PARAM, RESULT> {
	
	RESULT evaluate(PARAM obj);
	
	@Override
	default RESULT apply(PARAM obj) {
		return apply(obj);
	}
	
}