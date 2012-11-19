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

import com.sileria.android.Resource;
import com.sileria.android.view.SeekBarPreference;

/**
 * Handles updating of the summary text for a {@code ListPreference}.
 * <p/>
 * Example:
 * <blockquote><pre>
 *  SeekBarPreference seekPref = new SeekBarPreference( this );
 *  seekPref.setOnPreferenceChangeListener( new PrefsSeekBarListener(listPref) );
 * </pre></blockquote>
 *
 * Example of summary with var-arg string resource:
 * <blockquote><pre>
 *  SeekBarPreference botsPref = new SeekBarPreference( this );
 *  botsPref.setTitle( R.string.bots_prefs );
 *  botsPref.setKey( OPT_BOTS_COUNT );
 *  botsPref.setDefaultValue( DEF_BOTS_COUNT );
 *  botsPref.setMax( MAX_BOTS_COUNT );
 *  prefs.addPreference( botsPref );
 *  botsPref.setOnPreferenceChangeListener( new PrefsSeekBarListener( botsPref, R.string.bots_x ) );
 * </pre>
 * where R.string.bots_x is defined as:
 * <pre>
 *  &lt;string name="bots_x"&gt;Total Bots: %d&lt;/string&gt;
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date Dec 22nd, 2011
 */
public class PrefsSeekBarListener extends SeekBarPreference.AbstractSeekBarListener {

	private final int str;

	/**
	 * Construct a change lsitener for the specified widget.
	 */
	public PrefsSeekBarListener (SeekBarPreference pref) {
		this( pref, 0 );
	}

	/**
	 * Construct a change lsitener for the specified widget.
	 * @param pref SeekBarPreference object
	 * @param strResId string resource id that takes a integer argument
	 */
	public PrefsSeekBarListener (SeekBarPreference pref, int strResId) {
		super( pref );
		str = strResId;
		pref.setSummary( toSummary( pref.getProgress() ) );
	}

	/**
	 * Update the summary.
	 * @param newValue Integer value
	 */
	@Override
	protected void updateSummary (Object newValue) {
		setSummary( toSummary( newValue ) );
	}

	/**
	 * Convert integer progress to summary string.
	 * @param newValue should be an Integer instance
	 */
	@Override
	protected String toSummary (Object newValue) {
		Integer progress = newValue instanceof Integer ? (Integer)newValue : 0;
		return str != 0 ? Resource.getString( str, progress ) : progress.toString();
	}
}

