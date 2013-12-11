/**
 * Copyright (C) 2011
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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public abstract class Transformations {

	private Transformations() {
		// no instance
	}

	public static <T> Collection<T> emptyIfNull(Collection<T> source) {
		return Types.defaultIfNull(source, Collections.<T> emptyList());
	}

	public static <S, D, C extends Collection<? extends D>> ImmutableList<D> flatmap(Collection<? extends S> source,
			Function<? super S,C> transformation) {

		Preconditions.checkNotNull(source, "source is null");
		Preconditions.checkNotNull(transformation, "tranformation is null");

		return Folds.foldLeft(source, Folds.asCollectingFold(transformation), null);
	}

	public static <T> ImmutableList<T> flatmap(Collection<? extends Collection<? extends T>> collections) {
		return flatmap(collections, new Function<Collection<? extends T>, Collection<? extends T>>() {

			@Override
			public Collection<? extends T> apply(Collection<? extends T> input) {
				return input;
			}
		});
	}

	public static <K, T> Map<K, T> map(Collection<T> collection, Function<? super T, K> keytransformation) {
		return map(collection, keytransformation, new NoTransformation<T>());
	}

	public static <K, V, T> Map<K, V> map(Collection<T> collection, Function<? super T, K> keyTransformation,
			Function<? super T, V> valueTransformation) {
		return map(MapCreators.<K, V> linkedHashMap(), collection, keyTransformation, valueTransformation);
	}

	public static <K, V, T> Map<K, V> map(Collection<T> collection, Function<? super T, K> keyTransformation,
			Fold<? super T, V> valueFold) {
		return map(MapCreators.<K, V> linkedHashMap(), collection, keyTransformation, valueFold);
	}

	public static <K extends Enum<K>, T> EnumMap<K, T> map(Class<K> enumType, Collection<T> collection,
			Function<? super T, K> keytransformation) {
		return map(enumType, collection, keytransformation, new NoTransformation<T>());
	}

	public static <K extends Enum<K>, V, T> EnumMap<K, V> map(Class<K> enumType, Collection<T> collection,
			Function<? super T, K> keyTransformation, Function<? super T, V> valueTransformation) {
		return map(MapCreators.<K,V>enumMap(enumType), collection, keyTransformation, valueTransformation);
	}

	public static <K extends Enum<K>, V, T> EnumMap<K, V> map(Class<K> enumType, Collection<T> collection,
			Function<? super T, K> keyTransformation, Fold<? super T, V> valueFold) {
		return map(MapCreators.<K,V>enumMap(enumType), collection, keyTransformation, valueFold);
	}

	private static <K, V, T, M extends Map<K, V>> M map(MapCreator<K, V, M> mapCreator, Collection<T> collection,
			Function<? super T, K> keyTransformation, Function<? super T, V> valueTransformation) {
		return map(mapCreator, collection, keyTransformation, new Folds.SimpleMappingFold<T, V>(valueTransformation));
	}

	private static <K, V, T, M extends Map<K, V>> M map(MapCreator<K, V, M> mapCreator, Collection<T> collection,
			Function<? super T, K> keyTransformation, Fold<? super T, V> valueFold) {
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
		Preconditions.checkArgument(index>=0,"index < 0");
		Preconditions.checkArgument(index<=asList.size(),"index > size");
		return new Partition<T>(asList.subList(0, index), asList.subList(index, asList.size()));
	}

}
