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
