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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * LazyObject.
 *
 * @author Ahmed Shakil
 * @date Mar 19, 2010
 */
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

public class LazyObject<T> implements Callable<T> {

	private Class<T> clazz;
	private boolean singleton;
	private T object;

	private Class<?>[] paramTypes;
	private Object[] arguments;

	/**
	 * Construct a proxy for the specified <code>clazz</code>.
	 * @param clazz Class to be generated.
	 */
	public LazyObject (Class<T> clazz) {
		this( clazz, false );
	}

	/**
	 * Construct a proxy for the specified <code>clazz</code>.
	 * @param clazz Class to be generated.
	 * @param singleton Default behaviour of this proxy class.
	 */
	public LazyObject (Class<T> clazz, boolean singleton) {
		this.clazz = clazz;
		this.singleton = singleton;
	}

	/**
	 * Construct a proxy for the specified <code>clazz</code> with provided constructor arguments.
	 * @param clazz Class to be generated.
	 * @param singleton Default behaviour of this proxy class.
	 * @param args Constructor arguments
	 */
	public LazyObject (Class<T> clazz, boolean singleton, Object ... args) {
		this( clazz, singleton );

		if (args != null && args.length > 0)
			this.arguments = args;
	}

	/**
	 * Set the parameter types for the reflective constructor which will be called.
	 * @param paramTypes     parameter type of the arguments that the constructor takes.
	 */
	public void setParameterTypes (Class<?> ... paramTypes) {
		this.paramTypes = paramTypes;
	}

	/**
	 * Get a new instance of the object on every call
	 * regardless of default behaviour.
	 *
	 * @return instance of type <T>
	 */
	public T newInstance () {
		return newInstance( arguments );
	}

	/**
	 * Get a new instance of the object on every call
	 * regardless of default behaviour.
	 *
	 * @param args constructor arguments
	 *
	 * @return instance of type <T>
	 */
	public T newInstance (Object ... args) {
		try {

            if (clazz == null)
                clazz = getClazz();

			if (args == null)
                return clazz.newInstance();

			Class<?>[] paramTypes;
			if (this.paramTypes != null) {
				paramTypes = this.paramTypes;
			}
			else {
				paramTypes = new Class[args.length];
				for (int i=0; i<args.length; i++)
					paramTypes[i] = args[i].getClass();
			}

            Constructor<T> c = clazz.getConstructor( paramTypes );
            return c.newInstance( args );

		}
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

    /**
     * Returns the class for the object to be instantiated.
     * @return the class for the object to be instantiated.
     * @throws ClassNotFoundException if the class cannot be located
     */
    protected Class<T> getClazz () throws ClassNotFoundException {
        return clazz;
    }

	/**
	 * Get the singleton instance of the object
	 * regardless of default behaviour.
	 *
	 * @return instance of type <T>
	 */
	public T singleton () {
		if (object == null)
			object = newInstance();

		return object;

	}

	/**
	 * Gets instance of type <T>.
	 * Whether the method returns a new instance or a singleton instance
	 * depends on which constructor was used of this class
	 *
	 * @return Instance of type <T>
	 *
	 * @see #LazyObject(Class)
	 * @see #LazyObject(Class, boolean)
	 * @see #newInstance()
	 * @see #singleton()
	 */
	public T get () {
		return singleton ? singleton() : newInstance();

	}

	/**
	 * Same as {@link #get()}
	 * @see #get()
	 */
	public T call () {
		return get();
	}



}