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
package de.flapdoodle.guava.maps;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class FluentMapTest {

	@Test
	public void inverseMapMustWorkIfNoCollision() {
		Map<String, Integer> source = ImmutableMap.of("foo",2,"bar",17); 
		
		ImmutableMap<Integer, String> inversedMap = FluentMap.from(source)
			.inverse()
			.asImmutable();
		
		assertEquals("{2=foo, 17=bar}", inversedMap.toString());
	}
	
	@Test
	public void mapMustGiveAMapWithAllKeyWhereValuesContainedInSecondMap() {
		Map<String, Integer> source = ImmutableMap.of("foo",2,"bar",17);
		Map<Integer, String> second = ImmutableMap.of(2, "zwei",3,"drei");
		
		ImmutableMap<String, String> result = FluentMap.from(source)
			.transformValues(second)
			.asImmutable();
		
		assertEquals("{foo=zwei}", result.toString());
	}
}
