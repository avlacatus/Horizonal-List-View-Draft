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

import java.net.URL;

/**
 * A WebService request that retrieves the requested data into a byte array.
 * <p/>
 * The retrieved data can be then parsed into desired object by providing a parser to this class
 * or the raw byte[] array can be retrieved by calling {@link #getRaw()}.
 * <p/>
 *
 * <strong>Example:</strong>
 * <blockquote><pre>
 *  public static Cancellable loadImage (URL webserviceUrl, RemoteCallback callback) {
 *      BinaryRequest req = new BinaryRequest (webServiceUrl);
 *      req.setParser(new DrawableParser());   // parser that will convert bytes into a Drawable image.
 *      return new RemoteTask(callback).execute( req );
 *  }
 * </pre></blockquote>
 * <p/>
 * Parser can be provided in form of a {@link com.sileria.util.DataParser} or a {@link com.sileria.util.ParserFactory}.
 *
 * @author Ahmed Shakil
 * @date Mar 18, 2010
 *
 * @param <T> parsed data type
 */
public abstract class BinaryRequest<T> extends ParsedRequest<T, byte[]> {

    /**
     * Constructor, default.
     */
    public BinaryRequest () {
    }

	/**
	 * Construct a remote request with the specified callback.
	 * @param callback a {@link RemoteCallback}
	 */
    public BinaryRequest (RemoteCallback<T> callback) {
        super( callback );
    }

	/**
	 * Construct a remote request with the specified url.
	 * @param url request url.
	 */
    public BinaryRequest (URL url) {
        super( url, null );
    }

	/**
	 * Construct a remote request with the specified url and callback.
	 * @param url request url.
	 * @param callback a {@link RemoteCallback}
	 */
    public BinaryRequest (URL url, RemoteCallback<T> callback) {
        super( url, callback );
    }
}