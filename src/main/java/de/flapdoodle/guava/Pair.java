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

/**
 *
 * @author mmosmann
 */
public class Pair<A,B> {
	private final A a;
	private final B b;

	public Pair(A a,B b) {
		this.a = a;
		this.b = b;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	@Override
	public String toString() {
		return "Pair["+a+", "+b+"]";
	}
	
	public static <A,B> Pair<A,B> of(A a, B b) {
		return new Pair<A, B>(a, b);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null)
				? 0
				: a.hashCode());
		result = prime * result + ((b == null)
				? 0
				: b.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}
	
	
}
