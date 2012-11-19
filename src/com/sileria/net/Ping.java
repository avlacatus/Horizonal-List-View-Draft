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
import java.net.InetAddress;
import java.net.Socket;

import com.sileria.util.IO;

/**
 * Ping class to detect your service running on a specific host and port.
 *
 * @author Ahmed Shakil
 * @author Hassan Jawed
 * @date Feb 21, 2012
 */
public class Ping implements Runnable {

	/**
	 * Ping status.
	 */
	public enum Status {

		/** Ping was never initiated. **/
		IDLE,

		/** Looking up host address. **/
		LOOKING,

		/** Trying to make socket connect. **/
		CONNECTING,

		/** Ping was successful. **/
		SUCCESS,

		/** Ping failed. **/
		FAILED
	}

    private String hostname;
    private String address;
    private int port;
    private Status status = Status.IDLE;

	private long millis = -1;
	private IOException error;

	/**
	 * Constructor, private.
	 */
    private Ping (String hostname, int port) {
        this.hostname = address = hostname;
        this.port     = port;
    }

	/**
	 * Try to connect to host and port in background thread.
	 */
    public void run() {

		Socket socket = null;

        try {
			final long stamp = System.currentTimeMillis();

			// Lookup host.
            status = Status.LOOKING;
            InetAddress addr = InetAddress.getByName(hostname);
			hostname = addr.getHostName();
			address = addr.getHostAddress();

			// Connect to host.
            status = Status.CONNECTING;
            socket = new Socket(addr, port);

			// Success status
			millis = System.currentTimeMillis() - stamp;
            status = Status.SUCCESS;

        }
		catch (IOException e) {
			// Error status
			status = Status.FAILED;
			error = e;
		}
		finally {
			IO.close( socket );
		}
    }

	/**
	 * Get hostname return by <code>InetAddress</code> after ping was successful.
	 * Before the ping was made this method will return the hostname provided to this class.
	 */
	public String getHostname () {
		return hostname;
	}

	/**
	 * Get host address return by <code>InetAddress</code> after ping was successful.
	 * Before the ping was made this method will return the hostname provided to this class.
	 */
	public String getHostAddress () {
		return address;
	}

	/**
	 * Get ping status.
	 */
	public Status getStatus () {
		return status;
	}

	/**
	 * Get IOException that was caught in case of error.
	 */
	public IOException getError () {
		return error;
	}

	/**
	 * Get milliseconds to ping.
	 */
	public long getTime () {
		return millis;
	}

	/**
	 * Static method to start the Ping thread and returns the Ping object after ping was success or failed or timed out.
	 *
	 * @param hostname host name or address
	 * @param port port on the host machine to connect to
	 * @param timeout timeout in milliseconds to abort the mission
	 *
	 * @return Ping object to get status and other data.
	 */
	public static Ping ping (String hostname, int port, long timeout) {
        Ping ping = new Ping( hostname, port );
        try {
            Thread t = new Thread( ping );
            t.setDaemon( true );
            t.start();
            t.join( timeout );
        }
		catch (InterruptedException ie) {
			// do nothing.
		}
        return ping;
    }
}
