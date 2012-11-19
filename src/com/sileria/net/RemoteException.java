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

package com.sileria.net;

import java.io.IOException;
import java.security.PrivilegedActionException;

/**
 * RemoteException is thrown by any unknown problems from the RemoteRequests.
 * It may or may not contain a wrapped exception which would be the actual
 * cause of an original exception.
 *
 * @author Ahmed Shakil
 * @date Mar 18, 2010
 */
public class RemoteException extends IOException {

    private int httpErrCode;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public RemoteException () {
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RemoteException (String message) {
        super( message );
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public RemoteException (String message, Throwable cause) {
        //super( message, cause ); // commented until 2.3 support.
		super( message );
		initCause( cause );
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param responseCode HTTP response code if part of error
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public RemoteException (int responseCode, Throwable cause) {
        this( cause == null ? null : cause.getMessage(), cause );
        this.httpErrCode = responseCode;
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param responseCode HTTP response code if part of error
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public RemoteException (String message, int responseCode, Throwable cause) {
        this( message, cause );
        this.httpErrCode = responseCode;
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param responseCode HTTP response code if part of error
     */
    public RemoteException (String message, int responseCode) {
        super( message );
        this.httpErrCode = responseCode;
    }

    /**
     * Constructs a new exception with the specified {@link HttpStatus} enum.
     *
     * @param status HttpStatus enum.
     */
    public RemoteException (HttpStatus status) {
		this( status.message, status.code );
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public RemoteException (Throwable cause) {
		this( cause == null ? null : cause.getMessage(), cause );
    }

    /**
     * Get the HTTP response code if any available or just returns <code>-1</code>
     * if no status was available.
     *
     * @return server response code.
     */
    public int getResponseCode () {
        return httpErrCode;
    }

	/**
	 * Return message with error code.
	 */
	@Override
	public String getLocalizedMessage () {
		return httpErrCode <= 0 ? super.getLocalizedMessage() : String.format( "%s (%d).", getMessage(), httpErrCode );
	}
}
