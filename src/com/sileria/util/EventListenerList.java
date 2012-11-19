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

package com.sileria.util;

import java.io.*;
import java.lang.reflect.Array;

/**
 * EventListenerList; a copy of {@link javax.swing.event.EventListenerList}.
 * <p/>
 * A class that holds a list of EventListeners.  A single instance
 * can be used to hold all listeners (of all types) for the instance
 * using the list.  It is the responsiblity of the class using the
 * EventListenerList to provide type-safe API (preferably conforming
 * to the JavaBeans spec) and methods which dispatch event notification
 * methods to appropriate Event Listeners on the list.
 * <p/>
 * The main benefits that this class provides are that it is relatively
 * cheap in the case of no listeners, and it provides serialization for
 * event-listener lists in a single place, as well as a degree of MT safety
 * (when used correctly).
 * <p/>
 * Usage example:
 * Say one is defining a class that sends out FooEvents, and one wants
 * to allow users of the class to register FooListeners and receive
 * notification when FooEvents occur.  The following should be added
 * to the class definition:
 * <pre>
 * EventListenerList listenerList = new EventListenerList();
 * FooEvent fooEvent = null;
 *
 * public void addFooListener(FooListener l) {
 *     listenerList.add(FooListener.class, l);
 * }
 *
 * public void removeFooListener(FooListener l) {
 *     listenerList.remove(FooListener.class, l);
 * }
 *
 *
 * // Notify all listeners that have registered interest for
 * // notification on this event type.  The event instance
 * // is lazily created using the parameters passed into
 * // the fire method.
 *
 * protected void fireFooXXX() {
 *     // Guaranteed to return a non-null array
 *     Object[] listeners = listenerList.getListenerList();
 *     // Process the listeners last to first, notifying
 *     // those that are interested in this event
 *     for (int i = listeners.length-2; i>=0; i-=2) {
 *         if (listeners[i]==FooListener.class) {
 *             // Lazily create the event:
 *             if (fooEvent == null)
 *                 fooEvent = new FooEvent(this);
 *             ((FooListener)listeners[i+1]).fooXXX(fooEvent);
 *		 }
 *	 }
 * }
 * </pre>
 * foo should be changed to the appropriate name, and fireFooXxx to the
 * appropriate method name.  One fire method should exist for each
 * notification method in the FooListener interface.
 * <p/>
 *
 * @author Georges Saab
 * @author Hans Muller
 * @author James Gosling
 * @version 1.37 11/17/05
 */
public class EventListenerList implements Serializable {

	/* The list of ListenerType - Listener pairs */
	protected transient Object[] listenerList = Utils.EMPTY_OBJECT_ARRAY;

	/**
	 * Passes back the event listener list as an array
	 * of ListenerType-listener pairs.  Note that for
	 * performance reasons, this implementation passes back
	 * the actual data structure in which the listener data
	 * is stored internally!
	 * This method is guaranteed to pass back a non-null
	 * array, so that no null-checking is required in
	 * fire methods.  A zero-length array of Object should
	 * be returned if there are currently no listeners.
	 * <p/>
	 * WARNING!!! Absolutely NO modification of
	 * the data contained in this array should be made -- if
	 * any such manipulation is necessary, it should be done
	 * on a copy of the array returned rather than the array
	 * itself.
	 */
	public Object[] getListenerList () {
		return listenerList;
	}

	/**
	 * Return an array of all the listeners of the given type.
	 *
	 * @return all of the listeners of the specified type.
	 * @throws ClassCastException if the supplied class is not assignable to EventListener
	 */
	@SuppressWarnings( "unchecked" )
	public <T> T[] getListeners (Class<T> t) {
		Object[] lList = listenerList;
		int n = getListenerCount( lList, t );
		T[] result = (T[])Array.newInstance( t, n );
		int j = 0;
		for (int i = lList.length - 2; i >= 0; i -= 2) {
			if (lList[i] == t) {
				result[j++] = (T)lList[i + 1];
			}
		}
		return result;
	}

	/**
	 * Returns the total number of listeners for this listener list.
	 */
	public int getListenerCount () {
		return listenerList.length / 2;
	}

	/**
	 * Returns the total number of listeners of the supplied type
	 * for this listener list.
	 */
	public int getListenerCount (Class<?> t) {
		Object[] lList = listenerList;
		return getListenerCount( lList, t );
	}

	/**
	 * Listener count
	 */
	private int getListenerCount (Object[] list, Class t) {
		int count = 0;
		for (int i = 0; i < list.length; i += 2) {
			if (t == list[i])
				count++;
		}
		return count;
	}

	/**
	 * Adds the listener as a listener of the specified type.
	 *
	 * NOTE: This method does not allow same listener for
	 * the same class type to be added more than once.
	 * Same listener can be added for a different class type.
	 *
	 * @param t the type of the listener to be added
	 * @param l the listener to be added
	 */
	public synchronized <T> void add (Class<T> t, T l) {
		if (l == null) {
			// In an ideal world, we would do an assertion here
			// to help developers know they are probably doing
			// something wrong
			return;
		}
		if (!t.isInstance( l )) {
			throw new IllegalArgumentException( "Listener " + l + " is not of type " + t );
		}
		if (listenerList == Utils.EMPTY_OBJECT_ARRAY) {
			// if this is the first listener added,
			// initialize the lists
			listenerList = new Object[]{t, l};
		}
		else {
			if (indexOf( t, l ) >= 0) return;    // if already exist then do not do anything.

			// Otherwise copy the array and add the new listener
			int i = listenerList.length;
			Object[] tmp = new Object[i + 2];
			System.arraycopy( listenerList, 0, tmp, 0, i );

			tmp[i] = t;
			tmp[i + 1] = l;

			listenerList = tmp;
		}
	}

	/**
	 * Removes the listener as a listener of the specified type.
	 *
	 * @param t the type of the listener to be removed
	 * @param l the listener to be removed
	 */
	public synchronized <T> void remove (Class<T> t, T l) {
		if (l == null) {
			// In an ideal world, we would do an assertion here
			// to help developers know they are probably doing
			// something wrong
			return;
		}
		if (!t.isInstance( l )) {
			throw new IllegalArgumentException( "Listener " + l +
					" is not of type " + t );
		}

		// Is l on the list?
		int index = indexOf( t, l );

		// If so,  remove it
		if (index != -1) {
			Object[] tmp = new Object[listenerList.length - 2];
			// Copy the list up to index
			System.arraycopy( listenerList, 0, tmp, 0, index );
			// Copy from two past the index, up to
			// the end of tmp (which is two elements
			// shorter than the old list)
			if (index < tmp.length)
				System.arraycopy( listenerList, index + 2, tmp, index,
						tmp.length - index );
			// set the listener array to the new array or null
			listenerList = (tmp.length == 0) ? Utils.EMPTY_OBJECT_ARRAY : tmp;
		}
	}

	/**
	 * Remove all listeners.
	 */
	public synchronized void clear () {
		listenerList = Utils.EMPTY_OBJECT_ARRAY;
	}

	/**
	 * Is l on the list?
	 */
	private <T> int indexOf (Class<T> t, T l) {
		for (int i = listenerList.length - 2; i >= 0; i -= 2) {
			if ((listenerList[i] == t) && (listenerList[i + 1].equals( l ))) {
				return i;
			}
		}

		return -1;
	}

	// Serialization support.

	private void writeObject (ObjectOutputStream s) throws IOException {
		Object[] lList = listenerList;
		s.defaultWriteObject();

		// Save the non-null event listeners:
		for (int i = 0; i < lList.length; i += 2) {
			Class t = (Class)lList[i];
			Object l = lList[i + 1];
			if ((l != null) && (l instanceof Serializable)) {
				s.writeObject( t.getName() );
				s.writeObject( l );
			}
		}

		s.writeObject( null );
	}

	@SuppressWarnings( "unchecked" )
	private void readObject (ObjectInputStream s)
			throws IOException, ClassNotFoundException {
		listenerList = Utils.EMPTY_OBJECT_ARRAY;
		s.defaultReadObject();
		Object listenerTypeOrNull;

		while (null != (listenerTypeOrNull = s.readObject())) {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Object l = s.readObject();
			add( (Class<Object>)Class.forName( (String)listenerTypeOrNull, true, cl ), l );
		}
	}

	/**
	 * Returns a string representation of the EventListenerList.
	 */
	public String toString () {
		Object[] lList = listenerList;
		String s = "EventListenerList: ";
		s += lList.length / 2 + " listeners: ";
		for (int i = 0; i <= lList.length - 2; i += 2) {
			s += " type " + ((Class)lList[i]).getName();
			s += " listener " + lList[i + 1];
		}
		return s;
	}
}
