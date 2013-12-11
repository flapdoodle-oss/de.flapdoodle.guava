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
import java.util.EnumSet;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public abstract class Folds {

	private Folds() {
		// no instance
	}

	public static <S, D> D foldLeft(Collection<? extends S> collection, Fold<? super S, D> foldFunction, D leftValue) {
		Preconditions.checkNotNull(collection, "collection is null");
		Preconditions.checkNotNull(foldFunction, "foldFunction is null");
		
		D ret = leftValue;
		for (S value : collection) {
			ret = foldFunction.apply(ret, value);
		}
		return ret;
	}

	public static <T, V, C extends Collection<? extends V>> Fold<T, ImmutableList<V>> asCollectingFold(final Function<T, C> valueTransformation) {
		return new CollectingFold<T, V, C>() {

			@Override
			protected C apply(T right) {
				return valueTransformation.apply(right);
			}
		};
	}

	public static <T, V> Fold<T, Set<V>> asSetFold(final Function<T, V> valueTransformation) {
		return new SetFold<T, V>() {

			@Override
			protected V apply(T right) {
				return valueTransformation.apply(right);
			}
		};
	}

	public static <T, V extends Enum<V>> Fold<T, EnumSet<V>> asEnumSetFold(final Function<T, V> valueTransformation) {
		return new EnumSetFold<T, V>() {

			@Override
			protected V apply(T right) {
				return valueTransformation.apply(right);
			}
		};
	}

	abstract static class CollectingFold<T, V, C extends Collection<? extends V>> implements Fold<T, ImmutableList<V>> {

		@Override
		public final ImmutableList<V> apply(ImmutableList<V> left, T right) {
			ImmutableList<V> rightAsCollection = ImmutableList.copyOf(apply(right));
			return left != null
					? ImmutableList.<V> builder().addAll(left).addAll(rightAsCollection).build()
					: rightAsCollection;
		}

		protected abstract C apply(T right);
	}

	abstract static class SetFold<T, V> implements Fold<T, Set<V>> {

		@Override
		public final Set<V> apply(Set<V> left, T right) {
			ImmutableSet<V> rightAsSet = ImmutableSet.of(apply(right));
			return left != null
					? ImmutableSet.copyOf(Sets.<V> union(left, rightAsSet))
					: rightAsSet;
		}

		protected abstract V apply(T right);
	}

	abstract static class EnumSetFold<T, V extends Enum<V>> implements Fold<T, EnumSet<V>> {

		@Override
		public final EnumSet<V> apply(EnumSet<V> left, T right) {
			EnumSet<V> rightAsSet = EnumSet.of(apply(right));
			return left != null
					? EnumSet.copyOf(Sets.<V> union(left, rightAsSet))
					: rightAsSet;
		}

		protected abstract V apply(T right);
	}

	static class SimpleMappingFold<T, V> implements Fold<T, V> {

		private final Function<? super T, V> valueTransformation;

		public SimpleMappingFold(Function<? super T, V> valueTransformation) {
			this.valueTransformation = valueTransformation;
		}

		@Override
		public V apply(V left, T right) {
			V value = this.valueTransformation.apply(right);
			if (left != null) {
				throw new IllegalArgumentException("Entry " + right + " with mapped to " + value
						+ " is allready mapped to the same key as " + left);
			}
			return value;
		}
	}

}
