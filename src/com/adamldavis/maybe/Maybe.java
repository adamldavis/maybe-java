/*
   Copyright 2012 Nat Pryce

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.adamldavis.maybe;

import java.util.Collections;
import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

/**
 * The aim of the Maybe type is to avoid using 'null' references.
 * 
 * A Maybe<T> represents a possibly non-existent value of type T. The Maybe type
 * makes it impossible (without deliberate effort to circumvent the API) to use
 * the value when it does not exist.
 */
public abstract class Maybe<T> implements Iterable<T> {

	/**
	 * Whether the value is known or not.
	 * 
	 * @return True if value, false if no value.
	 */
	public abstract boolean isKnown();

	/**
	 * Returns the wrapped value if any, otherwise the given value.
	 * 
	 * @param defaultValue
	 *            Value to return if this instance of Maybe is empty.
	 * @return this Maybe's value if known, otherwise the given value.
	 */
	public abstract T otherwise(T defaultValue);

	public abstract Maybe<T> otherwise(Maybe<T> maybeDefaultValue);

	public abstract <E extends Throwable> T otherwiseThrow(Class<E> ex)
			throws E;

	public abstract <E extends Throwable> T otherwiseThrow(Class<E> ex,
			String message) throws E;

	public abstract <E extends Throwable> T otherwiseThrow(
			Supplier<E> exSupplier) throws E;

	public abstract <U> Maybe<U> to(Function<? super T, ? extends U> mapping);

	public abstract Maybe<Boolean> query(Predicate<? super T> mapping);

	public boolean isEmpty() {
		return !isKnown();
	}

	/**
	 * Synonymous with {@link Maybe#unknown()}.
	 * 
	 * @return an empty instance of Maybe.
	 */
	public static <T> Maybe<T> nothing() {
		return new AbsentValue<T>();
	}

	/**
	 * Represents an unknown value (replaces null).
	 * 
	 * @return an empty instance of Maybe.
	 */
	public static <T> Maybe<T> unknown() {
		return new AbsentValue<T>();
	}

	/**
	 * If given value is null, equivalent to {@link Maybe#unknown()}; otherwise
	 * equivalent to {@link #definitely(Object)}.
	 * 
	 * @param it
	 *            Possibly null reference.
	 * @return An instance of Maybe.
	 */
	public static <T> Maybe<T> maybe(T it) {
		if (it == null)
			return unknown();
		else
			return definitely(it);
	}

	private static class AbsentValue<T> extends Maybe<T> {
		@Override
		public boolean isKnown() {
			return false;
		}

		public Iterator<T> iterator() {
			return Collections.<T> emptyList().iterator();
		}

		@Override
		public T otherwise(T defaultValue) {
			return defaultValue;
		}

		@Override
		public Maybe<T> otherwise(Maybe<T> maybeDefaultValue) {
			return maybeDefaultValue;
		}

		@Override
		public <U> Maybe<U> to(Function<? super T, ? extends U> mapping) {
			return unknown();
		}

		@Override
		public Maybe<Boolean> query(Predicate<? super T> mapping) {
			return unknown();
		}

		@Override
		public String toString() {
			return "unknown";
		}

		@Override
		@SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass" })
		public boolean equals(Object obj) {
			return false;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public <E extends Throwable> T otherwiseThrow(Supplier<E> exSupplier)
				throws E {
			throw exSupplier.get();
		}

		@Override
		public <E extends Throwable> T otherwiseThrow(Class<E> ex) throws E {
			try {
				throw ex.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public <E extends Throwable> T otherwiseThrow(Class<E> ex,
				String message) throws E {
			try {
				throw (E) ex.getConstructor(String.class).newInstance(message);
			} catch (Exception unexpected) {
				throw new RuntimeException(unexpected);
			}
		}

	}

	public static <T> Maybe<T> definitely(final T theValue) {
		return new DefiniteValue<T>(theValue);
	}

	private static class DefiniteValue<T> extends Maybe<T> {
		private final T theValue;

		public DefiniteValue(T theValue) {
			this.theValue = theValue;
		}

		@Override
		public boolean isKnown() {
			return true;
		}

		public Iterator<T> iterator() {
			return Collections.singleton(theValue).iterator();
		}

		@Override
		public T otherwise(T defaultValue) {
			return theValue;
		}

		@Override
		public Maybe<T> otherwise(Maybe<T> maybeDefaultValue) {
			return this;
		}

		@Override
		public <U> Maybe<U> to(Function<? super T, ? extends U> mapping) {
			return definitely((U) mapping.apply(theValue));
		}

		@Override
		public Maybe<Boolean> query(Predicate<? super T> mapping) {
			return definitely(mapping.apply(theValue));
		}

		@Override
		public String toString() {
			return "definitely " + theValue.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			DefiniteValue<?> that = (DefiniteValue<?>) o;

			return theValue.equals(that.theValue);

		}

		@Override
		public int hashCode() {
			return theValue.hashCode();
		}

		@Override
		public <E extends Throwable> T otherwiseThrow(Supplier<E> exSupplier)
				throws E {
			return theValue;
		}

		@Override
		public <E extends Throwable> T otherwiseThrow(Class<E> ex) throws E {
			return theValue;
		}

		@Override
		public <E extends Throwable> T otherwiseThrow(Class<E> ex,
				String message) throws E {
			return theValue;
		}

	}
}