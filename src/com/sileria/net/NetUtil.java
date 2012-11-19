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

package com.sileria.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sileria.util.Log;

/**
 * Collection of network utility methods.
 *
 * @author Ahmed Shakil
 * @author Syed Kashif Ali
 * @date Jan 2, 2008
 */

public class NetUtil {

	// Ip address validator pattern.
	private static final String IPADDRESS_PATTERN =
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	/**
	 * Parse servlet parameter string into a map of key-values.
	 */
	public static Map<String, String> parseParam (String str) {
		if (str == null || str.length() == 0)
			return Collections.emptyMap();

		Map<String, String> map = new HashMap<String, String>();
		String[] params = str.split( "&" );
		for (String param : params) {
			String[] e = splitNext( param, "=" );
			map.put( e[0], e[1] );
		}

		return map;
	}

	/**
	 * Create a URL paramater string from the specified map.
	 */
	public static String toParamString (Map<?, ?> param) {
		StringBuilder sb = new StringBuilder();
		Iterator it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry me = (Map.Entry)it.next();
			sb.append( me.getKey() ).append( "=" ).append( me.getValue() );
			if (it.hasNext())
				sb.append( "&" );
		}
		return sb.toString();
	}

	/**
	 * Get the host name out of the full URL.
	 * This method does not include protocol
	 * or port numbers.
	 */
	public static String getHostName (String path) {
		if (path == null) return null;

		int start = path.indexOf( "://" );
		start = start < 0 ? 0 : start + 3;

		int end = path.indexOf( "/", start );
		end = end < 0 ? path.length() : end;

		int port = path.indexOf (":", start);
		end = port >= 0 && port < end ? port : end;

		return path.substring( start, end );
	}

	/**
	 * Get the host name out of the full URL.
	 * This method includes port number if
	 * part of the the provided string path.
	 */
	public static String getHostAddress (String path) {
		if (path == null) return null;

		int start = path.indexOf( "://" );
		start = start < 0 ? 0 : start + 3;

		int end = path.indexOf( "/", start );
		end = end < 0 ? path.length() : end;

		return path.substring( start, end );
	}

	/**
	 * Converts the given <code>ipNumber</code> to an IP Address.
	 *
	 * @param ip IP number
	 * @return String IP Address
	 *
	 * @see #toIPNumber(String)
	 */
	public static String toIPAddress (int ip) {
		return (ip & 0xFF) + "." +
				((ip >> 8) & 0xFF) + "." +
				((ip >> 16) & 0xFF) + "." +
				((ip >> 24) & 0xFF);
	}

	/**
	 * Validate ip address with regular expression
	 *
	 * @param ip ip address for validation
	 * @return true valid ip address, false invalid ip address
	 */
	public static boolean isValidIP (final String ip) {

		Pattern pattern;
		Matcher matcher;

		pattern = Pattern.compile( IPADDRESS_PATTERN ); //Compile regex pattern into the pattern object
		matcher = pattern.matcher( ip );				// Match the pattern with the given IP String.

		return matcher.matches();					   // Return match result.
	}


	/**
	 * Converts the given <code>ipAddress</code> into a long number representation.
	 * <p>
	 * IP address (IPv4 / IPv6) is divided into 4 sub-blocks.
	 * Each sub-block has a different weight number powered by 256.
	 * <p>
	 *
	 * <quote>
	 * Forumula 1:
	 * <pre>
	 * 		IP Number = 16777216*w + 65536*x + 256*y + z
	 * </pre>
	 * </quote>
	 *
	 * @param ipAddress IP Address
	 * @return IP number
	 *
	 * @see #toIPAddress(int) 
	 */
	public static long toIPNumber (String ipAddress) {
		long ipNum = 0L;

		try {
			InetAddress ia = InetAddress.getByName( ipAddress );

			byte[] ip = ia.getAddress();

			for (int i = 0; i < ip.length; i++) {
				//ipNum += (ip[i] & 0xFF) * Math.pow( 256, ip.length - i -1 );
				//ipNum += (ip[i] & 0xFF) * (1 << ((ip.length - i -1)*8));
				ipNum += (long)(ip[i] & 0xFF) << ((ip.length - i - 1) << 3);
			}

		}
		catch (UnknownHostException e) {
			Log.e( "Not a valid IP Address", e );
		}

		return ipNum;
	}

	/**
	 * Utility method to parse and get next token from the specified <code>text</code>.
	 * @param text Text to parse the token from
	 * @param pos starting position
	 * @param delim delimiter
	 * @return parse string if found. Returns null if null text is passed.
	 * @throws java.lang.NullPointerException if <code>text</code> is <code>null</code>
	 */
	private static String nextString (String text, int pos, String delim) {
		int n = text.indexOf( delim, pos );
		n = n < 0 ? text.length() : n;
		return text.substring( pos, n ).trim();
	}

	/**
	 * Split the text into two strings based on the first matching delimiter
	 * @param text Text to split
	 * @param delim delimiter
	 * @return Always returns an array of two strings.
	 * 		The first index contains the first token.
	 * 		The second index contains the remainder text.
	 * @throws java.lang.NullPointerException if <code>text</code> is <code>null</code>
	 */
	private static String[] splitNext (String text, String delim) {
		String s = nextString( text, 0, delim );
		int n = s == null ? -1 : s.length() + delim.length();
		return new String[] { s, n>=0 && n <= text.length() ? text.substring( n ) : null };
	}

}
