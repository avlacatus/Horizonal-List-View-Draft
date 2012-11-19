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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Some widely used common methods. Also most of the methods in this class uses generics.
 *
 * @author Ahmed Shakil
 * @date Mar 20, 2010
 */
public class Utils {

	public static final String   EMPTY_STRING = "";

	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final int[]    EMPTY_INTEGER_ARRAY = new int[0];
	public static final float[]  EMPTY_FLOAT_ARRAY = new float[0];
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
	public static final long[]   EMPTY_LONG_ARRAY = new long[0];
	public static final byte[]   EMPTY_BYTE_ARRAY = new byte[0];

	/**
	 * Milliseconds in a minute.
	 */
	public static final long MINUTE_MILLIS = 60000L;

	/**
	 * Milliseconds in an hour.
	 */
	public static final long HOUR_MILLIS = 3600000L;

	/**
	 * Milliseconds in a day.
	 */
	public static final long DAY_MILLIS = 86400000L;

	/**
	 * Milliseconds in a week.
	 */
	public static final long WEEK_MILLIS = 604800000L;

	/**
	 * Milliseconds in a year.
	 * <p/>
	 * NOTE: This is the actual milliseconds in an year and not based on (days milliseconds * 365).
	 */
	public static final long YEAR_MILLIS = 31558464000L;

	/**
	 * Returns <code>true</code> if string is null or empty;
	 * otherwise returns false.
	 */
	public static boolean isEmpty (CharSequence s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Returns <code>true</code> if string is null or empty;
	 * otherwise returns false.
	 */
	public static boolean isEmpty (String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Returns <code>true</code> if collection is null or empty;
	 * otherwise returns false.
	 */
	public static boolean isEmpty (Collection<?> c) {
		return c == null || c.size() == 0;
	}

	/**
	 * Returns <code>true</code> if map is null or empty;
	 * otherwise returns false.
	 */
	public static boolean isEmpty (Map<?, ?> m) {
		return m == null || m.size() == 0;
	}

	/**
	 * Returns <code>true</code> if collection is null or empty;
	 * otherwise returns false.
	 */
	public static boolean isEmpty (Object[] o) {
		return o == null || o.length == 0;
	}

	/**
	 * Returns the default value if <code>value</code> is <code>null</code>
	 */
	public static <T> T defaultIfNull (T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}

	/**
	 * Returns the first element from an array or <code>defaultValue</code> if <code>null</code>
	 */
	public static <T> T first (T[] array, T defaultValue) {
		return array == null || array.length == 0 ? defaultValue : array[0];
	}

	/**
	 * Returns the first element from an array or <code>defaultValue</code> if <code>null</code>
	 */
	public static <T> T first (List<T> array, T defaultValue) {
		return array == null || array.isEmpty() ? defaultValue : array.get( 0 );
	}

	/**
	 * Returns the a blank string if obj is null; otherwise a toString of the object.
	 */
	public static String blankIfNull (Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * This compares two object references and works
	 * regardless of whether one or both are null.
	 */
	public static boolean equals (Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals( o2 );
	}

	/**
	 * This compares two object references and works
	 * if both are non-null or only one is null.
	 * <p/>
	 * NOTE: If both are <code>null</code> then this method returns <code>false</code>
	 */
	public static boolean equalsNotNull (Object o1, Object o2) {
		return o1 == o2 ? o1 != null : o1 != null && o1.equals( o2 );
	}

	/**
	 * This compares two string references ignoring the case and works
	 * regardless of whether one or both are null.
	 */
	public static boolean equalsIgnoreCase (String s1, String s2) {
		return s1 == null ? s2 == null : s1.equalsIgnoreCase( s2 );
	}

	/**
	 * Returns the specified value if it is >= min && <= max.
	 * Otherwise if it is > max then will return max and if
	 * it is < min then it will return min.
	 *
	 * @param min   min limit
	 * @param max   max limit
	 * @param value valeu to compare.
	 * @return a value between min and max range.
	 */
	public static int between (int min, int max, int value) {
		return Math.min( Math.max( min, value ), max );
	}

	/**
	 * Returns the specified value if it is >= min && <= max.
	 * Otherwise if it is > max then will return max and if
	 * it is < min then it will return min.
	 *
	 * @param min   min limit
	 * @param max   max limit
	 * @param value valeu to compare.
	 * @return a value between min and max range.
	 */
	public static float between (float min, float max, float value) {
		return Math.min( Math.max( min, value ), max );
	}

	/**
	 * Returns the specified value if it is >= min && <= max.
	 * Otherwise if it is > max then will return max and if
	 * it is < min then it will return min.
	 *
	 * @param min   min limit
	 * @param max   max limit
	 * @param value valeu to compare.
	 * @return a value between min and max range.
	 */
	public static double between (double min, double max, double value) {
		return Math.min( Math.max( min, value ), max );
	}

	/**
	 * Returns the specified value if it is >= min && <= max.
	 * Otherwise if it is > max then will return max and if
	 * it is < min then it will return min.
	 *
	 * @param min   min limit
	 * @param max   max limit
	 * @param value valeu to compare.
	 * @return a value between min and max range.
	 */
	public static long between (long min, long max, long value) {
		return Math.min( Math.max( min, value ), max );
	}

	/**
	 * Parse int silently with default value.
	 */
	public static int parseInt (String value, int defValue) {
		if (value == null || "".equals( value ))
			return defValue;

		try {
			return Integer.parseInt( value );
		}
		catch (NumberFormatException e) {
			Log.w( "Cannot parse to Int: " + value, e );
			return defValue;
		}
	}

	/**
	 * Parse int silently with default value.
	 */
	public static long parseLong (String value, long defValue) {
		if (value == null || "".equals( value ))
			return defValue;

		try {
			return Long.parseLong( value );
		}
		catch (NumberFormatException e) {
			Log.w( "Cannot parse to Long: " + value, e );
			return defValue;
		}
	}

	/**
	 * Parse int silently with default value.
	 */
	public static double parseDouble (String value, double defValue) {
		if (value == null || "".equals( value ))
			return defValue;

		try {
			return Double.parseDouble( value );
		}
		catch (NumberFormatException e) {
			Log.w( "Cannot parse to Double: " + value, e );
			return defValue;
		}
	}

	/**
	 * Parse int silently with default value.
	 */
	public static float parseFloat (String value, float defValue) {
		if (value == null || "".equals( value ))
			return defValue;

		try {
			return Float.parseFloat( value );
		}
		catch (NumberFormatException e) {
			Log.w( "Cannot parse to Float: " + value, e );
			return defValue;
		}
	}

	/**
	 * Returns toString() value of an object if the object is non-null;
	 * otherwise returns an empty string.
	 */
	public static String toString (Object o) {
		return o == null ? EMPTY_STRING : o.toString();
	}

	/**
	 * Converts the specified <code>String</code> to title or proper case.
	 *
	 * @param str the string to convert
	 * @return new String converted to title case.
	 * If str arg was null or empty then same string object is returned.
	 */
	public static String toTitleCase (String str) {
		if (isEmpty( str )) return str;
		
		boolean space = true;
		StringBuilder buff = new StringBuilder( str );

		for (int i = 0, len = buff.length(); i < len; i++) {
			char c = buff.charAt( i );
			if (space) {
				if (!Character.isWhitespace( c )) {
					buff.setCharAt( i, Character.toTitleCase( c ) );
					space = false;
				}
			}
			else if (Character.isWhitespace( c )) {
				space = true;
			}
			else {
				buff.setCharAt( i, Character.toLowerCase( c ) );
			}
		}

		return buff.toString();
	}

	/**
	 * Returns the enum constant of the specified enum type with the
	 * specified name.  The name must match exactly an identifier used
	 * to declare an enum constant in this type.  (Extraneous whitespace
	 * characters are not permitted.)
	 *
	 * @param name case-sensitive name of the constant to return
	 * @param defaultValue if no match found (Cannot be <code>NULL</code>)
	 * @param <E> enum type
	 *
	 * @return enum constant for the specified name if found; otherwise default value.
	 */
	public static <E extends Enum<E>> E  valueOf (String name, E defaultValue) {

		if (isEmpty( name )) return defaultValue;

		if (defaultValue == null)
			throw new NullPointerException( "defaultValue cannot be null." );

		E[] enums = defaultValue.getDeclaringClass().getEnumConstants();
		for (int i = 0; i < enums.length; i++)
			if (enums[i].toString().equals( name ))
				return enums[i];

		return defaultValue;
	}

	/**
	 * Returns the enum constant of the specified enum type with the
	 * specified name.  The name must match exactly an identifier used
	 * to declare an enum constant in this type.  (Extraneous whitespace
	 * characters are not permitted.)
	 *
	 * @param name case-insensitive name of the constant to return
	 * @param defaultValue if no match found (Cannot be <code>NULL</code>)
	 * @param <E> enum type
	 *
	 * @return enum constant for the specified name if found; otherwise default value.
	 */
	public static <E extends Enum<E>> E  valueOfIgnoreCase (String name, E defaultValue) {

		if (isEmpty( name )) return defaultValue;

		if (defaultValue == null)
			throw new NullPointerException( "defaultValue cannot be null." );

		E[] enums = defaultValue.getDeclaringClass().getEnumConstants();
		for (int i = 0; i < enums.length; i++)
			if (enums[i].toString().equalsIgnoreCase( name ))
				return enums[i];

		return defaultValue;
	}

	/**
	 * Convert enums values() to string array.
	 */
	public static <E extends Enum<E>> String[] toStringValues (Class<E> e) {

		if (e == null) return EMPTY_STRING_ARRAY;

		E[] keys = e.getEnumConstants();
		String[] values = new String[keys.length];
		for (int i = 0; i < keys.length; i++)
			values[i] = keys[i].toString();

		return values;
	}

	/**
	 * Converts specified string id into 32 digit hash string.
	 *
	 * @return 32 character hex string or <code>null</code> if does not exist or had problems
	 */
	public static String toMD5Hash (String id) {
		if (id == null || id.length() == 0) return null;

		try {
			MessageDigest digest = MessageDigest.getInstance( "MD5" );
			digest.update( id.getBytes(), 0, id.length() );

			id = String.format("%032X", new BigInteger(1, digest.digest()) );
		}
		catch (NoSuchAlgorithmException e) {
			id = id.substring( 0, Math.min( id.length(), 32 ) );
		}

		return id;
	}

	/**
	 * Convert a string id into a unique has string. Useful for URIs.
	 */
	public static String toHashString (String id) {
		return Integer.toHexString( id.hashCode() );
	}
}
