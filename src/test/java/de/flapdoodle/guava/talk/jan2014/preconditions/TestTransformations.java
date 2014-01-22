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
package de.flapdoodle.guava.talk.jan2014.preconditions;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author mmosmann
 */
public class TestTransformations {

	@Test
	public void testTransformations() {
		List<Integer> numbers = Lists.newArrayList(1, 2, 3, 4);
		List<String> result = numbersAsDaysClassic(numbers);
		assertEquals("[1 Tag, 2 Tage, 3 Tage, 4 Tage]", result.toString());
		result = numbersAsDays(numbers);
		assertEquals("[1 Tag, 2 Tage, 3 Tage, 4 Tage]", result.toString());
		result = numbersAsDaysRefactored(numbers);
		assertEquals("[1 Tag, 2 Tage, 3 Tage, 4 Tage]", result.toString());
	}

	private static List<String> numbersAsDaysClassic(List<Integer> numbers) {
		List<String> ret=new ArrayList<String>();
		for (Integer number : numbers) {
			ret.add(numberAsDay(number));
		}
		return ret;
	}
	
	private static List<String> numbersAsDays(List<Integer> numbers) {
		return Lists.transform(numbers, new Function<Integer, String>() {

			@Override
			public String apply(Integer input) {
				return numberAsDay(input);
			}

		});
	}

	private static List<String> numbersAsDaysRefactored(List<Integer> numbers) {
		return Lists.transform(numbers, new NumberAsDay());
	}
	
	private static String numberAsDay(int input) {
		if (input == 1) {
			return input + " Tag";
		}
		return input + " Tage";
	}

	private static class NumberAsDay implements Function<Integer, String> {

		public NumberAsDay() {
		}

		@Override
		public String apply(Integer input) {
			return numberAsDay(input);
		}
	}

}
