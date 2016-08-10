package de.flapdoodle.guava.maps;

import java.util.Map;

public interface UnmodifiableMap<K, V> extends Map<K, V> {


	@Override
	@Deprecated
	public default V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public default V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public default void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public default void clear() {
		throw new UnsupportedOperationException();
	}
}
