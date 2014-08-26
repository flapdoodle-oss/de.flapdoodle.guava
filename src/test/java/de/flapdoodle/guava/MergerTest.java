package de.flapdoodle.guava;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Equivalence;
import com.google.common.collect.ImmutableList;


public class MergerTest {

	@Test
	public void mergeTwoNumberListWillGiveSumIfMatching() {
		ImmutableList<Integer> left=ImmutableList.of(1,2,4,10);		
		ImmutableList<Integer> right=ImmutableList.of(0,3,4,10);
		
		ImmutableList<Integer> result = Merger.merge(left, right, Equivalence.equals(), new Foldleft<Integer, Integer>() {
			@Override
			public Integer apply(Integer left, Integer right) {
				return left+right;
			}
		});
		
		assertEquals("[1, 2, 8, 20, 0, 3]", result.toString());
	}

}
