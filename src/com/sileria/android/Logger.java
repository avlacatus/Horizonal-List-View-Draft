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

import com.sileria.util.Log;

/**
 * Android implementation for Log class.
 *
 * @author Ahmed Shakil
 * @date Feb 16, 2010
 */
public final class Logger extends Log {

	private final String tag;

	/**
	 * Constructor.
	 */
	public Logger (String tag) {
		this.tag = tag;
	}

	/**
	 * Send a DEBUG log message and log the exception.
	 */
	@Override
	protected final void debug (String msg, Throwable e) {
		if (e == null)
			android.util.Log.d( tag, msg );
		else
			android.util.Log.d( tag, msg, e );
	}

	/**
	 * Send a ERROR log message and log the exception.
	 */
	@Override
	protected final void error (String msg, Throwable e) {
		if (e == null)
			android.util.Log.e( tag, msg );
		else
			android.util.Log.e( tag, msg, e );
	}

	/**
	 * Send a INFO log message and log the exception.
	 */
	@Override
	protected final void info (String msg, Throwable e) {
		if (e == null)
			android.util.Log.i( tag, msg );
		else
			android.util.Log.i( tag, msg, e );
	}

	/**
	 * Send a VERBOSE log message and log the exception.
	 */
	@Override
	protected final void verbose (String msg, Throwable e) {
		if (e == null)
			android.util.Log.v( tag, msg );
		else
			android.util.Log.v( tag, msg, e );
	}

	/**
	 * Send a WARN log message and log the exception.
	 */
	@Override
	protected final void warn (String msg, Throwable e) {
		if (e == null)
			android.util.Log.w( tag, msg );
		else
			android.util.Log.w( tag, msg, e );
	}
}
