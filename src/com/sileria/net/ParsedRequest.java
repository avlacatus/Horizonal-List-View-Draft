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

import java.net.MalformedURLException;
import java.net.URL;

import com.sileria.util.*;

/**
 * An abstract implementation of the {@linkplain RemoteRequest} that allows users to provide a {@link DataParser}
 * which can parse the raw data into concrete business objects. e.g. converting raw json or xml into you model
 * object classes.
 * <p/>
 * Parser can be provided in form of a {@link DataParser} or a {@link ParserFactory}.
 * <p/>
 * Also this class allows you to pass in a URL in the constructor if you already have a concrete URL to your
 * webservice and want to save headache of overriding the {@link #prepareURL()} method.
 * <p/>
 * Two most common implementation that you would need to use are: {@link StringRequest} and {@link BinaryRequest}
 *
 * @author Ahmed Shakil
 * @date Mar 20, 2010
 *
 * @see DataParser
 * @see RemoteCallback
 *
 * @see BinaryRequest
 * @see StringRequest
 *
 * @param <T> parsed data type
 * @param <R> raw data type
 */
public abstract class ParsedRequest<T, R> extends RemoteRequest<T> {

    /**
     * Data parser.
     */
    protected DataParser<T, R> parser;

    /**
     * Server URL.
     */
    protected URL url;

    /**
     * Raw data received from the server.
     */
    private R raw;

    /**
     * Constructor, default.
     */
    protected ParsedRequest () {
    }

	/**
	 * Construct a remote request with the specified callback.
	 * @param callback a {@link RemoteCallback}
	 */
    protected ParsedRequest (RemoteCallback<T> callback) {
        super( callback );
    }

	/**
	 * Construct a remote request with the specified url and callback.
	 * @param url request url.
	 * @param callback a {@link RemoteCallback}
	 */
    protected ParsedRequest (URL url, RemoteCallback<T> callback) {
        super( callback );
		this.url = url;
    }

    /**
     * Set the data parser for this request.
     * @param parser <code>DataParser</code> object
     */
    public void setParser (DataParser<T, R> parser) {
        this.parser = parser;
    }

    /**
     * Set a <code>ParserFactory</code> and factory <code>token</code> class.
     * @param factory parser factory
     * @param token parser token
     */
    public void setParser (ParserFactory factory, Class token) {
        this.parser = new FactoryAdapter( factory, token );
    }

    /**
     * Set the URL to fetch the data from.
     * @param url server url
     */
    public void setURL (URL url) {
        this.url = url;
    }

    /**
     * Returns a String URL for requesting the data.
     *
     * @return server request url
     */
    @Override
    protected URL prepareURL () throws MalformedURLException {
        return url;
    }

    /**
     * Parse data object from the specified String data.
     *
     * @param data raw data received from the server
     * @return data of type <T>
     * @throws ParseException in case of parsing errors
     */
    protected T processData (R data) throws ParseException {
        raw = data;
        if (parser != null)
            return parser.parse( data );

        return null;
    }

	/**
	 * A convenience method to run request and return the result all from one method.
	 * @return data of type <T>
	 * @throws RemoteException any unknown exception will be wrapped into this exception
	 */
	public R executeRaw () throws RemoteException {
		start();
		return raw;
	}

    /**
     * Get the raw data received from the server.
     * @return raw data
     */
    public R getRaw () {
        return raw;
    }

    /**
     * <code>DataParser</code> wrapper around the </code>ParserFactory</code>
     */
    private final class FactoryAdapter implements DataParser<T, R> {

        /**
         * Parser Factory.
         */
        private ParserFactory factory;

        /**
         * Parser class token.
         */
        private Class<?> token;

        /**
         * Construct factory adapter with specified parser factory and parser token.
         * @param factory parser factory
         * @param token parser token
         */
        private FactoryAdapter (ParserFactory factory, Class<?> token) {
            this.token = token;
            this.factory = factory;
        }

        /**
         * Parse search result into object.
         *
         * @throws ParseException in case of any unexpected parsing problem
         */
        public T parse (R data) throws ParseException {
            DataParser<T, R> parser = factory.getDataParser( token );
            if (parser == null)
                throw new ParseException( "No parser found in ParserFactory for: " + token );

            return parser.parse( data );
        }
    }
}
