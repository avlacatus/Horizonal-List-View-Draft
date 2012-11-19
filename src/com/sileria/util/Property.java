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

import java.util.Map;
import java.io.Serializable;

/**
 * A handy class for any kind of key-value pair.
 * Implements the <code>Map.Entry</code> interface
 * to provide flexibility of using with the
 * <code>Map</code> classes.
 * <p/>
 * Note: A class for this purpose now available
 * in JDK 1.6 also {@link java.util.AbstractMap.SimpleEntry}.
 *
 * @author Ahmed Shakil
 * @date Sep 19, 2007
 *
 * @param <K> Key object type
 * @param <V> Value object type
 */

public class Property<K, V> implements Map.Entry<K, V>, Serializable {

	static final long serialVersionUID = 5770328517701429411L;

	public final K key;
	public V value;

	/**
	 * Creates an entry representing a mapping from the specified
	 * key to the specified value.
	 *
	 * @param key   the key represented by this entry
	 * @param value the value represented by this entry
	 */
	public Property (K key, V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * Creates an entry representing the same mapping as the
	 * specified entry.
	 *
	 * @param entry the entry to copy
	 */
	public Property (Map.Entry<? extends K, ? extends V> entry) {
		this.key = entry.getKey();
		this.value = entry.getValue();
	}

	/**
	 * Returns the key corresponding to this entry.
	 *
	 * @return the key corresponding to this entry
	 */
	public K getKey () {
		return key;
	}

	/**
	 * Returns the value corresponding to this entry.
	 *
	 * @return the value corresponding to this entry
	 */
	public V getValue () {
		return value;
	}

	/**
	 * Replaces the value corresponding to this entry with the specified
	 * value.
	 *
	 * @param value new value to be stored in this entry
	 * @return the old value corresponding to the entry
	 */
	public V setValue (V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}

	/**
	 * Compares the specified object with this entry for equality.
	 * Returns {@code true} if the given object is also a map entry and
	 * the two entries represent the same mapping.	More formally, two
	 * entries {@code e1} and {@code e2} represent the same mapping
	 * if<pre>
	 *   (e1.getKey()==null ?
	 *    e2.getKey()==null :
	 *    e1.getKey().equals(e2.getKey()))
	 *   &amp;&amp;
	 *   (e1.getValue()==null ?
	 *    e2.getValue()==null :
	 *    e1.getValue().equals(e2.getValue()))</pre>
	 * This ensures that the {@code equals} method works properly across
	 * different implementations of the {@code Map.Entry} interface.
	 *
	 * @param o object to be compared for equality with this map entry
	 * @return {@code true} if the specified object is equal to this map
	 *         entry
	 *
	 * @see #hashCode
	 */
	public boolean equals (Object o) {
		if (!(o instanceof Map.Entry))
			return false;
		Map.Entry e = (Map.Entry)o;
		Object k1 = getKey();
		Object k2 = e.getKey();
		if (k1 == k2 || (k1 != null && k1.equals( k2 ))) {
			Object v1 = getValue();
			Object v2 = e.getValue();
			if (v1 == v2 || (v1 != null && v1.equals( v2 )))
				return true;
		}
		return false;
	}

	/**
	 * Returns the hash code value for this map entry.  The hash code
	 * of a map entry {@code e} is defined to be: <pre>
	 *   (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
	 *   (e.getValue()==null ? 0 : e.getValue().hashCode())</pre>
	 * This ensures that {@code e1.equals(e2)} implies that
	 * {@code e1.hashCode()==e2.hashCode()} for any two Entries
	 * {@code e1} and {@code e2}, as required by the general
	 * contract of {@link Object#hashCode}.
	 *
	 * @return the hash code value for this map entry
	 *
	 * @see #equals
	 */
	public int hashCode () {
		return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
	}

	/**
	 * Returns a String representation of this map entry.  This
	 * implementation returns the string representation of this
	 * entry's key followed by the equals character ("<tt>=</tt>")
	 * followed by the string representation of this entry's value.
	 *
	 * @return a String representation of this map entry
	 */
	public String toString () {
		return key + "=" + value;
	}
}