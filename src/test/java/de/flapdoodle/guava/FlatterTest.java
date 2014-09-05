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
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

public class FlatterTest {

	@Test
	public void flat() {
		ImmutableList<A> a = ImmutableList.<A> builder()
				.add(new A("a1", ImmutableList.of(new B("b1", ImmutableList.of(new C("c1"), new C("c2"), new C("c3"))))))
				.add(new A("a2", ImmutableList.of(new B("b3", ImmutableList.of(new C("c4"))),new B("b4", ImmutableList.of(new C("c5"), new C("c6"))))))
				.add(new A("a3", ImmutableList.of(new B("b5", ImmutableList.<C> of())))).build();

		FluentIterable<Flat<Flat<A, B>, C>> result = FluentIterable.from(a).transformAndConcat(Flatter.flat(A.asSub)).transformAndConcat(
			Flatter.<A, B, C> flatted(B.asSub));

		ImmutableList<Flat<Flat<A, B>, C>> asList = ImmutableList.copyOf(result);
		assertEquals(6, asList.size());

		Flat<Flat<A, B>, C> first = asList.get(0);
		Flat<Flat<A, B>, C> last = asList.get(5);
		assertEquals("a1:b1:c1", textOf(first));
		assertEquals("a2:b4:c6", textOf(last));
	}

	private String textOf(Flat<Flat<A, B>, C> e) {
		return e.parent().parent().text() + ":" + e.parent().value().text() + ":" + e.value().text();
	}

	static class A {

		public static Function<? super A, Iterable<? extends B>> asSub = new Function<A, Iterable<? extends B>>() {

			@Override
			public Iterable<? extends B> apply(A input) {
				return input.sub();
			}
		};

		private final Iterable<? extends B> sub;

		private final String text;

		public A(String text, Iterable<? extends B> b) {
			this.text = text;
			this.sub = ImmutableList.copyOf(b);
		}

		public Iterable<? extends B> sub() {
			return sub;
		}

		public String text() {
			return text;
		}
	}

	static class B {

		public static Function<? super B, Iterable<? extends C>> asSub = new Function<B, Iterable<? extends C>>() {

			@Override
			public Iterable<? extends C> apply(B input) {
				return input.sub();
			}
		};

		private final Iterable<? extends C> sub;

		private final String text;

		public B(String text, Iterable<? extends C> c) {
			this.text = text;
			this.sub = ImmutableList.copyOf(c);
		}

		public Iterable<? extends C> sub() {
			return sub;
		}

		public String text() {
			return text;
		}
	}

	static class C {

		private final String text;

		public C(String text) {
			this.text = text;
		}

		public String text() {
			return text;
		}
	}
}
