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
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import com.sileria.net.HttpReader;
import com.sileria.util.*;

/**
 * Image loader that uses {@link ImageCache} implementation.
 * <ul>
 * <li>Images are loading from provided URLs and cached internally.</li>
 * <li>The URL will be used as the key to fetch these cached images from the cache.</li>
 * <li>Image can be a <code>Bitmap</code> or <code>Drawable</code></li>
 * <li>Callbacks can be either {@link BitmapCallback}, {@link ImageCallback} or a {@link ImageView}</li>
 * <li><code>ImageView</code> callback is optimized for use with image views in any layout specially {@link ListView}s</li>
 * <li>Call one of the <code>get</code> methods in the class to start loading and caching images</li>
 * <li>You can call one of <code>cancel</code> methods to cancel a previous fetch request</li>
 * </ul>
 * <p/>
 * NOTE: ALL THE METHOD CALLS TO THIS CLASS MUST TAKE PLACE IN UI THREAD.
 * <p/>
 * Usage: Simply create and save an instance of this class and call one of the get methods to start getting results.
 * This class is not designed to have many instances. In usual case there should be one instance per app instance.
 * <p/>
 * By default the class uses {@link MemCache} but you can provide {@link DiskCache} or the {@link HybridCache} to
 * the constructor of this class. You can also provide any other {@link Cache} implementation that you like.
 *
 * @author Ahmed Shakil
 * @date Jul 1, 2012
 */
public class CachedImageLoader {

	private final Cache<String, BitmapDrawable> cache;
	private final LoaderQueue loader;

	private final Map<Content<BitmapDrawable, ImageOptions>, Set<ContentCallback<?,ImageOptions>>> listeners = new HashMap<Content<BitmapDrawable, ImageOptions>, Set<ContentCallback<?,ImageOptions>>>();
	private final Map<ImageView, ViewCallback> viewmap = new HashMap<ImageView, ViewCallback>();

	private Drawable emptyImage;

	/**
	 * Constructor, default.
	 */
	public CachedImageLoader () {
		this( new ImageCache() );
	}

	/**
	 * Constructor, default.
	 */
	public CachedImageLoader (int poolSize) {
		this( new ImageCache() );
		setPoolSize( poolSize );
	}

	/**
	 * Construct a loader class with your own <code>ImageCache</code> object.
	 */
	public CachedImageLoader (Cache<String, BitmapDrawable> cache) {
		this.cache = cache;
		this.loader = new LoaderQueue();

		loader.start();
	}

	/**
	 * Place holder image that will be used with an <code>ImageView</code> before a load call
	 * is made or after a image load fails.
	 * <p/>
	 * By default <code>null</code> will be set to the ImageView.
	 * @param empty place holder image
	 */
	public void setEmptyImage (Drawable empty) {
		emptyImage = empty;
	}

	/**
	 * Get the current pool size.
	 */
	public int getPoolSize () {
		return loader.getPoolSize();
	}

	/**
	 * Set the pool size to specify maximum number of image loaders to work concurrently.
	 * Default value is 1 meaning contents will be loaded serially.
	 * @param poolSize pool size. Cannot be less than 1.
	 */
	public void setPoolSize (int poolSize) {
		loader.setPoolSize( poolSize );
	}

	/**
	 * Fetch a <code>Bitmap</code> image from specified URL and notify the <code>callback</code>.
	 * If image from the url was cached then the cached image will be sent on the callback.
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 */
	public void get (String url, BitmapCallback callback) {
		enqeue( new Content<BitmapDrawable, ImageOptions>( url ), callback );
	}

	/**
	 * Fetch a <code>Bitmap</code> image from specified URL and notify the <code>callback</code>.
	 * If image from the url was cached then the cached image will be sent on the callback.
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 */
	public void get (String url, ImageOptions opt, BitmapCallback callback) {
		enqeue( new Content<BitmapDrawable, ImageOptions>( url, opt ), callback );
	}

	/**
	 * Fetch a <code>Drawable</code> image from specified URL and notify the <code>callback</code>.
	 * If image from the url was cached then the cached image will be sent on the callback.
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 */
	public void get (String url, ImageCallback callback) {
		enqeue( new Content<BitmapDrawable, ImageOptions>( url ), callback );
	}

	/**
	 * Fetch a <code>Drawable</code> image from specified URL and notify the <code>callback</code>.
	 * If image from the url was cached then the cached image will be sent on the callback.
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 */
	public void get (String url, ImageOptions opt, ImageCallback callback) {
		enqeue( new Content<BitmapDrawable, ImageOptions>( url, opt ), callback );
	}

	/**
	 * Fetch a <code>Drawable</code> image from specified URL and set it on the <code>ImageView</code>
	 * being used as <code>callback</code>. No callback notifications are sent.
	 * If image from the url was cached then the cached image will be set on the imageview and method
	 * will return right away.
	 * 
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 */
	public void get (String url, ImageView callback) {
		get( url, callback, emptyImage );
	}

	/**
	 * Fetch a <code>Drawable</code> image from specified URL and set it on the <code>ImageView</code>
	 * being used as <code>callback</code>. No callback notifications are sent.
	 * If image from the url was cached then the cached image will be set on the imageview and method
	 * will return right away.
	 *
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 * @param empty empty drawable to be set before the loading starts and in case of failure
	 */
	public void get (String url, ImageView callback, Drawable empty) {
		get( url, callback, empty, empty, null );
	}

	/**
	 * Fetch a <code>Drawable</code> image from specified URL and set it on the <code>ImageView</code>
	 * being used as <code>callback</code>. No callback notifications are sent.
	 * If image from the url was cached then the cached image will be set on the imageview and method
	 * will return right away.
	 *
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 * @param empty empty drawable to be set before the loading starts and in case of failure
	 */
	public void get (String url, ImageView callback, Drawable empty, ImageOptions opt) {
		get( url, callback, empty, empty, opt );
	}

	/**
	 * Fetch a <code>Drawable</code> image from specified URL and set it on the <code>ImageView</code>
	 * being used as <code>callback</code>. No callback notifications are sent.
	 * If image from the url was cached then the cached image will be set on the imageview and method
	 * will return right away.
	 *
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 * @param empty empty drawable to be set before the loading starts
	 * @param broken drawable representing a broken link in terms of failure
	 */
	public void get (String url, ImageView callback, Drawable empty, Drawable broken) {
		get( url, callback, empty, broken, null );
	}

	/**
	 * Fetch a <code>Drawable</code> image from specified URL and set it on the <code>ImageView</code>
	 * being used as <code>callback</code>. No callback notifications are sent.
	 * If image from the url was cached then the cached image will be set on the imageview and method
	 * will return right away.
	 *
	 * @param url URL to download the image from
	 * @param callback listener that gets triggered on success or failure
	 * @param empty empty drawable to be set before the loading starts
	 * @param broken drawable representing a broken link in terms of failure
	 */
	public void get (String url, ImageView callback, Drawable empty, Drawable broken, ImageOptions opt) {
		if (callback == null) return;

		// check if the image view is already attached to the same url
		ViewCallback cb = viewmap.get( callback );
		if (cb != null && Utils.equals( cb.url, url )) {
			callback.setImageDrawable( empty );
			return;
		}

		cancel( callback );    // cancel any previous callback

		// if empty url then set empty image and return
		if (Utils.isEmpty(url)) {
			callback.setImageDrawable( empty );
			return;
		}

		cb = new ViewCallback( url, callback, broken, opt );
		// if cached image found then set image and return
		if (cache.contains( cb.key )) {
			Drawable img = cache.get( cb.key );
			if (img != null) {
				callback.setImageDrawable( img );
				return;
			}
		}

		// create callback listener
		callback.setImageDrawable( empty );

		if (enqeue( new Content<BitmapDrawable, ImageOptions>( url, opt ), cb )) {              // enqueu url loading and callback
			viewmap.put( callback, cb );     // add to to image view map
		}
	}

	/**
	 * Just download and cache the image specified by the url without any callbacks.
	 * @param url URL of the image to cache
	 * @return <code>true</code> if already cached; otherwise <code>false</code>
	 */
	public boolean cache (String url) {
		return cache( url, null );
	}

	/**
	 * Just cache the image specified by the url without any callbacks.
	 * @param url URL of the image to cache
	 * @return <code>true</code> if already cached; otherwise <code>false</code>
	 */
	public boolean cache (String url, ImageOptions opt) {
		Content<BitmapDrawable, ImageOptions> key = new Content<BitmapDrawable, ImageOptions>( url, opt );
		String cacheKey = toCacheKey( key );
		return cache.contains( cacheKey ) || enqeue( key, null );
	}

	/**
	 * Clear the image cache.
	 */
	public void clear () {
		cache.clear();
	}

	/**
	 * Add the specified url and callback to the download queue.
	 * @param request content key to download
	 * @param callback listener that gets triggered on success or failure
	 */
	protected boolean enqeue (Content<BitmapDrawable, ImageOptions> request, ContentCallback<?,ImageOptions> callback) {
		if (request == null || Utils.isEmpty(request.key)) return false;

		// check for existing callbacks.
		Set<ContentCallback<?,ImageOptions>> set = listeners.get( request );
		if (set == null) {
			listeners.put( request, set = new HashSet<ContentCallback<?, ImageOptions>>(1) );
		}
		else if (set.contains( callback )) {
			return false;
		}

		if (set.isEmpty())            							// no need to re-queue
			loader.enqueue( request.key, request.options );     // add to download queue

		if (callback != null)                                   // allow null callback but do not register them
			set.add( callback );
		return true;
	}

	/**
	 * Common remove implementation.
	 */
	protected void remove (String url, ImageOptions opt, ContentCallback<?, ImageOptions> callback) {
		if (Utils.isEmpty(url)) return;

		Content<BitmapDrawable,ImageOptions> key = new Content<BitmapDrawable,ImageOptions>( url, opt );

		Set<ContentCallback<?,ImageOptions>> set = listeners.get( key );
		if (!Utils.isEmpty( set )) {
			set.remove( callback );
		}

		if (Utils.isEmpty( set )) {
			loader.remove( key );
			listeners.remove( key );
		}
	}

	/**
	 * Cancel a image loading for specified <code>url</code>.
	 * NOTE: All callbacks for this <code>url</code> will be removed from being notified.
	 */
	public void cancel (String url, ImageOptions opt) {
		Content<BitmapDrawable,ImageOptions> key = new Content<BitmapDrawable,ImageOptions>( url, opt );
		loader.remove( key );
		listeners.remove( key );
	}

	/**
	 * Cancel a listener callback for specified <code>callback</code>.
	 * If no other listeners were registered for the url then the
	 * image loading will also be cancelled.
	 */
	public void cancel (ImageView callback) {
		if (callback == null) return;
		
		ViewCallback cb = viewmap.remove( callback );
		if (cb != null) {
			loader.remove( cb.url, 0, cb.opt );
			remove( cb.url, cb.opt, cb );
		}
	}

	/**
	 * Cancel a listener callback for specified <code>callback</code>.
	 * If no other listeners were registered for the url then the
	 * image loading will also be cancelled.
	 */
	public void cancel (String url, ImageOptions opt, BitmapCallback callback) {
		remove( url, opt, callback );
	}

	/**
	 * Cancel a listener callback for specified <code>callback</code>.
	 * If no other listeners were registered for the url then the
	 * image loading will also be cancelled.
	 */
	public void cancel (String url, ImageOptions opt, ImageCallback callback) {
		remove( url, opt, callback );
	}

	/**
	 * Image view callback.
	 */
	protected class ViewCallback implements ImageCallback {
		protected String url, key;
		protected ImageView view;
		protected Drawable empty;
		protected Drawable broken;
		protected ImageOptions opt;

		public ViewCallback (ImageView view) {
			this.view   = view;
		}

		public ViewCallback (String url, ImageView view, Drawable empty) {
			this.url    = url;
			this.key    = url;
			this.view   = view;
			this.empty  = empty;
		}

		public ViewCallback (String url, ImageView view, Drawable empty, ImageOptions opt) {
			this.url    = url;
			this.view   = view;
			this.empty  = empty;
			this.opt    = opt;
			this.key    = toCacheKey( url, opt );
		}

		/**
		 * This method is invoked on each content load.
		 */
		public void onContentLoad (Content<Drawable,ImageOptions> content) {
			view.setImageDrawable( content.content );
			viewmap.remove( view );
		}

		/**
		 * This method is invoked for each content load failure.
		 */
		public void onContentFail (Content<Throwable, ImageOptions> error) {
			view.setImageDrawable( empty );
			viewmap.remove( view );
		}

		@Override
		public boolean equals (Object o) {
			return this == o || !(o == null || getClass() != o.getClass()) && view.equals( o );
		}

		@Override
		public int hashCode () {
			return view.hashCode();
		}
	}

	/**
	 * Queued image loader class.
	 */
	protected class LoaderQueue extends QueuedContentLoader<BitmapDrawable, ImageOptions> {

		/**
		 * Create the content loader that will be used to load the specific contents from a url or filename.
		 *
		 * @param request content request
		 * @param callback class point to the queued content loader that must be used to as a delegate
		 * @return ContentLoader implementation for your specific content type
		 */
		@Override
		protected ContentLoader<BitmapDrawable, ImageOptions> createLoader (Content<BitmapDrawable, ImageOptions> request, ContentCallback<BitmapDrawable, ImageOptions> callback) {
			return new CacheLoader(callback);
		}

		/**
		 * Image loaded successfully .
		 * @param result Content that was loaded
		 */
		@Override
		public void onContentLoad (Content<BitmapDrawable, ImageOptions> result) {

			// add to cache first
			String cacheKey = toCacheKey( result );
			if (!cache.contains( cacheKey )) {
				cache.put( cacheKey, result.content );
			}

			// check if any listeners
			Set<ContentCallback<?, ImageOptions>> set = listeners.get( result );
			if (set == null) return;

			Bitmap bitmap = result.content.getBitmap();
			Content<Bitmap, ImageOptions> bmpContent = null;
			Content<Drawable, ImageOptions> imgContent = null;

			// notify all listeners.
			for (ContentCallback<?, ImageOptions> cb : set) {
				if (cb instanceof ImageCallback) {
					if (imgContent == null)
						imgContent = new Content<Drawable, ImageOptions>( result );
					((ImageCallback)cb).onContentLoad( imgContent );
				}
				else if (cb instanceof BitmapCallback) {
					if (bmpContent == null)
						bmpContent = new Content<Bitmap, ImageOptions>( bitmap, result.id, result.key, result.options );
					((BitmapCallback)cb).onContentLoad( bmpContent );
				}
			}

			set.clear();
			listeners.remove( result );
		}

		/**
		 * Image load failed.
		 * @param error error that occured during the load
		 */
		@Override
		public void onContentFail (Content<Throwable, ImageOptions> error) {
			
			Set<ContentCallback<?,ImageOptions>> set = listeners.get( error );
			if (set == null) return;

			for (ContentCallback<?,ImageOptions> cb : set) {
				cb.onContentFail( error );
			}

			set.clear();
			listeners.remove( error );
		}
	}

	/**
	 * Bitmap drawable loader.
	 */
	protected class CacheLoader extends ContentLoader<BitmapDrawable, ImageOptions> {

		/**
		 * Constructor, default.
		 */
		public CacheLoader (ContentCallback<BitmapDrawable,ImageOptions> callback) {
			super( callback );
		}

		/**
		 * Load bitmap in background thread from the specified URL address.
		 *
		 * @param url URL address
		 * @param opt ImageOptions
		 *@param tries the number of tries that has happened so far.
		 *  @return Loaded Bitmap
		 *
		 * @throws java.io.IOException in case of IO exception.
		 */
		protected BitmapDrawable loadContent (String url, ImageOptions opt, int tries) throws IOException {

			// check the cache one more time.
			BitmapDrawable img = cache.get( url );
			if (img != null)
				return img;

			// try loading the image
			InputStream in = null;
			HttpReader reader = null;
			try {

				URL u = new URL( url );
				URLConnection conn = u.openConnection();
				conn.setConnectTimeout( timeout );
				conn.setUseCaches( useCache );

				Object content = conn.getContent();
				if (opt == null && content instanceof BitmapDrawable)
					img = (BitmapDrawable)content;
				else
					img = new ImageSerializer(opt).getContent( conn );
			}
			finally {
				IO.close( in );
				IO.close( reader );
			}

			return img;
		}
	}

	/**
	 * Generate unique hash for url/opt.
	 */
	private static String toCacheKey (String url, ImageOptions opt) {
		if (!ImageOptions.hasOptions( opt ))
			return url;

		int result = url != null ? url.hashCode() : 0;
		result = 31 * result + opt.hashCode();
		return Integer.toHexString( result );
	}

	/**
	 * Generate unique hash for url/opt.
	 */
	private static String toCacheKey (Content<?, ImageOptions> key) {
		return toCacheKey( key.key, key.options );
	}
}
