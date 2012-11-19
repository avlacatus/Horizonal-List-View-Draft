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

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sileria.android.Kit;
import com.sileria.util.*;

/**
 * ContentLoader is a generic abstract class that builts in the mechanism for loading
 * any kind of user defined content from the a stream or a webservice.
 * <p/>
 * To extend simply override {@link #loadContent(String, com.sileria.util.ContentOptions, int)}   method in your subclass.
 * <p/>
 * As an example check out the {@link com.sileria.android.util.ImageLoader} implementation.
 *
 * @author Ahmed Shakil
 * @date Jul 1, 2012
 *
 * @param <T> content type of any kind.
 * @param <O> content options type
 */
public abstract class ContentLoader<T, O extends ContentOptions> extends AsyncTask<String, Content, List<T>> implements Cancellable {

    private ContentCallback<T,O> callback;

	private CompletionCallback<ContentLoader<T, O>> doneCallback;

	private Content<T,O> request;

	protected int timeout;

	protected int buffSize                  = DEF_BUFF_SIZE;

	protected boolean useCache              = DEF_USE_CACHE;

	private static final int DEF_BUFF_SIZE  = 5120;
	private static boolean DEF_USE_CACHE    = true;

	private static final int RETRY_TIMES    = 2;
	private static final int RETRY_DELAY    = 100;

	/**
	 * Set the default cache usage setting for all requests.
	 * By default it is set to <code>true</code> for content loaders.
	 *
	 * @param useCache a <code>boolean</code> indicating whether
	 * or not to allow caching
	 */
	public static void setDefaultUseCache (boolean useCache) {
		DEF_USE_CACHE = useCache;
	}

    /**
     * Constructor, default.
	 * <p/>
	 * Note: This constructor must be called from UI thread.
     */
    protected ContentLoader () {
		this( null );
    }

    /**
     * Constructor specifying a callback
     */
    protected ContentLoader (ContentCallback<T,O> callback) {
        this.callback = callback;
    }

	/**
	 * Sets a specified timeout value, in milliseconds, to be used when opening
	 * a communications link to the resource referenced by this URLConnection.
	 * <p/>
	 * If the timeout expires before the connection can be established,
	 * a java.net.SocketTimeoutException is raised. A timeout of zero is
	 * interpreted as an infinite timeout.
	 * <p/>
	 * Note: This parameter will only take affect if default {@link com.sileria.net.HttpReader} or
	 * one if it's subclasses are used as the <code>RemoteReader</code>. If you provide
	 * your own implementation of <code>RemoteReader</code> set the parameters directly
	 * into that custom class.
	 * <p/>
	 * @param millis timeout value in milliseconds
	 */
	public void setTimeout (int millis) {
		this.timeout = millis;
	}

	/**
	 * Sets the value of the <code>useCaches</code> field of this
	 * <code>URLConnection</code> to the specified value.
	 *
	 * @param useCache a <code>boolean</code> indicating whether
	 * or not to allow caching
	 */
	public void setUseCache (boolean useCache) {
		this.useCache = useCache;
	}

	/**
	 * Set the content callback listener.
	 */
	public void setCallback (ContentCallback<T,O> callback) {
		this.callback = callback;
	}

	/**
	 * Set the task callback listener for this loader.
	 */
	public void setCallback (CompletionCallback<ContentLoader<T, O>> callback) {
		this.doneCallback = callback;
	}

	/**
	 * Set the initial buffer size to read the remote bytes. By default
	 * the size is specified by {@link #DEF_BUFF_SIZE}.
	 * <p/>
	 * Note: This parameter will only take affect if default {@link com.sileria.net.HttpReader} or
	 * one if it's subclasses are used as the <code>RemoteReader</code>. If you provide
	 * your own implementation of <code>RemoteReader</code> set the parameters directly
	 * into that custom class.
	 *
	 * @param buffSize Initial read buffer size
	 */
	public void setInitialReadSize (int buffSize) {
		this.buffSize = buffSize;
	}

	/**
	 * Executes the task with a single url to load and a tagging index.
	 * The task returns itself (this) so that the caller can keep a reference to it.
	 * This method must be invoked on the UI thread.
	 *
	 * @param url URL of the content
	 * @param id index or id for the image which will be sent to the
	 * 	      {@link ContentCallback#onContentLoad(com.sileria.util.Content)}
	 *
	 * @return Returns itself (this) so that the caller can keep a reference to it
	 */
	public ContentLoader<T, O> execute (String url, int id) {
		return execute( url, id, null );
	}

	/**
	 * Executes the task with a single url to load and a tagging index.
	 * The task returns itself (this) so that the caller can keep a reference to it.
	 * This method must be invoked on the UI thread.
	 *
	 * @param url URL of the content
	 * @param id index or id for the image which will be sent to the
	 * 	      {@link ContentCallback#onContentLoad(com.sileria.util.Content)} 
	 * @param opt ContentOptions implementation, can be null if not options needed or provided.
	 *
	 * @return Returns itself (this) so that the caller can keep a reference to it
	 */
	public ContentLoader<T, O> execute (String url, int id, O opt) {
		return execute( new Content<T,O>( id, url, opt ) );
	}

	/**
	 * Executes the task with a single url to load and a tagging index.
	 * The task returns itself (this) so that the caller can keep a reference to it.
	 * This method must be invoked on the UI thread.
	 *
	 * @param request requested data
	 *
	 * @return Returns itself (this) so that the caller can keep a reference to it
	 */
	ContentLoader<T, O> execute (Content<T, O> request) {
		this.request = request;
		return (ContentLoader<T, O>) super.execute( request.key );
	}

	/**
	 * Task callback in post execute.
	 */
	@Override
	protected void onPostExecute (List<T> contents) {
		super.onPostExecute( contents );

		if (doneCallback != null)
			doneCallback.onComplete( this );
	}

	/**
     * Load contents in background and notify the callback on the EDT.
     *
     * @param urls collection of content urls
     * @return content list of type <T>
     */
	@SuppressWarnings( "unchecked" )
    @Override
    protected List<T> doInBackground (String ... urls) {
        if (urls == null)
            return null;

        List<T> contents = new ArrayList<T>( urls.length );

        // load one by one.
		String u;
		for (int i = 0, count = urls.length; i < count; i++) {
			u = urls[i];

			if (Utils.isEmpty( u )) {
				publishProgress();
				continue;
			}

			T content = null;
			Throwable t = null;

			// keep trying couple of times in-case of error.
			for (int tries = 0; tries < RETRY_TIMES; ) {
				try {
					content = loadContent( u, request.options, tries );
					break;
				}
				catch (Throwable e) {
					Log.e( Kit.TAG, "Error loading content: " + u, t = e );
				}
				finally {
					System.gc();
				}

				if (++tries < RETRY_TIMES) {
					Log.w( Kit.TAG, "Content Load Failed, Will retry in " + RETRY_DELAY + "ms." );
					SystemClock.sleep( RETRY_DELAY );
				}
			}

			int id = request.id < 0 ? i : request.id;
			if (content != null) {
				publishProgress( new Content<T,O>( content, id, u, request.options ) );
				contents.add( content );
			}
			else {
				if (t == null)
					t = new RuntimeException( "Unknown error loading: " + u );
				publishProgress( new Content<Throwable, O>( t, id, u, request.options ) );
			}

		}

        return contents;
    }

	/**
	 * Load content in background thread from the specified URL address.
	 *
	 * @param url URL address or Filename
	 * @param opt implementation of <code>ContentOptions</code>
	 *@param tries the number of tries that has happened so far.
	 *  @return Loaded content from the url or filename
	 *
	 * @throws IOException in case of IO exception.
	 */
	protected abstract T loadContent (String url, O opt, int tries) throws IOException;

    /**
     * Publish messages.
     */
	@SuppressWarnings( "unchecked" )
	@Override
	protected void onProgressUpdate (Content ... progress) {
		if (callback == null) return;
		
        for (Content c : progress) {
			if (c.content instanceof Throwable)
				callback.onContentFail( c );
			else
				callback.onContentLoad( c );
        }
    }

	/**
	 * URL can be accessed after the execute call was made.
	 * @return url string that was passed to the execute method
	 */
	public String getURL () {
		return request == null ? null : request.key;
	}

	/**
	 * ID can be accessed after the execute call was made.
	 * @return id that was passed to the execute method
	 */
	public int getID () {
		return request == null ? -1 : request.id;
	}

	/**
	 * ID can be accessed after the execute call was made.
	 * @return id that was passed to the execute method
	 */
	Content<T, O> getRequest () {
		return request;
	}

    /**
     * Cancel the request or a thread.
     * <p/>
     * Note: This method does not guarentee immediate
     * cancellation, but may take a while to effectively
     * cancel the request.
     */
    public void cancel () {
        cancel( true );
    }

}
