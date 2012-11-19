/*
 * Copyright (c) 2003 - 2012 Sileria, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.sileria.util;

/**
 * A reference class to any object, which are use by caches.
 *
 * @author Ahmed Shakil
 * @date 08-24-2012
 */
public class CacheReference<T> implements Comparable<CacheReference> {

	private T referent;

	private long timestamp;

	Object key;
	int size;

	/**
	 * Creates a new cache reference that refers to the given object.  The new
	 * reference is not registered with any queue.
	 *
	 * @param referent object the new soft reference will refer to
	 */
	public CacheReference (T referent) {
		this.referent = referent;
		timestamp = System.currentTimeMillis();
	}

	/**
	 * Creates a new cache reference that refers to the given object.  The new
	 * reference is not registered with any queue.
	 *
	 * @param referent object the new soft reference will refer to
	 */
	public CacheReference (T referent, Object key, int size) {
		this( referent );
		this.key = key;
		this.size = size;
	}

	/**
	 * Returns this reference object's referent.  If this reference object has
	 * been cleared, either by the program or by the garbage collector, then
	 * this method returns <code>null</code>.
	 *
	 * @return The object to which this reference refers, or
	 * <code>null</code> if this reference object has been cleared
	 */
	public T get () {
		timestamp = System.currentTimeMillis();
		return referent;
	}

	/**
	 * Return the last time in milliseconds this object was accessed.
	 * @return system time in milliseconds.
	 */
	public long time () {
		return timestamp;
	}

	/**
	 * Return the key set for this cache
	 * @return cache key
	 */
	public Object key () {
		return key;
	}

	/**
	 * Return the size of the referent.
	 * @return size of referent.
	 */
	public int size () {
		return size;
	}

	/**
	 * Handy for sorting if needed. sorted in ascending order.
	 * eldest accessed items goes on top.
	 */
	public int compareTo (CacheReference that) {
		return (int)(this.timestamp - that.timestamp);
	}

	/**
	 * Clears this reference object.
	 */
	public void clear() {
		referent = null;
		key = null;
		size = 0;
	}



}
