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

import java.io.Serializable;

import com.google.common.base.Preconditions;

public abstract class Either<L, R> implements Serializable {

	public abstract boolean isLeft();

	public abstract L left();

	public abstract R right();

	public static <L, R> Left<L, R> left(L value) {
      return new Left<L, R>(value);
  }

	public static <L, R> Right<L, R> right(R value) {
      return new Right<L, R>(value);
  }

	public static <L, R> Either<L, R> leftOrRight(L left, R right, String errorMessage) {
		Preconditions.checkArgument(Logics.xor(left, right), errorMessage);
		if (left != null) {
			return left(left);
		}
		return right(right);
	}

	public static <L, R> Either<L, R> leftOrRight(L left, R right) {
		return leftOrRight(left, right, "only left or right");
	}

	public static final class Left<L, R> extends Either<L, R> {

		private final L value;

		public Left(L value) {
			this.value = value;
		}

		@Override
		public boolean isLeft() {
			return true;
		}

		@Override
		public L left() {
			return value;
		}

		@Override
		public R right() {
			throw new NullPointerException("is left");
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null)
					? 0
					: value.hashCode());
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
			Left<?,?> other = (Left<?,?>) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
		
		

	}

	public static final class Right<L, R> extends Either<L, R> {

		private final R value;

		public Right(R value) {
			this.value = value;
		}

		@Override
		public boolean isLeft() {
			return false;
		}

		@Override
		public L left() {
			throw new NullPointerException("is right");
		}

		@Override
		public R right() {
			return value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null)
					? 0
					: value.hashCode());
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
			Right<?,?> other = (Right<?,?>) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		
	}
}
