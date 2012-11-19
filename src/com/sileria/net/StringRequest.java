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
import java.io.UnsupportedEncodingException;
import java.net.URL;

import com.sileria.util.Log;
import com.sileria.util.ParseException;

/**
 * A WebService request that loads the requested data into a String object.
 * <p/>
 * The retrieved String can be then parsed into desired object by providing a parser to this class
 * or that String can be retrieved by calling {@link #getRaw()}.
 * <p/>
 *
 * <strong>Example:</strong>
 * <blockquote><pre>
 *  public static Cancellable loadCities (URL webserviceUrl, RemoteCallback callback) {
 *      StringRequest req = new StringRequest (webServiceUrl);
 *      req.setParser(new LocationParser());
 *      return new RemoteTask(callback).execute( req );
 *  }
 * </pre></blockquote>
 * <p/>
 * Parser can be provided in form of a {@link com.sileria.util.DataParser} or a {@link com.sileria.util.ParserFactory}.
 *
 * @author Ahmed Shakil
 * @date Feb 18, 2010
 *
 * @param <T> parsed data type
 */
public class StringRequest<T> extends ParsedRequest<T, String> {

    protected String enc = ENC;

    private final static String ENC = "UTF-8";

    /**
     * Constructor, default.
     */
    public StringRequest () {
    }

	/**
	 * Construct a remote request with the specified callback.
	 * @param callback a {@link RemoteCallback}
	 */
    public StringRequest (RemoteCallback<T> callback) {
        super( callback );
    }

	/**
	 * Construct a remote request with the specified url address.
	 * @param url request url.
	 */
	public StringRequest (URL url) {
		super( url, null );
	}

	/**
	 * Construct a remote request with the specified url and callback.
	 * @param url request url.
	 * @param callback a {@link RemoteCallback}
	 */
	public StringRequest (URL url, RemoteCallback<T> callback) {
		super( url, callback );
	}

	/**
     * Set th charset encoding name.
     * @param enc charset name
     */
    public void setCharsetName (String enc) {
        this.enc = enc;
    }

    /**
     * Wrap byte array method to convert into String object.
     *
     * @param data bytes received from the server
     * @return data of type <T>
     */
    @Override
    protected T processData (ByteArrayOutputStream data) throws ParseException, RemoteException {
        try {
            return processData( data.toString( enc ) );
        }
        catch (UnsupportedEncodingException e) {
            Log.e( "Unsupported encoding in processData()", e );
            throw new ParseException( "Unsupported encoding in processData()", e );
        }
    }

    /**
     * Wrapped to make it unextendible.
     */
    @Override
    protected final T processData (byte[] data) throws ParseException {
        throw new UnsupportedOperationException( "StringRequest.processData( byte[] ) method should never be called.");
    }

}
