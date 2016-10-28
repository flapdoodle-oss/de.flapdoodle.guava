package de.flapdoodle.guava;

import java.util.Iterator;
import java.util.function.BiFunction;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;

public abstract class Zipper {
	
	private Zipper() {
		// no instance
	}

	public static <A, B, C> Iterable<C> zip(Iterable<A> a, Iterable<B> b, BiFunction<A, B, C> zipper) {
		return zip(a.iterator(), b.iterator(), zipper);
	}
	
	public static <A,B,C> Iterable<C> zip(final Iterator<A> a, final Iterator<B> b, final BiFunction<A, B, C> zipper) {
		Preconditions.checkNotNull(a,"a is null");
		Preconditions.checkNotNull(b,"b is null");
		
		return () -> new UnmodifiableIterator<C>() {
	
			int pos=0;
			
			@Override
			public boolean hasNext() {
				boolean aNext=a.hasNext();
				boolean bNext=b.hasNext();
				if (aNext!=bNext) {
					throw new IndexOutOfBoundsException("no element in "+(aNext?"a":"b")+" found at "+pos);
				}
				return aNext & bNext;
			}
	
			@Override
			public C next() {
				pos++;
				return zipper.apply(a.next(),b.next());
			}
			
		};
	}


	public static <A,B> ImmutableList<Pair<A,B>> zip(Iterable<A> a, Iterable<B> b) {
		return zip(a.iterator(),b.iterator());
	}

	public static <A,B> ImmutableList<Pair<A,B>> zip(Iterator<A> a, Iterator<B> b) {
		return ImmutableList.copyOf(zip(a,b,(a1, b1) -> Pair.of(a1, b1)));
	}
}
