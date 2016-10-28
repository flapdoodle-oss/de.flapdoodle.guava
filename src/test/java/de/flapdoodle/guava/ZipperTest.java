package de.flapdoodle.guava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class ZipperTest {

	@Test
	public void zipperShouldNotFailOnInitWithDifferentLength() {
		Iterable<Integer> result = Zipper.zip(ImmutableList.of(1), ImmutableList.of(1,2), (a,b) -> a+b);
		Iterator<Integer> iterator = result.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(2),iterator.next());
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void zipperShouldFailWithDifferentLengthIfLimitReached() {
		Iterable<Integer> result = Zipper.zip(ImmutableList.of(1), ImmutableList.of(1,2), (a,b) -> a+b);
		Iterator<Integer> iterator = result.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(Integer.valueOf(2),iterator.next());
		iterator.hasNext();
		
		fail("should not happen");
	}
	
	@Test
	public void zipShouldReturnPairForEachEntry() {
		Collection<Pair<String, Integer>> result = Zipper.zip(Lists.newArrayList("A","B","C"), Lists.newArrayList(1,2,3));
		assertEquals("[Pair[A, 1], Pair[B, 2], Pair[C, 3]]",result.toString());
	}

}
