package de.flapdoodle.guava.monads;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Supplier;

public class TryTest {

	@Test
	public void tryWithoutErrorShouldGiveResult() {
		assertEquals("2", Try.with(s -> "" + s).apply(2));
		assertEquals("2", Try.with(s -> "" + s)
			.onErrorThrow((t,ex) -> null)
			.apply(2));
		assertEquals("2", Try.with(s -> "" + s)
			.withErrorThrow(ex -> true, (t,ex) -> null)
			.apply(2));
	}

	@Test
	public void tryWithErrorShouldThrowRuntimeException() {
		assertEquals("foo", exceptionOf(() -> Try.with(s -> {
					if (true) throw new RuntimeException("foo");
					return "" + s;
				})
				.apply(2))
			.getLocalizedMessage());
	}
	
	@Test
	public void tryWithErrorAndOnErrorShouldThrowRuntimeException() {
		assertEquals("bar: foo", exceptionOf(() -> Try.with(s -> {
					if (true) throw new RuntimeException("foo");
					return "" + s;
				})
				.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
				.apply(2))
			.getLocalizedMessage());
	}

	@Test
	public void tryWithErrorAndFilterShouldThrowMatchingRuntimeException() {
		assertEquals("foo found", exceptionOf(() -> Try.with(s -> {
					if (true) throw new RuntimeException("foo");
					return "" + s;
				})
				.withErrorThrow(ex -> ex.getLocalizedMessage().contains("foo"), (t,ex) -> new RuntimeException("foo found"))
				.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
				.apply(2))
			.getLocalizedMessage());
	}
	
	@Test
	public void tryWithErrorAndFilterShouldThrowMatchingRuntimeExceptionIfRecursiveException() {
		assertEquals("foo found", exceptionOf(() -> Try.with(s -> {
					try {
						if (true) throw new RuntimeException("foo");
					} catch (RuntimeException rx) {
						throw new RuntimeException("Outer Exception", rx);
					}
					return "" + s;
				})
				.withErrorThrow(ex -> ex.getLocalizedMessage().contains("foo"), (t,ex) -> new RuntimeException("foo found"))
				.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
				.apply(2))
			.getLocalizedMessage());
	}
	
	private <T> Throwable exceptionOf(Supplier<T> supplier) {
		try {
			T ret = supplier.get();
			throw new AssertionError("no exception thrown, but result: " + ret);
		} catch (Throwable rx) {
			return rx;
		}
	}
}
