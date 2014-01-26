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

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;


public class ExpectationsTest {

	@Test
	public void noneOrOneGivesOneIfOne() {
		assertTrue(Expectations.noneOrOne(Lists.newArrayList("foo")).isPresent());
	}

	@Test
	public void noneOrOneGivesNoneIfEmpty() {
		assertFalse(Expectations.noneOrOne(Lists.newArrayList()).isPresent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void noneOrOneFailsIfMoreThanOne() {
		Expectations.noneOrOne(Lists.newArrayList("foo","bar"));
	}

	@Test
	public void noneIfNotExactlyOne() {
		assertFalse(Expectations.noneOrOneIfOnlyOne(Lists.newArrayList()).isPresent());
		assertFalse(Expectations.noneOrOneIfOnlyOne(Lists.newArrayList("foo","bar")).isPresent());
	}

	@Test
	public void oneIfExactlyOne() {
		assertEquals("foo",Expectations.noneOrOneIfOnlyOne(Lists.newArrayList("foo")).get());
	}

	@Test
	public void defaultValueIfNotExactlyOne() {
		assertEquals("plop",Expectations.oneIfOnlyOne(Lists.newArrayList("foo","bar"),"plop"));
	}
}
