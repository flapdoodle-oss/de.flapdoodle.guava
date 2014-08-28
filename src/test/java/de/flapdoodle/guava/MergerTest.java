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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class MergerTest {

	@Test
	public void mergeTwoNumberListWillGiveSumIfMatching() {
		ImmutableList<Integer> left = ImmutableList.of(1, 2, 4, 10);
		ImmutableList<Integer> right = ImmutableList.of(0, 3, 4, 10);

		ImmutableList<Integer> result = Merger.merge(left, right, Equivalence.equals(), new Foldleft<Integer, Integer>() {

			@Override
			public Integer apply(Integer left, Integer right) {
				return left + right;
			}
		});

		assertEquals("[1, 2, 8, 20, 0, 3]", result.toString());
	}

	@Test
	public void removeEvenNumbersIfInSet() {
		ImmutableList<Integer> src = ImmutableList.of(1, 2, 3, 4, 5, 10);
		final ImmutableSet<Integer> set = ImmutableSet.of(2, 10);

		ImmutableList<Integer> removed = Merger.remove(src, new Predicate<Integer>() {

			@Override
			public boolean apply(Integer input) {
				return input % 2 == 0;
			}
		}, new Function<Integer, Optional<Integer>>() {

			@Override
			public Optional<Integer> apply(Integer input) {
				if (set.contains(input)) {
					return Optional.absent();
				}
				return Optional.of(input);
			}
		});
		
		assertEquals("[1, 3, 4, 5]", removed.toString());
	}
}
