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

import com.sileria.util.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * VoidRequest; sends a request but does not read response.
 * <p/>
 * Use this request when only a request needs to be sent to the server and no data is needed to be retreived back.
 *
 * @author Ahmed Shakil
 * @date Jan 28, 2010
 */
public class VoidRequest extends RemoteRequest<Void> {

    private URL url;

    /**
     * Constructor, default.
     */
    public VoidRequest () {
        this( null );
    }

    /**
     * Constructor, default.
     */
    public VoidRequest (URL url) {
        this.url = url;
        setReader( new VoidReader() );
    }

    /**
     * Set the request URL
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
     * Parse data object from the specified bytes.
     *
     * @param data bytes received from the server
     * @return data of type <T>
     * @throws ParseException in case of parsing problems
     */
    @Override
    protected Void processData (byte[] data) throws ParseException {
        return null;
    }

    /**
     * Only sends the request without reading anything back.
     */
    public static class VoidReader implements RemoteReader {

        public ByteArrayOutputStream readBytes (URL url) throws IOException {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.getResponseMessage();
            //com.sileria.util.Log.v( "Response code from: " + url + " = " + conn.getResponseMessage() );

            conn.disconnect();

            return null;
        }

        public void close () {
        }
    }
}
