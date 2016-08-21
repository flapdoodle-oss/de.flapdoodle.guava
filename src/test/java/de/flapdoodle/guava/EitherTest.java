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

import com.google.common.base.Function;

import de.flapdoodle.guava.Either.Left;
import de.flapdoodle.guava.Either.Right;


public class EitherTest {

	@Test
	public void leftShouldImplementHashcodeAndEquals() {
		Left<String, Object> a = Either.left("foo");
		Left<String, Object> a2 = Either.left("foo");
		Left<String, Object> b = Either.left("bar");
		
		assertEquals(a,a);
		assertEquals(a,a2);
		assertEquals(a2,a);
		assertEquals(a2,a2);
		
		assertEquals(a.hashCode(),a2.hashCode());
		
		assertNotEquals(a,b);
		assertNotEquals(a2,b);
		
	}

	@Test
	public void rightShouldImplementHashcodeAndEquals() {
		Right<Object, String> a = Either.right("foo");
		Right<Object, String> a2 = Either.right("foo");
		Right<Object, String> b = Either.right("bar");
		
		assertEquals(a,a);
		assertEquals(a,a2);
		assertEquals(a2,a);
		assertEquals(a2,a2);
		
		assertEquals(a.hashCode(),a2.hashCode());
		
		assertNotEquals(a,b);
		assertNotEquals(a2,b);
		
	}

	@Test
	public void bothShouldImplementHashcodeAndEquals() {
		Right<Object, String> a = Either.right("foo");
		Right<Object, String> a2 = Either.right("foo");
		Left<String, Object> b = Either.left("foo");
		
		assertEquals(a,a);
		assertEquals(a,a2);
		assertEquals(a2,a);
		assertEquals(a2,a2);
		
		assertEquals(a.hashCode(),a2.hashCode());
		
		assertNotEquals(a,b);
		assertNotEquals(a2,b);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void leftOrRightWithBothNullShouldFail() {
		Either.leftOrRight(null, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void leftOrRightWithBothNonNullShouldFail() {
		Either.leftOrRight("A", "B");
	}
	
	@Test(expected=NullPointerException.class)
	public void leftGetRightValueShouldFail() {
		Either.leftOrRight("A", null).right();
	}
	
	@Test(expected=NullPointerException.class)
	public void rightGetLeftValueShouldFail() {
		Either.leftOrRight(null, "B").left();
	}
	
	@Test
	public void mapLeftShouldMapValueOnlyIfLeft() {
		assertEquals("12", Either.left(12L).mapLeft(new Function<Long, String>() {

			@Override
			public String apply(Long input) {
				return ""+input;
			}
		}).left());
		
		assertEquals(Long.valueOf(12L), Either.<Long, String>left(12L).mapRight(new Function<String, String>() {

			@Override
			public String apply(String input) {
				throw new IllegalArgumentException("should not be called");
			}
		}).left());
	}
	
	@Test
	public void flatmapLeftShouldMapValueOnlyIfLeft() {
		assertEquals("12", Either.<Long, String>left(12L).flatmapLeft(new Function<Long, Either<Long, String>>() {

			@Override
			public Either<Long, String> apply(Long input) {
				return Either.right(""+input);
			}

		}).right());
		
		assertEquals("12", Either.<Long, String>right("12").flatmapLeft(new Function<Long, Either<Long, String>>() {

			@Override
			public Either<Long, String> apply(Long input) {
				throw new IllegalArgumentException("should not be called");
			}
		}).right());
	}
	
	@Test
	public void flatmapRightShouldMapValueOnlyIfLeft() {
		assertEquals(Long.valueOf(12), Either.<String, Long>right(12L).flatmapRight(new Function<Long, Either<String, Long>>() {

			@Override
			public Either<String, Long> apply(Long input) {
				return Either.right(input);
			}

		}).right());
		
		assertEquals("12", Either.<String, Long>left("12").flatmapRight(new Function<Long, Either<String, Long>>() {

			@Override
			public Either<String, Long> apply(Long input) {
				throw new IllegalArgumentException("should not be called");
			}
		}).left());
	}
	
	@Test
	public void mapRightShouldMapValueOnlyIfRight() {
		assertEquals("12", Either.right(12L).mapRight(new Function<Long, String>() {

			@Override
			public String apply(Long input) {
				return ""+input;
			}
		}).right());
		
		assertEquals(Long.valueOf(12L), Either.<String, Long>right(12L).mapLeft(new Function<String, String>() {

			@Override
			public String apply(String input) {
				throw new IllegalArgumentException("should not be called");
			}
		}).right());
	}
}
