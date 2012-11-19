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

/**
 * Observer
 *
 * @author Ahmed Shakil
 * @date 08/11/2008
 *
 * @param <T> Type of the argument which the {@link #observe(Object)} method takes.
 */

public interface Observer<T> {

	/**
	 * This method is called whenever the observed object is changed.
	 * An application calls an <tt>Observable</tt> object's
	 * <code>notifyObservers</code> method to have all the object's
	 * observers notified of the change.
	 *
	 * @param   arg   an argument passed to the <code>notifyObservers</code> method.
	 */
	void observe (T arg);

}
