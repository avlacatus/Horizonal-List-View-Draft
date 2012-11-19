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

package com.sileria.android;

import android.os.*;
import android.util.Log;

import com.sileria.android.event.ActionListener;
import com.sileria.util.EventListenerList;

import java.io.Serializable;
import java.util.EventListener;

/**
 * Fires one or more {@code ActionEvent}s at specified
 * intervals. An example use is an animation object that uses a
 * <code>Timer</code> as the trigger for drawing its frames.
 * <p/>
 * Setting up a timer
 * involves creating a <code>Timer</code> object,
 * registering one or more action listeners on it,
 * and starting the timer using
 * the <code>start</code> method.
 * For example,
 * the following code creates and starts a timer
 * that fires an action event once per second
 * (as specified by the first argument to the <code>Timer</code> constructor).
 * The second argument to the <code>Timer</code> constructor
 * specifies a listener to receive the timer's action events.
 * <p/>
 * <pre>
 *  int delay = 1000; //milliseconds
 *  ActionListener taskPerformer = new ActionListener() {
 *      public void onAction () {
 *          <em>//...Perform a task...</em>
 *	  }
 *  };
 *  new Timer(delay, taskPerformer).start();</pre>
 * <p/>
 * <p/>
 * {@code Timers} are constructed by specifying both a delay parameter
 * and an {@code Runnable}. The delay parameter is used
 * to set both the initial delay and the delay between event
 * firing, in milliseconds. Once the timer has been started,
 * it waits for the initial delay before firing its
 * first <code>ActionEvent</code> to registered listeners.
 * After this first event, it continues to fire events
 * every time the between-event delay has elapsed, until it
 * is stopped.
 * <p/>
 * After construction, the initial delay and the between-event
 * delay can be changed independently, and additional
 * <code>ActionListeners</code> may be added.
 * <p/>
 * If you want the timer to fire only the first time and then stop,
 * invoke <code>setRepeats(false)</code> on the timer.
 * <p/>
 * Although all <code>Timer</code>s perform their waiting
 * using a single, shared thread
 * (created by the first <code>Timer</code> object that executes),
 * the action event handlers for <code>Timer</code>s
 * execute on another thread -- the event-dispatching thread.
 * This means that the action handlers for <code>Timer</code>s
 * can safely perform operations on Swing components.
 * However, it also means that the handlers must execute quickly
 * to keep the GUI responsive.
 * <p/>
 * <p/>
 *
 * @author Dave Moore (Original Author)
 * @author Ahmed Shakil (Ported to Android)
 *
 * @see java.util.Timer <code>java.util.Timer</code>
 */
public class Timer implements Serializable {

	private final Handler handler;

	protected EventListenerList listenerList = new EventListenerList();

	// The following field strives to maintain the following:
	//    If coalesce is true, only allow one Runnable to be queued on the
	//    EventQueue and be pending (ie in the process of notifying the
	//    Runnable). If we didn't do this it would allow for a
	//    situation where the app is taking too long to process the
	//    actionPerformed, and thus we'ld end up queing a bunch of Runnables
	//    and the app would never return: not good. This of course implies
	//    you can get dropped events, but such is life.
	// notify is used to indicate if the Runnable can be notified, when
	// the Runnable is processed if this is true it will notify the listeners.
	// notify is set to true when the Timer fires and the Runnable is queued.
	// It will be set to false after notifying the listeners (if coalesce is
	// true) or if the developer invokes stop.
	private boolean notify = false;

	private int initialDelay, delay;
	private boolean repeats = true, coalesce = true;

	private Runnable doPostEvent = null;

	private boolean running;

	private static boolean logTimers;

	/**
	 * Creates a {@code Timer} with your own {@code handler} instance.
	 * The constructor initializes both the initial delay and
	 * between-event delay to {@code delay} milliseconds. If {@code delay}
	 * is less than or equal to zero, the timer fires as soon as it
	 * is started. If <code>listener</code> is not <code>null</code>,
	 * it's registered as an action listener on the timer.
	 *
	 * @param delay	milliseconds for the initial and between-event delay
	 * @param listener an initial listener; can be <code>null</code>
	 * @see #addActionListener
	 * @see #setInitialDelay
	 * @see #setRepeats
	 */
	public Timer (Handler handler, int delay, ActionListener listener) {
		this.handler = handler == null ? new Handler() : handler;
		this.delay = delay;
		this.initialDelay = delay;

		if (listener != null)
			addActionListener( listener );
	}

	/**
	 * Creates a {@code Timer} with your own {@code looper} to create a Handler
	 * instance. The constructor initializes both the initial delay and
	 * between-event delay to {@code delay} milliseconds. If {@code delay}
	 * is less than or equal to zero, the timer fires as soon as it
	 * is started. If <code>listener</code> is not <code>null</code>,
	 * it's registered as an action listener on the timer.
	 *
	 * @param delay	milliseconds for the initial and between-event delay
	 * @param listener an initial listener; can be <code>null</code>
	 * @see #addActionListener
	 * @see #setInitialDelay
	 * @see #setRepeats
	 */
	public Timer (Looper looper, int delay, ActionListener listener) {
		this( new Handler(looper), delay, listener );
	}

	/**
	 * Creates a {@code Timer} and initializes both the initial delay and
	 * between-event delay to {@code delay} milliseconds. If {@code delay}
	 * is less than or equal to zero, the timer fires as soon as it
	 * is started. If <code>listener</code> is not <code>null</code>,
	 * it's registered as an action listener on the timer.
	 *
	 * @param delay	milliseconds for the initial and between-event delay
	 * @param listener an initial listener; can be <code>null</code>
	 * @see #addActionListener
	 * @see #setInitialDelay
	 * @see #setRepeats
	 */
	public Timer (int delay, ActionListener listener) {
		this( new Handler(), delay, listener );
	}

	/**
	 * Creates a {@code Timer} and initializes both the initial delay and
	 * between-event delay to {@code delay} milliseconds. If {@code delay}
	 * is less than or equal to zero, the timer fires as soon as it
	 * is started. If <code>listener</code> is not <code>null</code>,
	 * it's registered as an action listener on the timer.
	 *
	 * This constructor allows you to trigger the timer once only or repeatedly.
	 *
	 * @param delay	milliseconds for the initial and between-event delay
	 * @param repeats repeat flag if the timer should repeat or run only once.
	 * @param listener an initial listener; can be <code>null</code>
	 * @see #addActionListener
	 * @see #setInitialDelay
	 * @see #setRepeats
	 */
	public Timer (int delay, boolean repeats, ActionListener listener) {
		this( delay, listener );
		this.repeats = repeats;
	}

	/**
	 * Timer queue implementation.
	 */
	class DoPostEvent implements Runnable {

		public void run () {
			if (running && doPostEvent==this) {
				if (!notify || !coalesce) {
					notify = true;
					post();
				}

				if (isRepeats())
					handler.postAtTime( this, SystemClock.uptimeMillis() + getDelay() );
				else
					running = false;
			}
		}
	}


	/**
	 * Adds an action listener to the <code>Timer</code>.
	 *
	 * @param listener the listener to add
	 * @see #Timer
	 */
	public void addActionListener (ActionListener listener) {
		listenerList.add( ActionListener.class, listener );
	}


	/**
	 * Removes the specified action listener from the <code>Timer</code>.
	 *
	 * @param listener the listener to remove
	 */
	public void removeActionListener (ActionListener listener) {
		listenerList.remove( ActionListener.class, listener );
	}


	/**
	 * Returns an array of all the action listeners registered
	 * on this timer.
	 *
	 * @return all of the timer's <code>Runnable</code>s or an empty
	 *         array if no action listeners are currently registered
	 * @see #addActionListener
	 * @see #removeActionListener
	 */
	public ActionListener[] getActionListeners () {
		return listenerList.getListeners( ActionListener.class );
	}


	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.
	 *
	 * @see EventListenerList
	 */
	protected void fireActionPerformed () {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).onAction();
			}
		}
	}

	/**
	 * Returns an array of all the objects currently registered as
	 * <code><em>Foo</em>Listener</code>s
	 * upon this <code>Timer</code>.
	 * <code><em>Foo</em>Listener</code>s
	 * are registered using the <code>add<em>Foo</em>Listener</code> method.
	 * <p/>
	 * You can specify the <code>listenerType</code> argument
	 * with a class literal, such as <code><em>Foo</em>Listener.class</code>.
	 * For example, you can query a <code>Timer</code>
	 * instance <code>t</code>
	 * for its action listeners
	 * with the following code:
	 * <p/>
	 * <pre>Runnable[] als = (Runnable[])(t.getListeners(Runnable.class));</pre>
	 * <p/>
	 * If no such listeners exist,
	 * this method returns an empty array.
	 *
	 * @param listenerType the type of listeners requested;
	 *                     this parameter should specify an interface
	 *                     that descends from <code>java.util.EventListener</code>
	 * @return an array of all objects registered as
	 *         <code><em>Foo</em>Listener</code>s
	 *         on this timer,
	 *         or an empty array if no such
	 *         listeners have been added
	 * @throws ClassCastException if <code>listenerType</code> doesn't
	 *                            specify a class or interface that implements
	 *                            <code>java.util.EventListener</code>
	 * @see #getActionListeners
	 * @see #addActionListener
	 * @see #removeActionListener
	 * @since 1.3
	 */
	public <T extends EventListener> T[] getListeners (Class<T> listenerType) {
		return listenerList.getListeners( listenerType );
	}

	/**
	 * Enables or disables the timer log. When enabled, a message
	 * is posted to <code>Log.d</code> whenever the timer goes off.
	 *
	 * @param flag <code>true</code> to enable logging
	 * @see #getLogTimers
	 */
	public static void setLogTimers (boolean flag) {
		logTimers = flag;
	}


	/**
	 * Returns <code>true</code> if logging is enabled.
	 *
	 * @return <code>true</code> if logging is enabled; otherwise, false
	 * @see #setLogTimers
	 */
	public static boolean getLogTimers () {
		return logTimers;
	}


	/**
	 * Sets the <code>Timer</code>'s between-event delay, the number of milliseconds
	 * between successive action events. This does not affect the initial delay
	 * property, which can be set by the {@code setInitialDelay} method.
	 *
	 * @param delay the delay in milliseconds
	 * @see #setInitialDelay
	 */
	public void setDelay (int delay) {
		if (delay < 0)
			throw new IllegalArgumentException( "Invalid delay: " + delay );
		else
			this.delay = delay;
	}


	/**
	 * Returns the delay, in milliseconds,
	 * between firings of action events.
	 *
	 * @see #setDelay
	 * @see #getInitialDelay
	 */
	public int getDelay () {
		return delay;
	}


	/**
	 * Sets the <code>Timer</code>'s initial delay, the time
	 * in milliseconds to wait after the timer is started
	 * before firing the first event. Upon construction, this
	 * is set to be the same as the between-event delay,
	 * but then its value is independent and remains unaffected
	 * by changes to the between-event delay.
	 *
	 * @param initialDelay the initial delay, in milliseconds
	 * @see #setDelay
	 */
	public void setInitialDelay (int initialDelay) {
		if (initialDelay < 0)
			throw new IllegalArgumentException( "Invalid initial delay: " +	initialDelay );
		else
			this.initialDelay = initialDelay;
	}


	/**
	 * Returns the <code>Timer</code>'s initial delay.
	 *
	 * @see #setInitialDelay
	 * @see #setDelay
	 */
	public int getInitialDelay () {
		return initialDelay;
	}


	/**
	 * If <code>flag</code> is <code>false</code>,
	 * instructs the <code>Timer</code> to send only one
	 * action event to its listeners.
	 *
	 * @param flag specify <code>false</code> to make the timer
	 *             stop after sending its first action event
	 */
	public void setRepeats (boolean flag) {
		repeats = flag;
	}


	/**
	 * Returns <code>true</code> (the default)
	 * if the <code>Timer</code> will send
	 * an action event
	 * to its listeners multiple times.
	 *
	 * @see #setRepeats
	 */
	public boolean isRepeats () {
		return repeats;
	}


	/**
	 * Sets whether the <code>Timer</code> coalesces multiple pending
	 * <code>ActionEvent</code> firings.
	 * A busy application may not be able
	 * to keep up with a <code>Timer</code>'s event generation,
	 * causing multiple
	 * action events to be queued.  When processed,
	 * the application sends these events one after the other, causing the
	 * <code>Timer</code>'s listeners to receive a sequence of
	 * events with no delay between them. Coalescing avoids this situation
	 * by reducing multiple pending events to a single event.
	 * <code>Timer</code>s
	 * coalesce events by default.
	 *
	 * @param flag specify <code>false</code> to turn off coalescing
	 */
	public void setCoalesce (boolean flag) {
		boolean old = coalesce;
		coalesce = flag;
		if (!old && coalesce) {
			// We must do this as otherwise if the Timer once notified
			// in !coalese mode notify will be stuck to true and never
			// become false.
			cancelEvent();
		}
	}


	/**
	 * Returns <code>true</code> if the <code>Timer</code> coalesces
	 * multiple pending action events.
	 *
	 * @see #setCoalesce
	 */
	public boolean isCoalesce () {
		return coalesce;
	}

	/**
	 * Starts the <code>Timer</code>,
	 * causing it to start sending action events
	 * to its listeners.
	 *
	 * @see #stop
	 */
	public void start () {
		running = true;
		handler.postAtTime( doPostEvent = new DoPostEvent(), SystemClock.uptimeMillis() + getInitialDelay() );
	}


	/**
	 * Returns <code>true</code> if the <code>Timer</code> is running.
	 *
	 * @see #start
	 */
	public boolean isRunning () {
		return running;
	}


	/**
	 * Stops the <code>Timer</code>,
	 * causing it to stop sending action events
	 * to its listeners.
	 *
	 * @see #start
	 */
	public void stop () {
		running = false;
		cancelEvent();
	}


	/**
	 * Restarts the <code>Timer</code>,
	 * canceling any pending firings and causing
	 * it to fire with its initial delay.
	 */
	public void restart () {
		handler.removeCallbacks( doPostEvent );

		stop();
		start();
	}


	/**
	 * Resets the internal state to indicate this Timer shouldn't notify
	 * any of its listeners. This does not stop a repeatable Timer from
	 * firing again, use <code>stop</code> for that.
	 */
	void cancelEvent () {
		notify = false;
	}


	void post () {
		if (logTimers)
			Log.d( Kit.TAG, "Timer ringing: " + Timer.this );

		if (notify) {
			fireActionPerformed();
			if (coalesce) {
				cancelEvent();
			}
		}
	}
}
