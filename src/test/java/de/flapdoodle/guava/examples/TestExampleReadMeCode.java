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
import com.google.common.collect.Maps;

import de.flapdoodle.guava.Foldleft;
import de.flapdoodle.guava.Folds;
import de.flapdoodle.guava.Transformations;

public class TestExampleReadMeCode {

	// ### Usage
	// ->
	// We did some minor extensions based on guava stuff which can be usefull in some situations. These extensions are not
	// optimized for speed or memory usage.
	// <-

	// #### Convert lists into maps

	// ##### Convert a list into a 1:1 map 
	@Test
	public void listToMap() {
		// ->
		List<Order> orders;
		// <-
		orders = Lists.newArrayList(new Order(1), new Order(2), new Order(4));
		// ->
		// ...
		Function<Order, Integer> keytransformation = new Function<Order, Integer>() {

			@Override
			public Integer apply(Order order) {
				return order.getId();
			}
		};

		Map<Integer, Order> orderMap = Transformations.map(orders, keytransformation);

		Order order = orderMap.get(2);
		// ...
		// <-
		assertNotNull(order);
	}
	
	// ##### Convert a list into a 1:1 map with transformations for key and value 
	@Test
	public void listToKeyValue() {
		// ->
		List<User> users;
		// <-
		users = Lists.newArrayList(new User(1,"klaus"), new User(2,"susi"), new User(4,"klaus"));
		// ->
		// ...
		Function<User, Integer> keytransformation = new Function<User, Integer>() {

			@Override
			public Integer apply(User user) {
				return user.id();
			}
		};
		
		Function<User, String> valuetransformation = new Function<User, String>() {

			@Override
			public String apply(User user) {
				return user.name();
			}
		};

		Map<Integer, String> userMap = Transformations.map(users, keytransformation, valuetransformation);

		String userName = userMap.get(2);
		// ...
		// <-
		assertEquals("susi",userName);
	}
	
	static class User {
		final int _id;
		final String _name;

		public User(int id, String name) {
			this._id = id;
			this._name = name;
		}

		public int id() {
			return _id;
		}

		public String name() {
			return _name;
		}
	}

	// ##### Convert a list into a map of lists
	@Test
	public void nameMap() {
		// ->
		// ...
		List<String> names = Lists.newArrayList("Achim", "Albert", "Susi", "Sonja", "Bert");

		Function<String, Character> keytransformation = new Function<String, Character>() {

			@Override
			public Character apply(String name) {
				return name.charAt(0);
			}
		};

		Map<Character, ImmutableList<? extends String>> nameMap = Transformations.map(names, keytransformation,
				Folds.asListFold(Transformations.<String> asCollection()));

		ImmutableList<? extends String> namesWithA = nameMap.get('A');

		// ...
		// <-
		assertEquals("[Achim, Albert]", namesWithA.toString());
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

	// #### Foldleft

	@Test
	public void foldLeft() {
		// ->
		// ...
		int result = Folds.foldLeft(Lists.newArrayList(1,2,3,4,5,6), new Foldleft<Integer, Integer>() {
			@Override
			public Integer apply(Integer left, Integer right) {
				return left+right;
			}
		}, 0);
		
		// ...
		// <-
		assertEquals(21, result);
	}
}
