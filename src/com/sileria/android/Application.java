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

/**
 * As a convenience class this can be used for you to extend your application class from
 * or simply define the class in your <code>AndroidManifest.xml</code> as follows:
 *
 * <blockquote><pre>
 * &lt;application android:name="com.sileria.android.Application"
 *              android:icon="@drawable/app_icon"
 *              android:label="@string/app_name"&gt;
 * </pre></blockquote>
 *
 * @author Joshua Musselwhite
 * @date 08-27-2012
 */
public class Application extends android.app.Application {

	/**
	 * Called when the application is starting, before any other application
	 * objects have been created.  Initialize Aniqroid here.
	 */
	@Override
	public void onCreate () {
		Kit.init( getApplicationContext() );
		super.onCreate();
	}

	/**
	 * This method is for use in emulated process environments.
	 * Kill Aniqroid here.
	 */
	@Override
	public void onTerminate () {
		super.onTerminate();
		Kit.destroy();
	}

	/**
	 * Release some resources here.
	 */
	@Override
	public void onLowMemory () {
		Resource.clearCache();
		super.onLowMemory();
	}
}
