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
import android.bluetooth.BluetoothAdapter;
import android.view.*;

/**
 * ECLAIR 2.0 wrapper.  API Level - 5 & 6.
 *
 * @author Ahmed Shakil
 * @date Dec 16, 2010
 */
class Eclair extends Donut {

	@Override
	public void overridePendingTransition (Activity activity, int enterAnim, int exitAnim) {
		activity.overridePendingTransition( enterAnim, exitAnim );
	}

	/**
	 * Enables or disables the drawing cache.
	 * This method does not work on G1 and possibly
	 * other phones so disabling it for API 1.6
	 */
	public void setDrawingCacheEnabled (View view, boolean enabled) {
		view.setDrawingCacheEnabled( enabled );
	}

	/**
	 * Check if Bluetooth is supported on this hardware platform.
	 */
	@Override
	public boolean isBluetoothSupported () {
		return BluetoothAdapter.getDefaultAdapter() != null;
	}


}
