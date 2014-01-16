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

import java.util.Collection;

public class Partition<T> {

	private final Collection<T> matching;
	private final Collection<T> notMatching;

	public Partition(Collection<T> matching, Collection<T> notMatching) {
		this.matching = matching;
		this.notMatching = notMatching;
	}

	public Collection<T> matching() {
		return this.matching;
	}

	public Collection<T> notMatching() {
		return this.notMatching;
	}
}
