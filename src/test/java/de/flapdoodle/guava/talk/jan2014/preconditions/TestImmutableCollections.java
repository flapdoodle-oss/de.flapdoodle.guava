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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author mmosmann
 */
public class TestImmutableCollections {
	
	@Test
	public void testWhyImmutable() {
		List<String> source=Lists.newArrayList("foo","bar","boo");

		List<String> result=badBeginsWith(source, 'b');
		assertEquals("[bar, boo]",result.toString());
		assertEquals("NOT expected","[bar, boo]",source.toString());
		
		source=ImmutableList.of("foo","bar","boo");
		try {
			badBeginsWith(source, 'b');
		} catch (UnsupportedOperationException usx) {
			
		}
	}
	
	static List<String> badBeginsWith(List<String> list, char c) {
		final Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String val=iterator.next();
			if (val.charAt(0)!=c) {
				iterator.remove();
			}
		}
		return list;
	}
	
	@Test
	public void testImmutableVsUnmodifyable() {
		List<String> source=Lists.newArrayList("a","b","c");
		
		StringList readOnlyWrapper=new StringList(Collections.unmodifiableList(source));
		StringList readOnlyCopy=new StringList(ImmutableList.copyOf(source));

		assertEquals("a",readOnlyWrapper.getFirst());
		assertEquals("a",readOnlyCopy.getFirst());
		
		source.remove(0);
		assertEquals("not expected", "b",readOnlyWrapper.getFirst());
		assertEquals("a",readOnlyCopy.getFirst());
	}
	
	static class StringList {
		private final List<String> data;

		public StringList(List<String> data) {
			this.data = data;
		}
		
		public String getFirst() {
			return data.get(0);
		}
		
	}
	
	@Test
	public void testReturnListVsImmutableList() {
		List<String> list;
		list=classicWay();
		list=alternativeWay();
		
		ImmutableList<String> immutable;
		// immutable=classicWay(); --> compile error
		immutable=alternativeWay();
		
		// list.remove(0); --> looks ok
		// immutable.remove(0); --> marked as deprecated, name suggest: dont use it
	}
	
	static List<String> classicWay() {
		return Lists.newArrayList("foo","bar");
	}
	
	static ImmutableList<String> alternativeWay() {
		return ImmutableList.of("foo","bar");
	}

	@Test
	public void testImmutableAsConcept() {
		Path path = new Path(Lists.newArrayList("foo","bar"));
		Path copy = new Path(path.parts());
		// share same list instance
	}
	
	static class Path {
		
		final ImmutableList<String> parts;

		public Path(ImmutableList<String> parts) {
			this.parts = parts; // use without copy
		}

		public Path(Collection<String> parts) {
			this(ImmutableList.copyOf(parts)); // make a copy
		}
		
		public ImmutableList<String> parts() {
			return parts;
		}
	}
	
	@Test
	public void testBuilder() {
		ImmutableList<String> list = ImmutableList.<String>builder()
						.add("foo")
						.add("bla","bar","boo")
						.addAll(Lists.newArrayList("ying","yang"))
						.build();
		
		assertEquals("[foo, bla, bar, boo, ying, yang]", list.toString());
	}
}
