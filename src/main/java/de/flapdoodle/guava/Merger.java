package de.flapdoodle.guava;

import com.google.common.base.Equivalence;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


public abstract class Merger {

	
	private Merger() {
		// no instance
	}
	
	public static <T> ImmutableList<T> merge(Iterable<? extends T> left, Iterable<? extends T> right,Equivalence<? super T> matcher,Foldleft<? super T, T> fold) {
		ImmutableList.Builder<T> builder=ImmutableList.builder();
		Iterable<? extends T> notMerged=right;
		for (T l : left) {
			Iterable<? extends T> matching = Iterables.filter(notMerged, matcher.equivalentTo(l));
			notMerged = Iterables.filter(notMerged, Predicates.not(matcher.equivalentTo(l)));
			
			boolean noMatching=true;
			for (T r : matching) {
				builder.add(fold.apply(l, r));
				noMatching=false;
			}
			if (noMatching) {
				builder.add(l);
			}
		}
		builder.addAll(notMerged);
		return builder.build();
	}
}
