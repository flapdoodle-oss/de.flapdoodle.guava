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

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.flapdoodle.guava.functions.NoTransformation;
import de.flapdoodle.guava.functions.ValueToCollection;
import java.util.Iterator;

public abstract class Transformations {

	private Transformations() {
		// no instance
	}

	public static <T> Collection<T> emptyIfNull(Collection<T> source) {
		return Types.defaultIfNull(source, Collections.<T> emptyList());
	}

	public static <S, D, C extends Collection<? extends D>> ImmutableList<? extends D> flatmap(Collection<? extends S> source,
			Function<? super S, C> transformation) {
		return Folds.foldLeft(source, Folds.asListFold(transformation), ImmutableList.<D> of());
	}

	public static <T> ImmutableList<? extends T> flatmap(Collection<? extends Collection<? extends T>> collections) {
		return flatmap(collections, new Function<Collection<? extends T>, Collection<? extends T>>() {

			@Override
			public Collection<? extends T> apply(Collection<? extends T> input) {
				return input;
			}
		});
	}
	
	public static <T> ImmutableList<? extends T> flatmap(Collection<? extends T> collection, Collection<? extends T> otherCollection, Collection<? extends T>... collections) {
		return flatmap(Varargs.asCollection(collection, otherCollection, collections));
	}

	public static <K, T> Map<K, T> map(Collection<T> collection, Function<? super T, K> keytransformation) {
		return map(collection, keytransformation, new NoTransformation<T>());
	}

	public static <K, V, T> Map<K, V> map(Collection<T> collection, Function<? super T, K> keyTransformation,
			Function<? super T, V> valueTransformation) {
		return map(MapCreators.<K, V> hashMap(), collection, keyTransformation, valueTransformation);
	}

	public static <K, V, T> Map<K, V> map(Collection<T> collection, Function<? super T, K> keyTransformation,
			Foldleft<? super T, V> valueFold) {
		return map(MapCreators.<K, V> hashMap(), collection, keyTransformation, valueFold);
	}

	public static <K extends Enum<K>, T> EnumMap<K, T> map(Class<K> enumType, Collection<T> collection,
			Function<? super T, K> keytransformation) {
		return map(enumType, collection, keytransformation, new NoTransformation<T>());
	}

	public static <K extends Enum<K>, V, T> EnumMap<K, V> map(Class<K> enumType, Collection<T> collection,
			Function<? super T, K> keyTransformation, Function<? super T, V> valueTransformation) {
		return map(MapCreators.<K, V> enumMap(enumType), collection, keyTransformation, valueTransformation);
	}

	public static <K extends Enum<K>, V, T> EnumMap<K, V> map(Class<K> enumType, Collection<T> collection,
			Function<? super T, K> keyTransformation, Foldleft<? super T, V> valueFold) {
		return map(MapCreators.<K, V> enumMap(enumType), collection, keyTransformation, valueFold);
	}

	private static <K, V, T, M extends Map<K, V>> M map(MapCreator<K, V, M> mapCreator, Collection<T> collection,
			Function<? super T, K> keyTransformation, Function<? super T, V> valueTransformation) {
		return map(mapCreator, collection, keyTransformation, new Folds.ValueFromLeftIllegalFold<T, V>(valueTransformation));
	}

	private static <K, V, T, M extends Map<K, V>> M map(MapCreator<K, V, M> mapCreator, Collection<T> collection,
			Function<? super T, K> keyTransformation, Foldleft<? super T, V> valueFold) {
		M map = mapCreator.newInstance();
		for (T value : collection) {
			K key = keyTransformation.apply(value);
			map.put(key, valueFold.apply(map.get(key), value));
		}
		return map;
	}

	public static <A, B> Predicate<B> map(final Predicate<A> predicate, final Function<B, A> transformation) {
		return new Predicate<B>() {

			@Override
			public boolean apply(B input) {
				return predicate.apply(transformation.apply(input));
			}
		};
	}

	public static <T> Collection<T> firstOf(Collection<T> collections, int items) {
		Preconditions.checkArgument(items > 0, "items must be bigger then 0");
		return Lists.newArrayList(collections).subList(0, Math.min(collections.size(), items));
	}

	public static <T> Optional<T> firstOf(Collection<T> collections) {
		if (collections.isEmpty()) {
			return Optional.absent();
		}
		return Optional.of(collections.iterator().next());
	}

	public static <T> Partition<T> partition(Collection<T> collection, Predicate<? super T> filter) {
		return new Partition<T>(Collections2.filter(collection, filter), Collections2.filter(collection,
				Predicates.not(filter)));
	}

	public static <T> Partition<T> split(Collection<T> source, int index) {
		List<T> asList = ImmutableList.copyOf(source);
		Preconditions.checkArgument(index >= 0, "index < 0");
		Preconditions.checkArgument(index <= asList.size(), "index > size");
		return new Partition<T>(asList.subList(0, index), asList.subList(index, asList.size()));
	}

	public static <A,B> Collection<Pair<A,B>> zip(Iterable<A> a, Iterable<B> b) {
		return zip(a.iterator(),b.iterator());
	}
	
	public static <A,B> Collection<Pair<A,B>> zip(Iterator<A> a, Iterator<B> b) {
		Preconditions.checkNotNull(a,"a is null");
		Preconditions.checkNotNull(b,"b is null");
		
		final ImmutableList.Builder<Pair<A, B>> builder = ImmutableList.<Pair<A,B>>builder();
		int pos=0;
		boolean done=false;
		do {
			boolean aNext=a.hasNext();
			boolean bNext=b.hasNext();
			if (aNext  && bNext) {
				builder.add(new Pair(a.next(),b.next()));
				pos++;
			} else {
				if ((aNext) || (bNext)) {
					throw new IndexOutOfBoundsException("no element in "+(aNext?"a":"b")+" found at "+pos);
				}
				done=true;
			}
		} while (!done);
		return builder.build();
	}

	public static <V> Function<V, V> noop() {
		return new NoTransformation<V>();
	}

	public static <V> Function<V, Collection<? extends V>> asCollection() {
		return new ValueToCollection<V>();
	}

	public static <S, D> Function<S, Collection<? extends D>> asCollection(Function<S, D> transformation) {
		return Functions.compose(new ValueToCollection<D>(), transformation);
	}
}
