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
package de.flapdoodle.guava.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.flapdoodle.guava.Folds;
import de.flapdoodle.guava.Transformations;

public class TestExampleReadMeCode {

	// ### Usage
	// ->
	// We did some minor extensions based on guava stuff which can be usefull in some situations. These extensions are not
	// optimized for speed or memory usage.
	// <-
	
	// #### Convert a list into a map
	@Test
	public void listToMap() {
		// ->
		List<Order> orders;
		// <-
		orders = Lists.newArrayList(new Order(1), new Order(2), new Order(4));
		// ->
		// ...
		Map<Integer, Order> orderMap = Transformations.map(orders, new Function<Order, Integer>() {

			@Override
			public Integer apply(Order order) {
				return order.getId();
			}
		});

		Order order = orderMap.get(2);
		// ...
		// <-
		assertNotNull(order);
	}

	// #### Convert a list into a map of lists
	@Test
	public void nameMap() {
		// ->
		// ...
		List<String> names = Lists.newArrayList("Achim", "Albert","Susi","Sonja","Bert");
		
		Function<String, Character> keytransformation = new Function<String, Character>() {
			@Override
			public Character apply(String name) {
				return name.charAt(0);
			}
		};
		
		Map<Character, ImmutableList<String>> nameMap = Transformations.map(names, keytransformation, Folds.asListFold(Transformations.<String>asCollection()));
		
		ImmutableList<String> namesWithA = nameMap.get('A');
		// ...
		// <-
		assertEquals("[Achim, Albert]",namesWithA.toString());
	}
	
	static class Order {

		int _id;

		public Order(int id) {
			_id = id;
		}

		public int getId() {
			return _id;
		}
	}
}
