package melnorme.utilbox.core.fntypes;

public interface Function<PARAM, RESULT> {
	
	RESULT evaluate(PARAM obj);
	
}