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

import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * This class is used to dispatch an action from any {@link Command}
 * source to any zero-argument method on the target object.
 *
 * To use this class, you create an instance of it and pass the constructor
 * a target object and the name of the method to be called on the target.
 * When an event arrives, ReflectiveAction looks up the method and calls it.
 * <p>
 * Example: <br>
 * <blockqoute><pre>
 * open.setOnClickListener(new ReflectiveAction(this, "doOpen")); <br>
 *
 * // method that will be invoked on click.
 * public void doOpen () {
 * }
 * </pre></blockqoute>
 * </p>
 *
 * @author Ahmed Shakil
 * @date Jan 11, 2009
 * @version 1.0
 */

public class ReflectiveAction extends Command {
    
	protected String methodName;
	protected WeakReference<Object> target;
	protected Object param;
	protected Class<?> paramType;

	/**
	 * Constructor
	 * @param target instance of the target class
	 * @param methodName method name in that class
	 */
	public ReflectiveAction (Object target, String methodName) {
		this.target     = new WeakReference<Object>( target );
		this.methodName = methodName;
	}

	/**
	 * Constructor that allow one parameter to be passed to the invoked method.
	 * @param target instance of the target class
	 * @param methodName method name in that class
	 * @param param parameter to pass
	 */
	@SuppressWarnings( "unchecked" )
	public <P> ReflectiveAction (Object target, String methodName, P param) {
		this( target, methodName, param, param == null ? null : (Class<P>)param.getClass() );
	}

	/**
	 * Constructor that allow one parameter to be passed of specified <code>paramType</code> class.
	 * @param target instance of the target class
	 * @param methodName method name in that class
	 * @param param parameter to pass
	 */
	public <P> ReflectiveAction (Object target, String methodName, P param, Class<? super P> paramType) {
		this.target     = new WeakReference<Object>( target );
		this.methodName = methodName;
		this.param      = param;
		this.paramType  = paramType;
	}

    /**
	 * This method invokes the method in the target class after
	 * the action event is dispatched.
	 */
	public void run ()	{
		try {
			Object target = this.target.get();
			if (paramType != null) {
				Method method = target.getClass().getMethod( methodName, paramType );
				method.invoke( target, param );
			}
			else {
				Method method = target.getClass().getMethod( methodName );
				method.invoke( target );
			}
		}
		catch (Exception ex) {
			Log.e( Kit.TAG, ex.getLocalizedMessage(), ex );
		}
	}

}
