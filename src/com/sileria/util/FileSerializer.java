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
public class FileSerializer<T> {

	/**
	 * Read an object from to the stream silently (no errors thrown)
	 * @param file to read from output stream to write to
	 * @return object object read from the file or <code>null</code> in case of error
	 */
	public T read (File file, ObjectSerializer<T> serializer) {
		if (file == null || !file.exists()) return null;

		T obj = null;
		FileInputStream fin = null;
		BufferedInputStream bin = null;

		try {
			fin = new FileInputStream( file );
			bin = new BufferedInputStream( fin );
			obj = serializer.read( bin );
		}
		catch (IOException e) {
			Log.e( e.getLocalizedMessage(), e );
		}
		catch (OutOfMemoryError e) {
			Log.e( e.getLocalizedMessage(), e );
		}
		finally {
			IO.close( bin );
			IO.close( fin );
		}
		return obj;
	}

	/**
	 * Stream to a file using the <code>ObjectSerializer</code> silently without throwing any errors.
	 * @param object object object to write
	 * @param file to write output stream to write to
	 * @param serializer object to write
	 * @return <code>true</code> in case of success; otherwise <code>false</code>
	 */
	public boolean write (T object, File file, ObjectSerializer<T> serializer) {
		if (file == null) return false;

		boolean success = true;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			fos = new FileOutputStream( file );
			bos = new BufferedOutputStream( fos );
			serializer.write( bos, object );
			bos.flush();
		}
		catch (IOException e) {
			Log.e( e.getLocalizedMessage(), e );
			success = false;
		}
		finally {
			IO.close( bos );
			IO.close( fos );
		}

		return success;
	}

}
