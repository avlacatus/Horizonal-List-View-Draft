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

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * Reflective proxy call class based on <code>Callable</code> and
 * <code>Callable</code> interfaces.
 *
 * @author Ahmed Shakil
 * @date Sep 18, 2007
 *
 * @param <T> Return type of the <code>call</code> method
 */

public class ReflectiveCallable<T> extends ReflectiveAction implements Callable<T> {

	/**
	 * Constructor specifying <b>target object</b> and the <b>method name</b>.
	 *
	 * @param target	 instance of the target class
	 * @param methodName method name in that class
	 */
	public ReflectiveCallable (Object target, String methodName) {
		super( target, methodName );
	}

	/**
	 * Constructor specifying <b>target object</b>, <b>method name</b> and the <b>action name</b>.
	 *
	 * @param target	 instance of the target class
	 * @param methodName method name in that class
	 * @param actionName action name
	 */
	public ReflectiveCallable (Object target, String methodName, String actionName) {
		super( target, methodName, actionName );
	}

	/**
	 * Computes a result, or throws a runtime exception if unable to do so.
	 * @return computed result
	 */
	@SuppressWarnings( "unchecked" )
	public T call () {
		try {
			Object target = this.target.get();
			Method method = target.getClass().getMethod( methodName );
			return (T)method.invoke(target);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e.getCause());
		}
		catch (Exception e) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * This method invokes the method in the target class after the action event is dispatched.
	 * <p/>
	 * <strong>Warning: </strong> Do not call this method directly.
	 * </p>
	 */
	public void run () {
		call();
	}

}