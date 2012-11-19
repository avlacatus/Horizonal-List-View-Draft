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
 * A convenience class that extends for {@link StringRequest} and
 * gives you unparsed data into the <code>String</code> format.
 * (Basicaly the class is same as StringRequest just saves you
 * headache of calling the {@linkplain #getRaw()} method.
 * <p/>
 * <strong>Note:</strong> Even though the class is a subclass of <code>ParsedRequest</code>
 * but providing a parser will not do anything and data will always be returned as Text.
 * <p/>
 * <strong>Example:</strong>
 * <blockquote><pre>
 *  public static Cancellable loadString (URL webserviceUrl, RemoteCallback callback) {
 *      return new RemoteTask(callback).execute( new TextRequest(webServiceUrl) );
 *  }
 * </pre></blockquote>
 *
 * If you are already in a background thread and want to use the the request directly:
 * 
 * <blockquote><pre>
 *      String s = new TextRequest (webServiceUrl).execute();
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date Feb 18, 2010
 */
public class TextRequest extends StringRequest<String> {

    /**
     * Constructor, default.
     */
    public TextRequest () {
    }

	/**
	 * Construct a remote request with the specified callback.
	 * @param callback a {@link RemoteCallback}
	 */
    public TextRequest (RemoteCallback<String> callback) {
        super( callback );
    }

	/**
	 * Construct a remote request with the specified url.
	 * @param url request url.
	 */
    public TextRequest (URL url) {
        super( url, null );
    }

	/**
	 * Construct a remote request with the specified url and callback.
	 * @param url request url.
	 * @param callback a {@link RemoteCallback}
	 */
    public TextRequest (URL url, RemoteCallback<String> callback) {
        super( url, callback );
    }

    /**
     * Wrap byte array method to convert into String object.
     *
     * @param data bytes received from the server
     * @return data of type <T>
     */
    @Override
    protected final String processData (ByteArrayOutputStream data) throws ParseException {
        try {
            return data.toString( enc );
        }
        catch (UnsupportedEncodingException e) {
            Log.e( "Unsupported encoding in processData()", e );
            throw new ParseException( "Unsupported encoding in processData()", e );
        }
    }
}