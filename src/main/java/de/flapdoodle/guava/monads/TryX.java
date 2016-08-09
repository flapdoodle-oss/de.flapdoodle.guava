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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

public abstract class TryX<T> {

    protected TryX() {
    }

    public static <U> TryX<U> ofFailable(Supplier<U> f) {
        Preconditions.checkNotNull(f);

        try {
            U y = f.get();
            return TryX.successful(y);
        } catch (RuntimeException t) {
            return TryX.failure(t);
        }
    }

    /**
     * Transform success or pass on failure.
     * Takes an optional type parameter of the new type.
     * You need to be specific about the new type if changing type
     * <p>
     * Try.ofFailable(() -> "1").<Integer>map((x) -> Integer.valueOf(x))
     *
     * @param f   function to apply to successful value.
     * @param <U> new type (optional)
     * @return Success<U> or Failure<U>
     */

    public abstract <U> TryX<U> map(Function<? super T, ? extends U> f);

    /**
     * Transform success or pass on failure, taking a Try<U> as the result.
     * Takes an optional type parameter of the new type.
     * You need to be specific about the new type if changing type.
     * <p>
     * Try.ofFailable(() -> "1").<Integer>flatMap((x) -> Try.ofFailable(() ->Integer.valueOf(x)))
     * returns Integer(1)
     *
     * @param f   function to apply to successful value.
     * @param <U> new type (optional)
     * @return Success<U> or Failure<U>
     */
    public abstract <U> TryX<U> flatMap(Function<? super T, TryX<U>> f);

    /**
     * Specifies a result to use in case of failure.
     * Gives access to the exception which can be pattern matched on.
     * <p>
     * Try.ofFailable(() -> "not a number")
     * .<Integer>flatMap((x) -> Try.ofFailable(() ->Integer.valueOf(x)))
     * .recover((t) -> 1)
     * returns Integer(1)
     *
     * @param f
     * @return
     */

    public abstract T recover(Function<? super RuntimeException, T> f);

    /**
     * Try applying f(t) on the case of failure.
     * @param f
     * @return a new Try in the case of failure, or the current Success.
     */
    public abstract TryX<T> recoverWith(Function<? super RuntimeException, TryX<T>> f);

    /**
     * Return a value in the case of a failure.
     * This is similar to recover but does not expose the exception type.
     *
     * @param value
     * @return
     */
    public abstract T orElse(T value);

    /**
     * Return another try in the case of failure.
     * Like recoverWith but without exposing the exception.
     *
     * @param f
     * @return
     */
    public abstract TryX<T> orElseTry(Supplier<T> f);

    /**
     * Gets the value on Success or throws the cause of the failure.
     *
     * @return
     */
    public abstract T get();

    public abstract boolean isSuccess();

    public abstract <F> TryX<T> onFailure(Function<RuntimeException, F> f);

    /**
     * Factory method for failure.
     *
     * @param e
     * @param <U>
     * @return a new Failure
     */

    public static <U> TryX<U> failure(RuntimeException e) {
        return new Failure<>(e);
    }

    /**
     * Factory method for success.
     *
     * @param x
     * @param <U>
     * @return a new Success
     */
    public static <U> TryX<U> successful(U x) {
        return new Success<U>(x);
    }
}

class Success<T> extends TryX<T> {
    private final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public <U> TryX<U> flatMap(Function<? super T, TryX<U>> f) {
    	Preconditions.checkNotNull(f);
        try {
            return f.apply(value);
        } catch (RuntimeException t) {
            return TryX.failure(t);
        }
    }

    @Override
    public T recover(Function<? super RuntimeException, T> f) {
    	Preconditions.checkNotNull(f);
        return value;
    }

    @Override
    public TryX<T> recoverWith(Function<? super RuntimeException, TryX<T>> f) {
        Preconditions.checkNotNull(f);
        return this;
    }

    @Override
    public T orElse(T value) {
        return this.value;
    }

    @Override
    public TryX<T> orElseTry(Supplier<T> f) {
        Preconditions.checkNotNull(f);
        return this;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public <U> TryX<U> map(Function<? super T, ? extends U> f) {
        Preconditions.checkNotNull(f);
        try {
            return new Success<U>(f.apply(value));
        } catch (RuntimeException t) {
            return TryX.failure(t);
        }
    }

    @Override
    public boolean isSuccess() {
        return true;
    }


    @Override
    public <F> TryX<T> onFailure(Function<RuntimeException, F> f) {
    	return this;
    }

}


class Failure<T> extends TryX<T> {
    private final RuntimeException e;

    Failure(RuntimeException e) {
        this.e = e;
    }

    @Override
    public <U> TryX<U> map(Function<? super T, ? extends U> f) {
        Preconditions.checkNotNull(f);
        return TryX.failure(e);
    }

    @Override
    public <U> TryX<U> flatMap(Function<? super T, TryX<U>> f) {
        Preconditions.checkNotNull(f);
        return TryX.<U>failure(e);
    }

    @Override
    public T recover(Function<? super RuntimeException, T> f) {
        Preconditions.checkNotNull(f);
        return f.apply(e);
    }

    @Override
    public TryX<T> recoverWith(Function<? super RuntimeException, TryX<T>> f) {
        Preconditions.checkNotNull(f);
        try{
            return f.apply(e);
        }catch(RuntimeException t){
            return TryX.failure(t);
        }
    }

    @Override
    public T orElse(T value) {
        return value;
    }

    @Override
    public TryX<T> orElseTry(Supplier<T> f) {
        Preconditions.checkNotNull(f);
        return TryX.ofFailable(f);
    }

    @Override
    public T get() {
        throw e;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public <F> TryX<T> onFailure(Function<RuntimeException, F> f) {
    	f.apply(e);
    	return this;
    }


}
