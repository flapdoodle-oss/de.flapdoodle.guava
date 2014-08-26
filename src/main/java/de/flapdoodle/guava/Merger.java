/**
 * Copyright (C) 2013
 *   Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
