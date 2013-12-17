# Organisation Flapdoodle OSS

We are now a github organisation. You are invited to participate.

## de.flapdoodle.guava

minor guava extensions

### Usage

We did some minor extensions based on guava stuff which can be usefull in some situations. These extensions are not
optimized for speed or memory usage.

#### Convert a list into a map

	List<Order> orders;
	...
	Map<Integer, Order> orderMap = Transformations.map(orders, new Function<Order, Integer>() {

		@Override
		public Integer apply(Order order) {
			return order.getId();
		}
	});

	Order order = orderMap.get(2);
	...

#### Convert a list into a map of lists

	...
	List<String> names = Lists.newArrayList("Achim", "Albert","Susi","Sonja","Bert");

	Function<String, Character> keytransformation = new Function<String, Character>() {
		@Override
		public Character apply(String name) {
			return name.charAt(0);
		}
	};

	Map<Character, ImmutableList<String>> nameMap = Transformations.map(names, keytransformation, Folds.asListFold(Transformations.<String>asCollection()));

	ImmutableList<String> namesWithA = nameMap.get('A');
	...
