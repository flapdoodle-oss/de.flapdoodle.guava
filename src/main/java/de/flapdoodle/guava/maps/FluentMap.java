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
package de.flapdoodle.guava.maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class FluentMap<K,V> implements Map<K, V> {

	private final Map<K, V> map;

	public FluentMap(Map<K, V> source) {
		this.map = source;
	}
	
	public <NV> FluentMap<K, NV> transformValues(Function<? super V, NV> function) {
		return from(Maps.transformValues(map, function));
	}
	
	public FluentMap<K, V> filterKeys(Predicate<? super K> keyPredicate) {
		return from(Maps.filterKeys(map, keyPredicate));
	}
	
	public FluentMap<K, V> filterValues(Predicate<? super V> valuePredicate) {
		return from(Maps.filterValues(map, valuePredicate));
	}
	
	public <NK> FluentMap<NK, Collection<V>> indexValue(Function<? super V, NK> function) {
		return from(FluentIterable.from(map.values()).index(function).asMap());
	}

	public FluentMap<V, K> inverse() {
		return from(FluentIterable.from(map.keySet()).uniqueIndex(new Function<K, V>() {
			@Override
			public V apply(K k) {
				return map.get(k);
			}
		}));
	}
	
	public <K2,V2> FluentMap<K,V2> transformValues(final Map<V, V2> other) {
		return filterValues(new Predicate<V>() {
			@Override
			public boolean apply(V v) {
				return other.containsKey(v);
			}
		}).transformValues(new Function<V, V2>() {
			@Override
			public V2 apply(V v) {
				return other.get(v);
			}
		});
	}
	
	public ImmutableMap<K, V> asImmutable() {
		return ImmutableMap.copyOf(map);
	}

	public static <K,V> FluentMap<K,V> from(Map<K,V> source) {
		return new FluentMap<>(source);
	}

	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	@Deprecated
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

}
