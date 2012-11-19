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

package com.sileria.android.util;

import com.sileria.net.Ping;
import com.sileria.util.AsyncCallback;

/**
 * A background task class that finds a service in your local network running on a specific port.
 *
 * This class only finds the first host that it was able to ping successfully and returns.
 *
 * @author Ahmed Shakil
 * @author Hassan Jawed
 * @date Feb 21, 2012
 */
public class HostPinger extends AbstractTask<Void, Void, Ping> {

	private final String ip;
	private final int port;

	private final int timeout;

	/**
	 * Constructor specifying the actual callback.
	 * 
	 * @param ip Local host ip address, this ip address will be excluded from search
	 * @param port port to ping on each address
	 * @param timeout time for each ping, not for the full lookup time
	 * @param callback callback to get the result of failure
	 */
	public HostPinger (String ip, int port, int timeout, AsyncCallback<Ping> callback) {
		super( callback );
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
	}

	/**
	 * Find service in background thread.
	 */
	@Override
	protected Ping doTask (Void... params) throws Exception {

		if (ip == null || ip.length() == 0) return null;

		String[] arr = ip.split( "\\." );
		if (arr.length < 3) return null;

		String ip = String.format( "%s.%s.%s.",arr[0], arr[1], arr[2] );

		Ping ping  = null;
		for( int i = 0; i < 255; i++ ) {
			ping = Ping.ping( ip + i, port, timeout );
			if (ping != null && ping.getStatus() == Ping.Status.SUCCESS )
				break;
		}

		return ping;
	}

}
