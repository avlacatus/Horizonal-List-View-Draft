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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simplest memory cache to store any kind of object into memory with associated string keys.
 * This cache holds strong references to a limited number of values which can be define by the
 * <code>maxSize</code> you provide to the constructor.
 * <p/>
 * This is the simplest implementation which uses hard references and a <code>ConcurrentHashMap</code> internally.
 * <p/>
 * Usage: Simply create and save an instance of this class and call one of the put/get methods to start caching and fetching objects.
 * <p/>
 * Note: This class is not designed to have many instances. In usual case there should be one instance per app instance.
 * 
 * @author Ahmed Shakil
 * @date 08-21-2012
 *
 * @see DiskCache
 * @see HybridCache
 * 
 * @param <K> Type of key being used
 * @param <V> Type of object being cached
 */
public class MemCache<K, V> implements Cache<K, V> {

	private int size;

	private final Map<K, CacheReference<V>> cache;

	private final int maxSize;

	private ByteCounter<V> byteCounter;

	/**
	 * The default max capacity for this table,
	 * used when not otherwise specified in a constructor.
	 */
	protected static final int DEFAULT_INITIAL_CAPACITY = 40;

	/**
	 * The default maximum capacity for this table,
	 * used when not otherwise specified in a constructor.
	 */
	protected static final int DEFAULT_MAXIMUM_CAPACITY = 99;

	/**
	 * The default maximum cache size in bytes. 1MB.
	 */
	protected static final int DEFAULT_MAXIMUM_BYTES = 1024 * 1024;

	/**
	 * Constructor, default.
	 */
	public MemCache () {
		this( DEFAULT_INITIAL_CAPACITY, DEFAULT_MAXIMUM_CAPACITY );
	}

	/**
	 * Construct soft cache with initial capacity and max size.
	 * @param initialCapacity initial cache size in number of items.
	 * @param maxSize max cache size for soft cache. This could be in bytes or item count.
	 * 	NOTE: If providing a max cache size in bytes make sure to provide a <code>ByteCounter</code>
	 */
	public MemCache (int initialCapacity, int maxSize) {
		cache = new ConcurrentHashMap<K, CacheReference<V>>(initialCapacity);
		this.maxSize = Math.max( 1, maxSize );
	}

	/**
	 * Construct soft cache with max size and a byte counter.
	 * @param maxSize max cache size for soft cache. This could be in bytes or item count.
	 * 	NOTE: If providing a max cache size in bytes make sure to provide a <code>ByteCounter</code>
	 * @param byteCounter object size counter used by the soft cache. If null then each object is calculated as size 1.
	 */
	public MemCache (int maxSize, ByteCounter<V> byteCounter) {
		this( DEFAULT_INITIAL_CAPACITY, maxSize );
		this.byteCounter = byteCounter;
	}

	/**
	 * Cache a <code>BitmapDrawable</code>.
	 * @param key String id or URL
	 * @param value item to cache.
	 */
	public final void put (K key, V value) {
		int objSize = sizeOf( value );
		cache.put( key, newReference( value, key, objSize ) );

		size += objSize;
		if (size > maxSize)
			trimToSize( maxSize );
		
		entryAdded( key, value, objSize );    // notify
	}

	/**
	 * Remove specified key and it's image reference from the cache.
	 * @param key String id or URL
	 */
	public final void remove (K key) {
		CacheReference<V> ref = cache.remove( key );

		if (ref != null) {
			size -= ref.size;
			entryRemoved( key, ref.get(), ref.size );  // notify
			ref.clear();
		}
	}

	/**
	 * Get the cache size. Could be in bytes or item count.
	 */
	public int size () {
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	public int total () {
		return maxSize;
	}

	/**
	 * Number of items in this cache.
	 */
	public int count () {
		return cache.size();
	}

	/**
	 * Clear full cache.
	 */
	public void clear () {
		cache.clear();
		size = 0;
	}

	/**
	 * Get the bitmap instance from the cache if previously cached; otherwise returns <code>null</code>.
	 * @param key String id or URL.
	 * @return a previously cached <code>BitmapDrawable</code> or <code>null</code>
	 */
	public V get (K key) {
		CacheReference<V> ref = cache.get( key );
		return ref == null ? null : ref.get();
	}

	/**
	 * Checks to see if the cached contains a valid image for the specified key.
	 * @param key String id or URL.
	 * @return <code>true</code> if cached; otherwise <code>false</code>
	 */
	public boolean contains (K key) {
		CacheReference<V> ref = cache.get( key );
		return ref != null && ref.get() != null;
	}

	/**
	 * Set <code>ByteCounter</code> to calculate byte of each object.
	 */
	public void setByteCounter (ByteCounter<V> byteCounter) {
		this.byteCounter = byteCounter;
	}

	/**
	 * Trim down cache size to the limited
	 */
	@SuppressWarnings( "unchecked" )
	protected void trimToSize (int size) {
		if (size() < size) return;

		// may need to optimize this approach later.
		Collection<CacheReference<V>> values = cache.values();
		CacheReference[] refs = values.toArray( new CacheReference[values.size()] );
		Arrays.sort( refs );
		for (int i=0, count=refs.length; i<count && size() > size; i++) {
			remove( (K)refs[i].key );
		}
	}

	/**
	 * Return size of the object references by specified key. Default implementation returns 1.
	 */
	protected int sizeOf (V obj) {
		return byteCounter == null ? 1 : byteCounter.sizeOf( obj );
	}

	/**
	 * Hook to override to do anything special.
	 */
	@SuppressWarnings( "unused" )
	protected void entryAdded (K key, V value, int size) {}

	/**
	 * Hook to override to do anything special.
	 * @param key cache key
	 * @param value item being removed. NOTE: this could be null by GC
	 * @param size cleared
	 */
	@SuppressWarnings( "unused" )
	protected void entryRemoved (K key, V value, int size) {}

	/**
	 * Override this method to create your own reference implementation.
	 */
	protected CacheReference<V> newReference (V value, K key, int size) {
		return new CacheReference<V>( value, key, size );
	}
}
