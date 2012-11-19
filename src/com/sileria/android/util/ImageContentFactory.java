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

import java.net.ContentHandler;
import java.net.ContentHandlerFactory;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * ImageContentFactory.
 *
 * @author Ahmed Shakil
 * @date 08-21-2012
 */
public class ImageContentFactory implements ContentHandlerFactory {

	private final CopyOnWriteArraySet<String> mimeTypes = new CopyOnWriteArraySet<String>( Arrays.asList(
			"image/png",
			"image/jpeg",
			"image/gif"
	));

	/**
	 * Constructor, default.
	 */
	public ImageContentFactory () {
	}

	/**
	 * Creates a new <code>ContentHandler</code> to read an object of type image.
	 *
	 * @param mime the MIME type for which a content handler is desired.
	 * @return a new <code>ContentHandler</code> to read an object from a
	 *         <code>URLStreamHandler</code>.
	 * @see ImageSerializer
	 */
	public ContentHandler createContentHandler (String mime) {
		return mimeTypes.contains( mime.toLowerCase() ) ? new ImageSerializer() : null;
	}
}
