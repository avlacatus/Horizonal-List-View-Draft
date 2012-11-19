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

/**
 * A singleton based extension of {@link SimpleLocator} that allows a {@link LocatorListener} to be set.
 * <p/>
 * NOTE: This is a singleton class, any attribute changes to this class will remain until reset explicitly.
 * <p/>
 * Usage:
 * <blockquote><pre>
 * // Start a GPS listener with delay of 3 seconds for NETWORK locator to kick in
 * // if the GPS location is not found withing 3 seconds. Sets the timeout to 40 seconds.
 * Locator.getInstance().find( 3000, 40000, this )
 *
 * // Only start a GPS listener with timeout of 40 seconds.
 * Locator.getInstance().findFine( 40000, this );
 *
 * // Listener methods:
 *
 * public void locationFound (android.location.Location loc) {
 *     Toast.makeText( home, R.string.msg_gps_found, Toast.LENGTH_SHORT ).show();
 * }
 *
 * public void locationFailed () {
 *     Toast.makeText( home, R.string.msg_gps_failed, Toast.LENGTH_SHORT ).show();
 * }
 * </pre></blockquote>
 *
 * Example of Timeout dialog:
 * <blockquote><pre>
 *
 * Locator.getInstance().setTimeoutListener( this );
 *
 * public boolean locationTimeout () {
 *     showDialog( TIMEOUT_DLG );
 *     return false;  // return false to not stop the location tracking
 * }
 * </pre></blockquote>
 * In the above example show your own dialog box and in case the user
 * wants to not wait any longer then simple call:
 * <blockquote><pre>
 * Locator.getInstance().cancel();
 * </pre></blockquote>
 *
 * Bonus example of time out dialog:
 * <blockquote><pre>
 * protected Dialog onCreateDialog (int id) {
 *
 *     switch(id) {
 *         case TIMEOUT_DLG:
 *             return new AlertDialog.Builder( this )
 *             .setMessage( R.string.msg_gps_timeout )
 *             .setPositiveButton( R.string.wait_more, null )
 *             .setNegativeButton( R.string.wait_stop, new ReflectiveAction(this, "evStopGPS") )
 *             .create();
 *     }
 * }
 *
 * public void evStopGPS () {
 *     Locator.getInstance().cancel();
 * }
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date Nov 29, 2009
 */
public class Locator extends SimpleLocator {

	private static Locator instance;

	private LocatorListener listener;

	/**
	 * Get singleton instance of this class.
	 */
	public static Locator getInstance () {
		if (instance == null)
			instance = new Locator();
		return instance;
	}

	/**
	 * Constructor, protected.
	 */
	protected Locator () {
	}

	/**
	 * Convenience method to find location and add listener.
	 */
	public boolean find (LocatorListener listener) {
		setLocatorListener( listener );
		return find();
	}

	/**
	 * Convenience method to find fine location and add listener.
	 */
	public boolean findFine (LocatorListener listener) {
		setLocatorListener( listener );
		return findFine();
	}

	/**
	 * Convenience method to find coarse location and add listener.
	 */
	public boolean findCoarse (LocatorListener listener) {
		setLocatorListener( listener );
		return findCoarse();
	}

	/**
	 * Convenience method to find fine location and add listener.
	 */
	public boolean findFine (int timeout, LocatorListener listener) {
		setLocatorListener( listener );
		setTimeout( timeout );
		return findFine();
	}

	/**
	 * Convenience method to find coarse location and add listener.
	 */
	public boolean findCoarse (int timeout, LocatorListener listener) {
		setLocatorListener( listener );
		setTimeout( timeout );
		return findCoarse();
	}

	/**
	 * Convenience method to find location with specified settings.
	 */
	public boolean find (int auxDelay, LocatorListener listener) {
		setLocatorListener( listener );
		return find( auxDelay );
	}

	/**
	 * Convenience method to find location with specified settings.
	 */
	public boolean find (int auxDelay, int timeout, LocatorListener listener) {
		setLocatorListener( listener );
		return find( auxDelay, timeout );
	}

	/**
	 * Set a single listener at a time.
	 */
	public void setLocatorListener (LocatorListener listener) {
		this.listener = listener;
	}

	/**
	 * Overriden to implement a single listener.
	 */
	@Override
	protected void fireLocationUpdate () {
		if (listener != null) {
			if (result != null)
				listener.locationFound( result );
			else
				listener.locationFailed();
		}
	}

	/**
	 * Overriden to disable multiple listeners.
	 * @param listener LocatorListener
	 */
	@Override
	public final void addLocatorListener (LocatorListener listener) {
		throw new UnsupportedOperationException( "Subclass Locator does not support this method" );
	}

	/**
	 * Overriden to disable multiple listeners.
	 * @param listener LocatorListener
	 */
	@Override
	public final void removeLocatorListener (LocatorListener listener) {
		throw new UnsupportedOperationException( "Subclass Locator does not support this method" );
	}
}
