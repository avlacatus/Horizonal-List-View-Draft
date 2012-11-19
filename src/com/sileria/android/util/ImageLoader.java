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
import android.graphics.drawable.Drawable;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import com.sileria.android.Resource;
import com.sileria.net.HttpReader;
import com.sileria.util.IO;

/**
 * ImageLoader class loads images from URLs in background thread in sequention order.
 * <p/>
 * <strong>Example</strong>:
 * <blockquote><pre>
 * public static Cancellable loadImages (ImageCallback callback, String... urls) {
 *     ImageLoader loader = new ImageLoader( callback );
 *     loader.execute( urls );
 *     return loader;
 * }
 * </pre></blockquote>
 * <strong>Note</strong>: Please do not launch too many instances of this image loader class. It will run out of memory
 * with too many instances running at the same time. For that purpose use {@link QueuedImageLoader}.
 *
 * @author Ahmed Shakil
 * @date Mar 27, 2010
 */
public class ImageLoader extends ContentLoader<Drawable, ImageOptions> {

	/**
	 * Constructor, default.
	 * Note: This constructor must be called from UI thread.
	 */
	public ImageLoader () {
	}

	/**
	 * Constructor specifying your own handler instance and callback listener object.
	 * Note: This constructor must be called from UI thread.
	 */
	public ImageLoader (ImageCallback callback) {
		super( callback );
	}

	/**
	 * Constructor specifying your own handler instance and callback listener object.
	 */
	protected ImageLoader (ContentCallback<Drawable,ImageOptions> callback) {
		super( callback );
	}

	/**
	 * Load drawable image in background thread from the specified URL address.
	 *
	 * @param url URL address
	 * @param opt expects an {@link ImageOptions} implementation or <code>nulll</code>
	 * @param tries the number of tries that has happened so far
	 * @return Loaded Drawable object or <code>null</code> if not able to decode
	 *
	 * @throws IOException in case of IO exception.
	 */
	protected Drawable loadContent (String url, ImageOptions opt, int tries) throws IOException {
		Drawable image = null;
		InputStream in = null;
		HttpReader reader = null;
		try {

			URL u = new URL( url );
			URLConnection conn = u.openConnection();
			conn.setConnectTimeout( timeout );
			conn.setUseCaches( useCache );

			if (opt != null) {
				image = new ImageSerializer( opt ).getContent( conn );
			}
			else {
				Object content = conn.getContent();
				if (content instanceof Drawable)
					image = (Drawable)content;
				else {
					in = new BufferedInputStream( content instanceof InputStream ? (InputStream)content : conn.getInputStream() );

					if (tries%2 == 0) {
						image = Drawable.createFromStream( in, "Image" );
					}
					else {
						reader = new HttpReader( buffSize, timeout, useCache );
						ByteArrayOutputStream bos = reader.readBytes( in );

						Bitmap bm = BitmapFactory.decodeByteArray( bos.toByteArray(), 0, bos.size() );
						image = bm == null ? null : new BitmapDrawable( Resource.getResources(), bm );

						IO.close( bos );
					}
				}
			}
		}
		finally {
			IO.close( in );
			IO.close( reader );
		}
		return image;
	}
}
