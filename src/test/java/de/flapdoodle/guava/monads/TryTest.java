package de.flapdoodle.guava.monads;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Supplier;

public class TryTest {

	@Test
	public void tryWithoutErrorShouldGiveResult() {
		assertEquals("2", Try.with(s -> "" + s).apply(2));
		assertEquals("2", Try.with(s -> "" + s)
			.onErrorThrow((t,ex) -> neverCallThis())
			.apply(2));
		assertEquals("2", Try.with(s -> "" + s)
			.withErrorThrow(ex -> true, (t,ex) -> neverCallThis())
			.apply(2));
	}

	@Test
	public void tryWithErrorShouldThrowRuntimeException() {
		assertExceptionMessage("foo", exceptionOf(() -> Try.with(s -> throwFoo()).apply(2)));
	}
	
	@Test
	public void tryWithErrorAndOnErrorShouldThrowRuntimeException() {
		assertExceptionMessage("bar: foo", exceptionOf(() -> Try.with(s -> throwFoo())
				.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
				.apply(2)));
	}

	@Test
	public void tryWithErrorAndFilterShouldThrowMatchingRuntimeException() {
		assertExceptionMessage("foo found", exceptionOf(() -> Try.with(s -> throwFoo())
				.withErrorThrow(ex -> ex.getLocalizedMessage().contains("foo"), (t,ex) -> new RuntimeException("foo found"))
				.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
				.apply(2)));
	}

	@Test
	public void tryWithErrorAndFilterShouldThrowMatchingRuntimeExceptionIfRecursiveException() {
		assertExceptionMessage("foo found", exceptionOf(() -> Try.with(s -> {
					try {
						return throwFoo();
					} catch (RuntimeException rx) {
						throw new RuntimeException("Outer Exception", rx);
					}
				})
				.withErrorThrow(ex -> ex.getLocalizedMessage().contains("foo"), (t,ex) -> new RuntimeException("foo found"))
				.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
				.apply(2)));
	}
	
	@Test
	public void tryAndRecoverShouldGiveSecondResult() {
		assertEquals("2: 2", Try.with(s -> {
			return throwFoo();
		})
		.withErrorThrow(ex -> ex.getLocalizedMessage().contains("foo"), (t,ex) -> new RuntimeException("foo found"))
		.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
		.recoverWith(s -> "2: "+s)
		.apply(2));
	}
	
	@Test
	public void tryShouldGiveFirstResultIfNoException() {
		assertEquals("1: 2", Try.with(s -> "1: "+s)
		.withErrorThrow(ex -> ex.getLocalizedMessage().contains("foo"), (t,ex) -> new RuntimeException("foo found"))
		.onErrorThrow((t,ex) -> new RuntimeException("bar: "+ex.getLocalizedMessage()))
		.recoverWith(s -> "2: "+s)
		.apply(2));
	}
	
	
	
	private <T> Throwable exceptionOf(Supplier<T> supplier) {
		try {
			T ret = supplier.get();
			throw new AssertionError("no exception thrown, but result: " + ret);
		} catch (Throwable rx) {
			return rx;
		}
	}

	private static <T> T throwFoo() {
		if (true) throw new RuntimeException("foo");
		return null;
	}
	
	private static <T> T neverCallThis() {
		if (true) throw new AssertionError("never call this");
		return null;
	}
	

	private void assertExceptionMessage(String message, Throwable throwable) {
		assertEquals(message, throwable.getLocalizedMessage());
	}
	
}
