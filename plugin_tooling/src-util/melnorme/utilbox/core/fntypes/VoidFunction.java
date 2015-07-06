package melnorme.utilbox.core.fntypes;


public interface VoidFunction<T> extends java.util.function.Function<T, Void> {
	
	@Override
	Void apply(T obj);
	
}