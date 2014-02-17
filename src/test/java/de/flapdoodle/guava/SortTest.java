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

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mosmann
 */
public class SortTest {

	public SortTest() {
	}

	@Test
	public void sortComparables() {
		assertEquals("[a, c, e]", Sort.sort(Lists.newArrayList("e", "a", "c")).toString());
	}
	
	@Test
	public void sortWithComparator() {
		Comparator<String> com=Ordering.natural().reverse();
		assertEquals("[e, c, a]", Sort.sortBy(Lists.newArrayList("e", "a", "c"), com).toString());
	}
	
	@Test
	public void sortWithTransformationToComparable() {
		assertEquals("[100, 20, 3]", Sort.sortBy(Lists.newArrayList(3, 20, 100), new Function<Integer, String>() {

			@Override
			public String apply(Integer input) {
				return input.toString();
			}
			
		}).toString());
	}
	
	@Test
	public void sortWithTransformationAndComparator() {
		Comparator<Integer> comparator=Ordering.natural();
		assertEquals("[3, 20, 100]", Sort.sortBy(Lists.newArrayList("3", "20", "100"), new Function<String, Integer>() {

			@Override
			public Integer apply(String input) {
				return Integer.valueOf(input);
			}
			
		}, comparator).toString());
	}

}
