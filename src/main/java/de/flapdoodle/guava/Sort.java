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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author mosmann
 */
public abstract class Sort {

	private Sort() {
		// no instance
	}

	public static <T extends Comparable<T>> List<T> sort(Iterable<T> source) {
		ArrayList<T> toSort = Lists.newArrayList(source);
		Collections.sort(toSort);
		return toSort;
	}

	public static <T> List<T> sortBy(Iterable<T> source, Comparator<? super T> comparator) {
		ArrayList<T> toSort = Lists.newArrayList(source);
		Collections.sort(toSort, comparator);
		return toSort;
	}

	public static <T, S> List<T> sortBy(Iterable<T> source, Function<T, S> sortTransformation, Comparator<? super S> comparator) {
		List<EntryRef<T, S>> result = Lists.transform(Lists.newArrayList(source), new EntryRefTransformation<T, S>(sortTransformation));
		List<EntryRef<T, S>> sorted = sortBy(result, new EntryRefComparator<S>(comparator));
		return Lists.transform(sorted, new EntryRefSourceTransformation());
	}

	public static <T, S extends Comparable<S>> List<T> sortBy(Iterable<T> source, Function<T, S> sortTransformation) {
		return sortBy(source, sortTransformation, Ordering.natural());
	}

	static class EntryRefSourceTransformation<T, S> implements Function<EntryRef<T, S>, T> {

		@Override
		public T apply(EntryRef<T, S> input) {
			return input.source();
		}

	}

	static class EntryRefTransformation<T, S> implements Function<T, EntryRef<T, S>> {

		private final Function<T, S> sortTransformation;

		public EntryRefTransformation(Function<T, S> sortTransformation) {
			this.sortTransformation = sortTransformation;
		}

		@Override
		public EntryRef<T, S> apply(T input) {
			return new EntryRef<T, S>(input, sortTransformation.apply(input));
		}
	}

	static class EntryRefComparator<S> implements Comparator<EntryRef<?, S>> {

		private final Comparator<? super S> comparator;

		public EntryRefComparator(Comparator<? super S> comparator) {
			this.comparator = comparator;
		}

		@Override
		public int compare(EntryRef<?, S> o1, EntryRef<?, S> o2) {
			return comparator.compare(o1.sortValue(), o2.sortValue());
		}

	}

	static class EntryRef<T, S> {

		private final T source;
		private final S sortValue;

		public EntryRef(T source, S sortValue) {
			this.source = source;
			this.sortValue = sortValue;
		}

		public T source() {
			return source;
		}

		public S sortValue() {
			return sortValue;
		}
	}
}
