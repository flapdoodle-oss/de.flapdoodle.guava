package de.flapdoodle.guava;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;


public abstract class Flatter {

  private Flatter() {
      // no instance
  }

  public static <P,T> Function<? super P, Iterable<? extends Flat<P,T>>> flat(final Function<? super P, ? extends Iterable<? extends T>> sub) {
      return new Function<P, Iterable<? extends Flat<P,T>>>() {
  
          @Override
          public Iterable<? extends Flat<P,T>> apply(final P parent) {
              return Iterables.transform(sub.apply(parent),new Function<T, Flat<P,T>>() {
                  public Flat<P,T> apply(T value) {
                      return new Flat<P, T>(parent, value);
                  };
              });
          }
          
      };
  }

  public static <A,P,T> Function<? super Flat<A,P>, Iterable<? extends Flat<Flat<A,P>, T>>> flatted(final Function<? super P, ? extends Iterable<? extends T>> sub) {
      return new Function<Flat<A,P>, Iterable<? extends Flat<Flat<A,P>,T>>>() {
  
          @Override
          public Iterable<? extends Flat<Flat<A,P>, T>> apply(final Flat<A,P> parent) {
              return Iterables.transform(sub.apply(parent.value()),new Function<T, Flat<Flat<A,P>,T>>() {
                  public Flat<Flat<A,P>,T> apply(T value) {
                      return new Flat<Flat<A,P>, T>(parent, value);
                  };
              });
          }
          
      };
  }
}
