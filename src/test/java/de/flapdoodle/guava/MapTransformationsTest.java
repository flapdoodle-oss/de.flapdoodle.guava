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

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;


public class MapTransformationsTest {

	@Test
	public void remapKeyAndValue() {
		Map<String, Integer> source=ImmutableMap.of("foo", 1,"bar",2);
		Map<Character,String> result=MapTransformations.transform(source, new Function<String, Character>() {
			@Override
			public Character apply(String input) {
				return input.charAt(0);
			}
		}, new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return input.toString();
			}
		});
		
		assertEquals(2,result.size());
		assertEquals("1",result.get('f'));
		assertEquals("2",result.get('b'));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void mapToSameKeyShouldFail() {
		Map<String, Integer> source=ImmutableMap.of("foo", 1,"bar",2);
		MapTransformations.transformKeys(source, new Function<String, String>() {
			@Override
			public String apply(String input) {
				return "not-unique";
			}
		});
	}	

}
