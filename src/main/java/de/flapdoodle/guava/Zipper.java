package de.flapdoodle.guava;

import java.util.Iterator;
import java.util.function.BiFunction;

public abstract class Zipper {
	
	private Zipper() {
		// no instance
	}

	public static <A, B, C> Iterable<C> zip(Iterable<A> iterableA, Iterable<B> iterableB, BiFunction<A, B, C> fn) {
		return () -> new Iterator<C>() {
			private final Iterator<A> itA = iterableA.iterator();
			private final Iterator<B> itB = iterableB.iterator();

			@Override
			public boolean hasNext() {
				return itA.hasNext() & itB.hasNext();
			}

			@Override
			public C next() {
				return fn.apply(itA.next(), itB.next());
			}
		};
	}
}
