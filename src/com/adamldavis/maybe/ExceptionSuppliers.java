/*
   Copyright 2013 Adam Davis

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

import com.google.common.base.Supplier;

/**
 * Provides factory methods for creating Exceptions. Used in conjunction with
 * {@link Maybe}, you can things like the following:
 * 
 * <pre>
 * maybeUsername.otherwiseThrow(illegalArgument(&quot;missing username&quot;));
 * </pre>
 * 
 * @author Adam L. Davis
 * @see Maybe
 */
public class ExceptionSuppliers {

	// enums as singletons
	static enum IAESupplier implements
			Supplier<IllegalArgumentException> {
		SINGLETON;
		@Override
		public IllegalArgumentException get() {
			return new IllegalArgumentException();
		}

	}

	static enum NPESupplier implements Supplier<NullPointerException> {
		SINGLETON;
		@Override
		public NullPointerException get() {
			return new NullPointerException();
		}

	}

	static enum ISESupplier implements Supplier<IllegalStateException> {
		SINGLETON;
		@Override
		public IllegalStateException get() {
			return new IllegalStateException();
		}

	}

	/**
	 * Supplier of IllegalArgumentException.
	 */
	public static Supplier<IllegalArgumentException> illegalArgument() {
		return IAESupplier.SINGLETON;
	}

	/**
	 * Supplier of IllegalStateException.
	 */
	public static Supplier<IllegalStateException> illegalState() {
		return ISESupplier.SINGLETON;
	}

	/**
	 * Supplier of NullPointerException.
	 */
	public static Supplier<NullPointerException> nullPointer() {
		return NPESupplier.SINGLETON;
	}

	/**
	 * Supplier of IllegalArgumentException.
	 * 
	 * @param message
	 *            String passed to the constructor of the exception.
	 */
	public static Supplier<IllegalArgumentException> illegalArgument(
			final String message) {
		return new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException(message);
			}
		};
	}

	/**
	 * Supplier of IllegalStateException.
	 * 
	 * @param message
	 *            String passed to the constructor of the exception.
	 */
	public static Supplier<IllegalStateException> illegalState(
			final String message) {
		return new Supplier<IllegalStateException>() {
			@Override
			public IllegalStateException get() {
				return new IllegalStateException(message);
			}
		};
	}

	/**
	 * Supplier of NullPointerException.
	 * 
	 * @param message
	 *            String passed to the constructor of the exception.
	 */
	public static Supplier<NullPointerException> nullPointer(
			final String message) {
		return new Supplier<NullPointerException>() {
			@Override
			public NullPointerException get() {
				return new NullPointerException(message);
			}
		};
	}

	/**
	 * Creates a new Exception Supplier of the given class type.
	 * 
	 * @param clazz
	 *            Exception class with a no-args constructor.
	 * @return A new instance of Supplier for the given class.
	 */
	public static <E extends Throwable> Supplier<E> exception(
			final Class<E> clazz) {
		return new Supplier<E>() {
			@Override
			public E get() {
				try {
					return (E) clazz.newInstance();
				} catch (Exception unexpected) {
					throw new RuntimeException(unexpected);
				}
			}
		};
	}

	/**
	 * Creates a new Exception Supplier of the given class type with given
	 * String passed to the constructor of the exception.
	 * 
	 * @param clazz
	 *            Exception class with a one-argument (String) constructor.
	 * @param message
	 *            String passed to the constructor of the exception.
	 * @return A new instance of Supplier for the given class.
	 */
	public static <E extends Throwable> Supplier<E> exception(
			final Class<E> clazz, final String message) {
		return new Supplier<E>() {
			@Override
			public E get() {
				try {
					return (E) clazz.getConstructor(String.class).newInstance(
							message);
				} catch (Exception unexpected) {
					throw new RuntimeException(unexpected);
				}
			}
		};
	}
}
