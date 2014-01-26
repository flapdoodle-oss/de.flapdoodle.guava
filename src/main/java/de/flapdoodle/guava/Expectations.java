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
import java.util.Iterator;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

public abstract class Expectations {

	private Expectations() {
		// no instance
	}

	private static <T> Optional<T> intNoneOrOne(Iterator<T> iterator) {
		Preconditions.checkNotNull(iterator, "iterator is null");
		if (!iterator.hasNext()) {
			return Optional.absent();
		}
		return Optional.of(iterator.next());
	}

	public static <T> Optional<T> noneOrOneIfOnlyOne(Iterator<T> iterator) {
		Optional<T> ret = intNoneOrOne(iterator);
		if (iterator.hasNext()) {
			return Optional.absent();
		}
		return ret;
	}

	public static <T> Optional<T> noneOrOneIfOnlyOne(Iterable<T> values) {
		Preconditions.checkNotNull(values, "values is null");
		return noneOrOneIfOnlyOne(values.iterator());
	}

	public static <T> T oneIfOnlyOne(Iterator<T> iterator, T defaultValue) {
		return noneOrOneIfOnlyOne(iterator).or(Optional.fromNullable(defaultValue)).orNull();
	}

	public static <T> T oneIfOnlyOne(Iterable<T> values, T defaultValue) {
		Preconditions.checkNotNull(values, "values is null");
		return oneIfOnlyOne(values.iterator(), defaultValue);
	}

	public static <T> Optional<T> noneOrOne(Iterator<T> iterator) {
		Optional<T> ret = intNoneOrOne(iterator);
		Preconditions.checkArgument(iterator.hasNext() == false, "contains more than one");
		return ret;
	}

	public static <T> Optional<T> noneOrOne(Iterable<T> values) {
		Preconditions.checkNotNull(values, "values is null");
		return noneOrOne(values.iterator());
	}

	/**
	 * @see Iterables#any(Iterable, Predicate)
	 */
	@Deprecated
	public static <T> boolean any(Collection<T> candidates, Predicate<T> condition) {
		return Iterables.any(candidates, condition);
	}

}
