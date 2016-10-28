package de.flapdoodle.guava.monads;

import java.util.function.BiFunction;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import de.flapdoodle.guava.Optionals;
import de.flapdoodle.guava.Pair;
import de.flapdoodle.guava.functions.Function;

public class Try {
	public static <T, R> TryWithOnError<T, R> with(Function<T, R> src) {
		return new TryWithOnError<>(src);
	}
	
	public static interface OnError<T> extends BiFunction<T, Throwable, RuntimeException> {
		
	}
	
	public static class TryWithOnError<T, R> implements Function<T, R> {
		
		private final Function<T, R> function;
		private final ImmutableList<Pair<Predicate<Throwable>, OnError<T>>> filters;
		private final Optional<OnError<T>> onError;

		public TryWithOnError(Function<T, R> function, ImmutableList<Pair<Predicate<Throwable>, OnError<T>>> filters, Optional<OnError<T>> onError) {
			this.function = function;
			this.filters = filters;
			this.onError = onError;
		}
		
		public TryWithOnError(Function<T, R> function) {
			this(function, ImmutableList.of(), Optional.absent());
		}
		 
		public TryWithOnError<T, R> onErrorThrow(OnError<T> onError) {
			Preconditions.checkArgument(!this.onError.isPresent(),"onError already set to %s",this.onError);
			return new TryWithOnError<T, R>(this.function, this.filters, Optional.of(onError));
		}
		
		public TryWithOnError<T, R> withErrorThrow(Predicate<Throwable> filter, OnError<T> onError) {
			return new TryWithOnError<T, R>(this.function, ImmutableList.<Pair<Predicate<Throwable>, OnError<T>>>builder()
					.addAll(this.filters)
					.add(Pair.of(filter, onError))
					.build(), this.onError);
		}
		
		public Function<T, R> recoverWith(Function<T, R> supplier) {
			return recoverWith((t,ex) -> supplier.apply(t));
		}

		public Function<T, R> recoverWith(BiFunction<T, RuntimeException, R> supplier) {
			return t -> {
				try {
					return this.apply(t);
				} catch (RuntimeException rx) {
					return supplier.apply(t,rx);
				}
			};
		}

		@Override
		public R apply(T t) {
			try {
				return function.apply(t);
			} catch (RuntimeException rx) {
				for (Pair<Predicate<Throwable>, OnError<T>> filter : filters) {
					Optional<Throwable> match = filterException(rx, filter.a());
					if (match.isPresent()) {
						throw filter.b().apply(t, match.get());
					}
				};
				if (onError.isPresent()) {
					throw onError.get().apply(t, rx);
				}
				throw rx;
			}
		}
	}
	
	public static Optional<Throwable> filterException(Throwable rx,Predicate<Throwable> filter) {
		if (filter.apply(rx)) {
			return Optional.of(rx);
		} 
		return Optionals.flatmap(Optional.fromNullable(rx.getCause()).transform(cause -> filterException(cause, filter)));
	};
}
