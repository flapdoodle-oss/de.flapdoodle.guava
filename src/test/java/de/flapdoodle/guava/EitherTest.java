package de.flapdoodle.guava;

import static org.junit.Assert.*;

import org.junit.Test;

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
}
