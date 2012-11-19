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

package com.sileria.android.bc;

import android.app.AlertDialog;
import android.content.Context;

/**
 * HoneyComb 3.0 -3.2 WRAPPER.  API Level - 11 to 13.
 *
 * @author Ahmed Shakil
 * @date Jan 16, 2012
 */
class HoneyComb extends GingerBread {


	/**
	 * AlertDialog.Builder constructor that takes theme id is not available until API 11.
	 * This factory methods lets you take care of that problem.
	 * @param ctx Context
	 * @param theme The actual theme that an AlertDialog uses is a private implementation, however you can
     * 			here supply either the name of an attribute in the theme from which to get the dialog's style
	 * 			(such as {@link android.R.attr#alertDialogTheme} or one of the constants
	 */
	public AlertDialog.Builder createAlertBuilder (Context ctx, int theme) {
		return new AlertDialog.Builder( ctx, theme );
	}

}
