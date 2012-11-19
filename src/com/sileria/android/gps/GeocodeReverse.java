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

package com.sileria.android.gps;

import android.location.*;
import android.util.Log;

import java.io.IOException;
import java.util.*;

import com.sileria.android.Kit;
import com.sileria.android.util.AbstractTask;
import com.sileria.util.AsyncCallback;
import com.sileria.util.Utils;

/**
 * Reverse Geocode based on search parameter and return multiple addresses based on provided max limit
 *
 * @author Ahmed Shakil
 * @date 4/26/11
 */
public class GeocodeReverse extends AbstractTask<String, Void, List<Address>> {

	private final int max;

	/**
	 * Construct geocode task with specified callback.
	 */
	public GeocodeReverse (int max, AsyncCallback<List<Address>> callback) {
		super( callback );
		this.max = max;
	}

	/**
	 * Geocode in the background.
	 */
	@Override
	protected List<Address> doTask (String ... params) {

		String text = Utils.first( params, null );

		Geocoder geo = new Geocoder( Kit.getAppContext() );
		List<Address> address = Collections.emptyList();
		try {
			address = geo.getFromLocationName( text, max );
		}
		catch (IOException e) {
			Log.e( Kit.TAG, e.getLocalizedMessage(), e );
		}
		return address;
	}

}
