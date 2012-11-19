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

import com.sileria.util.*;

/**
 * Abstract base adpater class to make AsyncTask work with {@link com.sileria.util.AsyncCallback}.
 *
 * Note: Instead of overriding {@linkplain #doInBackground(Object[])} method you will have to implement
 * {@link #doTask(Object[])} method if you wa
 *
 * @see RemoteTask
 *
 * @author Ahmed Shakil
 * @date Mar 20, 2010
 */
public abstract class AbstractTask<Params, Progress, Result>
        extends AbstractWorker<Params, Progress, Result, Throwable>
        implements Cancellable, AsyncCallback<Result> {

	/**
     * Constructor, default.
	 * NOTE: this contructor will set itself as the callback.
	 * Unless you specify the callback instance in the constructor or setter method.
     */
    protected AbstractTask () {
    }

    /**
     * Constructor specifying the actuall callback.
     */
    protected AbstractTask (AsyncCallback<Result> callback) {
		super( callback );
    }

    /**
     * Constructor specifying the actuall callback.
     */
    protected AbstractTask (ProgressCallback<Result, Progress> callback) {
		super( callback );
    }

	/**
	 * Set the callback listener.
	 */
	public void setCallback (AsyncCallback<Result> callback) {
		this.callback = callback;
	}

	/**
	 * Set the callback listener.
	 */
	public void setCallback (ProgressCallback<Result, Progress> callback) {
		this.callback = callback;
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

	/**
	 * Sorry! Final method. Override {@link #doTask(Object[])} method instead.
	 */
	@Override
	protected final Result doInBackground (Params... params) {
		long start = SystemClock.uptimeMillis();
		Result result = null;
		try {
			result = doTask( params );
		}
		catch (Throwable e) {
			error = e;
		}
		finally {
			millis = SystemClock.uptimeMillis() - start;
		}

		return result;
	}

}
