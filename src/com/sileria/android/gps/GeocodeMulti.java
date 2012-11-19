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
import java.util.ArrayList;
import java.util.List;

import com.sileria.android.Kit;
import com.sileria.android.util.AbstractTask;
import com.sileria.util.AsyncCallback;
import com.sileria.util.Utils;

/**
 * Geocode multiple locations at a time in background task.
 *
 * @author Ahmed Shakil
 * @date 4/26/11
 */
public class GeocodeMulti extends AbstractTask<Location, Void, List<Address>> {

	/**
	 * Construct geocode task with specified callback.
	 */
	public GeocodeMulti (AsyncCallback<List<Address>> callback) {
		super( callback );
	}

	/**
	 * Geocode in the background.
	 */
	@Override
	protected List<Address> doTask (Location ... params) {

		Geocoder geo = new Geocoder( Kit.getAppContext() );
		List<Address> address = new ArrayList<Address>();
		try {
			for (Location loc : params)
				address.add( Utils.first( geo.getFromLocation( loc.getLatitude(), loc.getLongitude(), 1 ), null ) );
		}
		catch (IOException e) {
			Log.e( Kit.TAG, e.getLocalizedMessage(), e );
		}
		return address;
	}
}
