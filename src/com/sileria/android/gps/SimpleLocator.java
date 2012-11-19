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

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.*;

import com.sileria.android.Kit;
import com.sileria.util.*;

/**
 * Starts a GPS location listener if available,
 * otherwise starts a network location listener.
 * <p/>
 * If GPS is taking long then also start a secondary
 * location listener that listens to the network
 * location.
 * <p/>
 * <strong>NOTE:</strong>This class triggers the
 * update to the listeners only once. To get updates
 * please use one of the find methods that takes
 * the update milliseconds as one of the parameters
 * or call {@link #setUpdates(int)} before calling
 * {@linkplain #find()}
 *
 * @see Locator
 *
 * @author Ahmed Shakil
 * @date Nov 29, 2009
 */
public class SimpleLocator implements Cancellable, Comparator<Location> {

	private EventListenerList listeners;

	protected Location result;

	protected final LocationManager mgr = (LocationManager)Kit.getSystemService( Context.LOCATION_SERVICE );

	private Finder finder;
	private TimeoutHandler timeoutHandler;
	private LocatorTimeout locatorTimeout;

	private int coarseMeter                        = COARSE_METERS;
	private int fineMeters                         = FINE_METERS;

	private int timeout                            = -1;
	private int updates                            = -1;
	private int auxDelay                           = 25000;

	private final static int COARSE_METERS         = 700;
	private final static int FINE_METERS           = 100;

	private final static int GPS_DELAY_MILLIS      = 1000;

	private final Handler handler = new Handler();

	/**
	 * Constructor, default.
	 */
	public SimpleLocator () {
	}

	/**
	 * Find fine or coarse location; which ever is available first.
	 *
	 * @return <code>true</code> if the provider is enabled and finding location; otherwise <code>false</code>
	 */
	public boolean find () {
		if (!Kit.isAnyLocationEnabled())
			return false;

		clear();

		finder = new Finder().start();
		return finder != null;
	}

	/**
	 * Convenience method to find location with specified auxilary network location delay time.
	 */
	public boolean find (int auxDelay) {
		setAuxDelay( auxDelay );
		return find();
	}

	/**
	 * Convenience method to find location with specified settings.
	 */
	public boolean find (int auxDelay, int updates) {
		setAuxDelay( auxDelay );
		setUpdates( updates );
		return find();
	}

	/**
	 * Convenience method to find location with specified settings.
	 */
	public boolean find (int auxDelay, int updates, int timeout) {
		setAuxDelay( auxDelay );
		setUpdates( updates );
		setTimeout( timeout );
		return find();
	}

	/**
	 * Find fine location only.
	 *
	 * @return <code>true</code> if the provider is enabled and finding location; otherwise <code>false</code>
	 */
	public boolean findFine () {
		clear();
		finder = new Finder().startFine( false );
		return finder != null;
	}

	/**
	 * Find coarse location only.
	 * 
	 * @return <code>true</code> if the provider is enabled and finding location; otherwise <code>false</code>
	 */
	public boolean findCoarse () {
		clear();
		finder = new Finder().startCoarse( false );
		return finder != null;
	}

	/**
	 * Cancel currently running task.
	 */
	public void cancel () {
		dismissTimeout();

		if (finder != null) {
			finder.cancel();
			finder = null;
		}
	}

	/**
	 * Clear all listeners.
	 */
	private void clear () {
		if (finder != null)
			finder.clear();
	}

	/**
	 * Finder class does the actual work.
	 */
	private class Finder implements android.location.LocationListener, Cancellable, Runnable {

		/**
		 * Accuracy in meters. This will become a negative value of same
		 * number of location is found or the request is cancelled.
		 */
		private int accuracy = FINE_METERS;

		private final Finder parent;

		private Finder aux;

		/**
		 * Constructor, default.
		 */
		private Finder () {
			parent = null;
		}

		/**
		 * Constructor, private.
		 */
		private Finder (Finder parent) {
			this.parent = parent;
		}

		/**
		 * Start the location listener for fine and then coarse location.
		 */
		private Finder start () {

			// if GPS is on start listener
			if (startFine(true) != null) {
				return this;
			}
			else {
				Log.i( Kit.TAG, "GPS PROVIDER is disabled" );

				Finder simloc = startCoarse(true);  // start secondary locator
				if (simloc != null)
					return simloc;
				else
					Log.i( Kit.TAG, "NETWORK PROVIDER is disabled" );
			}

			return null;
		}

		/**
		 * Start the location listener for fine location.
		 */
		private Finder startFine (boolean launchAux) {
			if (!mgr.isProviderEnabled( LocationManager.GPS_PROVIDER )) return null;

			Log.i( Kit.TAG, "Attaching GPS location listener..." );
			mgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, updates <= 0 ? GPS_DELAY_MILLIS : updates, fineMeters, this );

			if (launchAux) {
				if (mgr.isProviderEnabled( LocationManager.NETWORK_PROVIDER ))
					handler.postDelayed( this, auxDelay );
				else
					Log.i( Kit.TAG, "NETWORK PROVIDER is disabled" );
			}

			// start timeout listener
			if (timeout > 0)
				handler.postDelayed( timeoutHandler = new TimeoutHandler(), timeout );

			return this;
		}

		/**
		 * Start the location listener for coarse location.
		 */
		private Finder startCoarse (boolean handleTimeout) {
			if (!mgr.isProviderEnabled( LocationManager.NETWORK_PROVIDER )) return null;

			accuracy = COARSE_METERS;

			if (handleTimeout && timeout > 0) {
				// start timeout listener
				handler.postDelayed( timeoutHandler = new TimeoutHandler(), timeout );
			}

			Log.i( Kit.TAG, "Attaching secondary COARSE listener..." );
			mgr.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, updates <= 0 ? GPS_DELAY_MILLIS : updates, coarseMeter, this );

			return this;
		}

		/**
		 * Start the network location detection after a delayed time.
		 */
		public void run () {
			if (accuracy <= 0) return;

			Log.i( Kit.TAG, "Received callback to launch auxilary listener..." );

			aux = new Finder(this).startCoarse( false );
		}

		/**
		 * On Location Change
		 */
		public void onLocationChanged (Location loc) {

			if (loc.getAccuracy() > accuracy)
				return;

			Log.d( Kit.TAG, ( provider(loc) + " Location found with accuracy " + loc.getAccuracy() ) );

			if (updates <= 0)
				finish( loc );
			else
				update( loc );

		}

		public void onStatusChanged (String s, int i, Bundle bundle) {
		}

		public void onProviderEnabled (String provider) {
		}

		public void onProviderDisabled (String provider) {
		}

		/**
		 * Cancel the request or a thread.
		 * <p/>
		 * Note: This method does not guarentee immediate
		 * cancellation, but may take a while to effectively
		 * cancel the request.
		 */
		public void cancel () {
			dismissTimeout();

			accuracy = -Math.abs( accuracy );  // make negative after location found.

			// avoid calling cancel of the
			if (parent != null) {
				parent.cancel();
				return;
			}

			Log.i( Kit.TAG, "Detaching location listeners..." );

			if (aux != null) {
				Log.i( Kit.TAG, "Detaching COARSE listener..." );
				mgr.removeUpdates( aux );
			}

			Log.i( Kit.TAG, "Detaching GPS listener..." );
			mgr.removeUpdates( this );
		}

		/**
		 * Clear listeners.
		 */
		private void clear () {
			if (parent != null) mgr.removeUpdates( parent );
			if (aux != null) mgr.removeUpdates( aux );

			accuracy = Math.abs( accuracy );

			mgr.removeUpdates( this );
			dismissTimeout();

			result = null;
		}

	}

	/**
	 * Try to get the best and the latest last location.
	 * Compare the dates on last locations and return the latest one.
	 *
	 * @param savedLocation optional parameter to include in the comparison of other last locations
	 *   Note: Make sure this provided location has time in it, otherwise it becomes useless.
	 *
	 * @return Location object if any found; otherwise <code>null</code>.
	 */
	public Location getLastKnownLocation (Location savedLocation) {
		Log.d( Kit.TAG, "LocationFinder.getLastKnownLocation()" );

		final LocationManager mgr = (LocationManager)Kit.getSystemService( Context.LOCATION_SERVICE );

		Location net = Kit.isProviderSupported( LocationManager.NETWORK_PROVIDER ) ? mgr.getLastKnownLocation( LocationManager.NETWORK_PROVIDER ) : null;
		Location gps = Kit.isProviderSupported( LocationManager.GPS_PROVIDER ) ? mgr.getLastKnownLocation( LocationManager.GPS_PROVIDER ) : null;

		logd( net, LocationManager.NETWORK_PROVIDER );
		logd( gps, LocationManager.GPS_PROVIDER );
		logd( savedLocation, "stored" );

		Location last = Collections.max( Arrays.asList( savedLocation, net, gps ), this );
		logd( last, "Return last known" );
		return last;
	}

	/**
	 * Log message.
	 */
	private void logd (Location loc, String name) {
		Log.d( Kit.TAG, name + " location: "
				+ (loc == null ? null : String.format( "[%.5f, %.5f / %3$tm %<te,%<tY %<tT]",
											loc.getLatitude(), loc.getLongitude(), loc.getTime() ) ));
	}

	/**
	 * Compare two locations based on recorded time.
	 */
	public int compare (Location l1, Location l2) {
		if (l1 == l2) return 0;
		if (l1 == null) return -1;
		if (l2 == null) return 1;

		return (l1.getTime()<l2.getTime() ? -1 : 1);
	}

	private String provider (Location loc) {
		return loc == null ? "UNKNOWN" : (LocationManager.GPS_PROVIDER.equals(loc.getProvider()) ? "FINE" : "COARSE");
	}

	/**
	 * Callback method called when successful.
	 *
	 * @param result Result of type <code><T></code>
	 */
	protected void update (Location result) {
		this.result = result;

		if (result != null) {
			Log.d( Kit.TAG, String.format( "%s Location found [lat:%f, lon:%f] with accuracy %f",
					LocationManager.GPS_PROVIDER.equals(result.getProvider()) ? "FINE" : "COARSE",
					result.getLatitude(), result.getLongitude(), result.getAccuracy() ) );
		}
		else
			Log.d( Kit.TAG, "NO LOCATION FOUND." );

		fireLocationUpdate();
	}

	/**
	 * Callback method called when successful.
	 *
	 * @param result Result of type <code><T></code>
	 */
	protected void finish (Location result) {
		cancel();
		update( result );
	}

	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.
	 *
	 * @see EventListenerList
	 */
	protected void fireLocationUpdate () {
		if (listeners == null) return;

		// Guaranteed to return a non-null array
		Object[] listeners = this.listeners.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == LocatorListener.class) {
				if (result != null)
					((LocatorListener) listeners[i + 1]).locationFound( result );
				else
					((LocatorListener) listeners[i + 1]).locationFailed();
			}
		}
	}

	/**
	 * Get any found location.
	 */
	public Location getLocation () {
		return result;
	}

	/**
	 * Check to see if a location was found or not.
	 */
	public boolean found () {
		return result != null;
	}

	/**
	 * Check to see if a location finder is currently running.
	 */
	public boolean finding () {
		return finder != null;
	}

	/**
	 * Set the accuracy in meters for coarse location finder.
	 * @param coarseMeter accuracy in meters
	 */
	public void setCoarseAccuracy (int coarseMeter) {
		this.coarseMeter = coarseMeter;
	}

	/**
	 * Set the accuracy in meters for fine location finder.
	 * @param fineMeters accuracy in meters
	 */
	public void setFineAccuracy (int fineMeters) {
		this.fineMeters = fineMeters;
	}

	/**
	 * Set listener timeout in milliseconds.
	 * @param timeout timeout in milliseconds
	 */
	public void setTimeout (int timeout) {
		this.timeout = timeout;
	}

	/**
	 * Set update milliseconds to get a scheduled callbacks.
	 * @param millis updat time in milliseconds
	 */
	public void setUpdates (int millis) {
		this.updates = millis;
	}

	/**
	 * Set listener timeout in milliseconds.
	 * @param timeout timeout in milliseconds
	 * @param listener location timeout handler
	 */
	public void setTimeout (int timeout, LocatorTimeout listener) {
		this.timeout = timeout;
		this.locatorTimeout = listener;
	}

	/**
	 * Set time to start the auxilary locator.
	 * @param auxDelay auxilary locator starting time
	 */
	public void setAuxDelay (int auxDelay) {
		this.auxDelay = auxDelay;
	}

	/**
	 * Add a locator listener
	 * @param listener LocatorListener
	 */
	public void addLocatorListener (LocatorListener listener) {
		if (listener == null) return;

		if (listeners == null)
			listeners = new EventListenerList();

		listeners.add( LocatorListener.class, listener );
	}

	/**
	 * Remove the specified <code>LocatorListener</code>.
	 * @param listener LocatorListener
	 */
	public void removeLocatorListener (LocatorListener listener) {
		if (listener != null && listeners != null)
			listeners.remove( LocatorListener.class, listener );
	}

	/**
	 * Set the location timeout handler.
	 *
	 * @param locatorTimeout location timeout handler
	 */
	public void setTimeoutListener (LocatorTimeout locatorTimeout) {
		this.locatorTimeout = locatorTimeout;
	}

	/**
	 * Dismiss the timeout handler
	 */
	private void dismissTimeout () {
		timeoutHandler = null;
	}

	/**
	 * Timeout message handler
	 */
	public class TimeoutHandler implements Runnable {

		/**
		 * Show timeout dialog
		 */
		public void run () {
			if (timeoutHandler == null) return;

			if (locatorTimeout != null && !locatorTimeout.locationTimeout())
				handler.postDelayed( this, timeout );
			else if (timeoutHandler != null)
				finish( null );
		}
	}

}
