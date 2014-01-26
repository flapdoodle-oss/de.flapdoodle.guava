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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.flapdoodle.guava.Expectations;

public class TestExpectations {

	@Test
	public void giveMeOneIfOnlyOneExistAndFailIfMore() {
		Optional<String> result = Expectations.noneOrOne(Lists.<String> newArrayList());
		assertFalse(result.isPresent());
		result = Expectations.noneOrOne(Lists.newArrayList("foo"));
		assertTrue(result.isPresent());

		try {
			result = Expectations.noneOrOne(Lists.newArrayList("foo", "bar"));
			fail("should not be reached");
		} catch (IllegalArgumentException iax) {

		}
	}

}
