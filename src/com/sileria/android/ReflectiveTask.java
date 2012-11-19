/*
 * Copyright (c) 2001 - 2012 Sileria, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.sileria.android;

import android.os.AsyncTask;
import com.sileria.util.Cancellable;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

/**
 * An implementation of <code>AsyncTask</code> class using reflection
 * can be used if you have a lot of classes extending from AsyncTask
 * and you want to either not write the boiler-plate code everytime or
 * you want to simply cut down the number of classes you create.
 * <p/>
 * To use this class, you create an instance of it and pass the constructor
 * a target object and the name of the construct method and the finish method.
 * <p/>
 * Construct method <b>should</b> return an Object, otherwise the
 * <code>getValue()</code> method will return <code>null</code> if
 * this is a void method. <br>
 * Finish method <b>must</b> a single Object as an argument
 * <p/>
 * <i>Example: </i><br>
 * <blockquote><pre>
 * new ReflectiveTask( this, "onDataLoad", "onDataLoaded").execute();
 *
 * // load in background.
 * public void onDataLoad () {
 *     DataStore.getInstance().loadData();
 * }
 *
 * // loading done callback.
 * public void onDataLoaded () {
 * }
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @version 2.0
 * @date Aug 28, 2010
 *
 * @param <T> the result type returned by this {@code AsyncTask's}
 * {@code doInBackground} and {@code get} methods
 */
public class ReflectiveTask<T> implements Cancellable {

	private String workMethod;
	private String doneMethod;
	private WeakReference<Object> target;
	private Class<T> paramType;

	private Servant task;
	
	/**
	 * Constructor specifying the construct and finish methods in the target object
	 *
	 * @param target	 instance of the target class
	 * @param workMethod the doInBackground method
	 * @param doneMethod done method
	 */
	public ReflectiveTask (Object target, String workMethod, String doneMethod) {
		this.target = new WeakReference<Object>( target );
		this.workMethod = workMethod;
		this.doneMethod = doneMethod;
	}

	/**
	 * Constructor specifying the construct method in the target object
	 *
	 * @param target	 instance of the target class
	 * @param workMethod the doInBackground method
	 */
	public ReflectiveTask (Object target, String workMethod) {
		this.target = new WeakReference<Object>( target );
		this.workMethod = workMethod;
	}

	/**
	 * Constructor specifying the target object with specified method names and paramType signature
	 *
	 * @param target	 instance of the target class
	 * @param workMethod the doInBackground method
	 * @param doneMethod done method
	 * @param paramType  parameter type the finish method will take as argument
	 */
	public ReflectiveTask (Object target, String workMethod, String doneMethod, Class<T> paramType) {
		this.target = new WeakReference<Object>( target );
		this.workMethod = workMethod;
		this.doneMethod = doneMethod;
		this.paramType = paramType;
	}

	/**
	 * Starts the execution of the task in the background.
	 * The task returns itself (this) so that the caller can keep a reference to it.
	 * This method must be invoked on the UI thread.
	 */
	public ReflectiveTask execute () {
		(task = new Servant()).execute();
		return this;
	}

	/**
	 * @inheritDoc
	 */
	public void cancel () {
		if (task != null)
			task.cancel( true );
		task = null;
	}

	/**
	 * Swing Worker
	 */
	private class Servant extends AsyncTask<Void, Void, T> {
		/**
		 * Called on the event dispatching thread (not on the worker thread)
		 * after the <code>construct</code> method has returned.
		 */
		protected void onPostExecute(T result) {
			task = null;
			if (doneMethod == null || isCancelled()) return;

			try {
				Object target = ReflectiveTask.this.target.get();
				if (paramType != null) {
					Method method = target.getClass().getMethod( doneMethod, paramType );
					method.invoke( target, get() );
				}
				else {
					Method method = target.getClass().getMethod( doneMethod );
					method.invoke( target );
				}
			}
			catch (InvocationTargetException e) {
				throw new RuntimeException( e.getCause() );
			}
			catch (NoSuchMethodException e) {
				throw new RuntimeException( e );
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException( e );
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Compute the value to be returned by the <code>get</code> method.
		 * This method is called on the worker thread
		 */
		@Override @SuppressWarnings("unchecked")
		protected T doInBackground (Void... voids) {
			try {
				Object target = ReflectiveTask.this.target.get();
				Method method = target.getClass().getMethod( workMethod );
				return (T)method.invoke( target );
			}
			catch (InvocationTargetException e) {
				throw new RuntimeException( e.getCause() );
			}
			catch (NoSuchMethodException e) {
				throw new RuntimeException( e );
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException( e );
			}
		}

	}
}