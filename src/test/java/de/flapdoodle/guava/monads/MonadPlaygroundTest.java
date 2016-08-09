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
package de.flapdoodle.guava.monads;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

public class MonadPlaygroundTest {

	@Test
	public void tryMonad() {
		
		Try.failable(new Function<String, Integer>() {
			@Override
			public Integer apply(String input) {
				return Integer.valueOf(input);
			}
		}).onFailure(new Consumer<RuntimeException>() {
			@Override
			public void accept(RuntimeException rx) {
				rx.printStackTrace();
			}
		});
//		Try.start(new Supplier<String>() {
//			@Override
//			public String get() {
//				return "foo";
//			}
//		});
	}
	
	static abstract class Try<T, R> {
		
		public static <T, R> Throwing<T, R> failable(Function<T, R> function) {
			return new Throwing<T, R>(function);
		}
	}
	
	
	static class Throwing<T, R> extends Try<T, R> {

		private Function<T, R> function;

		public Throwing(Function<T, R> function) {
			this.function = function;
		}
		
		public Catching<T, R> onFailure(Consumer<RuntimeException> onFailure) {
			return new Catching<T, R>(function, onFailure);
		}
		
	}
	
	static class Catching<T, R> extends Try<T, R> {

		private Function<T, R> function;
		private Consumer<RuntimeException> onFailure;

		public Catching(Function<T, R> function,
				Consumer<RuntimeException> onFailure) {
					this.function = function;
					this.onFailure = onFailure;
		}
		
//		public Either<L, R>
		
	}
		
	static interface Consumer<T> {
		void accept(T value);
	}
//	static class Success<T, R> extends Try<T, R> {
//		
//		public <C> Try<T, C> map(Function<T, C> map) {
//			return new Lazy<T, C>(map);
//		}
//	}
//	
//	static class Lazy<T, R> extends Try<T, R> {
//
//		private Function<T, R> map;
//
//		public Lazy(Function<T, R> map) {
//			this.map = map;
//		}
//		
//	}
	
//	static abstract class Try<A,B> {
//		
//		static <A> Try<A, RuntimeException> start(Supplier<A> start) {
//			return new Lazy<A>(start);
//		}
//		
//	    public abstract <U> Try<U,B> flatMap(Function<? super A, Try<U,B>> onSucceed);
//	}
//		
//	static class Lazy<A> extends Try<A, RuntimeException> {
//		
//		private Supplier<A> supplier;
//
//		public Lazy(Supplier<A> supplier) {
//			this.supplier = supplier;
//		}
//		
//		@Override
//		public <U> Try<U, RuntimeException> flatMap(
//				Function<? super A, Try<U, RuntimeException>> onSucceed) {
//			try {
//				return onSucceed.apply(supplier.get());
//			} catch (RuntimeException rx) {
//				return Failure.of(rx);
//			}
//		}
//		
//	}
//	
//	static class Failure<A> extends Try<A, RuntimeException> {
//
//		private RuntimeException rx;
//
//		public Failure(RuntimeException rx) {
//			this.rx = rx;
//		}
//
//		@Override
//		public <U> Try<U, RuntimeException> flatMap(
//				Function<? super A, Try<U, RuntimeException>> onSucceed) {
//			return of(rx);
//		}
//		
//		public static <U> Try<U, RuntimeException> of(RuntimeException rx) {
//			return new Failure<U>(rx);
//		}
//		
//	}
//
//	static class Success<A, B> extends Try<A, B> {
//
//		@Override
//		public <U> Try<U, B> flatMap(Function<? super A, Try<U, B>> onSucceed) {
//				return null;
//		}
//	}
}
