# Organisation Flapdoodle OSS

We are now a github organisation. You are invited to participate.

## de.flapdoodle.guava

minor guava extensions

### Maven

Stable (Maven Central Repository, Released: 19.12.2013 - wait 24hrs for [maven central](http://repo1.maven.org/maven2/de/flapdoodle/guava/de.flapdoodle.guava/maven-metadata.xml))

	<dependency>
		<groupId>de.flapdoodle.embed</groupId>
		<artifactId>de.flapdoodle.guava</artifactId>
		<version>1.1</version>
	</dependency>

Snapshots (Repository http://oss.sonatype.org/content/repositories/snapshots)

	<dependency>
		<groupId>de.flapdoodle.embed</groupId>
		<artifactId>de.flapdoodle.guava</artifactId>
		<version>1.2-SNAPSHOT</version>
	</dependency>

### Usage

We did some minor extensions based on guava stuff which can be usefull in some situations. These extensions are not
optimized for speed or memory usage.

#### Convert lists into maps

##### Convert a list into a 1:1 map 

	List<Order> orders;
	...
	Function<Order, Integer> keytransformation = new Function<Order, Integer>() {

		@Override
		public Integer apply(Order order) {
			return order.getId();
		}
	};

	Map<Integer, Order> orderMap = Transformations.map(orders, keytransformation);

	Order order = orderMap.get(2);
	...

##### Convert a list into a map of lists

	...
	List<String> names = Lists.newArrayList("Achim", "Albert", "Susi", "Sonja", "Bert");

	Function<String, Character> keytransformation = new Function<String, Character>() {

		@Override
		public Character apply(String name) {
			return name.charAt(0);
		}
	};

	Map<Character, ImmutableList<String>> nameMap = Transformations.map(names, keytransformation,
			Folds.asListFold(Transformations.<String> asCollection()));

	ImmutableList<String> namesWithA = nameMap.get('A');

	...

#### Foldleft

	...
	int result = Folds.foldLeft(Lists.newArrayList(1,2,3,4,5,6), new Foldleft<Integer, Integer>() {
		@Override
		public Integer apply(Integer left, Integer right) {
			return left+right;
		}
	}, 0);

	...
