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

import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.*;

/**
 * <code>StringProperties<code> is a wrapper class for {@link java.util.Properties} which has pure
 * <code>String</code> based key-value pair implementation. Your are not allowed to insert non-string
 * keys or values to the internal <code>Properties</code> object.
 * <p/>
 * Apart from being a wrapper for the <code>Properties</code>, this class provides following convenience
 * funtionalities:
 * <p/>
 * It can also be used as a wrapper for your own <code>Map<String, String></code> instance object.
 * <p/>
 * There are convinience methods for getting and setting Java's primitive types as values.
 * <p/>
 * A {@linkplain PropertyChangeListener} can be added to this class to received {@linkplain PropertyChangeEvent}s.
 * <p/>
 *
 * @author Ahmed Shakil
 * @version 1.0
 * @date Feb 4, 2007
 */
public class StringProperties {

	/** Properties */
	private Map<String, String> map;

	/** Properties */
	private Properties properties;

	/** Event listener list */
	private EventListenerList listenerList = new EventListenerList();

	/**
	 * Constructs a <code>StringProperties</code> class wrapping a new <code>Properties</code> object.
	 */
	public StringProperties () {
		this( new Properties() );
	}

	/**
	 * Constructs a <code>StringProperties<code> wrapper with the specified <code>map</code>.
	 * <p/>
	 * <i>Note: If this constructor is used then the <code>protected</code> member variable</i>
	 * <code>properties</code> will always be <code>null</code>.
	 * @param map Map of <code>String</code> keys and values.
	 */
	public StringProperties (Map<String, String> map) {
		this.map = map;
	}

	/**
	 * Constructs a <code>StringProperties</code> with the specified <code>properties</code> object.
	 * <p/>
	 * <i>Note: Be careful when using this constructor not to provide a <code>properties</code> map
	 * with non <code>String</code> keys or values in it.</i>
	 * @param properties <code>Properties</code>.
	 */
	public StringProperties (Properties properties) {
		@SuppressWarnings( "unchecked" )
		Map<String, String> map = (Map)properties;
		this.map = map;
		this.properties = properties;
	}

	/**
	 * Associates the specified value with the specified key in this
	 * preference node.
	 *
	 * @param key key with which the specified value is to be associated.
	 * @param value value to be associated with the specified key.
	 * @return     the previous value of the specified key in this property
	 *             list, or <code>null</code> if it did not have one.
	 */
	public final String put (String key, String value) {
		String oldValue = map.put( key, value );
		firePropertyChange( key, oldValue, value );
		return oldValue;
	}

	/**
	 * Returns the value associated with the specified key in this preference
	 * node.  Returns <tt>null</tt> if there is no value associated
	 * with the key, or the backing store is inaccessible.
	 *
	 * @param key key whose associated value is to be returned.
	 * @return the value associated with <tt>key</tt>, or <tt>def</tt>
	 *         if no value is associated with <tt>key</tt>, or the backing
	 *         store is inaccessible.
	 */
	public final String get (String key) {
		return map.get( key );
	}

	/**
	 * Returns the value associated with the specified key in this preference
	 * node.  Returns the specified default if there is no value associated
	 * with the key, or the backing store is inaccessible.
	 *
	 * <p/>Some implementations may store default values in their backing
	 * stores.  If there is no value associated with the specified key
	 * but there is such a <i>stored default</i>, it is returned in
	 * preference to the specified default.
	 *
	 * @param key key whose associated value is to be returned.
	 * @param def the value to be returned in the event that this
	 *        preference node has no value associated with <tt>key</tt>.
	 * @return the value associated with <tt>key</tt>, or <tt>def</tt>
	 *         if no value is associated with <tt>key</tt>, or the backing
	 *         store is inaccessible.
	 */
	public final String get (String key, String def) {
		String val = map.get( key );
		return (val == null) ? def : val;
	}

	/**
	 * Associates a string representing the specified int value with the
	 * specified key in this preference node.  The associated string is the
	 * one that would be returned if the int value were passed to
	 * {@link Integer#toString(int)}.  This method is intended for use in
	 * conjunction with {@link #get}.
	 *
	 * @param key key with which the string form of value is to be associated.
	 * @param value value whose string form is to be associated with key.
	 */
	public final void put (String key, int value) {
		put( key, Integer.toString(value) );
	}

	/**
	 * Returns the int value represented by the string associated with the
	 * specified key in this preference node.  The string is converted to
	 * an integer as by {@link Integer#parseInt(String)}.  Returns the
	 * specified default if there is no value associated with the key,
	 * the backing store is inaccessible, or if
	 * <tt>Integer.parseInt(String)</tt> would throw a {@link
	 * NumberFormatException} if the associated value were passed.  This
	 * method is intended for use in conjunction with {@link #put}.
	 *
	 * <p/>If the implementation supports <i>stored defaults</i> and such a
	 * default exists, is accessible, and could be converted to an int
	 * with <tt>Integer.parseInt</tt>, this int is returned in preference to
	 * the specified default.
	 *
	 * @param key key whose associated value is to be returned as an int.
	 * @param def the value to be returned in the event that this
	 *        preference node has no value associated with <tt>key</tt>
	 *        or the associated value cannot be interpreted as an int,
	 *        or the backing store is inaccessible.
	 * @return the int value represented by the string associated with
	 *         <tt>key</tt> in this preference node, or <tt>def</tt> if the
	 *         associated value does not exist or cannot be interpreted as
	 *         an int.
	 */
	public final int get (String key, int def) {
		String s = map.get( key );
		if (s == null || s.length()==0)	return def;

		try {
			return Integer.parseInt( s );
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * Associates a string representing the specified long value with the
	 * specified key in this preference node.  The associated string is the
	 * one that would be returned if the long value were passed to
	 * {@link Long#toString(long)}.  This method is intended for use in
	 * conjunction with {@link #get}.
	 *
	 * @param key key with which the string form of value is to be associated.
	 * @param value value whose string form is to be associated with key.
	 */
	public final void put (String key, long value) {
		put( key, Long.toString(value) );
	}

	/**
	 * Returns the long value represented by the string associated with the
	 * specified key in this preference node.  The string is converted to
	 * a long as by {@link Long#parseLong(String)}.  Returns the
	 * specified default if there is no value associated with the key,
	 * the backing store is inaccessible, or if
	 * <tt>Long.parseLong(String)</tt> would throw a {@link
	 * NumberFormatException} if the associated value were passed.  This
	 * method is intended for use in conjunction with {@link #put}.
	 *
	 * <p/>If the implementation supports <i>stored defaults</i> and such a
	 * default exists, is accessible, and could be converted to a long
	 * with <tt>Long.parseLong</tt>, this long is returned in preference to
	 * the specified default.
	 *
	 * @param key key whose associated value is to be returned as a long.
	 * @param def the value to be returned in the event that this
	 *        preference node has no value associated with <tt>key</tt>
	 *        or the associated value cannot be interpreted as a long,
	 *        or the backing store is inaccessible.
	 * @return the long value represented by the string associated with
	 *         <tt>key</tt> in this preference node, or <tt>def</tt> if the
	 *         associated value does not exist or cannot be interpreted as
	 *         a long.
	 */
	public final long get (String key, long def) {
		String s = map.get( key );
		if (s == null || s.length()==0)	return def;

		try {
			return Long.parseLong( s );
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * Associates a string representing the specified boolean value with the
	 * specified key in this preference node.  The associated string is
	 * <tt>"true"</tt> if the value is true, and <tt>"false"</tt> if it is
	 * false.  This method is intended for use in conjunction with
	 * {@link #get}.
	 *
	 * @param key key with which the string form of value is to be associated.
	 * @param value value whose string form is to be associated with key.
	 */
	public final void put (String key, boolean value) {
		put( key, Boolean.toString(value) );
	}

	/**
	 * Returns the boolean value represented by the string associated with the
	 * specified key in this preference node.  Valid strings
	 * are <tt>"true"</tt>, which represents true, and <tt>"false"</tt>, which
	 * represents false.  Case is ignored, so, for example, <tt>"TRUE"</tt>
	 * and <tt>"False"</tt> are also valid.  This method is intended for use in
	 * conjunction with {@link #put}.
	 *
	 * <p/>Returns the specified default if there is no value
	 * associated with the key, the backing store is inaccessible, or if the
	 * associated value is something other than <tt>"true"</tt> or
	 * <tt>"false"</tt>, ignoring case.
	 *
	 * <p/>If the implementation supports <i>stored defaults</i> and such a
	 * default exists and is accessible, it is used in preference to the
	 * specified default, unless the stored default is something other than
	 * <tt>"true"</tt> or <tt>"false"</tt>, ignoring case, in which case the
	 * specified default is used.
	 *
	 * @param key key whose associated value is to be returned as a boolean.
	 * @param def the value to be returned in the event that this
	 *        preference node has no value associated with <tt>key</tt>
	 *        or the associated value cannot be interpreted as a boolean,
	 *        or the backing store is inaccessible.
	 * @return the boolean value represented by the string associated with
	 *         <tt>key</tt> in this preference node, or <tt>def</tt> if the
	 *         associated value does not exist or cannot be interpreted as
	 *         a boolean.
	 */
	public final boolean get (String key, boolean def) {
		String s = map.get( key );
		if (s == null)	return def;
		return Boolean.parseBoolean( s );
	}

	/**
	 * Associates a string representing the specified float value with the
	 * specified key in this preference node.  The associated string is the
	 * one that would be returned if the float value were passed to
	 * {@link Float#toString(float)}.  This method is intended for use in
	 * conjunction with {@link #get}.
	 *
	 * @param key key with which the string form of value is to be associated.
	 * @param value value whose string form is to be associated with key.
	 */
	public final void put (String key, float value) {
		put( key, Float.toString(value) );
	}

	/**
	 * Returns the float value represented by the string associated with the
	 * specified key in this preference node.  The string is converted to an
	 * integer as by {@link Float#parseFloat(String)}.  Returns the specified
	 * default if there is no value associated with the key, the backing store
	 * is inaccessible, or if <tt>Float.parseFloat(String)</tt> would throw a
	 * {@link NumberFormatException} if the associated value were passed.
	 * This method is intended for use in conjunction with {@link #put}.
	 *
	 * <p/>If the implementation supports <i>stored defaults</i> and such a
	 * default exists, is accessible, and could be converted to a float
	 * with <tt>Float.parseFloat</tt>, this float is returned in preference to
	 * the specified default.
	 *
	 * @param key key whose associated value is to be returned as a float.
	 * @param def the value to be returned in the event that this
	 *        preference node has no value associated with <tt>key</tt>
	 *        or the associated value cannot be interpreted as a float,
	 *        or the backing store is inaccessible.
	 * @return the float value represented by the string associated with
	 *         <tt>key</tt> in this preference node, or <tt>def</tt> if the
	 *         associated value does not exist or cannot be interpreted as
	 *         a float.
	 */
	public final float get (String key, float def) {
		String s = map.get( key );
		if (s == null || s.length()==0)	return def;

		try {
			return Float.parseFloat( s );
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * Associates a string representing the specified double value with the
	 * specified key in this preference node.  The associated string is the
	 * one that would be returned if the double value were passed to
	 * {@link Double#toString(double)}.  This method is intended for use in
	 * conjunction with {@link #get}.
	 *
	 * @param key key with which the string form of value is to be associated.
	 * @param value value whose string form is to be associated with key.
	 */
	public final void put (String key, double value) {
		put( key, Double.toString(value) );
	}

	/**
	 * Returns the double value represented by the string associated with the
	 * specified key in this preference node.  The string is converted to an
	 * integer as by {@link Double#parseDouble(String)}.  Returns the specified
	 * default if there is no value associated with the key, the backing store
	 * is inaccessible, or if <tt>Double.parseDouble(String)</tt> would throw a
	 * {@link NumberFormatException} if the associated value were passed.
	 * This method is intended for use in conjunction with {@link #put}.
	 *
	 * <p/>If the implementation supports <i>stored defaults</i> and such a
	 * default exists, is accessible, and could be converted to a double
	 * with <tt>Double.parseDouble</tt>, this double is returned in preference
	 * to the specified default.
	 *
	 */
	public final double get (String key, double def) {
		String s = map.get( key );
		if (s == null || s.length()==0)	return def;

		try {
			return Double.parseDouble( s );
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			return def;
		}
	}

	/**
	 * Removes the mapping for a key from this map if it is present
	 * (optional operation).   More formally, if this map contains a mapping
	 * from key <tt>k</tt> to value <tt>v</tt> such that
	 * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
	 * is removed.  (The map can contain at most one such mapping.)
	 *
	 * <p/>Returns the value to which this map previously associated the key,
	 * or <tt>null</tt> if the map contained no mapping for the key.
	 *
	 * <p/>If this map permits null values, then a return value of
	 * <tt>null</tt> does not <i>necessarily</i> indicate that the map
	 * contained no mapping for the key; it's also possible that the map
	 * explicitly mapped the key to <tt>null</tt>.
	 *
	 * <p/>The map will not contain a mapping for the specified key once the
	 * call returns.
	 *
	 * @param key key whose mapping is to be removed from the map
	 * @return the previous value associated with <tt>key</tt>, or
	 *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
	 */
	public String remove (String key) {
		return map.remove( key );
	}

	/**
	 * Wrapper method for wrapping the {@link java.util.Properties#load(java.io.InputStream)}.
	 * @param in the input stream
	 */
	public void load (InputStream in) throws IOException {
		Properties p = getProperties();
		p.load( in );

		// if propeties is null, means we are only using the map object
		// in that case export all the key-value pairs to the map object.
		if (properties != p) {
			map.clear();
			map.putAll( (Map)p );
		}
	}

	/**
	 * Wrapper method for wrapping the {@link java.util.Properties#loadFromXML(java.io.InputStream)}.
	 * @param in the input stream
	 */
	public void loadFromXML (InputStream in) throws IOException {
		Properties p = getProperties();
		p.loadFromXML( in );

		// if propeties is null, means we are only using the map object
		// in that case export all the key-value pairs to the map object.
		if (properties != p) {
			map.clear();
			map.putAll( (Map)p );
		}
	}

	/**
	 * Wrapper method for wrapping the {@link java.util.Properties#store(java.io.OutputStream, String)}.
	 * @param out       an output stream.
	 * @param comments  a description of the property list.
	 */
	public void store (OutputStream out, String comments) throws IOException {
		getProperties().store( out, comments );
	}

	/**
	 * Wrapper method for wrapping the {@link java.util.Properties#storeToXML(java.io.OutputStream, String)}.
	 *
	 * @param os the output stream on which to emit the XML document.
	 * @param comment a description of the property list, or <code>null</code> if no comment is desired.
	 */
	public void storeToXML (OutputStream os, String comment) throws IOException {
		getProperties().storeToXML( os, comment );
	}

	/**
	 * Wrapper method for wrapping the {@link java.util.Properties#storeToXML(java.io.OutputStream, String, String)}.
	 *
	 * @param os the output stream on which to emit the XML document.
	 * @param comment a description of the property list, or <code>null</code> if no comment is desired.
	 * @param encoding encoding
	 */
	public synchronized void storeToXML (OutputStream os, String comment, String encoding) throws IOException {
		getProperties().storeToXML( os, comment, encoding );
	}

	/**
	 * Get internal <code>properties<code> object. If the <code>properties</code> member is
	 * <code>null</code> then return a newly created <code>Properties</code> object from the
	 * <code>map</code> member.
	 * @return Returns <code>Properties</code> object, never return <code>null</code>.
	 */
	private Properties getProperties () {
		Properties prop = properties;
		if (prop == null) {
			prop = new Properties();
			prop.putAll( map );
		}
		return prop;
	}

	/**
	 * Add a property change listener.
	 * @param listener <code>PropertyChangeListener</code> object
	 */
	public void addPropertyChangeListener (PropertyChangeListener listener) {
		listenerList.add(PropertyChangeListener.class, listener);
	}

	/**
	 * Remove the specified property change listener <code>listener</code>.
	 * @param listener <code>PropertyChangeListener</code> object
	 */
	public void removePropertyChangeListener (PropertyChangeListener listener) {
		listenerList.remove(PropertyChangeListener.class, listener);
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 */
	protected void firePropertyChange (String propertyName, Object oldValue, Object newValue) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		PropertyChangeEvent event = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==PropertyChangeListener.class) {
				// Lazily create the event:
				if (event == null)
					event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
				((PropertyChangeListener)listeners[i+1]).propertyChange(event);
			}
		}
	}

}