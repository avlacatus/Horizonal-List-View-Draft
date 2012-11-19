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
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Comparator;

/**
 * I/O utils contains most common of the commons package.
 *
 * @author Ahmed Shakil
 * @date 1/13/11
 */
public class IO {

	/**
	 * The number of bytes in a kilobyte.
	 */
	public static final int ONE_KB = 1024;

	/**
	 * The number of bytes in a megabyte.
	 */
	public static final int ONE_MB = ONE_KB * ONE_KB;

	/**
	 * The number of bytes in a 10 MB.
	 */
	private static final int TEN_MB = ONE_MB * 10;

	/**
	 * The number of bytes in a gigabyte.
	 */
	public static final int ONE_GB = ONE_KB * ONE_MB;

	/**
	 * An empty array of type <code>File</code>.
	 */
	public static final File[] EMPTY_FILE_ARRAY = new File[0];

	/**
	 * The UTF-8 character set, used to decode octets in URLs.
	 */
	public static final Charset UTF8 = Charset.forName( "UTF-8" );

	/**
	 * The default buffer size to use for copy
	 */
	private static final int COPY_BUFFER = ONE_KB;

	/**
	 * Unix file separator.
	 */
	private static final char UNIX_SEPARATOR = '/';

	/**
	 * Windows file separator.
	 */
	private static final char WINDOWS_SEPARATOR = '\\';

	/**
	 * File extension separator.
	 */
	private static final char EXTENSION_SEPARATOR = '.';

	/**
	 * Constructor, protected.
	 */
	protected IO () {
	}

	/**
	 * Copy bytes from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>.
	 * <p/>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 *
	 * @param input  the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException		  if an I/O error occurs
	 */
	public static long copy (InputStream input, OutputStream output) throws IOException {

		byte[] buffer = new byte[COPY_BUFFER];
		long count = 0;
		int n;
		while ((n = input.read( buffer )) != -1) {
			output.write( buffer, 0, n );
			count += n;
		}
		return count;
	}

	/**
	 * Copy one file to another using nio channels.
	 *
	 * @param srcFile  the validated source file, must not be <code>null</code>
	 * @param destFile the validated destination file, must not be <code>null</code>
	 * @throws IOException if an error occurs
	 */
	public static void copyFile (File srcFile, File destFile) throws IOException {

		if (!srcFile.exists()) {
			throw new FileNotFoundException( "Source '" + srcFile + "' does not exist" );
		}
		if (srcFile.isDirectory()) {
			throw new IOException( "Source '" + srcFile + "' exists but is a directory" );
		}
		if (srcFile.getCanonicalPath().equals( destFile.getCanonicalPath() )) {
			throw new IOException( "Source '" + srcFile + "' and destination '" + destFile + "' are the same" );
		}
		if (destFile.getParentFile() != null && !destFile.getParentFile().exists()) {
			if (!destFile.getParentFile().mkdirs()) {
				throw new IOException( "Destination '" + destFile + "' directory cannot be created" );
			}
		}
		if (destFile.exists() && !destFile.canWrite()) {
			throw new IOException( "Destination '" + destFile + "' exists but is read-only" );
		}
		if (destFile.exists() && destFile.isDirectory())
			throw new IOException( "Destination '" + destFile + "' exists but is a directory" );

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fis = new FileInputStream( srcFile );
			fos = new FileOutputStream( destFile );
			in = fis.getChannel();
			out = fos.getChannel();

			long size = in.size();
			long pos = 0, count;

			while (pos < size) {
				count = (size - pos) > TEN_MB ? TEN_MB : (size - pos);
				pos += out.transferFrom( in, pos, count );
			}

		}
		finally {
			close( out );
			close( fos );
			close( in );
			close( fis );
		}

		if (srcFile.length() != destFile.length())
			throw new IOException( "Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'" );
	}

	/**
	 * Copies bytes from an {@link InputStream} <code>source</code> to a file
	 * <code>destination</code>. The directories up to <code>destination</code>
	 * will be created if they don't already exist. <code>destination</code>
	 * will be overwritten if it already exists.
	 *
	 * @param source	  the <code>InputStream</code> to copy bytes from, must not be <code>null</code>
	 * @param destination the non-directory <code>File</code> to write bytes to
	 *                    (possibly overwriting), must not be <code>null</code>
	 * @throws IOException if <code>destination</code> is a directory
	 * @throws IOException if <code>destination</code> cannot be written
	 * @throws IOException if <code>destination</code> needs creating but can't be
	 * @throws IOException if an IO error occurs during copying
	 */
	public static void copyFile (InputStream source, File destination) throws IOException {

		try {
			FileOutputStream output = openOutputStream( destination );
			try {
				copy( source, output );
			}
			finally {
				close( output );
			}
		}
		finally {
			close( source );
		}
	}

	/**
	 * Opens a {@link FileInputStream} for the specified file, providing better
	 * error messages than simply calling <code>new FileInputStream(file)</code>.
	 * <p/>
	 * At the end of the method either the stream will be successfully opened,
	 * or an exception will have been thrown.
	 * <p/>
	 * An exception is thrown if the file does not exist.
	 * An exception is thrown if the file object exists but is a directory.
	 * An exception is thrown if the file exists but cannot be read.
	 *
	 * @param file the file to open for input, must not be <code>null</code>
	 * @return a new {@link FileInputStream} for the specified file
	 * @throws FileNotFoundException if the file does not exist
	 * @throws IOException		   if the file object is a directory
	 * @throws IOException		   if the file cannot be read
	 */
	public static FileInputStream openInputStream (File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException( "File '" + file + "' exists but is a directory" );
			}
			if (!file.canRead()) {
				throw new IOException( "File '" + file + "' cannot be read" );
			}
		} else {
			throw new FileNotFoundException( "File '" + file + "' does not exist" );
		}

		return new FileInputStream( file );
	}

	//-----------------------------------------------------------------------

	/**
	 * Opens a {@link FileOutputStream} for the specified file, checking and
	 * creating the parent directory if it does not exist.
	 * <p/>
	 * At the end of the method either the stream will be successfully opened,
	 * or an exception will have been thrown.
	 * <p/>
	 * The parent directory will be created if it does not exist.
	 * The file will be created if it does not exist.
	 * An exception is thrown if the file object exists but is a directory.
	 * An exception is thrown if the file exists but cannot be written to.
	 * An exception is thrown if the parent directory cannot be created.
	 *
	 * @param file the file to open for output, must not be <code>null</code>
	 * @return a new {@link FileOutputStream} for the specified file
	 * @throws IOException if the file object is a directory
	 * @throws IOException if the file cannot be written to
	 * @throws IOException if a parent directory needs creating but that fails
	 */
	public static FileOutputStream openOutputStream (File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException( "File '" + file + "' exists but is a directory" );
			}
			if (!file.canWrite()) {
				throw new IOException( "File '" + file + "' cannot be written to" );
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				if (!parent.mkdirs()) {
					throw new IOException( "File '" + file + "' could not be created" );
				}
			}
		}

		return new FileOutputStream( file );
	}

	/**
	 * Unconditionally close a <code>Closeable</code>.
	 * <p/>
	 * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored.
	 * This is typically used in finally blocks.
	 * <p/>
	 * Example code:
	 * <pre>
	 *   Closeable closeable = null;
	 *   try {
	 *       closeable = new FileReader("foo.txt");
	 *       // process closeable
	 *       closeable.close();
	 *   } catch (Exception e) {
	 *       // error handling
	 *   } finally {
	 *       IOUtils.close(closeable);
	 *   }
	 * </pre>
	 *
	 * @param closeable the object to close, may be null or already closed
	 */
	public static void close (Closeable closeable) {
		try {
			if (closeable != null)
				closeable.close();
		}
		catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * Unconditionally close a <code>Socket</code>.
	 *
	 * @param socket the object to close, may be null or already closed
	 */
	public static void close (Socket socket) {
		try {
			if (socket != null)
				socket.close();
		}
		catch (IOException ioe) {
			// ignore
		}
	}


	//-----------------------------------------------------------------------

	/**
	 * Implements the same behaviour as the "touch" utility on Unix. It creates
	 * a new file with size 0 or, if the file exists already, it is opened and
	 * closed without modifying it, but updating the file date and time.
	 * <p/>
	 * This method throws an IOException if the last
	 * modified date of the file cannot be set. Also, this method
	 * creates parent directories if they do not exist.
	 *
	 * @param file  the File to touch
	 * @throws IOException If an I/O problem occurs
	 */
	public static void touch (File file) throws IOException {
		if (!file.exists()) {
			close( openOutputStream(file) );
		}

		boolean success = file.setLastModified(System.currentTimeMillis());
		if (!success) {
			throw new IOException("Unable to set the last modification time for " + file);
		}
	}

	/**
	 * Cleans a directory quitely without deleting it.
	 *
	 * @param directory directory to clean
	 * @return <code>true</code> if successful; otherwise <code>false</code>.
	 */
	public static boolean cleanDirectory (File directory) {
		return cleanDirectory( directory, null );
	}

	/**
	 * Cleans a directory without deleting it.
	 *
	 * @param directory directory to clean
	 * @return <code>true</code> if successful; otherwise <code>false</code>.
	 */
	public static boolean cleanDirectory (File directory, FileFilter filter) {
		if (directory == null) return false;
		if (!directory.exists()) return false;
		if (!directory.isDirectory()) return false;

		File[] files = filter == null ? directory.listFiles() : directory.listFiles( filter );
		if (files == null)  // null if security restricted or not a directory
			return false;

		boolean success = true;
		for (File file : files) 
			success &= forceDelete( file );

		return success;
	}

	/**
	 * Deletes a directory recursively.
	 *
	 * @param directory directory to delete
	 * @return <code>true</code> if successful; otherwise <code>false</code>.
	 */
	public static boolean deleteDirectory (File directory) {
		if (directory == null) return false;
		if (!directory.exists()) return false;
		if (!directory.isDirectory()) return false;

		if (!isSymlink( directory ))
			cleanDirectory( directory );

		return directory.delete();
	}

	/**
	 * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
	 * <p/>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
	 * </ul>
	 *
	 * @param file file or directory to delete, can be <code>null</code>
	 * @return <code>true</code> if the file or directory was deleted, otherwise
	 *         <code>false</code>
	 */
	public static boolean deleteFile (File file) {
		if (file == null || !file.exists())
			return false;

		try {
			if (file.isDirectory()) {
				cleanDirectory( file );
			}
		}
		catch (Exception ignored) {
		}

		try {
			return file.delete();
		}
		catch (Exception ignored) {
			return false;
		}
	}

	//-----------------------------------------------------------------------

	/**
	 * Deletes a file. If file is a directory, delete it and all sub-directories.
	 * <p/>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li>You get exceptions when a file or directory cannot be deleted.
	 * (java.io.File methods returns a boolean)</li>
	 * </ul>
	 *
	 * @param file file or directory to delete, must not be <code>null</code>
	 * @return <code>true</code> if successful; otherwise <code>false</code>.
	 */
	public static boolean forceDelete (File file) {
		if (file == null || !file.exists())
			return false;

		if (file.isDirectory()) {
			return deleteDirectory( file );
		}

		return file.delete();
	}

	/**
	 * Determines whether the specified file is a Symbolic Link rather than an actual file.
	 * <p/>
	 * Will not return true if there is a Symbolic Link anywhere in the path,
	 * only if the specific file is.
	 *
	 * @param file the file to check
	 * @return true if the file is a Symbolic Link
	 */
	public static boolean isSymlink (File file) {
		if (file == null || !file.exists())
			return false;

		try {
			File fileInCanonicalDir;
			if (file.getParent() == null) {
				fileInCanonicalDir = file;
			}
			else {
				File canonicalDir = file.getParentFile().getCanonicalFile();
				fileInCanonicalDir = new File( canonicalDir, file.getName() );
			}

			return !fileInCanonicalDir.getCanonicalFile().equals( fileInCanonicalDir.getAbsoluteFile() );
		}
		catch (IOException ioe) {
			return false;
		}

	}

	/**
	 * Get the filename from a string path.
	 * @param path file path
	 * @return filename
	 */
	public static String getFileName (String path) {
		if (path == null) return null;

		int i = indexOfLastSeparator( path );
		return i > 0 && i < path.length()-1 ? path.substring( i+1 ) : path;
	}

	/**
	 * Get the file extention for the path name with an option
	 * of having the extension character in the returned value.
	 * @param   path file name or path
	 * @param   includeExtChar if <code>true</code> return
	 *          the extension with the extension separator character
	 * @return  Returns a file extension.
	 *          If no extension is found then returns a blank string.
	 * 			If the provided path was null then this will return <code>null</code>.
	 */
	public static String getFileExt (String path, boolean includeExtChar) {
		if (path == null) return null;

		int dot = path.lastIndexOf( EXTENSION_SEPARATOR );
		if ( dot < 1 || dot > path.length()-1) return "";

		int slash = indexOfLastSeparator( path );
		if (dot < slash) return "";

		return path.substring( includeExtChar ? dot : dot+1 );
	}

	/**
	 * Get the file extention for the path name.
	 * @param   path file name or path
	 * @return  Returns a file extension.
	 *          If no extension is found then returns a blank string.
	 * 			If the provided path was null then this will return <code>null</code>.
	 */
	public static String getFileExt (String path) {
		return getFileExt( path, false );
	}

	/**
	 * Get the folder path stripping out the filename.
	 */
	public static String getParentPath (String path) {
		if (path == null) return null;

		int i = indexOfLastSeparator( path );
		return i == -1 ? null : path.substring( 0, i );
	}

	/**
	 * Get the index of the last separator in the specified <code>filename</code>
	 * @param path file path
	 * @return index of last file separator if found; otherwise <code>null</code>
	 */
	public static int indexOfLastSeparator (String path) {
		if (path == null)
			return -1;

		return Math.max(path.lastIndexOf(UNIX_SEPARATOR), path.lastIndexOf(WINDOWS_SEPARATOR));
	}

	/**
	 * Comparator to sort files based on last modified in ascending order.
	 */
	public static class FileTimeComparator implements Comparator<File> {
		public int compare (File f1, File f2) {
			long result = f1.lastModified() - f2.lastModified();
			if (result < 0)
				return -1;
			else if (result > 0)
				return 1;
			else
				return 0;
		}
	}

	/**
	 * Comparator to sort files based on size in ascending order.
	 */
	public static class FileSizeComparator implements Comparator<File> {
		public int compare (File f1, File f2) {
			long result = f1.length() - f2.length();
			if (result < 0)
				return -1;
			else if (result > 0)
				return 1;
			else
				return 0;
		}
	}
}
