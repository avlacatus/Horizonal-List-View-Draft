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

import android.location.Location;

import java.util.EventListener;

/**
 * LocatorListener interface to listen to {@link SimpleLocator} update.
 *
 * @author Ahmed Shakil
 * @date Nov 29, 2009
 */
public interface LocatorListener extends EventListener {

	/**
	 * Invoked when a valid location was found.
	 *
	 * Note: This method is only called once.
	 * 
	 * @param loc a valid location object if found
	 */
	void locationFound (Location loc);

	/**
	 * Invoked in case of error or timeout.
	 */
	void locationFailed ();

}
