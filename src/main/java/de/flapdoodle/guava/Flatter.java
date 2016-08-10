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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;


public abstract class Flatter {

  private Flatter() {
      // no instance
  }

  public static <P,T> Function<? super P, Iterable<? extends Flat<P,T>>> flat(final Function<? super P, ? extends Iterable<? extends T>> sub) {
      return parent -> Iterables.transform(sub.apply(parent),value -> new Flat<P, T>(parent, value));
  }

  public static <A,P,T> Function<? super Flat<A,P>, Iterable<? extends Flat<Flat<A,P>, T>>> flatted(final Function<? super P, ? extends Iterable<? extends T>> sub) {
      return parent -> Iterables.transform(sub.apply(parent.value()),value -> new Flat<Flat<A,P>, T>(parent, value));
  }
}
