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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * 
 * @author mmosmann
 */
public abstract class Varargs {

	private Varargs() {
		// no instance
	}

	public static <T> ImmutableList<T> asCollection(T arg, T... others) {
		return ImmutableList.<T> builder().add(arg).add(others).build();
	}

	public static <T> ImmutableList<T> asCollection(T arg, T other, T... others) {
		return ImmutableList.<T> builder().add(arg).add(other).add(others).build();
	}

	@SafeVarargs
	public static <T> ImmutableList<T> asCollection(Collection<T> list, T arg, T... others) {
		return ImmutableList.<T> builder().addAll(list).add(arg).add(others).build();
	}

	@SafeVarargs
	public static <T> ImmutableList<T> asCollection(T[] list, T arg, T... others) {
		return ImmutableList.<T> builder().add(list).add(arg).add(others).build();
	}

	@SafeVarargs
	public static <T> ImmutableList<T> asCollectionPrependVarargs(Collection<T> list, T arg, T... others) {
		return ImmutableList.<T> builder().add(arg).add(others).addAll(list).build();
	}

	@SafeVarargs
	public static <T> ImmutableList<T> asCollectionPrependVarargs(T[] list, T arg, T... others) {
		return ImmutableList.<T> builder().add(arg).add(others).add(list).build();
	}

	public static <T> T[] asArray(Collection<? extends T> collection, Class<T> type) {
		return Iterables.toArray(collection, type);
	}
}
