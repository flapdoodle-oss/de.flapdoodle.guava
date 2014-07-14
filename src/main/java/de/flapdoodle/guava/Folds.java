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
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public abstract class Folds {

	private Folds() {
		// no instance
	}

	public static <S, D> D foldLeft(Iterable<? extends S> collection, Foldleft<? super S, D> foldFunction, D leftValue) {
		Preconditions.checkNotNull(collection, "collection is null");
		Preconditions.checkNotNull(foldFunction, "foldFunction is null");

		D ret = leftValue;
		for (S value : collection) {
			ret = foldFunction.apply(ret, value);
		}
		return ret;
	}

	public static <R, V> Foldleft<R, ImmutableList<? extends V>> asListFold(
			final Function<R, ? extends Collection<? extends V>> valueTransformation) {
		return new TransformationFold<R, V, ImmutableList<? extends V>>(new ImmutableListFold<V>(), valueTransformation);
	}

	public static <R, V> Foldleft<R, ImmutableSet<? extends V>> asSetFold(
			final Function<R, ? extends Collection<? extends V>> valueTransformation) {
		return new TransformationFold<R, V, ImmutableSet<? extends V>>(new ImmutableSetFold<V>(), valueTransformation);
	}

	public static <R, V extends Enum<V>> Foldleft<R, EnumSet<V>> asEnumSetFold(Class<V> enumType,
			final Function<R, ? extends Collection<? extends V>> valueTransformation) {
		return new TransformationFold<R, V, EnumSet<V>>(new EnumSetFold<V>(enumType), valueTransformation);
	}

	public static <R, V> Foldleft<R, Iterable<? extends V>> asIterableFold(
			final Function<R, ? extends Iterable<? extends V>> valueTransformation) {
		return new IterableTransformationFold<R, V, Iterable<? extends V>>(new IterableFoldImpl<V>(), valueTransformation);
	}

	
	interface CollectingFold<R, C extends Collection<? extends R>> extends Foldleft<Collection<? extends R>, C> {

	}

	static class ImmutableListFold<R> implements CollectingFold<R, ImmutableList<? extends R>> {

		@Override
		public ImmutableList<? extends R> apply(ImmutableList<? extends R> left, Collection<? extends R> right) {
			left = Types.defaultIfNull(left, ImmutableList.<R> of());

			if (right.isEmpty())
				return left;
			if (left.isEmpty())
				return ImmutableList.copyOf(right);
			return ImmutableList.<R> builder().addAll(left).addAll(right).build();
		}
	}

	static class ImmutableSetFold<R> implements CollectingFold<R, ImmutableSet<? extends R>> {

		@Override
		public ImmutableSet<? extends R> apply(ImmutableSet<? extends R> left, Collection<? extends R> right) {
			left = Types.defaultIfNull(left, ImmutableSet.<R> of());

			if (right.isEmpty())
				return left;

			ImmutableSet<R> ret;

			if (left.isEmpty()) {
				ret = ImmutableSet.copyOf(right);
			} else {
				ret = ImmutableSet.<R> builder().addAll(left).addAll(right).build();
			}
			if (ret.size() < (left.size() + right.size())) {
				throw new IllegalArgumentException("colliding entries: " + left + "-" + right);
			}
			return ret;
		}
	}
	
	static class EnumSetFold<R extends Enum<R>> implements CollectingFold<R, EnumSet<R>> {

		private final Class<R> _enumType;

		public EnumSetFold(Class<R> enumType) {
			_enumType = enumType;
		}

		@Override
		public EnumSet<R> apply(EnumSet<R> left, Collection<? extends R> right) {
			left = Types.defaultIfNull(left, EnumSet.noneOf(_enumType));

			if (right.isEmpty())
				return left;

			EnumSet<R> ret = EnumSet.copyOf(left);
			ret.addAll(right);
			if (ret.size() < left.size() + right.size()) {
				throw new IllegalArgumentException("colliding entries: " + left + "-" + right);
			}
			return ret;
		}
	}

	static class TransformationFold<R, D, C extends Collection<? extends D>> implements Foldleft<R, C> {

		private final Foldleft<Collection<? extends D>, C> _fold;
		private final Function<R, ? extends Collection<? extends D>> _transformation;

		public TransformationFold(Foldleft<Collection<? extends D>, C> fold, Function<R, ? extends Collection<? extends D>> transformation) {
			_fold = fold;
			_transformation = transformation;
		}

		@Override
		public C apply(C left, R right) {
			return _fold.apply(left, _transformation.apply(right));
		}
	}

	interface IterableFold<R, C extends Iterable<? extends R>> extends Foldleft<Iterable<? extends R>, C> {

	}

	static class IterableFoldImpl<R> implements IterableFold<R, Iterable<? extends R>> {

		@Override
		public Iterable<? extends R> apply(Iterable<? extends R> left, Iterable<? extends R> right) {
			left = Types.defaultIfNull(left, ImmutableList.<R> of());

			return Iterables.concat(left,right);
		}
	}

	static class IterableTransformationFold<R, D, C extends Iterable<? extends D>> implements Foldleft<R, C> {

		private final Foldleft<Iterable<? extends D>, C> _fold;
		private final Function<R, ? extends Iterable<? extends D>> _transformation;

		public IterableTransformationFold(Foldleft<Iterable<? extends D>, C> fold, Function<R, ? extends Iterable<? extends D>> transformation) {
			_fold = fold;
			_transformation = transformation;
		}

		@Override
		public C apply(C left, R right) {
			return _fold.apply(left, _transformation.apply(right));
		}
	}


	static class ValueFromLeftIllegalFold<T, V> implements Foldleft<T, V> {

		private final Function<? super T, V> valueTransformation;

		public ValueFromLeftIllegalFold(Function<? super T, V> valueTransformation) {
			this.valueTransformation = valueTransformation;
		}

		@Override
		public V apply(V left, T right) {
			V value = this.valueTransformation.apply(right);
			if (left != null) {
				throw new IllegalArgumentException("entry '" + right + "' mapped to '" + value
						+ "' is allready mapped to the same key as '" + left+"'");
			}
			return value;
		}
	}

}
