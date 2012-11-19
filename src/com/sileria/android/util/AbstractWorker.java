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

import com.sileria.util.*;

/**
 * Abstract base adpater class to make AsyncTask work with {@link com.sileria.util.AsyncObserver} or its sub-classes.
 *
 * @see com.sileria.android.util.RemoteTask
 *
 * @author Ahmed Shakil
 * @date Mar 20, 2010
 */
public abstract class AbstractWorker<Params, Progress, Result, Err extends Throwable>
        extends AsyncTask<Params, Progress, Result>
        implements Cancellable, AsyncObserver<Result, Err> {

    protected AsyncObserver<Result, Err> callback;

	protected CompletionCallback<AbstractWorker<Params, Progress, Result, Err>> doneCallback;

    protected Err error;

	protected long millis;

	/**
     * Constructor, default.
	 * NOTE: this contructor will set itself as the callback.
	 * Unless you specify the callback instance in the constructor or setter method.
     */
    protected AbstractWorker () {
		this.callback = this;
    }

    /**
     * Constructor specifying the actuall callback.
     */
    protected AbstractWorker (AsyncObserver<Result, Err> callback) {
        this.callback = callback;
    }

	/**
	 * Set the callback listener.
	 */
	public void setCallback (AsyncObserver<Result, Err> callback) {
		this.callback = callback;
	}

	/**
	 * Set the task completion listener for this task.
	 */
	public void setCallback (CompletionCallback<AbstractWorker<Params, Progress, Result, Err>> callback) {
		this.doneCallback = callback;
	}

	/**
	 * Override this method to perform a computation on a background thread. The
	 * specified parameters are the parameters passed to {@link #execute}
	 * by the caller of this task.
	 *
	 * Make your overriden class throw any exception and you will get the error
	 * in the {@link #onFailure(Throwable)} call in UI thread (ofcourse).
	 *
	 * When the method is successful {@link #onSuccess(Object)} method will be called
	 * with the return value from this method and also in the UI thread.
	 *
	 * This method can call {@link #publishProgress} to publish updates
	 * on the UI thread.
	 *
	 * @param params The parameters of the task
	 *
	 * @return A result, defined by the subclass of this task
	 * @throws Exception in case of any error
	 */
	protected abstract Result doTask (Params ... params) throws Exception;

	@Override
	protected void onProgressUpdate (Progress ... values) {
		if (callback instanceof ProgressCallback && !Utils.isEmpty( values )) {
			for (Progress p : values)
				((ProgressCallback<Result, Progress>)callback).onProgress( p );
		}
	}

	/**
     * Done
     */
    @Override
    protected void onPostExecute (Result result) {
        if (callback == null) return;

        if (error != null)
            callback.onFailure( error );
        else
            callback.onSuccess( result );

		if (doneCallback != null)
			doneCallback.onComplete( this );
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

	/**
	 * Callback method called when successful.
	 *
	 * @param result Result of type {@code <T>}
	 */
	public void onSuccess (Result result) {
	}

	/**
	 * Called in case of any error.
	 *
	 * @param e Throwable exception
	 */
	public void onFailure (Err e) {
	}

	/**
	 * Get the total time in milliseconds it took for the service to complete or fail.
	 * This method is not effective unless a subclass updates the {@link #millis} variable.
	 */
	public long getExecutionTime () {
		return millis;
	}
}
