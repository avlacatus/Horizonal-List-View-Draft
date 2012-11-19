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

package com.sileria.android.event;

import android.view.View;

import java.util.Set;
import java.util.LinkedHashSet;

/**
 * AbstractDelayedCallback
 *
 * @author Ahmed Shakil
 * @date Sep 18, 2007
 */

public abstract class AbstractDelayedCallback {

	/**
	 * Timer used for the delay.
	 */
	protected Timer timer;

	/**
	 * Action to invoke when delay is reached.
	 */
	protected ActionListener action;

	/**
	 * Delay value
	 */
	protected int delay = DELAY;

	/**
	 * Default delay in milliseconds.
	 */
	protected static final int DELAY = 300;

	/**
	 * Components are maintained in this list each time
	 * they trigger the input event until the action listener
	 * is fired and set is flushed out.
	 */
	private Set<View> viewset = new LinkedHashSet<View>(1);


	/**
	 * Constructs a delayed callback object with the specified
	 * <code>action</code> to be invoked when the delay happens.
	 * <p/>
	 * The default value of delay will be used.
	 *
	 * @param action <code>Command</code> to be invoked.
	 */
	protected AbstractDelayedCallback (ActionListener action) {
		this.action = action;
	}

	/**
	 * Constructs a delayed callback object with the specified
	 * <code>action</code> to be invoked when the specified
	 * <code>delay</code> is reached.
	 *
	 * @param action <code>ActionListener</code> to be invoked
	 * @param delay Delay in milliseconds to wait before calling the action
	 */
	protected AbstractDelayedCallback (ActionListener action, int delay) {
		this.action = action;
		this.delay = delay;
	}

	/**
	 * Resets the timer to start counting again.
	 * This method should be called from
	 * the listener calls in the subclasses.
	 *
	 * @param source Component that triggered the event
	 */
	protected void updateTimer (View source) {

		viewset.add( source );

		if (delay == 0) {
			fireActionPerformed();
		}
		else if (timer == null) {
			timer = new Timer( delay, action );
			timer.setRepeats( false );
			timer.start();
		}
		else
			timer.restart();
	}

	/**
	 * Trigger the action peformed event.
	 */
	private void fireActionPerformed () {

		if (action != null) {
			for (View source : viewset)
				action.onAction();
		}

		viewset.clear();

	}

	/**
	 * Timer class subclassed from com.sileria.android.Timer to fire
	 * ActionEvent with action component as the source.
	 */
	private class Timer extends com.sileria.android.Timer {

		/**
		 * Construct a swing timer object.
		 * @param delay milliseconds for the initial and between-event delay
		 * @param listener  an initial listener; can be <code>null</code>
		 */
		private Timer (int delay, ActionListener listener) {
			super( delay, listener );
		}

		/**
		 * Notifies all listeners that have registered interest for
		 * notification on this event type.
		 */
		@Override
		protected void fireActionPerformed () {
			AbstractDelayedCallback.this.fireActionPerformed();
		}
	}
}
