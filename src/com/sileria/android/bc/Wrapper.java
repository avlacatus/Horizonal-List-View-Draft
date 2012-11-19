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

package com.sileria.android.bc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.*;
import android.webkit.WebSettings;
import android.widget.*;

import com.sileria.util.Utils;

import java.util.HashMap;

/**
 * Wrapper.
 *
 * @author Ahmed Shakil
 * @date Dec 16, 2010
 */
public abstract class Wrapper {

	private static Wrapper instance;

	/**
	 * Always allow a user to over-scroll this view, provided it is a
	 * view that can scroll.
	 */
	public static final int OVER_SCROLL_ALWAYS = 0;

	/**
	 * Allow a user to over-scroll this view only if the content is large
	 * enough to meaningfully scroll, provided it is a view that can scroll.
	 */
	public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;

	/**
	 * Never allow a user to over-scroll this view.
	 */
	public static final int OVER_SCROLL_NEVER = 2;

	/**
	 * Only those orientation are available that are new.
	 */
	public enum ScreenOrientation {
		/**
		 * Constant corresponding to <code>sensorLandscape</code> in
		 * the {@link android.R.attr#screenOrientation} attribute.
		 */
		SCREEN_ORIENTATION_SENSOR_LANDSCAPE (6),

		/**
		 * Constant corresponding to <code>sensorPortrait</code> in
		 * the {@link android.R.attr#screenOrientation} attribute.
		 */
		SCREEN_ORIENTATION_SENSOR_PORTRAIT (7),

		/**
		 * Constant corresponding to <code>reverseLandscape</code> in
		 * the {@link android.R.attr#screenOrientation} attribute.
		 */
		SCREEN_ORIENTATION_REVERSE_LANDSCAPE (8),

		/**
		 * Constant corresponding to <code>reversePortrait</code> in
		 * the {@link android.R.attr#screenOrientation} attribute.
		 */
		SCREEN_ORIENTATION_REVERSE_PORTRAIT (9),

		/**
		 * Constant corresponding to <code>fullSensor</code> in
		 * the {@link android.R.attr#screenOrientation} attribute.
		 */
		SCREEN_ORIENTATION_FULL_SENSOR (10);

		final int value;

		private ScreenOrientation (int value) {
			this.value = value;
		}
	}

	/**
	 * Singleton instance.
	 */
	public static Wrapper getInstance () {
		if (instance == null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.DONUT)
				throw new UnsupportedOperationException( "Wrappers prior to 1.6 not available." );

			String latest;
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			map.put(  4, "com.sileria.android.bc.Donut"       );
			map.put(  5, "com.sileria.android.bc.Eclair"      );
			map.put(  6, "com.sileria.android.bc.Eclair"      );
			map.put(  7, "com.sileria.android.bc.EclairMR1"   );
			map.put(  8, "com.sileria.android.bc.Froyo"       );
			map.put(  9, "com.sileria.android.bc.GingerBread" );
			map.put( 10, "com.sileria.android.bc.GingerBread" );
			map.put( 11, "com.sileria.android.bc.HoneyComb"   );
			map.put( 12, "com.sileria.android.bc.HoneyComb"   );
			map.put( 13, "com.sileria.android.bc.HoneyComb"   );
			map.put( 14, "com.sileria.android.bc.IceCream"    );
			map.put( 15, "com.sileria.android.bc.IceCream"    );
			map.put( 16, latest = "com.sileria.android.bc.JellyBean" );

			String classname = Utils.defaultIfNull( map.get( Build.VERSION.SDK_INT ), latest );
			try {
				instance = (Wrapper)Class.forName( classname ).newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException( "Serious error in Wrapper class", e );
			}
		}
		return instance;
	}

	/**
	 * Introduced in Eclair.
	 * @see Activity#overridePendingTransition(int, int)
	 */
	public void overridePendingTransition (Activity activity, int enterAnim, int exitAnim) {}

	/**
	 * Enables or disables the drawing cache.
	 * This method does not work on G1 and possibly
	 * other phones so disabling it for API 1.6
	 */
	public void setDrawingCacheEnabled (View view, boolean enabled) {
	}

	/**
	 * Introduced in GingerBread.
	 * @see View#setOverScrollMode(int)
	 */
	public void setOverScrollMode(View view, int overScrollMode) {}

	/**
	 * Sets the drawable that will be drawn above all other list content.
	 * This area can become visible when the user overscrolls the list.
	 *
	 * @param list ListView to set the header image to
	 * @param header The drawable to use
	 */
	public void setOverscrollHeader(ListView list, Drawable header) {}

	/**
	 * Sets the drawable that will be drawn below all other list content.
	 * This area can become visible when the user overscrolls the list,
	 * or when the list's content does not fully fill the container area.
	 *
	 * @param list ListView to set the footer image to
	 * @param footer The drawable to use
	 */
	public void setOverscrollFooter(ListView list, Drawable footer) {}

	/**
	 * Wrapper for {@link android.view.Display#getRotation()}.
	 */
	public int getRotation (Display display) {
		return display.getOrientation();
	}

	/**
	 * Introduced in API level 9. Use this method only with orientations that are new.
	 * This method does will not do anything on devices running below API-9.
	 *
	 * @param requestedOrientation An orientation constant as used in
	 * {@link android.content.pm.ActivityInfo#screenOrientation ActivityInfo.screenOrientation}.
	 */
	public void setRequestedOrientation (Activity activity, ScreenOrientation requestedOrientation) {
	}

	/**
	 * AlertDialog.Builder constructor that takes theme id is not available until API 11.
	 * This factory methods lets you take care of that problem.
	 * @param ctx Context
	 * @param theme The actual theme that an AlertDialog uses is a private implementation, however you can
     * 			here supply either the name of an attribute in the theme from which to get the dialog's style
	 * 			(such as {@link android.R.attr#alertDialogTheme} or one of the constants
	 */
	public AlertDialog.Builder createAlertBuilder (Context ctx, int theme) {
		return new AlertDialog.Builder( new ContextThemeWrapper( ctx, theme ) );
	}

	/**
	 * Set whether the WebView loads a page with overview mode.
	 */
	public void setLoadWithOverviewMode(WebSettings settings, boolean overview) {}

	/**
	 * Release surface which is not available until API level 14.
	 */
	public abstract void release (Surface surface);

	/**
	 * Check if Bluetooth is supported on this hardware platform.
	 */
	public abstract boolean isBluetoothSupported ();


}
