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

package com.sileria.android;

import android.content.DialogInterface;
import android.preference.Preference;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.widget.*;
import android.view.*;

import com.sileria.android.event.ActionListener;
import com.sileria.android.event.LayoutListener;


/**
 * A single common class that implements all the Android UI event callbacks.
 *
 * @param <T> object type
 *
 * @author Ahmed Shakil
 * @date Jan 5, 2009
 */
public abstract class Command<T> implements Runnable,
		ActionListener,
		View.OnClickListener,
		View.OnTouchListener,
		View.OnLongClickListener,
		DialogInterface.OnClickListener,
		DialogInterface.OnCancelListener,
		DialogInterface.OnDismissListener,
		TextView.OnEditorActionListener,
		TextWatcher,
		AdapterView.OnItemClickListener,
		AdapterView.OnItemSelectedListener,
		Preference.OnPreferenceClickListener,
		Preference.OnPreferenceChangeListener,
		LayoutListener,
		Animation.AnimationListener
{

	protected T value;

	public void execute () {
		run();
	}

	public void onClick (View view) {
		run();
	}

	public boolean onLongClick (View v) {
		run();
		return true;
	}

	public void onCancel (DialogInterface di) {
		run();
	}

	@SuppressWarnings("unchecked")
	public void onClick (DialogInterface di, int index) {
		value = (T)(Integer)index;
		run();
	}

	public void onDismiss (DialogInterface di) {
		run();
	}

	public boolean onEditorAction (TextView textView, int i, KeyEvent keyEvent) {
		run();
		return true;
	}

	public void onItemClick (android.widget.AdapterView<?> adapterView, android.view.View view, int i, long l) {
		run();
	}

	public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
		run();
	}

	public void onNothingSelected (AdapterView<?> parent) {}

	public boolean onPreferenceClick (Preference preference) {
		run();
		return true;
	}

	public boolean onPreferenceChange (android.preference.Preference preference, java.lang.Object o) {
		run();
		return true;
	}

	public void onAction () {
		run();
	}

	public boolean onTouch (View view, MotionEvent motionEvent) {
		run();
		return true;
	}

	public void onLaidOut () {
		run();
	}

	public void onAnimationStart (Animation animation) {
	}

	public void onAnimationEnd (Animation animation) {
		run();
	}

	public void onAnimationRepeat (Animation animation) {
	}

	public void beforeTextChanged (CharSequence s, int start, int count, int after) {
	}

	public void onTextChanged (CharSequence s, int start, int before, int count) {
	}

	public void afterTextChanged (Editable s) {
		run();
	}
}
