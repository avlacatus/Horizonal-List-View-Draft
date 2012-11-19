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
 * An abstraction layer for logging mechanism to be shared between platforms.
 * e.g. if you are writing code to share between Android and other Java based
 * platforms then this class can be used as the base class to any logging
 * framework that exists on each of those platfoms. 
 *
 * @author Ahmed Shakil
 * @date Feb 16, 2009
 */
public abstract class Log {

    private static Log log = null;

	/**
	 * Set the global logger.
	 * @param logger a subclass of <code>Log</code> class.
	 */
	public static void setLogger (Log logger) {
		log = logger;
	}

    /**
     * Constructor, protected.
     */
    protected Log () {
        log = this;
    }

    /**
     * Send a DEBUG log message and log the exception.
     */
    public static void d (String msg, Throwable e) {
        if (log != null)
            log.debug( msg, e );
    }

    /**
     * Send a DEBUG log message.
     */
    public static void d (String msg) {
        if (log != null)
            log.debug( msg, null );
    }

    /**
     * Send a ERROR log message and log the exception.
     */
    public static void e (String msg, Throwable e) {
        if (log != null)
            log.error( msg, e );
    }

    /**
     * Send a ERROR log message.
     */
    public static void e (String msg) {
        if (log != null)
            log.error( msg, null );
    }

    /**
     * Send a INFO log message and log the exception.
     */
    public static void i (String msg, Throwable e) {
        if (log != null)
            log.info( msg, e );
    }

    /**
     * Send a INFO log message.
     */
    public static void i (String msg) {
        if (log != null)
            log.info( msg, null );
    }

    /**
     * Send a VERBOSE log message and log the exception.
     */
    public static void v (String msg, Throwable e) {
        if (log != null)
            log.verbose( msg, e );
    }

    /**
     * Send a VERBOSE log message.
     */
    public static void v (String msg) {
        if (log != null)
            log.verbose( msg, null );
    }

    /**
     * Send a WARN log message and log the exception.
     */
    public static void w (String msg, Throwable e) {
        if (log != null)
            log.warn( msg, e );
    }

    /**
     * Send a WARN log message.
     */
    public static void w (String msg) {
        if (log != null)
            log.warn( msg, null );
    }

    /**
     * Send a DEBUG log message and log the exception.
     */
    protected abstract void debug (String msg, Throwable e);

    /**
     * Send a ERROR log message and log the exception.
     */
    protected abstract void error (String msg, Throwable e);

    /**
     * Send a INFO log message and log the exception.
     */
    protected abstract void info (String msg, Throwable e);

    /**
     * Send a VERBOSE log message and log the exception.
     */
    protected abstract void verbose (String msg, Throwable e);

    /**
     * Send a WARN log message and log the exception.
     */
    protected abstract void warn (String msg, Throwable e);

}