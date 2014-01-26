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
package de.flapdoodle.guava.talk.jan2014.preconditions;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.flapdoodle.guava.Foldleft;
import de.flapdoodle.guava.Folds;
import de.flapdoodle.guava.Transformations;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author mmosmann
 */
public class TestFolds {
	
	@Test
	public void testCount() {
		List<Integer> source = Lists.newArrayList(1,2,3,4,5,6);
		int result = Folds.foldLeft(source, new Foldleft<Integer, Integer>() {
			@Override
			public Integer apply(Integer left, Integer right) {
				return left+1;
			}
		}, 0);
		assertEquals(6,result);
	}

	@Test
	public void testConcat() {
		List<String> source = Lists.newArrayList("fu","man","chu");
		String result = Folds.foldLeft(source, new Foldleft<String, String>() {
			@Override
			public String apply(String left, String right) {
				return left+" "+right;
			}
		}, "name is");
		assertEquals("name is fu man chu",result);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMapByFoldWithOneToOne() {
		List<String> names = Lists.newArrayList("Achim", "Albert", "Susi", "Sonja", "Bert");

		Function<String, Character> keytransformation = new Function<String, Character>() {

			@Override
			public Character apply(String name) {
				return name.charAt(0);
			}
		};

		Map<Character, String> nameMap = Transformations.map(names, keytransformation,new Foldleft<String, String>() {

			@Override
			public String apply(String left, String right) {
				if (left!=null) {
					throw new IllegalArgumentException("key for "+right+" allready mapped to "+left);
				}
				return right;
			}
		});
	}
}
