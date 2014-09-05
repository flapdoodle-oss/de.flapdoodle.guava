package de.flapdoodle.guava;

public class Flat<P, T> {

	private final P parent;
	private final T value;

	public Flat(P parent, T value) {
		this.parent = parent;
		this.value = value;
	}

	public P parent() {
		return parent;
	}

	public T value() {
		return value;
	}
}
