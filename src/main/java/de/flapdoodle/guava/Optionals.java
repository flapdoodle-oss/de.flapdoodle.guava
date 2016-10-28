package de.flapdoodle.guava;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

public abstract class Optionals {

	private Optionals() {
		// no instance
	}
	
	public static <T> Optional<T> flatmap(Optional<Optional<T>>  src) {
		return src.isPresent() ? src.get() : Optional.absent();
	}
	
	public static <T> ImmutableList<T> flatmap(Collection<Optional<T>> src) {
		return FluentIterable.from(src).filter(Optional::isPresent).transform(Optional::get).toList();
	}
}
