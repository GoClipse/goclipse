package melnorme.utilbox.core.fntypes;

/**
 * A delegate method that given an argument of type T returns true or false 
 * @deprecated Use Java 8 utils instead
 */
@Deprecated
public interface Predicate<T> extends java.util.function.Predicate<T> {

	boolean evaluate(T obj);
	
	@Override
	default boolean test(T obj) {
		return evaluate(obj);
	}
	
}