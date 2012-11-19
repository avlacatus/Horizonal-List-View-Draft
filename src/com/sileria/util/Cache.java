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
 * Interface specification for a simple cache manager.
 * Three basic implementation are available at this point:
 * <ul>
 *     <li>A {@link MemCache} which is an LRU based cache manager that uses memory space</li>
 *     <li>A {@link DiskCache} which is an  LRU based cache manager that uses disk space</li>
 *     <li>A {@link HybridCache} that is a composite of above two cache managers</li>
 * </ul>
 *
 * @author Ahmed Shakil
 * @date 08-21-2012
 *
 * @param <K> Type of key being used
 * @param <V> Type of object being cached
 */
public interface Cache<K, V> {

	/**
	 * Cache an object.
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 * @param value item to cache.
	 */
	void put (K key, V value);

	/**
	 * Get the object instance from the cache if previously cached; otherwise returns <code>null</code>.
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 * @return a previously cached <code>Object</code> or <code>null</code>
	 */
	V get (K key);

	/**
	 * Checks to see if the cached contains a valid object for the specified key.
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 * @return <code>true</code> if cached; otherwise <code>false</code>
	 */
	boolean contains (K key);

	/**
	 * Remove specified key and it's object reference from the cache.
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 */
	void remove (K key);

	/**
	 * Get cache size. This could be number of cached items for some caches
	 * and size in bytes for others. The actual implementation should document
	 * what kind of size the method returns.
	 */
	int size ();

	/**
	 * Maximum cache size available including the used and free space.
	 */
	int total ();

	/**
	 * Clear full cache.
	 */
	void clear ();
}
