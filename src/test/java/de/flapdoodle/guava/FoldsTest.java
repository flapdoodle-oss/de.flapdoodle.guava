/**
 * Copyright (C) 2011
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.flapdoodle.guava.Folds.CollectingFold;

public class FoldsTest {

	@Test
	public void foldLeftWithEmptyCollectionShouldReturnStartValue() {
		String result=Folds.foldLeft(Lists.<String>newArrayList(), new Foldleft<String, String>() {
			@Override
			public String apply(String left, String right) {
				return "bar";
			}
		}, "foo");
		assertEquals("foo",result);
	}

	@Test
	public void foldLeftShouldReturnValueFromFold() {
		String result=Folds.foldLeft(Lists.newArrayList("blub"), new Foldleft<String, String>() {
			@Override
			public String apply(String left, String right) {
				return "bar";
			}
		}, "foo");
		assertEquals("bar",result);
	}

	@Test
	public void foldLeftShouldCallApplyWithStartValueAndRightShouldReturnValueFromFold() {
		String result=Folds.foldLeft(Lists.newArrayList("blub"), new Foldleft<String, String>() {
			@Override
			public String apply(String left, String right) {
				return left+"-"+right;
			}
		}, "foo");
		assertEquals("foo-blub",result);
	}

	@Test
	public void transformationToCollectionAsListFoldShouldFoldToList() {
		Foldleft<String, ImmutableList<String>> asListFold = Folds.asListFold(new Function<String, Collection<? extends String>>() {
			@Override
			public Collection<? extends String> apply(String input) {
				return Lists.newArrayList("before",input,"after");
			}
		});
		
		ImmutableList<String> result = asListFold.apply(ImmutableList.<String>of(), "X");
		
		assertEquals("[before, X, after]", result.toString());
	}

	@Test
	public void transformationToCollectionAsSetFoldShouldFoldToSet() {
		Foldleft<String, ImmutableSet<String>> asSetFold = Folds.asSetFold(new Function<String, Collection<? extends String>>() {
			@Override
			public Collection<? extends String> apply(String input) {
				return Lists.newArrayList("before",input,"after");
			}
		});
		
		ImmutableSet<String> result = asSetFold.apply(ImmutableSet.<String>of(), "X");
		
		assertEquals("[before, X, after]", result.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void transformationToCollectionAsSetFoldShouldFailIfCollision() {
		Foldleft<String, ImmutableSet<String>> asSetFold = Folds.asSetFold(new Function<String, Collection<? extends String>>() {
			@Override
			public Collection<? extends String> apply(String input) {
				return Lists.newArrayList(input,input);
			}
		});
		
		asSetFold.apply(ImmutableSet.<String>of(), "X");
	}

	@Test
	public void transformationToCollectionAsEnumSetFoldShouldFoldToSet() {
		Foldleft<String, EnumSet<DummyEnum>> asSetFold = Folds.asEnumSetFold(DummyEnum.class, new Function<String, Collection<? extends DummyEnum>>() {
			@Override
			public Collection<? extends DummyEnum> apply(String input) {
				return Lists.newArrayList(DummyEnum.valueOf(input));
			}
		});
		
		EnumSet<DummyEnum> result = asSetFold.apply(EnumSet.noneOf(DummyEnum.class), "A");
		
		assertEquals("[A]", result.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void transformationToCollectionAsEnumSetFoldShouldFailIfCollision() {
		Foldleft<String, EnumSet<DummyEnum>> asSetFold = Folds.asEnumSetFold(DummyEnum.class, new Function<String, Collection<? extends DummyEnum>>() {
			@Override
			public Collection<? extends DummyEnum> apply(String input) {
				return Lists.newArrayList(DummyEnum.valueOf(input),DummyEnum.valueOf(input));
			}
		});
		
		asSetFold.apply(EnumSet.noneOf(DummyEnum.class), "A");
	}

	@Test
	public void listFoldUseCases() {
		foldUseCases(new Folds.ImmutableListFold<String>(), listTestSet());
	}

	@Test
	public void setFoldUseCases() {
		foldUseCases(new Folds.ImmutableSetFold<String>(), setTestSet());
	}

	@Test
	public void enumSetFoldUseCases() {
		foldUseCases(new Folds.EnumSetFold<DummyEnum>(DummyEnum.class), enumsetTestSet());
	}

	private <R, C extends Collection<R>> void foldUseCases(CollectingFold<R, C> fold, FoldTestset<R, C> testSet) {
		TestPair<R, C> empty = testSet.empty();
		assertEquals("foldOfEmptyIsEmpty", empty.result(), fold.apply(empty.left(), empty.right()));
		for (TestPair<R, C> filled : testSet.filled()) {
			assertEquals("foldOf " + filled, filled.result(), fold.apply(filled.left(), filled.right()));
		}
		Optional<TestPair<R, C>> colliding = testSet.colliding();
		if (colliding.isPresent()) {
			try {
				fold.apply(colliding.get().left(), colliding.get().right());
				fail("colliding");
			} catch (RuntimeException rx) {

			}
		}
	}

	static FoldTestset<String, ImmutableList<String>> listTestSet() {
		TestPair<String, ImmutableList<String>> empty = TestPair.of(ImmutableList.<String> of(),
				Collections.<String> emptyList(), ImmutableList.<String> of());

		Optional<TestPair<String, ImmutableList<String>>> colliding = Optional.<TestPair<String, ImmutableList<String>>> absent();

		return new FoldTestset<String, ImmutableList<String>>(empty, colliding, TestPair.of(ImmutableList.of("foo"),
				Lists.<String> newArrayList(), ImmutableList.of("foo")), TestPair.of(ImmutableList.<String> of(),
				Lists.newArrayList("bar"), ImmutableList.of("bar")), TestPair.of(ImmutableList.of("foo"),
				Lists.newArrayList("bar"), ImmutableList.of("foo", "bar")));
	}

	static FoldTestset<String, ImmutableSet<String>> setTestSet() {
		TestPair<String, ImmutableSet<String>> empty = TestPair.of(ImmutableSet.<String> of(),
				Collections.<String> emptyList(), ImmutableSet.<String> of());

		Optional<TestPair<String, ImmutableSet<String>>> colliding = Optional.<TestPair<String, ImmutableSet<String>>> of(TestPair.of(
				ImmutableSet.of("foo"), Sets.newHashSet("foo"), ImmutableSet.of("foo")));

		return new FoldTestset<String, ImmutableSet<String>>(empty, colliding, TestPair.of(ImmutableSet.of("foo"),
				Sets.<String> newHashSet(), ImmutableSet.of("foo")), TestPair.of(ImmutableSet.<String> of(),
				Sets.newHashSet("bar"), ImmutableSet.of("bar")), TestPair.of(ImmutableSet.of("foo"), Sets.newHashSet("bar"),
				ImmutableSet.of("foo", "bar")));
	}

	static FoldTestset<DummyEnum, EnumSet<DummyEnum>> enumsetTestSet() {
		TestPair<DummyEnum, EnumSet<DummyEnum>> empty = TestPair.of(EnumSet.noneOf(DummyEnum.class),
				Collections.<DummyEnum> emptyList(), EnumSet.noneOf(DummyEnum.class));

		Optional<TestPair<DummyEnum, EnumSet<DummyEnum>>> colliding = Optional.<TestPair<DummyEnum, EnumSet<DummyEnum>>> of(TestPair.of(
				EnumSet.of(DummyEnum.A, DummyEnum.B), Sets.newHashSet(DummyEnum.C, DummyEnum.B), EnumSet.of(DummyEnum.B)));

		return new FoldTestset<DummyEnum, EnumSet<DummyEnum>>(empty, colliding, TestPair.of(EnumSet.of(DummyEnum.A),
				Sets.<DummyEnum> newHashSet(), EnumSet.of(DummyEnum.A)), TestPair.of(EnumSet.noneOf(DummyEnum.class),
				Sets.newHashSet(DummyEnum.B), EnumSet.of(DummyEnum.B)), TestPair.of(EnumSet.of(DummyEnum.A),
				Sets.newHashSet(DummyEnum.B), EnumSet.of(DummyEnum.A, DummyEnum.B)));
	}

	static class FoldTestset<R, F extends Collection<R>> {

		private final TestPair<R, F> empty;
		private final ImmutableList<TestPair<R, F>> filled;
		private final Optional<TestPair<R, F>> colliding;

		public FoldTestset(TestPair<R, F> empty, Optional<TestPair<R, F>> colliding, TestPair<R, F>... filled) {
			this.empty = empty;
			this.colliding = colliding;
			this.filled = ImmutableList.copyOf(Lists.newArrayList(filled));
		}

		public TestPair<R, F> empty() {
			return empty;
		}

		public ImmutableList<TestPair<R, F>> filled() {
			return filled;
		}

		public Optional<TestPair<R, F>> colliding() {
			return colliding;
		}

	}

	static class TestPair<R, F extends Collection<R>> {

		private final F left;
		private final Collection<? extends R> right;
		private final F result;

		public TestPair(F left, Collection<? extends R> right, F result) {
			this.left = left;
			this.right = right;
			this.result = result;
		}

		public F left() {
			return left;
		}

		public Collection<? extends R> right() {
			return right;
		}

		public F result() {
			return result;
		}

		public static <R, F extends Collection<R>> TestPair<R, F> of(F left, Collection<? extends R> right, F result) {
			return new TestPair<R, F>(left, right, result);
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(getClass()).add("left", left).add("right", right).add("result", result).toString();
		}
	}

	static enum DummyEnum {
		A,
		B,
		C
	}
}
