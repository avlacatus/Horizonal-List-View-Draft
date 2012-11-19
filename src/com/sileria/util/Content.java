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

package com.sileria.util;

/**
 * General content mutable definition. Made public in-case it comes handy for someone.
 *
 * @author Ahmed Shakil
 * @date July 1, 2010
 *
 * @param <T> content type
 * @param <O> options type
 */
public class Content<T, O extends ContentOptions> implements java.io.Serializable {

	private static final long serialVersionUID = 8328047200523347874L;
	
	public final int id;
	public final String key;
	public final T content;

	public final O options;

	public Content (T content) {
		this( content, 0, null );
	}

	public Content (T content, String key) {
		this( content, 0, key );
	}

	public Content (T content, int id) {
		this( content, id, null );
	}

	public Content (T content, int id, String key) {
		this.id = id;
		this.key = key;
		this.content = content;
		this.options = null;
	}

	public Content (T content, int id, String key, O options) {
		this.id = id;
		this.key = key;
		this.content = content;
		this.options = options;
	}

	public Content (String key) {
		this( 0, key, null );
	}

	public Content (String key, O options) {
		this( 0, key, options );
	}

	public Content (int id, String key, O options) {
		this.id = id;
		this.key = key;
		this.content = null;
		this.options = options;
	}

	/**
	 * Copy constructor.
	 */
	public Content (Content<? extends T, O> that) {
		this.id = that.id;
		this.key = that.key;
		this.content = that.content;
		this.options = that.options;
	}

	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Content that = (Content)o;

		if (id != that.id) return false;
		if (key != null ? !key.equals( that.key ) : that.key != null) return false;
		if (options != null ? !options.equals( that.options ) : that.options != null) return false;

		return true;
	}

	@Override
	public int hashCode () {
		int result = id;
		result = 31 * result + (key != null ? key.hashCode()    : 0);
		result = 31 * result + (options != null ? options.hashCode() : 0);
		return result;
	}

	@Override
	public String toString () {
		return key;
	}
}
