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

public abstract class Either<L, R> {
    protected Either(){
    }

    public static <K, U> Left<K, U> left(K value){
        return new Left<K, U>(value);
    }

    public static <K, U> Right<K, U> right(U value){
        return new Right<K, U>(value);
    }

    public abstract Either<R, L> swap();

    public abstract boolean isLeft();
}

class Left<L, R> extends Either<L, R>{
    private L value;

    protected Left(L value) {
        this.value = value;
    }

    @Override
    public Either<R, L> swap() {
        return new Right<R, L>(value);
    }

    @Override
    public boolean isLeft() {
        return true;
    }
}

class Right<L, R> extends Either<L, R>{
    private R value;

    protected Right(R value){
        this.value = value;
    }


    @Override
    public Either<R, L> swap() {
        return new Left<R, L>(value);
    }

    @Override
    public boolean isLeft() {
        return false;
    }
}