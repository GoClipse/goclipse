package melnorme.utilbox.core.fntypes;


public interface VoidFunction<T> extends Function<T, Void> {
	
	@Override
	Void evaluate(T obj);
	
}