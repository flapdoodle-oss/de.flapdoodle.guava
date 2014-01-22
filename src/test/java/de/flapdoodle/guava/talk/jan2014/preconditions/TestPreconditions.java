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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TestPreconditions {

	@Test
	public void withoutPreconditions() {
		try {
			IndexWrapper index = new IndexWrapper(-1);
			elementOf(index, Lists.newArrayList("foo"));
			fail("should not be reached");
		}catch (RuntimeException iox) {
			iox.printStackTrace();
		}
	}

	@Test
	public void withPreconditions() {
		try {
			IndexChecked index = new IndexChecked(-1);
			elementOf(index, Lists.newArrayList("foo"));
			fail("should not be reached");
		}catch (RuntimeException iox) {
			iox.printStackTrace();
		}
	}


	static <T> T elementOf(Index index, List<T> list) {
		return list.get(index.value());
	}

	interface Index {
		int value();
	}

	static class IndexWrapper implements Index {

		private final int value;

		public IndexWrapper(int value) {
			this.value = value;
		}

		@Override
		public int value() {
			return value;
		}

	}

	static class IndexChecked implements Index {

		private final int value;

		public IndexChecked(int value) {
			Preconditions.checkArgument(value >= 0, "value < 0");
			this.value = value;
		}

		public int value() {
			return value;
		}
	}
}
