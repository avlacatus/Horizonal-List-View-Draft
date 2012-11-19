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

package com.sileria.android.event;

import android.preference.EditTextPreference;

/**
 * Handles updating of the summary text for a {@code EditTextPreference}.
 * <p/>
 * Example:
 * <blockquote><pre>
 *  EditTextPreference editPref = new EditTextPreference( this );
 *  editPref.setOnPreferenceChangeListener( new PrefsEditListener(editPref) );
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date 1/23/11
 */

public class PrefsEditListener extends PrefsChangeListener<EditTextPreference> {

	/**
	 * Construct a change lsitener for the specified widget.
	 */
	public PrefsEditListener (EditTextPreference pref) {
		super( pref );
		updateSummary( pref.getText() );
	}
}

