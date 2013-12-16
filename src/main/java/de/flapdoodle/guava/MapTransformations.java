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

import java.util.Map;

import com.google.common.base.Function;

public abstract class MapTransformations {

	private MapTransformations() {
		// no instance
	}

	public static <S, D, V, M> Map<D, M> transform(Map<S, V> map, final Function<? super S, D> keytransformation,
			final Foldleft<? super V, M> valuetransformation) {
		return Transformations.map(map.entrySet(), new Function<Map.Entry<S, V>, D>() {

			@Override
			public D apply(Map.Entry<S, V> input) {
				return keytransformation.apply(input.getKey());
			}
		}, new Foldleft<Map.Entry<S, V>, M>() {

			@Override
			public M apply(M left, Map.Entry<S, V> right) {
				try {
					return valuetransformation.apply(left, right.getValue());
				} catch (RuntimeException rx) {
					throw new RuntimeException("apply left(" + left + ") and right(" + right + ")", rx);
				}
			}
		});
	}

	public static <S, D, V, M> Map<D, M> transform(Map<S, V> map, final Function<? super S, D> keytransformation,
			final Function<? super V, M> valuetransformation) {
		return transform(map, keytransformation, new Folds.ValueFromLeftIllegalFold<V, M>(valuetransformation));
	}

	public static <S, D, V> Map<D, V> transformKeys(Map<S, V> map, final Function<? super S, D> keytransformation) {
		return transform(map, keytransformation, new NoTransformation<V>());
	}
}
