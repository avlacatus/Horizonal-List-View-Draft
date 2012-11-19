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

import java.io.*;

/**
 * Generic interface to read or write any kind of object from input stream or to outputstream respectively.
 *
 * @author Ahmed Shakil
 * @date 08-24-2012
 */
public interface ObjectSerializer<T> {

	/**
	 * Write to the stream.
	 * @param os output stream to write to
	 * @param object object to write
	 * @throws java.io.IOException in case of IO errors
	 */
	boolean write (OutputStream os, T object) throws IOException;

	/**
	 * Read object from to the stream.
	 * @param in output stream to write to
	 * @return object object to write
	 * @throws java.io.IOException in case of IO errors
	 */
	T read (InputStream in) throws IOException;

}
