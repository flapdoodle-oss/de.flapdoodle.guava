/*
 * Copyright 2014 mmosmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.flapdoodle.guava.talk.jan2014.preconditions;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author mmosmann
 */
public class TestOptional {
	
	@Test
	public void testOptional() {
		assertFalse(findUser(12).isPresent());
		assertEquals("foo",emailForUser(12).or("foo"));
		
		try {
			loadExistingUser(12);
			fail("can not be reached");
		} catch (IllegalStateException state) {
			
		}
		
	}
	
	static Optional<User> findUser(int userId) {
		return Optional.absent();
	}
	
	static Optional<String> emailForUser(int userId) {
		return findUser(userId).transform(new Function<User, String>() {

			@Override
			public String apply(User user) {
				return user.name;
			}
		});
	}
	
	static User loadExistingUser(int userId) {
		return findUser(userId).get();
	}
	
	static class User {
		public String name;
	}
}
