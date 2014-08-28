# Organisation Flapdoodle OSS

We are now a github organisation. You are invited to participate.

## de.flapdoodle.guava

minor guava extensions

### Maven

Stable (Maven Central Repository, Released: 28.08.2013 - wait 24hrs for [maven central](http://repo1.maven.org/maven2/de/flapdoodle/guava/de.flapdoodle.guava/maven-metadata.xml))

	<dependency>
		<groupId>de.flapdoodle.guava</groupId>
		<artifactId>de.flapdoodle.guava</artifactId>
		<version>1.3.7</version>
	</dependency>

Snapshots (Repository http://oss.sonatype.org/content/repositories/snapshots)

	<dependency>
		<groupId>de.flapdoodle.guava</groupId>
		<artifactId>de.flapdoodle.guava</artifactId>
		<version>1.3.8-SNAPSHOT</version>
	</dependency>

### Usage

We did some minor extensions based on guava stuff which can be usefull in some situations. These extensions are not
optimized for speed or memory usage.

#### Collections stuff

##### First element of collections

	List<String> data;
	...
	Optional<String> result = Expectations.noneOrOne(data);
	...

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

##### Convert a list into a 1:1 map with transformations for key and value 

	List<User> users;
	...
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

	Map<Character, ImmutableList<? extends String>> nameMap = Transformations.map(names, keytransformation,
			Folds.asListFold(Transformations.<String> asCollection()));

	ImmutableList<? extends String> namesWithA = nameMap.get('A');

	...

#### Foldleft

foldLeft can be used for many aggregation operations like sum, count, concat.. 
	...
	List<Integer> numbers = Lists.newArrayList(1,2,3,4,5,6);
	int result = Folds.foldLeft(numbers, new Foldleft<Integer, Integer>() {
		@Override
		public Integer apply(Integer left, Integer right) {
			return left+right;
		}
	}, 0);

	...
