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

import java.io.Serializable;

/**
 * A generic mutable <code>Object</code> wrapper.
 *
 * @author Ahmed Shakil
 * @date Jan 30, 2008
 *
 * @param <T> object type
 */
public class MutableObject<T> implements Serializable {

	static final long serialVersionUID = -4997098134953250066L;

	/**
	 * Mutable object.
	 */
	private T value;

	/**
	 * Constructs a <code>MutableObject</code> with <code>null</code> value.
	 */
	public MutableObject () {
	}

	/**
	 * Constructs a <code>MutableObject</code> with the specified <code>value</code>.
	 * @param value mutable value object
	 */
	public MutableObject (T value) {
		this.value = value;
	}

	/**
	 * Get the mutable value.
	 * @return mutable value
	 */
	public T get () {
		return value;
	}

	/**
	 * Set the mutable object.
	 * @param value mutable value
	 */
	public void set (T value) {
		this.value = value;
	}

	/**
	 * Compares this object against the specified object.
	 * Returns <code>true</code> if argument is not <code>null</code> and
	 * is a <code>MutableObject</code> object having the same value as this object.
	 *
	 * @param obj the object to compare with.
	 * @return <code>true</code> if the objects are the same; otherwise <code>false</code>.
	 */
	public boolean equals (Object obj) {
		if (obj instanceof MutableObject) {
			Object other = ((MutableObject) obj).value;
			return value == other || (value != null && value.equals(other));
		}
		return false;
	}

	/**
	 * Returns the value's hash code or <tt>0</tt> if the value is <code>null</code>.
	 *
	 * @return the value's hash code or <tt>0</tt> if the value is <code>null</code>.
	 */
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}

	/**
	 * Returns the String value of this mutable if non-null.
	 *
	 * @return the mutable value as a string
	 */
	public String toString() {
		return value == null ? "null" : value.toString();
	}
}
