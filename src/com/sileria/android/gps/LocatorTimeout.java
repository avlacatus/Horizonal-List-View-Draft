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

package com.sileria.android.gps;

/**
 * Timeout handler for {@link SimpleLocator}. This interface
 * lets the user choose whether to continue waiting for gps
 * location listener or dismiss the call.
 *
 * It is handy to display a user dialog to ask user whether
 * he wants to wait longer.
 *
 * @author Ahmed Shakil
 * @date Nov 29, 2009
 */
public interface LocatorTimeout {

	/**
	 * Invoked when a timeout occurs in the SimpleLocator class.
	 * 
	 * @return <code>true</code> to timeout; otherwise <code>false</code> to resume.
	 */
	boolean locationTimeout ();
}
