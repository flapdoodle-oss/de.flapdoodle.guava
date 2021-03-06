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
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

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

	/**
	 * @see Iterators#getOnlyElement(Iterator, Object)
	 * gives one, if only one value exist
	 * gives none on everything else
	 * 
	 * @param values
	 * @return optional
	 */
	@Deprecated
	public static <T> Optional<T> noneOrOneIfOnlyOne(Iterator<T> iterator) {
		Optional<T> ret = intNoneOrOne(iterator);
		if (iterator.hasNext()) {
			return Optional.absent();
		}
		return ret;
	}

	/**
	 * @see Iterators#getOnlyElement(Iterator, Object)
	 * gives one, if only one value exist
	 * gives none on everything else
	 * 
	 * @param values
	 * @return optional
	 */
	@Deprecated
	public static <T> Optional<T> noneOrOneIfOnlyOne(Iterable<T> values) {
		Preconditions.checkNotNull(values, "values is null");
		return noneOrOneIfOnlyOne(values.iterator());
	}

	/**
	 * @see Iterators#getOnlyElement(Iterator, Object)
	 * gives first value, if one value exist
	 * gives defaultValue if empty or more than one value exist,
	 * 
	 * @param values
	 * @return value
	 */
	@Deprecated
	public static <T> T oneIfOnlyOne(Iterator<T> iterator, T defaultValue) {
		return noneOrOneIfOnlyOne(iterator).or(Optional.fromNullable(defaultValue)).orNull();
	}

	/**
	 * @see Iterators#getOnlyElement(Iterator, Object)
	 * gives first value, if one value exist
	 * gives defaultValue if empty or more than one value exist,
	 * 
	 * @param values
	 * @return value
	 */
	@Deprecated
	public static <T> T oneIfOnlyOne(Iterable<T> values, T defaultValue) {
		Preconditions.checkNotNull(values, "values is null");
		return oneIfOnlyOne(values.iterator(), defaultValue);
	}

	/**
	 * @see Iterators#getOnlyElement(Iterator, Object)
	 * gives none if empty, one if one element, fails if more than one
	 * 
	 * @param values
	 * @return optional
	 * @throws IllegalArgumentException if more than one value exist
	 */
	@Deprecated
	@InlineCallToReplaceDeprecatedFunction
	public static <T> Optional<T> noneOrOne(Iterator<T> iterator) {
		return Optional.fromNullable(Iterators.getOnlyElement(iterator, null));
//		Optional<T> ret = intNoneOrOne(iterator);
//		Preconditions.checkArgument(iterator.hasNext() == false, "contains more than one");
//		return ret;
	}

	/**
	 * @see Iterators#getOnlyElement(Iterator, Object)
	 * gives none if empty, one if one element, fails if more than one
	 * 
	 * @param values
	 * @return optional
	 * @throws IllegalArgumentException if more than one value exist
	 */
	@Deprecated
	@InlineCallToReplaceDeprecatedFunction
	public static <T> Optional<T> noneOrOne(Iterable<T> values) {
		return Optional.fromNullable(Iterables.getOnlyElement(values, null));
//		Preconditions.checkNotNull(values, "values is null");
//		return noneOrOne(values.iterator());
	}

	/**
	 * @see Iterables#any(Iterable, Predicate)
	 */
	@Deprecated
	@InlineCallToReplaceDeprecatedFunction
	public static <T> boolean any(Collection<T> candidates, Predicate<T> condition) {
		return Iterables.any(candidates, condition);
	}

}
