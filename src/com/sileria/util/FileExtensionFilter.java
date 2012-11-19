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

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileFilter;


/**
 * An implementation of {@code FileFilter} and {@code FilenameFilter}
 * that filters using a specified set of extensions.
 * The extension for a file is the portion of the file name after the last ".".
 * Files whose name does not contain a "." have no file name extension.
 * File name extension comparisons are case insensitive.
 * <p/>
 * The following example creates a
 * {@code FileExtensionFilter} that will show {@code jpg} files:
 * <pre>
 * FileFilter filter = new FileExtensionFilter("JPEG file", "jpg", "jpeg");
 * JFileChooser fileChooser = ...;
 * fileChooser.addChoosableFileFilter(filter);
 * </pre>
 * <p/>
 *
 * @author Ahmed Shakil
 * @version 1.0
 * @see FileFilter
 */

public class FileExtensionFilter implements FileFilter, FilenameFilter {

	private String[] extensions;
	private String description;

	/**
	 * Creates a {@code FileExtensionFilter} with the specified
	 * description and file name extensions. The returned {@code
	 * FileExtensionFilter} will accept all directories and any
	 * file with a file name extension contained in {@code extensions}.
	 *
	 * @param desc textual description for the filter, may be {@code null}
	 * @param ext  the accepted file name extensions
	 * @throws IllegalArgumentException if extensions is {@code null}, empty,
	 *                                  contains {@code null}, or contains an empty string
	 * @see #accept
	 */
	public FileExtensionFilter (String desc, String... ext) {
		description = desc == null ? "" : desc;

		extensions = new String[ext.length];
		for (int i = 0; i < ext.length; i++)
			extensions[i] = ext[i].toLowerCase();
	}


	/**
	 * Whether the given file is accepted by this filter.
	 */
	public boolean accept (File file) {
		if (file.isDirectory()) return true;

		if (extensions != null)
			for (String anExt : extensions)
				if (file.getName().toLowerCase().endsWith( anExt ))
					return true;

		return false;
	}

	/**
	 * Tests if a specified file should be included in a file list.
	 *
	 * @param dir  the directory in which the file was found.
	 * @param name the name of the file.
	 * @return <code>true</code> if and only if the name should be
	 *         included in the file list; <code>false</code> otherwise.
	 */
	public boolean accept (File dir, String name) {
		return accept( new File( dir, name ) );
	}

	/**
	 * The description of this filter. For example: "JPG and GIF Images"
	 */
	public String getDescription () {
		return description;
	}


}
