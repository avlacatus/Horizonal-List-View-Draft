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

package com.sileria.android;


import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.sileria.util.Utils;

/**
 * Compositing & Special Effects.
 *
 * @author Ahmed Shakil
 * @date Oct 27, 2007
 */

public final class Effects {

	/**
	 * Creates a translucent <code>Drawable</code> from the
	 * specified <code>image</code> with reflection on the bottom
	 * where the reflected area will be of same height and scale
	 * as the original image.
	 *
	 * @param image <code>Drawable</code> to create reflection of
	 * @return Newly created image with reflection
	 */
	public static Drawable createReflection (int image) {
		return createReflection( image, 1.0f );
	}

	/**
	 * Creates a translucent <code>Drawable</code> from the specified <code>image</code>
	 * and the reflected area height being the percent specified by <code>size</code>.
	 * <p/>
	 * By default the reflected image will not be scaled.
	 *
	 * @param image <code>Drawable</code> to create reflection of
	 * @param size Reflected area height in percent of the original image height.
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image.
	 * 			Valid argument must be greater than zero.
	 *
	 * @return Newly created image with reflection
	 *
	 * @see #createReflection(int, float, float, int)
	 */
	public static Drawable createReflection (int image, float size) {
		return createReflection( image, size, size, 0 );
	}

	/**
	 * Creates a translucent <code>Drawable</code> from <code>image</code>
	 * with specified <code>size</code> and <code>scale</code>.
	 *
	 * @param image <code>Drawable</code> to create reflection of
	 * @param size Reflected area height in percent of the original image height.
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image.
	 * 			Valid argument must be between zero and one. (with zero being exclusive)
	 * @param scale the reflection scale ratio to the original image height
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image
	 * 			Valid argument must be between zero and one for scaling down the reflect,
	 * 			and between 1 and 10 to scale up. (zero being exclusive)
	 * @param gap padding to put between image bottom and reflection start.
	 * 
	 * @return Newly created image with reflection
	 *
	 * @throws IllegalArgumentException if <code>size</code> or <code>scale</code>
	 * 			are zero or negative value.
	 */
	public static Drawable createReflection (int image, float size, float scale, int gap) {
		Bitmap bmp = Resource.getBitmap( image );
		Drawable img = new BitmapDrawable( Resource.getResources(), createReflection( bmp, size, scale, gap ) );
		bmp.recycle();
		return img;
	}
	
	/**
	 * Creates a translucent <code>Bitmap</code> from the
	 * specified <code>image</code> with reflection on the bottom
	 * where the reflected area will be of same height and scale
	 * as the original image.
	 *
	 * @param image <code>Bitmap</code> to create reflection of
	 * @return Newly created image with reflection
	 */
	public static Bitmap createReflection (Bitmap image) {
		return createReflection( image, 1.0f );
	}

	/**
	 * Creates a translucent <code>Bitmap</code> from the specified <code>image</code>
	 * and the reflected area height being the percent specified by <code>size</code>.
	 * <p/>
	 * By default the reflected image will not be scaled.
	 *
	 * @param image <code>Bitmap</code> to create reflection of
	 * @param size Reflected area height in percent of the original image height.
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image.
	 * 			Valid argument must be greater than zero.
	 *
	 * @return Newly created image with reflection
	 *
	 * @see #createReflection(Bitmap, float, float, int)
	 */
	public static Bitmap createReflection (Bitmap image, float size) {
		return createReflection( image, size, size, 0 );
	}

	/**
	 * Creates a translucent <code>Bitmap</code> from <code>image</code>
	 * with specified <code>size</code> and <code>scale</code>.
	 *
	 * @param image <code>Bitmap</code> to create reflection of
	 * @param size Reflected area height in percent of the original image height.
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image.
	 * 			Valid argument must be between zero and one. (with zero being exclusive)
	 * @param scale the reflection scale ratio to the original image height
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image
	 * 			Valid argument must be between zero and one for scaling down the reflect,
	 * 			and between 1 and 10 to scale up. (zero being exclusive)
	 * @param gap padding to put between image bottom and reflection start.
	 * @return Newly created image with reflection
	 *
	 * @throws IllegalArgumentException if <code>size</code> or <code>scale</code>
	 * 			are zero or negative value.
	 */
	public static Bitmap createReflection (Bitmap image, float size, float scale, int gap) {

		if (size <= 0.f || size > 1f )
			throw new IllegalArgumentException( "size must be > 0.0 and < 1.0");

		if (scale <= 0.f || scale > 10f)
			throw new IllegalArgumentException( "scale must be > 0.0 and < 10.");

		final int width = image.getWidth();
		final int height = image.getHeight();
		final int refSize = (int)(height * size);

		//This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale( 1, -scale );

		//Create a Bitmap with the flip matix applied to it.
		//We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap( image, 0, height - refSize, width, refSize, matrix, false );
		final int refHeight = reflectionImage.getHeight();

		//Create a new bitmap with same width but taller to fit reflection
		Bitmap newImage = Bitmap.createBitmap( width, height + refHeight + gap, Bitmap.Config.ARGB_8888 );

		//Create a new Canvas with the bitmap that's big enough for
		//the image plus gap plus reflection
		Canvas canvas = new Canvas( newImage );

		//Draw in the original image
		canvas.drawBitmap( image, 0, 0, null );

		//Draw in the reflection
		canvas.drawBitmap( reflectionImage, 0, height + gap, null );

		//Create a shader that is a linear gradient that covers the reflection
		LinearGradient shader = new LinearGradient( 0, image.getHeight(), 0,
				newImage.getHeight() + gap, 0x70ffffff, 0x00ffffff,
				Shader.TileMode.CLAMP );

		//Set the paint to use this shader (linear gradient)
		Paint paint = new Paint();
		paint.setShader( shader );

		//Set the Transfer mode to be porter duff and destination in
		paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.DST_IN ) );

		//Draw a rectangle using the paint with our linear gradient
		canvas.drawRect( 0, height, width, newImage.getHeight() + gap, paint );

		// recycle unused
		reflectionImage.recycle();

		return newImage;
	}

	/**
	 * Creates and returns a translucent <code>Bitmap</code> from <code>image</code>
	 * with specified <code>size</code> and <code>scale</code> containing only the
	 * reflected piece and not the full image.
	 *
	 * @param image <code>Bitmap</code> to create reflection of
	 * @param size Reflected area height in percent of the original image height.
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image.
	 * 			Valid argument must be between zero and one. (with zero being exclusive)
	 * @param scale the reflection scale ratio to the original image height
	 * 			<code>1.0f</code> being 100% meaning the same height as the original image
	 * 			Valid argument must be between zero and one for scaling down the reflect,
	 * 			and between 1 and 10 to scale up. (zero being exclusive)
	 *
	 * @return Newly created image with reflection
	 *
	 * @throws IllegalArgumentException if <code>size</code> or <code>scale</code>
	 * 			are zero or negative value.
	 */
	public static Bitmap createReflectionOnly (Bitmap image, float size, float scale) {

		if (size <= 0.f || size > 1f )
			throw new IllegalArgumentException( "size must be > 0.0 and <= 1.0");

		if (scale <= 0.f || scale > 10f)
			throw new IllegalArgumentException( "scale must be > 0.0 and <= 10.");

		final int width = image.getWidth();
		final int height = image.getHeight();
		final int refSize = (int)(height * size);

		//This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale( 1, -scale );

		//Create a Bitmap with the flip matix applied to it.
		//We only want the bottom half of the image
		Bitmap newImage = Bitmap.createBitmap( image, 0, height - refSize, width, refSize, matrix, true );
		final int refHeight = newImage.getHeight();

		//Create a shader that is a linear gradient that covers the reflection
		LinearGradient shader = new LinearGradient( 0, 0, 0, refHeight, 0x70ffffff, 0x00ffffff,	Shader.TileMode.CLAMP );

		//Set the paint to use this shader (linear gradient)
		Paint paint = new Paint();
		paint.setShader( shader );

		//Set the Transfer mode to be porter duff and destination in
		paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.DST_IN ) );

		//Create a new Canvas with the bitmap that's big enough for
		//the image plus gap plus reflection
		Canvas canvas = new Canvas( newImage );

		//Draw a rectangle using the paint with our linear gradient
		canvas.drawRect( 0, 0, width, refHeight, paint );

		return newImage;
	}

	/**
	 * Creates and returns a new <code>Bitmap</code> after applying the blue screen composite
	 * to the provided image where the specified <code>blue</code> color is replaced with <code>color</code>
	 *
	 * @param bmp source bitmap
	 * @param blue color of blue screen
	 * @param color new color to be replaced
	 * @param tolerance tolerance value between 0 - 255
	 * 	Tolerance near 0: avoid any colors even remotely similar to the op-color
	 * 	Tolerance near 255: avoid only colors nearly identical to the op-color
	 * @param width new width of the final
	 * @param height new height
	 *
	 * @see AvoidXfermode
	 *
	 * @return blue screen composited image
	 */
	public static Bitmap createChromaKeyComposite (Bitmap bmp, int blue, int color, int tolerance, int width, int height) {

		Bitmap overlay = createChromaKeyComposite( bmp, blue, color, tolerance );
		Bitmap image = Bitmap.createScaledBitmap( overlay, width, height, true );
		overlay.recycle();
		return image;
	}

	/**
	 * Creates and returns a new <code>Bitmap</code> after applying the blue screen composite
	 * to the provided image where the specified <code>blue</code> color is replaced with <code>color</code>
	 *
	 * @param bmp source bitmap
	 * @param blue color of blue screen
	 * @param color new color to be replaced
	 * @param tolerance tolerance value between 0 - 255
	 * 	Tolerance near 0: avoid any colors even remotely similar to the op-color
	 * 	Tolerance near 255: avoid only colors nearly identical to the op-color
	 *
	 * @see AvoidXfermode
	 *
	 * @return blue screen composited image
	 */
	public static Bitmap createChromaKeyComposite (Bitmap bmp, int blue, int color, int tolerance) {
		final int w = bmp.getWidth();
		final int h = bmp.getHeight();

		// create empty bitmap
		Bitmap overlay = Bitmap.createBitmap( w, h, bmp.getConfig() );

		// create empty canvas
		Canvas g = new Canvas( overlay );
		Paint p = new Paint();

		// draw src bitmap on canvas
		g.drawBitmap( bmp, 0, 0, p );

		// gets the target area to chang color
		p.setXfermode( new AvoidXfermode( blue, Utils.between( 0, 255, tolerance ), AvoidXfermode.Mode.TARGET ) );
		p.setColor( color );

		// paint over target area
		g.drawPaint( p );

		return overlay;
	}

	/**
	 * Create an border image of <code>borderColor</code> and <code>borderSize</code>
	 * with inside area cut empty and rounded with radius specified by <code>cornerRadius</code>.
	 *
	 * @param width width of the new bitmap
	 * @param height height of the new bitmap
	 * @param borderColor border color
	 * @param borderSize border size
	 * @param cornerRadius inner rounded border radius
	 *
	 * @return newly created image with inner rounded empty area
	 */
	public static Bitmap createRoundedMatte (int width, int height, int borderColor, int borderSize, float cornerRadius) {

		// Create a transparent bitmap
		Bitmap matte = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
		Canvas g = new Canvas( matte ); // image canvas to draw

		// draw outer border
		Paint p = new Paint( Paint.ANTI_ALIAS_FLAG );
		p.setColor( borderColor );
		g.drawRect( new RectF( 0, 0, width, height ), p );

		// cut inner section using PorterDuffXfermode filter
		// A out B http://en.wikipedia.org/wiki/File:Alpha_compositing.svg
		p.setColor( Color.BLACK );
		p.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_OUT ) );
		RectF rect = new RectF( borderSize, borderSize, width - borderSize, height - borderSize );
		g.drawRoundRect( rect, cornerRadius, cornerRadius, p );

		return matte;
	}

//	/**
//	 * Creates an opaque <code>BufferedImage</code> from the
//	 * specified <code>image</code> with reflection on the bottom
//	 * where the reflected area will be of same height and scale
//	 * as the original image. The new image will have the specified
//	 * background color.
//	 *
//	 * @param image <code>BufferedImage</code> to create reflection of
//	 * @return Newly created image with reflection
//	 *
//	 * @see #createReflection(java.awt.image.BufferedImage, float, float)
//	 */
//	public static BufferedImage createReflection (BufferedImage image, Color bg) {
//		return makeOpaque( createReflection( image ), bg );
//	}
//
//	/**
//	 * Creates an opaque <code>BufferedImage</code> from the specified <code>image</code>
//	 * and the reflected area height being the percent specified by <code>size</code>.
//	 * The opaque image will have the background color specified by <code>bg</code>.
//	 * <p/>
//	 * By default the reflected image will not be scaled.
//	 *
//	 * @param image <code>BufferedImage</code> to create reflection of
//	 * @param size Reflected area height in percent of the original image height.
//	 * 			<code>1.0f</code> being 100% meaning the same height as the original image.
//	 * 			Valid argument must be greater than zero.
//	 *
//	 * @return Newly created image with reflection
//	 *
//	 * @see #createReflection(java.awt.image.BufferedImage, float, float)
//	 */
//	public static BufferedImage createReflection (BufferedImage image, float size, Color bg) {
//		return makeOpaque( createReflection( image, size ), bg );
//	}
//
//	/**
//	 * Creates an opaque <code>BufferedImage</code> from the <code>image</code>
//	 * with specified <code>size</code> and <code>scale</code>.
//	 * The new opaque image will have the specified background color.
//	 *
//	 * @param image <code>BufferedImage</code> to create reflection of
//	 * @param size Reflected area height in percent of the original image height.
//	 * 			<code>1.0f</code> being 100% meaning the same height as the original image.
//	 * 			Valid argument must be greater than zero.
//	 * @param scale Reflected image vertical scale ratio to the original image height
//	 * 			<code>1.0f</code> being 100% meaning the same height as the original image
//	 * 			Valid argument must be greater than zero.
//	 * @return Newly created image with reflection
//	 *
//	 * @throws IllegalArgumentException if <code>size</code> or <code>scale</code>
//	 * 			are zero or negative value.
//	 *
//	 * @see #createReflection(java.awt.image.BufferedImage, float, float)
//	 */
//	public static BufferedImage createReflection (BufferedImage image, float size, float scale, Color bg) {
//		return makeOpaque( createReflection( image, size, scale), bg );
//	}
//
//
}
