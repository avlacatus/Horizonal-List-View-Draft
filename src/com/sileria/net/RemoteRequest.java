/*
 * Copyright (c) 2003 - 2012 Sileria, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.sileria.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

import com.sileria.util.*;

/**
 * RemoteRequest is the base-class for all remote web service calls.
 * <p/>
 * Override {@link #processData} and {@link #prepareURL()} to create your own
 * implementation of the <code>RemoteRequest</code> class. This class will provide
 * all the basic HTTP reading features.
 * <p/>
 * You can also see {@link ParsedRequest} or any of it's subclasses on how this class
 * is extended and make usable.
 * <p/>
 * Provide timeout, data buffer size and callbacks to this class to utilized the class for you own custom use.
 * <p/>
 * <strong>NOTE</strong>: This class is not run in background thread and you should always wrap it inside
 * a {@link RemoteExecutor} to be able to run as a background task.
 * <p/>
 * <strong>How to wrap inside a RemoteExecutor</strong>
 * <blockquote><pre>
 *      return new RemoteTask(callback).execute( new MyRemoteRequest() );
 * </pre></blockquote>
 *
 * <strong>ALSO NOTE</strong>: This class implements the {@link Cancellable} interface;
 * meaning that all these request can be cancelled if the user hits the back
 * or cancel button or once the application shuts down. (If the class is wrapped
 * inside a {@linkplain RemoteExecutor} then call the {@linkplain com.sileria.net.RemoteExecutor#cancel()} instead.
 *
 * @see ParsedRequest
 * @see StringRequest
 * @see BinaryRequest
 *
 * @author Ahmed Shakil
 * @date Apr 29, 2009
 * 
 * @param <T> Data type that this class retrieves and returns
 */
public abstract class RemoteRequest<T> implements Cancellable, HttpReader.ConnectionInterceptor {

    private RemoteReader reader;

	private RemoteWriter writer;

    private RemoteCallback<T> callback;

	private RequestMethod method = DEF_REQ_METHOD;

    private boolean cancelled;

	private Object requestor;

    private T data;

	private RemoteException error;

	private int responseCode = -1;

	protected int timeout = DEF_TIMEOUT;

    protected int buffSize = DEF_BUFF_SIZE;

	protected boolean useCache = DEF_USE_CACHE;

	protected HttpReader.ConnectionInterceptor connInterceptor = DEF_CONN_INTERCEPTOR;

    private static RequestMethod DEF_REQ_METHOD = RequestMethod.GET;
    private static int DEF_BUFF_SIZE            = 5120;
    private static int DEF_TIMEOUT              = 0;
    private static boolean DEF_USE_CACHE        = false;

	private static HttpReader.ConnectionInterceptor DEF_CONN_INTERCEPTOR;

	/**
	 * Set the default initial read size for all requests.
	 * @param buffSize Initial read buffer size
	 * @see #setInitialReadSize(int) 
	 */
	public static void setDefaultReadSize (int buffSize) {
		DEF_BUFF_SIZE = buffSize;
	}

	/**
	 * Set the default connection timeout for all requests.
	 * @param millis timeout value in milliseconds
	 */
	public static void setDefaultTimeout (int millis) {
		if (millis < 0)
			throw new IllegalArgumentException("timeout can not be negative");
		
		DEF_TIMEOUT = millis;
	}

	/**
	 * Set the default cache usage setting for all requests.
	 *
	 * @param useCache a <code>boolean</code> indicating whether
	 * or not to allow caching
	 */
	public static void setDefaultUseCache (boolean useCache) {
		DEF_USE_CACHE = useCache;
	}

	/**
	 * Set the default connection interceptor.
	 * @param connInterceptor global connection interceptor.
	 */
	public static void setDefaultConnectionInterceptor (HttpReader.ConnectionInterceptor connInterceptor) {
		DEF_CONN_INTERCEPTOR = connInterceptor;
	}

	/**
	 * Set the default request method to specified method for all remote requests.
	 *
	 * Only {@link RequestMethod#GET} (default)
	 * or {@link RequestMethod#POST} types are allowed.
	 *
	 * @param method {@link RequestMethod#GET} (default) or {@link RequestMethod#POST}
	 */
	public static void setDefaultRequestMethod (RequestMethod method) {
		DEF_REQ_METHOD = Utils.defaultIfNull( method, RequestMethod.GET );
	}

    /**
     * Constructor, default.
     */
    protected RemoteRequest () {
    }

    /**
     * Construct a remote request with the specified callback.
	 * @param callback a {@link RemoteCallback}
     */
    protected RemoteRequest (RemoteCallback<T> callback) {
        this.callback = callback;
    }

    /**
     * Initialize method; called just before the network request is initiated.
     */
    protected void init () {
    }

    /**
     * Post execute method; called just after the network call is done and succesful.
	 * NOTE: This method will not be called in case of errors.
	 * <p/>
	 * You can use {@link #getResponseCode()} to check status here.
     */
    protected void done () {
    }

    /**
     * Fail method; called just after the network call is done with errors.
	 * <p/>
	 * You can use {@link #getResponseCode()} to check status here.
	 *
	 * @return <code>true</code> if handling the error so it is not thrown.
     */
    protected boolean fail () {
		return false;
    }

    /**
     * Final location complete call.
     */
    protected final void setSuccess (T result) {
        data = result;
        if (callback != null)
            callback.onSuccess( result );
    }

    /**
     * Set the failure state.
	 * @return true if failure handled; otherwise <code>false</code> which will then throw an exception.
     */
    protected final boolean setFailure (final RemoteException e) {
		error = e;
        cancel();

        if (callback != null) {
            callback.onFailure( e );
			return true;
		}

		return false;
    }

    /**
     * Get data once it has been retreived by a {@link #start()} or {@link #request()} call.
     * <p/>
     * Note: This will be <code>null</code> if the data was not processed
     * by this {@linkplain #processData} method.
     *
     * @return data of type <T>
     */
    public T get () {
        return data;
    }

    /**
     * A convenience method to run request and return the result all from one method.
     * @return data of type <T>
	 * @throws RemoteException any unknown exception will be wrapped into this exception
     */
    public T execute () throws RemoteException {
        start();
        return data;
    }

	/**
	 * Get error that may have occured during the remote call. <code>null</code> when no errors.
	 * @return error if any
	 */
	public RemoteException error () {
		return error;
	}

	/**
	 * Gets the status code from an HTTP response message.
	 * For example, in the case of the following status lines:
	 * <PRE>
	 * HTTP/1.0 200 OK
	 * HTTP/1.0 401 Unauthorized
	 * </PRE>
	 * It will return 200 and 401 respectively.
	 * Returns -1 if no code can be discerned
	 * from the response (i.e., the response is not valid HTTP).
	 * @return the HTTP Status-Code, or -1
	 */
	public int getResponseCode () {
		return responseCode;
	}

	/**
     * Set the callback listener.
     * @param callback listener
     */
    public void setCallback (RemoteCallback<T> callback) {
        this.callback = callback;
    }

	/**
	 * Set the request method. Only {@link RequestMethod#GET} (default)
	 * or {@link RequestMethod#POST} types are allowed.
	 * @param method {@link RequestMethod#GET} (default)
	 * or {@link RequestMethod#POST}
	 */
	public void setRequestMethod (RequestMethod method) {
		this.method = Utils.defaultIfNull( method, DEF_REQ_METHOD );
	}

	/**
	 * Allows you to intercept the HttpURLConnection before read and/or after io failure.
	 * @param interceptor ConnectionInterceptor object
	 */
	public void setConnectionInterceptor (HttpReader.ConnectionInterceptor interceptor) {
		this.connInterceptor = interceptor;
	}

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening
     * a communications link to the resource referenced by this URLConnection.
     * <p/>
     * If the timeout expires before the connection can be established,
     * a java.net.SocketTimeoutException is raised. A timeout of zero is
     * interpreted as an infinite timeout.
     * <p/>
     * Note: This parameter will only take affect if default {@link HttpReader} or
     * one if it's subclasses are used as the <code>RemoteReader</code>. If you provide
     * your own implementation of <code>RemoteReader</code> set the parameters directly
     * into that custom class.
     * <p/>
     * @param millis timeout value in milliseconds
     */
    public void setTimeout (int millis) {
		if (millis < 0)
			throw new IllegalArgumentException("timeout can not be negative");
        this.timeout = millis;
    }

    /**
     * Set the initial buffer size to read the remote bytes. By default
     * the size is specified by {@link #DEF_BUFF_SIZE}.
     * <p/>
     * Note: This parameter will only take affect if default {@link HttpReader} or
     * one if it's subclasses are used as the <code>RemoteReader</code>. If you provide
     * your own implementation of <code>RemoteReader</code> set the parameters directly
     * into that custom class.
     *
     * @param buffSize Initial read buffer size
     */
    public void setInitialReadSize (int buffSize) {
        this.buffSize = buffSize;
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
     * Get the object that requested this request.
     *
     * @return Requestor source object. This may be <code>null</code>
     */
    public Object getRequestor () {
        return requestor;
    }

    /**
     * Set the source object that initiated this request.
     *
     * @param requestor source object. This may be <code>null</code>
     */
    public void setRequestor (Object requestor) {
        this.requestor = requestor;
    }

    /**
     * Start the remote remote request.
	 * @throws RemoteException any unknown exception will be wrapped into this exception
     */
    public void start () throws RemoteException {
        request();
    }

    /**
     * final run method to call the fetch in a subclass and catch any thrown exception.
	 * @throws RemoteException any unknown exception will be wrapped into this exception
     */
    private void request () throws RemoteException {
        try {
			init();          // init
            doRequest();     // request
			done();          // done
        }
        catch (Throwable t) {
			// throw if neither one handles it.
			final RemoteException e = prepareException( t );
			if (!(setFailure( e ) | fail()))
				throw e;
        }
    }

	/**
	 * Prepare the final exception before throwing it. Override this method to throw your own exception.
	 */
	protected RemoteException prepareException (Throwable t) {
		return t instanceof RemoteException ? (RemoteException)t : new RemoteException( t );
	}

    /**
     * Read the data from the URL.
     *
     * @throws RemoteException any unknown exception will be wrapped into this exception
     * @throws ParseException in case of data parsing problems
     */
    protected void doRequest () throws RemoteException, ParseException {
        
        URL url;

        try {
            url = prepareURL();
        }
        catch (MalformedURLException e) {
            Log.e( "Malformed URL", e);
            throw new RemoteException( e );
        }

        // check for invalid url
        if (url == null)
            throw new RemoteException( getClass().getName() + ".prepareURL() returned an invalid URL: " + url );

        // read data into a byte array stream
        ByteArrayOutputStream data;
        try {
			data = readData( url );
			responseCode = (reader instanceof HttpReader) ? ((HttpReader)reader).getResponseCode() : -1;
        }
        catch (RemoteException e) {
			if ((responseCode = e.getResponseCode()) <= 0)
				responseCode = (reader instanceof HttpReader) ? ((HttpReader)reader).getResponseCode() : -1;
			throw e;
		}
        catch (IOException e) {
			responseCode = (reader instanceof HttpReader) ? ((HttpReader)reader).getResponseCode() : -1;
            Log.e( e.getLocalizedMessage(), e);
            throw new RemoteException( e.getLocalizedMessage(), responseCode, e );
        }

        // everything went smooth
        setSuccess( data == null ? null : processData( data ) );
    }

    /**
     * Process the byte data stream and convert to object of type <T>.
     * @throws RemoteException thrown in case of network problems like wrong response code returned
     * @throws ParseException thrown in case of data parsing problems
     */
    protected T processData (ByteArrayOutputStream data) throws ParseException, RemoteException {
        return processData( data.toByteArray() );
    }

    /**
     * Returns a String URL for requesting the data.
     *
     * @return server request url
     * @throws MalformedURLException  if an unknown protocol is specified.
     */
    protected abstract URL prepareURL () throws MalformedURLException;

    /**
     * Parse data object from the specified bytes.
     *
     * @param data bytes received from the server
     * @return data of type <T>
     * @throws ParseException in case of parsing problems
     */
    protected abstract T processData (byte[] data) throws ParseException;

	/**
	 * Prepare connection before the actual I/O e.g. header params.
	 * @param conn HttpURLConnection to be setup.
	 */
	public void processConnection (HttpURLConnection conn) throws IOException {
	}

	/**
	 * Lets you intercent the connection after an I/O failure.
	 * Note: If you need to check response code, query the HttpURLConnection directly
	 * for it. The RemoteRequest#responseCode is not available at this point.
	 * @param conn HttpURLConnection to process.
	 */
	public void processIOException (HttpURLConnection conn) throws IOException {
	}

	/**
	 * Returns a String with encoded parameters.
	 *
	 * @return encoded parameter string
	 */
	protected String getParamString () {
		return null;
	}

    /**
     * Stop the receiving thread
     */
    public void cancel () {
        cancelled = true;
		IO.close( reader );
    }

    /**
     * Checks on both side if the task was cancelled by the user or by this request.
     *
     * @return <code>true</code> if the task was cancelled; otherwise <code>false</code>
     */
    protected boolean isCancelled () {
        return cancelled;
    }

    /**
     * Set your own byte reader here.
     * @param reader RemoteReader
     */
    public void setReader (RemoteReader reader) {
        this.reader = reader;
    }

	/**
	 * Set a data writer.
	 * @param writer RemoteWriter
	 */
	public void setWriter (RemoteWriter writer) {
		this.writer = writer;
	}

    /**
     * Read byte data from the http url connection.
     *
     * @throws java.io.IOException in case of error
     */
    protected ByteArrayOutputStream readData (URL url) throws IOException {
        if (reader == null)
            reader = new HttpReader();

        if (reader instanceof HttpReader) {
            ((HttpReader)reader).setWriter( writer );
            ((HttpReader)reader).setRequestMethod( method );
            ((HttpReader)reader).setParamString( getParamString() );
            ((HttpReader)reader).setUseCache( useCache );
			((HttpReader)reader).setTimeout( timeout );
			((HttpReader)reader).setInitialReadSize( buffSize );
			((HttpReader)reader).setConnectionInterceptor( Utils.defaultIfNull( connInterceptor, this ) );
        }

        return reader.readBytes( url );
    }

}
