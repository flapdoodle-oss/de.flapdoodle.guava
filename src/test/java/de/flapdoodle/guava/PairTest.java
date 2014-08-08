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

import org.junit.Test;


public class PairTest {

	@Test
	public void shouldImplementHashAndEquals() {
		Pair<String, Integer> a = Pair.of("foo", 2);
		Pair<String, Integer> a2 = Pair.of("foo", 2);
		Pair<String, Integer> b = Pair.of("bee", 2);
		
		assertEquals(a,a);
		assertEquals(a,a2);
		assertEquals(a2,a);
		assertEquals(a2,a2);
		
		assertEquals(a.hashCode(),a2.hashCode());
		
		assertNotEquals(a,b);
		assertNotEquals(a2,b);
	}

}
