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

import android.graphics.drawable.Drawable;

import com.sileria.util.Content;

/**
 * QueuedImageLoader pools a set of <code>ImageLoader</code>s to load images
 * in an efficient way.
 * <p/>
 * Example of this class inside a list adapter with thumbnails being loaded one by one
 * <blockquote><pre>
 *
 * // class member
 * private QueuedImageLoader thumbLoader;
 *
 * // inner class
 * private class VideoListAdapter extends AbstractListAdapter<Video> implements ImageCallback {
 *
 *     protected VideoListAdapter (Context ctx) {
 *         super( ctx, null );
 *     }
 *
 *     public View getView (int i, View view, ViewGroup viewGroup) {
 *         VideoRenderer r = view == null ? new VideoRenderer( ctx ) : (VideoRenderer) view;
 *         Video v = getItem( i );
 *         r.setVideo( v );
 *
 *         if (v.thumb == null) {
 *             if (thumbLoader == null) {
 *                 thumbLoader = new QueuedImageLoader( this );
 *                 thumbLoader.setPoolSize( 2 );
 *             }
 *             thumbLoader.enqueue( v.thumbnail.url, i );
 *             thumbLoader.execute();
 *         }
 *
 *     }
 *
 *     public void onImageLoad (Drawable image, int index) {
 *         get( index ).thumb = image;
 *         notifyDataSetChanged();
 *     }
 *
 *     public void onImageFail (Throwable e, int index) {
 *     }
 * }
 *
 * </pre></blockquote>
 * 
 * @author Ahmed Shakil
 * @date Dec 20, 2010
 */
public class QueuedImageLoader extends QueuedContentLoader<Drawable, ImageOptions> {

	/**
	 * Constructor, default.
	 * Note: This constructor must be called from UI thread.
	 */
	public QueuedImageLoader () {
	}

	/**
	 * Construct an image loader with image callback.
	 * Note: This constructor must be called from UI thread.
	 */
	public QueuedImageLoader (ImageCallback callback) {
		super( callback );
	}

	/**
	 * Return instance of {@link ImageLoader} class.
	 */
	@Override
	protected ContentLoader<Drawable, ImageOptions> createLoader (Content<Drawable, ImageOptions> request, ContentCallback<Drawable, ImageOptions> callback) {
		return new ImageLoader( callback );
	}
}
