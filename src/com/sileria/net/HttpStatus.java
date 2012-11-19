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

/**
 * HttpStatus.
 *
 * @author Ahmed Shakil
 * @date 07-29-2012
 */
public enum HttpStatus {

	CONTINUE                        ( 100, "Continue" ),
	SWITCHING_PROTOCOLS             ( 101, "Switching Protocols" ),
	PROCESSING                      ( 102, "Processing" ),
	OK                              ( 200, "OK" ),
	CREATED                         ( 201, "Created" ),
	ACCEPTED                        ( 202, "Accepted" ),
	NON_AUTHORITATIVE_INFORMATION   ( 203, "Non-Authoritative Information" ),
	NO_CONTENT                      ( 204, "No Content" ),
	RESET_CONTENT                   ( 205, "Reset Content" ),
	PARTIAL_CONTENT                 ( 206, "Partial Content" ),
	MULTI_STATUS                    ( 207, "Multi-Status" ),
	MULTIPLE_CHOICES                ( 300, "Multiple Choices" ),
	MOVED_PERMANENTLY               ( 301, "Moved Permanently" ),
	MOVED_TEMPORARILY               ( 302, "Moved Temporarily" ),
	SEE_OTHER                       ( 303, "See Other" ),
	NOT_MODIFIED                    ( 304, "Not Modified" ),
	USE_PROXY                       ( 305, "Use Proxy" ),
	SWITCH_PROXY                    ( 306, "Switch Proxy" ),
	TEMPORARY_REDIRECT              ( 307, "Temporary Redirect" ),
	BAD_REQUEST                     ( 400, "Bad Request" ),
	UNAUTHORIZED                    ( 401, "Unauthorized" ),
	PAYMENT_REQUIRED                ( 402, "Payment Required" ),
	FORBIDDEN                       ( 403, "Forbidden" ),
	NOT_FOUND                       ( 404, "Not Found" ),
	METHOD_NOT_ALLOWED              ( 405, "Method Not Allowed" ),
	NOT_ACCEPTABLE                  ( 406, "Not Acceptable" ),
	PROXY_AUTHENTICATION_REQUIRED   ( 407, "Proxy Authentication Required" ),
	REQUEST_TIMEOUT                 ( 408, "Request Timeout" ),
	CONFLICT                        ( 409, "Conflict" ),
	GONE                            ( 410, "Gone" ),
	LENGTH_REQUIRED                 ( 411, "Length Required" ),
	PRECONDITION_FAILED             ( 412, "Precondition Failed" ),
	REQUEST_TOO_LONG                ( 413, "Request Entity Too Large" ),
	REQUEST_URI_TOO_LONG            ( 414, "Request-URI Too Long" ),
	UNSUPPORTED_MEDIA_TYPE          ( 415, "Unsupported Media Type" ),
	REQUESTED_RANGE_NOT_SATISFIABLE ( 416, "Requested Range Not Satisfiable" ),
	EXPECTATION_FAILED              ( 417, "Expectation Failed" ),
	INSUFFICIENT_SPACE_ON_RESOURCE  ( 419, "Insufficient Space on Resource" ),
	METHOD_FAILURE                  ( 420, "Method Failure" ),
	UNPROCESSABLE_ENTITY            ( 422, "Unprocessable Entity" ),
	LOCKED                          ( 423, "Locked" ),
	FAILED_DEPENDENCY               ( 424, "Failed Dependency" ),
	INTERNAL_SERVER_ERROR           ( 500, "Internal Server Error" ),
	NOT_IMPLEMENTED                 ( 501, "Not Implemented" ),
	BAD_GATEWAY                     ( 502, "Bad Gateway" ),
	SERVICE_UNAVAILABLE             ( 503, "Service Unavailable" ),
	GATEWAY_TIMEOUT                 ( 504, "Gateway Timeout" ),
	HTTP_VERSION_NOT_SUPPORTED      ( 505, "HTTP Version Not Supported" ),
	INSUFFICIENT_STORAGE            ( 507, "Insufficient Storage" ),

	UNDEFINED                       (  -1, "Unknown or Undefined Error" );

	public final int code;
	public final String message;

	private HttpStatus (int code, String msg) {
		this.code = code;
		this.message = msg;
	}

	/**
	 * Get error message.
	 */
	public String getMessage () {
		return message;
	}

	/**
	 * Get error code.
	 */
	public int getCode () {
		return code;
	}

	/**
	 * Returns the enum constant of the specified enum type with the
	 * specified error code.
	 *
	 * @param code one of the specified error code
	 * @return enum constant if code matches one of the defined error code
	 * otherwise <code>UNDEFINED</code>
	 */
	public static HttpStatus valueOf (int code) {
		HttpStatus[] actions = values();
		for (HttpStatus sc : actions)
			if (sc.code == code)
				return sc;

		return UNDEFINED;
	}
}
