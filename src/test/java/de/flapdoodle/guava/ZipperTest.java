package de.flapdoodle.guava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class ZipperTest {

	@Test
	public void zipperShouldNotFailOnInitWithDifferentLength() {
		Iterable<Integer> result = Zipper.zip(ImmutableList.of(1), ImmutableList.of(1,2), (a,b) -> a+b);
		Iterator<Integer> iterator = result.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(2),iterator.next());
		assertFalse(iterator.hasNext());
	}
}
