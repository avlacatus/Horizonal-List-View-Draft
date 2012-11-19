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
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.*;

/**
 * GingerBread 2.3 WRAPPER.  API Level - 9 & 10.
 *
 * @author Ahmed Shakil
 * @date Dec 16, 2010
 */
class GingerBread extends Froyo {


	/**
	 * Introduced in GingerBread.
	 * @see {@link android.view.View#setOverScrollMode(int)}
	 */
	@Override
	public void setOverScrollMode(View view, int mode) {
		view.setOverScrollMode( mode );
	}

	/**
	 * Sets the drawable that will be drawn above all other list content.
	 * This area can become visible when the user overscrolls the list.
	 *
	 * @param list ListView to set the header image to
	 * @param header The drawable to use
	 */
	public void setOverscrollHeader(ListView list, Drawable header) {
		list.setOverscrollHeader( header );
	}

	/**
	 * Sets the drawable that will be drawn below all other list content.
	 * This area can become visible when the user overscrolls the list,
	 * or when the list's content does not fully fill the container area.
	 *
	 * @param list ListView to set the footer image to
	 * @param footer The drawable to use
	 */
	public void setOverscrollFooter(ListView list, Drawable footer) {
		list.setOverscrollFooter( footer );
	}

	/**
	 * Introduced in API level 9. Use this method only with orientations that are new.
	 * This method does will not do anything on devices running below API-9.
	 *
	 * @param requestedOrientation An orientation constant as used in
	 * {@link android.content.pm.ActivityInfo#screenOrientation ActivityInfo.screenOrientation}.
	 */
	public void setRequestedOrientation (Activity activity, ScreenOrientation requestedOrientation) {
		activity.setRequestedOrientation( requestedOrientation.value );
	}

}
