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

package com.sileria.android.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.sileria.android.Resource;
import com.sileria.util.*;

/**
 * A simple memory cache for storing Bitmaps or Drawables in memory associated with a string id or a url.
 * <p/>
 * This class extends from <code>MemCache</code> meaning it uses strong references to the bitmaps and to
 * clear the cache we need to explicitly do it. This was done because in the past, a popular memory cache
 * implementation was a SoftReference or WeakReference bitmap cache, however this is not recommended in
 * Android any more, since starting Android 2.3 (API Level 9) the garbage collector is more aggressive with
 * collecting soft/weak references which makes them fairly ineffective. In addition, prior to Android 3.0 (API Level 11),
 * the backing data of a bitmap was stored in native memory which is not released in a predictable manner,
 * potentially causing an application to briefly exceed its memory limits and crash..
 * <p/>
 * Usage: Simply create and save an instance of this class and call one of the put/get methods to start caching and fetching images.
 * <p/>
 * Note: This class is not designed to have many instances. In usual case there should be one instance per app instance.
 * 
 * @author Ahmed Shakil
 * @date 08-21-2012
 */
public class ImageCache extends MemCache<String, BitmapDrawable> {

	/**
	 * Construct ImageCache object with default cache size of 1MB.
	 */
	public ImageCache () {
		this( MemCache.DEFAULT_MAXIMUM_BYTES );
	}

	/**
	 * Construct ImageCache object with specified cache size limit.
	 * @param maxSize max cache size in bytes. If less than 1KB defaults to 1KB.
	 */
	public ImageCache (int maxSize) {
		super( DEFAULT_INITIAL_CAPACITY, Math.max( IO.ONE_KB, maxSize ) );
	}

	/**
	 * Cache a <code>Bitmap</code>.
	 * @param key String id or URL
	 * @param bmp image to cache.
	 */
	public void put (String key, Bitmap bmp) {
		if (bmp == null)
			throw new NullPointerException( "Bitmap cannot be NULL." );
		put( key, new BitmapDrawable( Resource.getResources(), bmp ) );
	}

	/**
	 * This is a convenience method to cache a <code>Drawable</code> only
	 * if it is an instance of <code>BitmapDrawable</code>; otherwise
	 * it will not be cached and ignore silently.
	 * 
	 * @param key String id or URL.
	 * @param img image to cache. MUST be <code>BitmapDrawable</code>
	 */
	public void put (String key, Drawable img) {
		if (img instanceof BitmapDrawable)
			put( key, (BitmapDrawable)img );
	}

	/**
	 * Get the bitmap instance from the cache if previously cached; otherwise returns <code>null</code>.
	 *
	 * @param key String id or URL.
	 * @return a previously cached <code>BitmapDrawable</code> or <code>null</code>
	 */
	@Override
	public BitmapDrawable get (String key) {
		BitmapDrawable b = super.get( key );

		Bitmap bmp = b == null ? null : b.getBitmap();
		return bmp != null && !bmp.isRecycled() ? b : null;
	}

	/**
	 * Get the bitmap instance from the cache if previously cached; otherwise returns <code>null</code>.
	 * @param key String id or URL.
	 * @return a previously cached <code>Drawable</code> or <code>null</code>
	 */
	public Drawable getDrawable (String key) {
		return get( key );
	}

	/**
	 * Get the bitmap instance from the cache if previously cached; otherwise returns <code>null</code>.
	 * @param key String id or URL.
	 * @return a previously cached <code>Bitmap</code> or <code>null</code>
	 */
	public Bitmap getBitmap (String key) {
		BitmapDrawable b = get( key );
		return b == null ? null : b.getBitmap();
	}

	/**
	 * Checks to see if the cached contains a valid image for the specified key.
	 *
	 * @param key String id or URL.
	 * @return <code>true</code> if cached; otherwise <code>false</code>
	 */
	@Override
	public boolean contains (String key) {
		return getBitmap( key ) != null;
	}

	/**
	 * Return the bitmap size.
	 */
	@Override
	protected int sizeOf (BitmapDrawable img) {
		//return bmp == null ? 0 : bmp.getBitmap().getByteCount();  <--- method not available until API 12
		if (img == null) return 0;

		Bitmap bmp = img.getBitmap();
		if (bmp == null) return 0;
		
		return bmp.getRowBytes() * bmp.getHeight();
	}

	/**
	 * Set <code>ByteCounter</code> to calculate byte of each object.
	 */
	@Override
	public void setByteCounter (ByteCounter<BitmapDrawable> bitmapDrawableByteCounter) {
		throw new UnsupportedOperationException( "This feature is supported for ImageCache." );
	}
}
