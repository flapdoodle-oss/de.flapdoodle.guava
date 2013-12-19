/**
 * Copyright (C) 2011
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

import java.util.EnumMap;
import java.util.Map;

import com.google.common.collect.Maps;

public abstract class MapCreators {

	private MapCreators() {
		// no instance
	}

	public static <K extends Enum<K>, V> MapCreator<K, V, EnumMap<K, V>> enumMap(Class<K> type) {
		return new EnumMapCreator<K, V>(type);
	}

	public static <K, V> MapCreator<K, V, Map<K, V>> linkedHashMap() {
		return new LinkedHashMapCreator<K, V>();
	}

	private static final class EnumMapCreator<K extends Enum<K>, V> implements MapCreator<K, V, EnumMap<K, V>> {

		private final Class<K> type;

		EnumMapCreator(Class<K> type) {
			this.type = type;
		}

		@Override
		public final EnumMap<K, V> newInstance() {
			return Maps.newEnumMap(this.type);
		}
	}

	private static final class LinkedHashMapCreator<K, V> implements MapCreator<K, V, Map<K, V>> {

		@Override
		public final Map<K, V> newInstance() {
			return Maps.newLinkedHashMap();
		}
	}

}
