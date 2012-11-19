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
import java.net.ContentHandler;
import java.net.URLConnection;

import com.sileria.android.Resource;
import com.sileria.net.HttpReader;
import com.sileria.util.IO;
import com.sileria.util.ObjectSerializer;

/**
 * Default content handler implementation for a bitmap drawable loader.
 *
 * @author Ahmed Shakil
 * @date 08-21-2012
 */
public class ImageSerializer extends ContentHandler implements ObjectSerializer<BitmapDrawable> {

	private final ImageOptions opt;

	/**
	 * Constructor, default.
	 */
	public ImageSerializer () {
		this( null );
	}

	/**
	 * Constructor specifying required width and height of the image to be loaded.
	 * @param opt ImageOptions providing more loading options like required with and height
	 */
	public ImageSerializer (ImageOptions opt) {
		this.opt = opt;
	}

	/**
	 * Given a URL connect stream positioned at the beginning of the
	 * representation of an object, this method reads that stream and
	 * creates an object from it.
	 *
	 * @param conn a URL connection.
	 * @return the object read by the <code>ContentHandler</code>.
	 * @throws java.io.IOException if an I/O error occurs while reading the object.
	 */
	@Override
	public BitmapDrawable getContent (URLConnection conn) throws IOException {
		BitmapDrawable bmp;
		InputStream in = new BufferedInputStream( conn.getInputStream() );
		bmp = read( in );
		IO.close( in );
		return bmp;
	}

	/**
	 * Given a URL connect stream positioned at the beginning of the
	 * representation of an object, this method reads that stream and
	 * creates an object from it.
	 *
	 * @param conn a URL connection.
	 * @return the object read by the <code>ContentHandler</code>.
	 * @throws java.io.IOException if an I/O error occurs while reading the object.
	 */
	public Bitmap getBitmap (URLConnection conn) throws IOException {
		InputStream in = new BufferedInputStream( conn.getInputStream() );
		Bitmap bmp = decodeStream( in );
		IO.close( in );
		return bmp;
	}

	/**
	 * Read bitmap from the specified input stream.
	 *
	 * @param in input stream to read from
	 * @return The decoded bitmap, or null if the image data could not be decoded.
	 * @throws java.io.IOException in case of IO errors
	 */
	public Bitmap decodeStream (InputStream in) throws IOException {
		if (ImageOptions.hasOptions( opt )) {
			if (opt.width > 0 && opt.height > 0)
				return decodeScaledBitmap( in );
			else if (opt.inSampleSize > 1)
				return decodeSampledBitmap( in );
		}

		return BitmapFactory.decodeStream( in );
	}

	/**
	 * Load sampled image.
	 * @throws IOException if an I/O error occurs while reading the object.
	 */
	private Bitmap decodeSampledBitmap (InputStream in) throws IOException {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = opt.inSampleSize;
		return BitmapFactory.decodeStream( in, null, options );
	}

	/**
	 * Load scaled image.
	 * @throws IOException if an I/O error occurs while reading the object.
	 */
	private Bitmap decodeScaledBitmap (InputStream in) throws IOException {

		// read stream
		HttpReader reader = new HttpReader();
		ByteArrayOutputStream baos = reader.readBytes( in );
		byte[] bytes = baos.toByteArray();
		IO.close( baos );
		IO.close( reader );

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray( bytes, 0, bytes.length, options );

		// Calculate inSampleSize
		options.inSampleSize = Resource.calcInSampleSize(options, opt.width, opt.height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray( bytes, 0, bytes.length, options );
	}

	/**
	 * Write to the stream.
	 *
	 * @param os	 output stream to write to
	 * @param image bitmap drawable image to write
	 * @throws java.io.IOException in case of IO errors
	 */
	public boolean write (OutputStream os, BitmapDrawable image) throws IOException {
		if (image == null) return false;

		Bitmap bitmap = image.getBitmap();
		if (bitmap == null) return false;

		bitmap.compress( Bitmap.CompressFormat.PNG, 100, os );
		return true;
	}

	/**
	 * Read object from to the stream.
	 *
	 * @param in output stream to write to
	 * @return object object to write
	 * @throws java.io.IOException in case of IO errors
	 */
	public BitmapDrawable read (InputStream in) throws IOException {
		Bitmap bitmap = decodeStream( in );
		return bitmap == null ? null : new BitmapDrawable( Resource.getResources(), bitmap );
	}
}
