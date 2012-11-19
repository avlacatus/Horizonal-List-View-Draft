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

package com.sileria.android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import com.sileria.net.HttpReader;
import com.sileria.util.IO;

/**
 * ImageLoader class loads images from URLs in background thread in sequention order.
 * <p/>
 * <strong>Example</strong>:
 * <blockquote><pre>
 * public static Cancellable loadBitmaps (BitmapCallback callback, String... urls) {
 *     BitmapLoader loader = new BitmapLoader( callback );
 *     loader.execute( urls );
 *     return loader;
 * }
 * </pre></blockquote>
 * <strong>Note</strong>: Please do not launch too many instances of this image loader class. It will run out of memory
 * with too many instances running at the same time. For that purpose use {@link com.sileria.android.util.QueuedBitmapLoader}.
 *
 * @author Ahmed Shakil
 * @date Jul 1, 2012
 */
public class BitmapLoader extends ContentLoader<Bitmap, ImageOptions> {

	/**
	 * Constructor, default.
	 * Note: This constructor must be called from UI thread.
	 */
	public BitmapLoader () {
	}

	/**
	 * Constructor specifying your own handler instance and callback listener object.
	 * Note: This constructor must be called from UI thread.
	 */
	public BitmapLoader (BitmapCallback callback) {
		super( callback );
	}

	/**
	 * Constructor specifying your own handler instance and callback listener object.
	 */
	protected BitmapLoader (ContentCallback<Bitmap,ImageOptions> callback) {
		super( callback );
	}
	/**
	 * Load bitmap in background thread from the specified URL address.
	 *
	 * @param url URL address
	 * @param opt image options
	 *@param tries the number of tries that has happened so far.
	 *  @return Loaded Bitmap
	 *
	 * @throws IOException in case of IO exception.
	 */
	@Override
	protected Bitmap loadContent (String url, ImageOptions opt, int tries) throws IOException {
		Bitmap bmp = null;
		InputStream in = null;
		HttpReader reader = null;
		try {

			URL u = new URL( url );
			URLConnection conn = u.openConnection();
			conn.setConnectTimeout( timeout );
			conn.setUseCaches( useCache );

			if (opt != null) {
				bmp = new ImageSerializer( opt ).getBitmap( conn );
			}
			else {
				Object content = conn.getContent();
				if (content instanceof BitmapDrawable)
					bmp = ((BitmapDrawable)content).getBitmap();
				else if (content instanceof Bitmap)
					bmp = (Bitmap)content;
				else {
					in = new BufferedInputStream( content instanceof InputStream ? (InputStream)content : conn.getInputStream() );

					if (tries%2 == 0) {
						bmp = BitmapFactory.decodeStream( in );
					}
					else {
						reader = new HttpReader( buffSize, timeout, useCache );
						ByteArrayOutputStream bos = reader.readBytes( in );

						bmp = BitmapFactory.decodeByteArray( bos.toByteArray(), 0, bos.size() );
						IO.close( bos );
					}
				}
			}
		}
		finally {
			IO.close( in );
			IO.close( reader );
		}
		return bmp;
	}
}
