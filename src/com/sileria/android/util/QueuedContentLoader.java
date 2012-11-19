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

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sileria.util.*;

/**
 * ContentLoader is a generic abstract class that builts in the funtionality for loading
 * any kind of user defined content from a stream, file or a webservice in a serial or pooled mechanism.
 * <p/>
 * To extend simply override {@link #createLoader(com.sileria.util.Content, ContentCallback)} 
 * <p/>
 * As an example check out the {@link com.sileria.android.util.QueuedImageLoader} implementation.
 *
 * @author Ahmed Shakil
 * @date Jul 1, 2012
 *
 * @param <T> content type of any kind.
 * @param <O> ContentOption type
 */
public abstract class QueuedContentLoader<T, O extends ContentOptions> implements ContentCallback<T,O>, CompletionCallback<ContentLoader<T, O>>, Cancellable {

	private final Queue<Content<T,O>> queue = new ConcurrentLinkedQueue<Content<T, O>>();

	private final Map<Content<T,O>, ContentLoader<T, O>> loaders = new ConcurrentHashMap<Content<T,O>, ContentLoader<T, O>>( DEFAULT_POOL_SIZE );

	private ContentCallback<T,O> callback;

	private boolean running;

	private int poolSize = DEFAULT_POOL_SIZE;

	private static int DEFAULT_POOL_SIZE = 1;

	/**
	 * Set the pool size for all content loaders.
	 * Default value is 1 meaning contents will be loaded serially.
	 * @param poolSize pool size. Cannot be less than 1.
	 */
	public static void setDefaultPoolSize (int poolSize) {
		DEFAULT_POOL_SIZE = Math.max( 1, poolSize );
	}

	/**
	 * Constructor, default.
	 * Note: This constructor must be called from UI thread.
	 */
	protected QueuedContentLoader () {
		this( null );
	}

	/**
	 * Construct an image loader with image callback.
	 * Note: This constructor must be called from UI thread.
	 */
	protected QueuedContentLoader (ContentCallback<T,O> callback) {
		this.callback = callback;
	}

	/**
	 * Add image url to load.
	 *
	 * @param url URL of the image
	 */
	public boolean enqueue (String url) {
		return enqueue( url, 0, null );
	}
	
	/**
	 * Add image url to load.
	 *
	 * @param url URL of the image
	 * @param id optional index or id for the image which will be sent to the {@link ContentCallback#onContentLoad(com.sileria.util.Content)}
	 *  pass zero to ignore the id.
	 */
	public boolean enqueue (String url, int id) {
		return enqueue( url, id, null );
	}

	/**
	 * Add image url to load.
	 *
	 * @param url URL of the image
	 */
	public boolean enqueue (String url, O opt) {
		return enqueue( url, 0, opt );
	}

	/**
	 * Add image url to load.
	 *
	 * @param url URL of the image
	 * @param id optional index or id for the image which will be sent to the {@link ContentCallback#onContentLoad(com.sileria.util.Content)}
	 *  pass zero to ignore the id.
	 */
	public boolean enqueue (String url, int id, O opt) {
		return enqueue( new Content<T, O>( id, url, opt ) );
	}

	/**
	 * Add image url to load.
	 */
	boolean enqueue (Content<T,O> request) {

		if (request == null || Utils.isEmpty( request.key )) return false;

//		// add id if non-zero
//		if (request.id != 0) {
//			Integer i = idmap.get( request );
//			if (i != null) {
//				if (i == request.id)
//					return false;
//				else
//					throw new IllegalArgumentException( "Same URL cannot be added with different ID." );
//			}
//			idmap.put( request, request.id );
//		}

		queue.add( request );      // add url to download queue

		if (running)
			requeue();

		return true;
	}

	/**
	 * Cancel content downloads and remove them from the download queue.
	 * Each download will be stopped if it was running, and no callbacks
	 * will be received for this item.
	 */
	public void remove (String url, int id, O opt) {
		remove( new Content<T,O>(id, url, opt) );
	}

	/**
	 * Cancel content downloads and remove them from the download queue.
	 * Each download will be stopped if it was running, and no callbacks
	 * will be received for this item.
	 */
	public void remove (String url, O opt) {
		remove( new Content<T,O>(0, url, opt) );
	}

	/**
	 * Cancel content downloads and remove them from the download queue.
	 * Each download will be stopped if it was running, and no callbacks
	 * will be received for this item.
	 */
	public void remove (String ... urls) {
		for (String url : urls) {
			remove( new Content<T,O>(0, url, null) );
		}
	}

	/**
	 * Cancel content downloads and remove them from the download queue.
	 * Each download will be stopped if it was running, and no callbacks
	 * will be received for this item.
	 */
	protected void remove (Content<T, O> request) {
		ContentLoader<T, O> loader = loaders.remove( request );
		if (loader != null)
			loader.cancel();
		queue.remove( request );

		if (running)
			requeue();
	}

	/**
	 * Starts the queue loader. More items can be added
	 * to the queue after the task has been started.
	 * This method must be invoked on the UI thread.
	 */
	public void start () {
		running = true;
		requeue();
	}

	/**
	 * Executes the task. The task returns itself (this) so that the caller
	 * can keep a reference to it. This method must be invoked on the UI thread.
	 *
	 * @return Returns itself (this) so that the caller can keep a reference to it
	 */
	public QueuedContentLoader execute () {
		start();
		return this;
	}

	/**
	 * Return instance of {@link BitmapLoader} class.
	 */
	protected abstract ContentLoader<T, O> createLoader (Content<T, O> request, ContentCallback<T, O> callback);

	/**
	 * Check queue.
	 */
	private synchronized void requeue () {
		if (!running) return;

		int count = Math.min( poolSize - loaders.size(), queue.size() );

		for (int i=0; i<count; i++) {
			Content<T,O> request = queue.poll();
			ContentLoader<T, O> loader = createLoader( request, this );
			loader.setCallback( (CompletionCallback<ContentLoader<T, O>>)this );
			loaders.put( request, loader.execute( request ) );
		}
	}

	/**
	 * Get the current pool size.
	 */
	public int getPoolSize () {
		return poolSize;
	}

	/**
	 * Set the pool size to specify maximum number of image loaders to work concurrently.
	 * Default value is 1 meaning contents will be loaded serially.
	 * @param poolSize pool size. Cannot be less than 1.
	 */
	public void setPoolSize (int poolSize) {
		this.poolSize = Math.max( 1, poolSize );
	}

	/**
	 * Image loaded successfully .
	 * @param content Content that was loaded
	 */
	public void onContentLoad (Content<T, O> content) {
		if (running && callback != null && loaders.containsKey( content ))
			callback.onContentLoad( content );
	}

	/**
	 * Image load failed.
	 * @param error that occured during the load
	 */
	public void onContentFail (Content<Throwable, O> error) {
		if (running && callback != null && loaders.containsKey( error ))
			callback.onContentFail( error );
	}

	/**
	 * Task completed.
	 * @param task Task that was running.
	 */
	public void onComplete (ContentLoader<T, O> task) {
		Content<T, O> request = task.getRequest();
		ContentLoader<T, O> loader = loaders.get( request );
		if (loader == task)
			loaders.remove( request );
		requeue();
	}

	/**
	 * Cancel all calls.
	 */
	public synchronized void cancel () {
		running = false;

		for (ContentLoader<T, O> loader : loaders.values())
			loader.cancel();
		
		loaders.clear();
		queue.clear();
	}
}
