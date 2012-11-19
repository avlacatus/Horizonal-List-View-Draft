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

import java.io.*;
import java.net.*;

import com.sileria.util.IO;
import com.sileria.util.Utils;

/**
 * Default implementation of RemoteReader.
 *
 * @author Ahmed Shakil
 */
public class HttpReader implements RemoteReader {

    private HttpURLConnection conn;
    
	private RemoteWriter writer;

	private RequestMethod method;

	private String params;

    private int timeout;

	private boolean useCache;

    private int buffSize;

    private boolean read;

    private int responseCode = -1;
	
	private ConnectionInterceptor connInterceptor;

	/**
     * Constructor, default.
     */
    public HttpReader () {
    }

    /**
     * Constuctor specifing initial buffer size and http time out.
     * @param buffSize initial read buffer size
     * @param timeout timeout in milliseconds.
     */
    public HttpReader (int buffSize, int timeout, boolean useCache) {
        this.buffSize = buffSize;
        this.timeout = timeout;
		this.useCache = useCache;
    }

    /**
     * Read byte data from the http url connection.
     *
     * @throws java.io.IOException in case of error
     */
    public ByteArrayOutputStream readBytes (URL url) throws IOException {
        ByteArrayOutputStream data = null;
        InputStream in = null;

        responseCode = -1;       // reset before starting operation

        try {
			conn = createConnection( url );
			conn.setRequestMethod( method == null ? RequestMethod.GET.toString() : method.toString() );
			conn.setConnectTimeout( timeout );
			conn.setUseCaches( useCache );

			if (connInterceptor != null)
				connInterceptor.processConnection( conn );

			writeBytes( conn );

            in = new BufferedInputStream( conn.getInputStream() );
            data = readBytes( in );
        }
		catch (final IOException e) {
			try {
				if (conn != null) {
					responseCode = conn.getResponseCode();
					if (connInterceptor != null)
						connInterceptor.processIOException( conn );
				}
			}
			catch (final RemoteException rme) {
				throw rme;
			}
			catch (IOException ioe) { /* do nothing */ }
			throw e;
		}
        finally {
            try {
				if (conn != null && responseCode < 0) {
                	responseCode = conn.getResponseCode();
				}
            }
			catch (IOException ioe) { /* do nothing */ }
            finally {
				IO.close( in );
                close();
            }
        }

        return data;
    }

	/**
	 * Create URL connection.
	 * @throws java.io.IOException in case of malformed url exception
	 */
	protected HttpURLConnection createConnection (URL url) throws IOException {

		if ((method == null || method == RequestMethod.GET) && (url.getQuery() == null && params != null)) {
			String s = url.toExternalForm();
			if (s.endsWith( "/" ) && !s.endsWith( "//" ))
				s = s.substring( 0, s.length()-1 );
			url = new URL( s + (params.startsWith( "&" ) ? params : ("&" + params)) );
		}

		return (HttpURLConnection) url.openConnection();
	}

	/**
     * Read the locations string from a <code>InputStream</code>.
     *
     * @param in InputStream
     * @return Bytes read
     * @throws IOException in case of error
     */
    public ByteArrayOutputStream readBytes (InputStream in) throws IOException {

        ByteArrayOutputStream bos = null;
        read = true;

        if (in == null)
            return bos;

        int chunkSize = Utils.between( Math.min(32, buffSize), Math.max( 32, buffSize ), in.available() );

        bos = new ByteArrayOutputStream( chunkSize );
        int result = in.read();
        while (result != -1 && read) {
            byte b = (byte) result;
            bos.write( b );
            result = in.read();
        }

        return bos;
    }

	/**
	 * Write bytes to the url connection.
	 * 
	 * @throws IOException in case of any errors
	 */
	protected void writeBytes (HttpURLConnection conn) throws IOException {
		if (writer != null) {
			writeBytes( conn.getOutputStream() );
		}
		else if ((method == RequestMethod.POST || method == RequestMethod.PUT) && params != null) {
			if (Utils.isEmpty( conn.getRequestProperty( "Content-Type" ) ))
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", ""+params.length());

			conn.setDoInput( true );
			conn.setDoOutput( true );

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write( params );
			wr.flush();
		}
	}

	/**
	 * Write bytes to the output stream.
	 * Note: Only called if a writer is set
	 * @see #setWriter(com.sileria.net.RemoteWriter)
	 * @throws IOException in case of any errors
	 */
	protected void writeBytes (OutputStream out) throws IOException {
		if (writer != null)
			writer.writeData( out );
	}

    /**
     * Close any network connections.
     */
    public synchronized void close () throws IOException {
        if (conn != null) {
            conn.disconnect();
            conn = null;
        }
        read = false;
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening
     * a communications link to the resource referenced by this URLConnection.
     *
     * If the timeout expires before the connection can be established,
     * a java.net.SocketTimeoutException is raised. A timeout of zero is
     * interpreted as an infinite timeout.
     *
     * @param millis timeout value in milliseconds
     */
    public void setTimeout (int millis) {
        this.timeout = millis;
    }

    /**
     * Set the initial buffer size to read the remote bytes. 
     *
     * @param buffSize Initial read buffer size
     */
    public void setInitialReadSize (int buffSize) {
        this.buffSize = buffSize;
    }

    /**
     * Get the final response code if the data is all read; otherwise returns <code>-1</code>.
     */
    public int getResponseCode () {
        return responseCode;
    }

	/**
	 * Set a data writer.
	 * @param writer RemoteWriter
	 */
	public void setWriter (RemoteWriter writer) {
		this.writer = writer;
	}

	/**
	 * Set the request method. Only {@link RequestMethod#GET} (default)
	 * or {@link RequestMethod#POST} types are allowed.
	 * @param method {@link RequestMethod#GET} (default)
	 * or {@link RequestMethod#POST}
	 */
	public void setRequestMethod (RequestMethod method) {
		this.method = Utils.defaultIfNull( method, RequestMethod.GET );
	}

	/**
	 * Set the String with encoded parameters.
	 *
	 * @param params encoded parameter string
	 */
	public void setParamString (String params) {
		this.params = params;
	}

	/**
	 * Sets the value of the <code>useCaches</code> field of this
	 * <code>URLConnection</code> to the specified value.
	 *
	 * @param useCache a <code>boolean</code> indicating whether
	 * or not to allow caching
	 */
	public void setUseCache (boolean useCache) {
		this.useCache = useCache;
	}

	/**
	 * Allows you to intercept the HttpURLConnection before read and/or after io failure.
	 * @param interceptor ConnectionInterceptor object
	 */
	public void setConnectionInterceptor (ConnectionInterceptor interceptor) {
		this.connInterceptor = interceptor;
	}

	/**
	 * Handle any extra connection setup before the action I/O starts.
	 */
	public static interface ConnectionInterceptor {

		/**
		 * Prepare connection before the actual I/O e.g. header params.
		 * @param conn HttpURLConnection to be setup.
		 * @throws java.io.IOException in case of IO error or RemoteException
		 */
		void processConnection (HttpURLConnection conn) throws IOException;

		/**
		 * Lets you intercent the connection after an I/O failure.
		 * @param conn HttpURLConnection to process.
		 * @throws java.io.IOException in case of IO error or RemoteException
		 */
		void processIOException (HttpURLConnection conn) throws IOException;
	}
}
