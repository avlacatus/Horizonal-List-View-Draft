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

import com.sileria.util.ContentOptions;

/**
 * ImageOptions is used to determine loading options for an image e.g. required width/height,etc...
 *
 * @author Ahmed Shakil
 * @date 08-26-2012
 *
 * @see QueuedImageLoader
 * @see ImageLoader
 * @see CachedImageLoader
 */
public class ImageOptions implements ContentOptions {

	private static final long serialVersionUID = -4945136492051658717L;

	/**
	 * If set to a value > 1, requests the decoder to subsample the original
	 * image, returning a smaller image to save memory. The sample size is
	 * the number of pixels in either dimension that correspond to a single
	 * pixel in the decoded bitmap. For example, inSampleSize == 4 returns
	 * an image that is 1/4 the width/height of the original, and 1/16 the
	 * number of pixels. Any value <= 1 is treated the same as 1. Note: the
	 * decoder will try to fulfill this request, but the resulting bitmap
	 * may have different dimensions that precisely what has been requested.
	 * Also, powers of 2 are often faster/easier for the decoder to honor.
	 */
	public int inSampleSize;

	/**
	 * Required width. This takes precedence over {@link #inSampleSize}
	 */
	public int width;

	/**
	 * Required height. This takes precedence over {@link #inSampleSize}
	 */
	public int height;

	/**
	 * Constructor, default.
	 */
	public ImageOptions () {
	}

	/**
	 * Construct <code>ImageOptions</code> with specified dimensions.
	 * @param width required width for image being loaded
	 * @param height required height for image being loaded
	 */
	public ImageOptions (int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Construct <code>ImageOptions</code> specifying the subsampling size.
	 * @param inSampleSize subsample size
	 */
	public ImageOptions (int inSampleSize) {
		this.inSampleSize = inSampleSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImageOptions that = (ImageOptions)o;

		if (Math.max( 1, inSampleSize ) != Math.max( 1, that.inSampleSize)) return false;

		return height == that.height && width == that.width;

	}

	@Override
	public int hashCode () {
		int result = Math.max( 1, inSampleSize );
		result = 31 * result + width;
		result = 31 * result + height;
		return result;
	}

	/**
	 * Utility method to avoid boiler code.
	 */
	public static boolean hasOptions (ImageOptions opt) {
		return opt != null &&
				((opt.width > 0 && opt.height > 0)
						|| (opt.inSampleSize > 1));
	}
}
