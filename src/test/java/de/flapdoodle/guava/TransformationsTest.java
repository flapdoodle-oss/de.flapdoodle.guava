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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class TransformationsTest {

	@Test
	public void nullCollectionShouldGiveEmpty() {
		assertTrue(Transformations.emptyIfNull(null).isEmpty());
	}

	@Test
	public void nonNullShouldReturnUnmodified() {
		ArrayList<String> source = Lists.newArrayList();
		assertTrue(Transformations.emptyIfNull(source).isEmpty());
		assertTrue(source == Transformations.emptyIfNull(source));
	}

	@Test
	public void flatmapShouldGiveAllEntries() {
		ImmutableList<? extends String> result = Transformations.flatmap(Lists.newArrayList("A", "B"),
				new Function<String, Collection<String>>() {

					@Override
					public Collection<String> apply(String input) {
						return Lists.newArrayList(input, input, input);
					}
				});

		assertEquals("[A, A, A, B, B, B]", result.toString());
	}

	@Test
	public void iterableFlatmapShouldGiveAllEntries() {
		Iterable<? extends String> result = Transformations.flatmap(Lists.newArrayList("A", "B"),
				new Function<String, Iterable<String>>() {

					@Override
					public Iterable<String> apply(String input) {
						return Lists.newArrayList(input, input, input);
					}
				});

		assertEquals("[A, A, A, B, B, B]", result.toString());
	}

	@Test
	public void flatmapShouldGiveAllEntriesFromLists() {
		List<? extends List<String>> lists = ImmutableList.<List<String>> builder().add(Lists.newArrayList("A", "B")).add(
				Lists.newArrayList("C", "D", "E")).build();

		ImmutableList<? extends String> result = Transformations.flatmap(lists);

		assertEquals("[A, B, C, D, E]", result.toString());
	}

	@Test
	public void mapListToMap() {
		Map<String, String> map = Transformations.map(Lists.newArrayList("Achim", "Susi", "Jochen"),
				new Function<String, String>() {

					@Override
					public String apply(String input) {
						return input.substring(0, 1);
					}
				});

		assertEquals(3, map.size());
		assertEquals("Susi", map.get("S"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void mapListToMapShouldFailIfKeyCollides() {
		Transformations.map(Lists.newArrayList("Achim", "Susi", "Jochen", "Arnim"), new Function<String, String>() {

			@Override
			public String apply(String input) {
				return input.substring(0, 1);
			}
		});
	}

	@Test
	public void mapListToMapWithFoldShouldGiveFoldedValuesInMap() {
		Map<String, ? extends List<? extends String>> map = Transformations.map(
				Lists.newArrayList("Achim", "Susi", "Jochen", "Arnim"), new Function<String, String>() {

					@Override
					public String apply(String input) {
						return input.substring(0, 1);
					}
				}, Folds.asListFold(Transformations.<String> asCollection()));

		assertEquals(3, map.size());
		assertEquals("[Susi]", map.get("S").toString());
		assertEquals("[Achim, Arnim]", map.get("A").toString());
	}
	
	@Test
	public void predicateWithTransformationIsPredicateWithDifferentType() {
		Predicate<Integer> predicate = Transformations.map(Predicates.equalTo("12"),new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return input.toString();
			}
		});
		
		assertTrue(predicate.apply(12));
		assertFalse(predicate.apply(11));
	}

	@Test
	public void firstOfShouldGiveEntriesOrLessIfCollectionIsSmaller() {
		assertEquals(2,Transformations.firstOf(Lists.newArrayList("A","B"), 17).size());
	}
	
	@Test
	public void firstOfShouldGiveOptionalPresentIfCollectionIsNotEmpty() {
		assertTrue(Transformations.firstOf(Lists.newArrayList("A","B")).isPresent());
		assertFalse(Transformations.firstOf(Lists.newArrayList()).isPresent());
		assertEquals("C", Transformations.firstOf(Lists.newArrayList("C","B")).get());
	}
	
	@Test
	public void partitionShouldSeparateMatchingFromOthers() {
		Partition<Integer> partition = Transformations.partition(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7),
				new Predicate<Integer>() {

					@Override
					public boolean apply(Integer input) {
						return input % 2 != 0;
					}
				});
		
		assertEquals("[1, 3, 5, 7]",partition.matching().toString());
		assertEquals("[2, 4, 6]",partition.notMatching().toString());
	}

	@Test
	public void splitInBoundsShouldGiveValidAnswers() {
		Partition<String> partition = Transformations.split(Lists.newArrayList("A", "B", "C"), 1);
		assertEquals("[A]", partition.matching().toString());
		assertEquals("[B, C]", partition.notMatching().toString());

		partition = Transformations.split(Lists.newArrayList("A", "B", "C"), 0);
		assertEquals("[]", partition.matching().toString());
		assertEquals("[A, B, C]", partition.notMatching().toString());

		partition = Transformations.split(Lists.newArrayList("A", "B", "C"), 3);
		assertEquals("[A, B, C]", partition.matching().toString());
		assertEquals("[]", partition.notMatching().toString());
	}
	
	@Test
	public void asCollectionShouldGiveCollectionForValue() {
		assertEquals("[foo]", Transformations.asCollection().apply("foo").toString());
	}
}
