package melnorme.utilbox.core.fntypes;

public interface Function<T, R> {
	
	R evaluate(T obj);
	
}