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

import java.util.*;
import java.util.concurrent.*;

import com.sileria.util.*;

/**
 * SerialTask executes a collection of <code>Callable</code>s in a serial
 * order. If any fails then the rest of the calls are not executed and the
 * task is terminated.
 *
 * @author Ahmed Shakil
 * @date Jul 1, 2012
 */
public class SerialTask extends AbstractTask<Callable<?>, Content<?,?>, List<?>> {

	private Object[] result;

	private final ExecutorService worker = Executors.newSingleThreadExecutor();

	/**
	 * Constructor default.
	 */
	public SerialTask () {
	}

	/**
	 * Constructor with AsyncCallback
	 * @param callback callback to observer success or failure
	 */
	public SerialTask (AsyncCallback<List<?>> callback) {
		super( callback );
	}

	/**
	 * Constructor with TaskCallback
	 * @param callback callback to observer success, failure and progress.
	 */
	public SerialTask (ProgressCallback<List<?>, Content<Object, ?>> callback) {
		super( callback );
	}

	/**
	 * Call the callables in serial order.
	 * @param calls collection of callable where the actual task will be performed in the <code>call</code> method
	 * @return list of results returned from the call methods.
	 * @throws Exception in case of any error
	 */
	@Override
	protected List<?> doTask (Callable<?>... calls) throws Exception {

		if (Utils.isEmpty( calls )) return Collections.emptyList();

		result = new Object[calls.length];

		for (int i = 0, count = calls.length; i < count; i++) {
			
			Callable<?> call = calls[i];                // get the first call
			result[i] = worker.submit( call ).get();    // call and get data

			if (callback instanceof ProgressCallback)       // publish content if anyone listening
				publishProgress( new Content(result[i], i) );
		}

		worker.shutdown();

		return Arrays.asList( result );
	}

	/**
	 * Get result for specifed index representing the index order
	 * in which the callables where provided to the execute method.
	 * @param i index
	 * @return Resulting object if it has been executed otherwise null
	 */
	public Object get (int i) {
		return result == null ? null : result[i];
	}
}
