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

import android.os.SystemClock;

import com.sileria.net.*;
import com.sileria.util.AsyncCallback;

/**
 * A background worker class and an extension of {@code AsyncTask} to make
 * {@link com.sileria.net.RemoteRequest} based webservices calls be executed
 * in the background thread.
 * <p/>
 * {@link com.sileria.net.RemoteCallback#onSuccess(Object)} or {@link com.sileria.net.RemoteCallback#onFailure(Throwable)}
 * is called based on success or failure of the webservice request.
 * <p/>
 * Example:
 * <blockquote><pre>
 *      return new RemoteTask(callback).execute( new MyRemoteRequest() );
 * </pre></blockquote>
 * <p/>
 * Note this class works with both {@link com.sileria.net.RemoteCallback} as well as {@link com.sileria.util.AsyncCallback}
 * @author Ahmed Shakil
 * @date Mar 20, 2010
 */
public class RemoteTask<T> extends AbstractWorker<Object, Void, T, RemoteException> implements RemoteCallback<T>, RemoteExecutor<T> {

    private RemoteRequest<T> request;

	private AsyncCallback<T> aCallback;

    /**
     * Constructor specifying the actuall callback.
	 * @param callback RemoteCallback
     */
    public RemoteTask (RemoteCallback<T> callback) {
		this( null, callback );
    }

    /**
     * Constructor specifying the actuall callback.
	 * @param callback RemoteCallback
     */
    public RemoteTask (AsyncCallback<T> callback) {
		this( null, callback );
    }

    /**
     * Constructor specifying the actuall callback.
	 * @param request  RemoteRequest
     */
    public RemoteTask (RemoteRequest<T> request) {
		this( request, (RemoteCallback<T>)null );
    }

    /**
     * Constructor specifying the actuall callback.
	 * @param request  RemoteRequest
	 * @param callback RemoteCallback
     */
    public RemoteTask (RemoteRequest<T> request, RemoteCallback<T> callback) {
		super( callback );

		this.request = request;
		if (request != null)
			this.request.setCallback( this );
    }

    /**
     * Constructor specifying the actuall callback.
	 * @param request  RemoteRequest
	 * @param callback RemoteCallback
     */
    public RemoteTask (RemoteRequest<T> request, AsyncCallback<T> callback) {
		aCallback = callback;

		this.request = request;
		if (request != null)
			this.request.setCallback( this );
    }

    /**
     * Execute a <code>RemoteRequest</code> in another thread
     * and return the result to the specified <code>callback</code>
     */
    public RemoteTask<T> execute () {
        super.execute();
        return this;
    }

	/**
	 * Execute a <code>RemoteRequest</code> in another thread
	 * and return the result to the specified <code>callback</code>
	 */
	public RemoteExecutor<T> execute (RemoteRequest<T> request) {
		this.request = request;
		this.request.setCallback( this );
		super.execute();
		return this;
	}

    /**
     * Run in background.
     */
    @Override
	protected final T doInBackground (Object ... params) {
		if (request == null)
			return null;

		long start = SystemClock.uptimeMillis();
		T result = null;
		try {
			result = doTask( params );
		}
		catch (RemoteException e) {
			error = e;
		}
		finally {
			millis = SystemClock.uptimeMillis() - start;
		}

		return result;
	}

	/**
	 * Override this method to perform a computation on a background thread. The
	 * specified parameters are the parameters passed to {@link #execute}
	 * by the caller of this task.
	 * <p/>
	 * Make your overriden class throw any exception and you will get the error
	 * in the {@link #onFailure(Throwable)} call in UI thread (ofcourse).
	 * <p/>
	 * When the method is successful {@link #onSuccess(Object)} method will be called
	 * with the return value from this method and also in the UI thread.
	 * <p/>
	 * This method can call {@link #publishProgress} to publish updates
	 * on the UI thread.
	 *
	 * @param params The parameters of the task
	 * @return A result, defined by the subclass of this task
	 */
	@Override
	protected T doTask (Object... params) throws RemoteException {
		return request.execute();
	}

	/**
	 * Done
	 */
	@Override
	protected void onPostExecute (T result) {
		if (aCallback != null) {
			if (error != null)
				aCallback.onFailure( error );
			else
				aCallback.onSuccess( result );

			if (doneCallback != null)
				doneCallback.onComplete( this );
		}
		else
			super.onPostExecute( result );
	}

	/**
     * Called in case of any error.
     *
     * @param e Throwable exception
     */
    public void onFailure (RemoteException e) {
		error = e;
    }

	/**
     * Cancel the request or a thread.
     * <p/>
     * Note: This method does not guarentee immediate
     * cancellation, but may take a while to effectively
     * cancel the request.
     */
    public void cancel () {
        if (request != null)
            request.cancel();
        super.cancel();
    }
}
