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

import com.sileria.android.Kit;
import com.sileria.android.util.AbstractTask;
import com.sileria.util.AsyncCallback;
import com.sileria.util.Utils;

/**
 * Geocode in a background task.
 * <p/>
 * Call {@link #execute(android.location.Location)} for Geocoding a Location.
 * <br>
 * Call {@link #execute(String)} for reverse Geocoding a Location name.
 *
 * @author Ahmed Shakil
 * @date 4/26/11
 */
public class GeocodeTask extends AbstractTask<Void, Void, Address> {

	private Location location;
	private String search;

	/**
	 * Construct geocode task with specified callback.
	 */
	public GeocodeTask (AsyncCallback<Address> callback) {
		super( callback );
	}

	/**
	 * Geocode in the background.
	 */
	@Override
	protected Address doTask (Void ... params) {

		Geocoder geo = new Geocoder( Kit.getAppContext() );
		Address address = null;
		try {
			if (location != null)
				address = Utils.first( geo.getFromLocation( location.getLatitude(), location.getLongitude(), 1 ), null );
			else if (Utils.isEmpty( search ))
				address = Utils.first( geo.getFromLocationName( search, 1 ), null );
		}
		catch (IOException e) {
			Log.e( Kit.TAG, e.getLocalizedMessage(), e );
		}
		return address;
	}

    /**
     * Execute a background task to geocode specified <code>location</code>.
     *
     * @param location Location
     */
    public GeocodeTask execute (Location location) {
		this.location = location;
        super.execute();
        return this;
    }

	/**
	 * Execute a background task to reverse geocode specified <code>location</code>.
	 *
	 * @param location location name
	 */
	public GeocodeTask execute (String location) {
		this.search = location;
		super.execute();
		return this;
	}
}
