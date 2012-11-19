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

package com.sileria.android.view;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

/**
 * A generic class to be used as a loading panel or an empty view for a <code>ListView</code> class.
 * <p/>
 * You have ability to show a progress bar and a text message or only a text message which can be
 * used e.g. when data is being loaded for a list to display the wait cursor and a message and if
 * data was failed then only an error message.
 *
 * @author Ahmed Shakil
 * @date 4/29/12
 */
public class LoadingPanel extends LinearLayout {

	private TextView message;
	private ProgressBar progress;

	/**
	 * Simple constructor to use when creating a view from code.
	 *
	 * @param context The Context the view is running in, through which it can
	 *        access the current theme, resources, etc.
	 */
	public LoadingPanel (Context context) {
		super( context );
		createContent( context );
	}

	/**
	 * Constructor that is called when inflating a view from XML. This is called
	 * when a view is being constructed from an XML file, supplying attributes
	 * that were specified in the XML file.
	 *
	 * @param context The Context the view is running in, through which it can
	 *        access the current theme, resources, etc.
	 * @param attrs The attributes of the XML tag that is inflating the view.
	 */
	public LoadingPanel (Context context, AttributeSet attrs) {
		super( context, attrs );
		createContent( context );
	}

	/**
	 * Create view contents.
	 */
	private void createContent (Context ctx) {
		setId( R.id.empty );
		setGravity( Gravity.CENTER );
		setOrientation( LinearLayout.HORIZONTAL );

		// progress
		progress = new ProgressBar( ctx );
		progress.setId( R.id.progress );
		addView( progress );

		// loading
		message = new TextView( ctx );
		message.setId( R.id.message );
		addView( message );
	}

	/**
	 * Set the progress bar text.
	 * @param text progress bar text
	 */
	public void setProgressText (String text) {
		message.setText( text );
	}

	/**
	 * Set the progress bar size.
	 * @param size progress bar size
	 */
	public void setProgressSize (int size) {
		progress.setLayoutParams( size <= 0 ? null : new LinearLayout.LayoutParams( size, size ) );
	}

	/**
	 * Accessor to underlying <code>TextView</code> object.
	 * @return the TextView object.
	 */
	public TextView getTextView () {
		return message;
	}

	/**
	 * Accessor to underlying <code>ProgressBar</code> object.
	 * @return the ProgressBar object.
	 */
	public ProgressBar getProgressBar () {
		return progress;
	}

	/**
	 * Show a plain text message without the progress bar.
	 * @param text message to display
	 */
	public void showMessage (String text) {
		message.setText( text );
		progress.setVisibility( View.GONE );
	}

	/**
	 * Show a plain text message without the progress bar.
	 * @param text message to display
	 */
	public void showMessage (int text) {
		message.setText( text );
		progress.setVisibility( View.GONE );
	}

	/**
	 * Show a progress bar and specified text message.
	 * @param text progress message to display
	 */
	public void showProgress (String text) {
		message.setText( text );
		progress.setVisibility( View.VISIBLE );
	}

	/**
	 * Show a progress bar and specified text message.
	 * @param text progress message to display
	 */
	public void showProgress (int text) {
		message.setText( text );
		progress.setVisibility( View.VISIBLE );
	}

}
