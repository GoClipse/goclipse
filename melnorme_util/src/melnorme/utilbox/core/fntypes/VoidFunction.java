package melnorme.utilbox.core.fntypes;

import java.util.function.Consumer;

public interface VoidFunction<T> extends java.util.function.Function<T, Void>, Consumer<T> {
	
	@Override
	default Void apply(T obj) {
		accept(obj);
		return null;
	}
	
}