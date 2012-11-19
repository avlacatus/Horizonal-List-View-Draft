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

import android.preference.ListPreference;

/**
 * Handles updating of the summary text for a {@code ListPreference}.
 * <p/>
 * Example:
 * <blockquote><pre>
 *  ListPreference listPref = new ListPreference( this );
 *  listPref.setOnPreferenceChangeListener( new PrefsListListener(listPref) );
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date 1/23/11
 */
public class PrefsListListener extends PrefsChangeListener<ListPreference> {

	/**
	 * Construct a change lsitener for the specified widget.
	 */
	public PrefsListListener (ListPreference pref) {
		super( pref );
		updateSummary( pref.getValue() );
	}

	protected void updateSummary (Object newValue) {

		CharSequence[] entries = pref.getEntryValues();
		for (int i=0; i<entries.length; i++) {
			if (entries[i].equals( newValue )) {
				pref.setSummary( pref.getEntries()[i] );
				break;
			}
		}
	}
}

