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

package com.sileria.util;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;


/**
 * Observable
 *
 * @author Ahmed Shakil
 * @date 08/11/2008
 *
 * @param <T> Type of the argument which the {@link Observer#observe(Object)} method takes.
 */

public class Observable<T> {

	private List<Observer<T>> observers;

	/**
	 * Construct an Observable with zero Observers.
	 */
	public Observable () {
		observers = Collections.synchronizedList( new ArrayList<Observer<T>>() );
	}

	/**
	 * Adds an observer to the set of observers for this object, provided
	 * that it is not the same as some observer already in the set.
	 * The order in which notifications will be delivered to multiple
	 * observers is not specified. See the class comment.
	 *
	 * @param   o   an observer to be added.
	 * @throws NullPointerException   if the parameter o is null.
	 */
	public synchronized void add (Observer<T> o) {
		if (o == null)
			throw new NullPointerException();

		if (!observers.contains(o)) {
			observers.add(o);
		}
	}

	/**
	 * Deletes an observer from the set of observers of this object.
	 * Passing <CODE>null</CODE> to this method will have no effect.
	 * @param   o   the observer to be deleted.
	 */
	public synchronized void remove (Observer o) {
		observers.remove(o);
	}

	/**
	 * Clears the observer list so that this object no longer has any observers.
	 */
	public synchronized void clear () {
		observers.clear();
	}

	/**
	 * If this object has changed, as indicated by the
	 * <code>hasChanged</code> method, then notify all of its observers
	 * and then call the <code>clearChanged</code> method to indicate
	 * that this object has no longer changed.
	 * <p/>
	 * Each observer has its <code>update</code> method called with two
	 * arguments: this observable object and the <code>arg</code> argument.
	 *
	 * @param   arg   any object.
	 * @see     Observer#observe(Object)
	 */
	public void notifyObservers (T arg) {

		if (observers.isEmpty()) return;

		 // a temporary snapshot of the state of current Observers.
		@SuppressWarnings( "unchecked" )
		Observer<T>[] obArray = new Observer[observers.size()];

		synchronized (this) {
			obArray = observers.toArray(obArray);
		}

		for (int i = obArray.length-1; i>=0; i--)
			obArray[i].observe(arg);
	}

}
