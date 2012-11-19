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
import java.util.*;

/**
 * A generic disk cache management implemention. Simply provide a cache folder and an
 * ObjectSerializer and you are all set to go. You can also provide the max size limit
 * for the cache folder in bytes (defaults to 4MB).
 * <p/>
 * NOTE: The cache-folder needs to be dedicated to this cache only and cannot be a shared folder.
 * NOTE: Preferred use is to have only one shared instance of this class througout your app.
 *
 * @author Ahmed Shakil
 * @date 08-24-2012
 *
 * @see MemCache
 * @see HybridCache
 *
 * @param <K> Type of key being used
 * @param <V> Type of object being cached
 */
public class DiskCache<K, V> implements Cache<K, V> {

	private final File cacheDir;
	private final ObjectSerializer<V> serializer;
	private final FileSerializer<V> writer;

	private int size;
	private final int maxSize;

	/**
	 * Construct a disk cache specifing a cache folder and object serializer with
	 * default max cache size of 4MB.
	 * @param cacheDir a unique and dedicated folder for this cache management only
	 * @param serializer object serializer to read/write the type of object to and from the filesystem
	 */
	public DiskCache (File cacheDir, ObjectSerializer<V> serializer) {
		this( cacheDir, serializer, 4 * IO.ONE_MB );
	}

	/**
	 * Construct a disk cache specifing a cache folder, object serializer and cache size limit.
	 * @param cacheDir a unique and dedicated folder for this cache management only
	 * @param serializer object serializer to read/write the type of object to and from the filesystem
	 * @param maxSize max cache size in bytes. If less than 10KB defaults to 10KB.
	 */
	public DiskCache (File cacheDir, ObjectSerializer<V> serializer, int maxSize) {
		if (cacheDir == null)
			throw new NullPointerException( "Cache directory cannot be NULL." );

		if (serializer == null)
			throw new NullPointerException( "Object serializer cannot be NULL." );

		this.cacheDir = cacheDir;
		this.serializer = serializer;
		this.maxSize = Math.max( 10*IO.ONE_KB, maxSize );

		this.writer = new FileSerializer<V>();

		if (!cacheDir.exists())
        	cacheDir.mkdirs();

		size = (int)calcTotalSize();
		trimToSize( maxSize );
	}

	/**
	 * Cache an object.
	 *
	 * @param key Object being used as a key, usually a string a Uri or ID.
	 * @param value item to cache.
	 */
	public void put (K key, V value) {
		if (key == null)
			throw new NullPointerException( "Key cannot be NULL." );

		File file = toFile( key );
		int fs = writeFile( file, value );
		size += fs;
		if (size > maxSize)
			trimToSize( maxSize );
	}

	/**
	 * Get the object instance from the cache if previously cached; otherwise returns <code>null</code>.
	 *
	 * @param key String id or URL.
	 * @return a previously cached <code>Object</code> or <code>null</code>
	 */
	public V get (K key) {
		return readFile( toFile( key ), true );
	}

	/**
	 * Checks to see if the cached contains a valid object for the specified key.
	 *
	 * @param key String id or URL.
	 * @return <code>true</code> if cached; otherwise <code>false</code>
	 */
	public boolean contains (K key) {
		return toFile( key ).exists();
	}

	/**
	 * Remove specified key and it's object reference from the cache.
	 *
	 * @param key String id or URL
	 */
	public void remove (K key) {
		remove( toFile( key ) );
	}

	/**
	 * Remove specified file from the cache.
	 */
	protected void remove (File file) {
		if (file == null) return;

		long fs = file.length();
		if (file.delete()) {
			size -= fs;
		}
	}

	/**
	 * Get number of item count in the cache.
	 */
	public int size () {
		return size;
	}

	/**
	 * Maximum cache size available including the used and free space.
	 */
	public int total () {
		return maxSize;
	}

	/**
	 * Clear full cache.
	 */
	public void clear () {
        for (File f : cacheDir.listFiles())
        	f.delete();
		size = 0;
	}

	/**
	 * Trim down cache size to the limited.
	 */
	protected void trimToSize (int size) {
		if (size() < size) return;

		// may need to optimize this approach later.
		File[] files = cacheDir.listFiles();
		Arrays.sort( files, FILE_TIME_COMPARATOR );
		for (int i=0, count=files.length; i<count && size() > size; i++) {
			remove( files[i] );
		}
	}

	/**
	 * Calculate the total size of files in this folder.
	 */
	protected long calcTotalSize () {
		long total = 0;
		for (File f : cacheDir.listFiles())
			total += f.length();
		return total;
	}

	/**
	 * Convert key to file.
	 */
	protected final File toFile (K key) {
		return new File(cacheDir, Utils.toHashString( key.toString() ) );
	}

	/**
	 * Read object from file.
	 * @param file file to read
	 * @param access change the last modified time if true
	 * @return object if read; otherwise <code>null</code>
	 */
	protected final V readFile (File file, boolean access) {
		V obj = writer.read( file, serializer );
		if (obj != null && access)
			file.setLastModified( System.currentTimeMillis() );
		return obj;
	}

	/**
	 * Write object to file.
	 * @param file file to write
	 * @param object object being written
	 * @return lenght of file
	 */
	protected final int writeFile (File file, V object) {
		return writer.write( object, file, serializer ) ? (int)file.length() : 0;
	}

	protected final static Comparator<File> FILE_TIME_COMPARATOR = new IO.FileTimeComparator();
}
