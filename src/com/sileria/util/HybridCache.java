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

import java.io.File;

/**
 * A composite of soft and disk cache. You can keep certain items in memory and rest on disk.
 *
 * @author Ahmed Shakil
 * @date 08-27-2012
 *
 * @see DiskCache
 * @see MemCache
 *
 * @param <K> Type of key being used
 * @param <V> Type of object being cached
 */
public class HybridCache<K, V> implements Cache<K, V> {

	private final MemCache<K, V> soft;
	private final DiskCache<K, V> disk;

	/**
	 * Construct a hybrid caching system with cache options and default cache size for both soft and disk caches.
	 * @param cacheDir a unique and dedicated folder for this cache management only
	 * @param serializer object serializer to read/write the type of object to and from the filesystem
	 * @param counter object size counter used by the soft cache. If null then each object is calculated as size 1.
	 */
	public HybridCache (File cacheDir, ObjectSerializer<V> serializer, ByteCounter<V> counter) {
		soft = new MemCache<K, V>( counter == null ? MemCache.DEFAULT_MAXIMUM_CAPACITY : MemCache.DEFAULT_MAXIMUM_BYTES, counter );
		disk = new DiskCache<K, V>( cacheDir, serializer );
	}

	/**
	 * Construct a hybrid caching system with specified cache sizes, cache.
	 * @param maxSoft max cache size for soft cache. This could be in bytes or item count.
	 * 	NOTE: If providing a max cache size in bytes make sure to provide a <code>ByteCounter</code>
	 * @param maxDisk max cache size for disk cache in bytes
	 * @param cacheDir a unique and dedicated folder for this cache management only
	 * @param serializer object serializer to read/write the type of object to and from the filesystem
	 * @param counter object size counter used by the soft cache. If null then each object is calculated as size 1.
	 */
	public HybridCache (int maxSoft, int maxDisk, File cacheDir, ObjectSerializer<V> serializer, ByteCounter<V> counter) {
		soft = new MemCache<K, V>( maxSoft, counter );
		disk = new DiskCache<K, V>( cacheDir, serializer, maxDisk );
	}

	/**
	 * Cache an object.
	 *
	 * @param key   Object being used as a key, usually a string a Uri or ID.
	 * @param value item to cache.
	 */
	public void put (K key, V value) {
		soft.put( key, value );
		disk.put( key, value );
	}

	/**
	 * Get the object instance from the cache if previously cached; otherwise returns <code>null</code>.
	 *
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 * @return a previously cached <code>Object</code> or <code>null</code>
	 */
	public V get (K key) {
		if (soft.contains( key ))
			return soft.get( key );

		return disk.get( key );
	}

	/**
	 * Checks to see if the cached contains a valid object for the specified key.
	 *
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 * @return <code>true</code> if cached; otherwise <code>false</code>
	 */
	public boolean contains (K key) {
		return soft.contains( key ) || disk.contains( key );
	}

	/**
	 * Remove specified key and it's object reference from the cache.
	 *
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 */
	public void remove (K key) {
		soft.remove( key );
		disk.remove( key );
	}

	/**
	 * Get cache size. This could be number of cached items for some caches
	 * and size in bytes for others. The actual implementation should document
	 * what kind of size the method returns.
	 */
	public int size () {
		return soft.size() + disk.size();
	}

	/**
	 * Maximum cache size available including the used and free space.
	 */
	public int total () {
		return soft.total() + disk.total();
	}

	/**
	 * Clear full cache.
	 */
	public void clear () {
		soft.clear();
		disk.clear();
	}
}
