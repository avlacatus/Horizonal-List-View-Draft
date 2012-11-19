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

import com.sileria.util.LazyObject;

/**
 * Create an object from a class name.
 *
 * This class can be used to delay loading of the Class for the
 * instance to be created.
 *
 * The improved performance is at the cost of a slight performance
 * reduction the first time <code>get</code> (since Reflection APIs are used)
 *
 * By default, once an object is created its stored for future retreivals.
 *
 * This behavior can be changed by calling the alternate constructor
 * In that case it will create a new instance everytime <code>get</code>
 * is called at cost of slight performance reduction at each call.
 *
 * @author Ahmed Shakil
 * @date Oct 26, 2008
 *
 * @param <T> Return type of the <code>get</code> method.
 */

public class ReflectiveObject<T> extends LazyObject<T> {

	private final String classname;

	/**
	 * Construct a proxy for the specified <code>clazz</code>.
	 * @param classname Class to be generated.
	 */
	public ReflectiveObject (String classname) {
        this( classname, false );
	}

	/**
	 * Construct a proxy for the specified <code>clazz</code>.
     * @param classname Class to be generated.
	 * @param singleton Default behaviour of this proxy class.
	 */
	public ReflectiveObject (String classname, boolean singleton) {
        super( null, singleton );
        this.classname = classname;
	}

	/**
	 * Construct a proxy for the specified <code>clazz</code> with provided constructor arguments.
     * @param classname Class to be generated.
	 * @param singleton Default behaviour of this proxy class.
	 * @param args Constructor arguments
	 */
	public ReflectiveObject (String classname, boolean singleton, Object ... args) {
		super( null, singleton, args );
        this.classname = classname;
	}

    /**
     * Returns the class for the object to be instantiated.
     * @return the class for the object to be instantiated.
     */
    @SuppressWarnings ("unchecked")
    protected Class<T> getClazz () throws ClassNotFoundException {
        return (Class<T>) Class.forName( classname );
    }


}