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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * This class basically does not trigger the event as long as user is typing fast.
 * When user pauses then all callback is invoked. You can also set the delay yourself.
 * <p/>
 * A delayed EditorView text watcher callback can be used when you want to
 * free the text editor from updating widgets like toolbars, actions
 * or to run spell check while the user is typing.
 * <p/>
 * Following example call:
 * <blockqoute><pre>
 * EditText searchText = new EditText( ctx );
 * searchText.addTextChangedListener( new DelayedTextWatcher( searchText, new ActionListener() {
 *     public void onAction () {
 *          String searchTerm = searchText.getText().toString();
 *          // do something with the searchTerm here
 *     }
 * }, 200 ) );
 * </pre></blockqoute>
 * </p>
 *
 *
 * @author Ahmed Shakil
 * @date Sep 18, 2009
 */

public class DelayedTextWatcher extends AbstractDelayedCallback implements TextWatcher {

	private View view;

	/**
	 * Constructs a delayed callback object with the specified
	 * <code>action</code> to be invoked when the delay happens.
	 * <p/>
	 * The default value of delay will be used.
	 * <p/>
	 * The default key event which will be trapped is KEY_TYPED.
	 * <p/>
	 *
	 * @param action <code>ActionListener</code> to be invoked.
	 */
	public DelayedTextWatcher(View view, ActionListener action) {
		super( action );
	}

	/**
	 * Constructs a delayed callback object with the specified
	 * <code>action</code> to be invoked when the specified
	 * <code>delay</code> is reached.
	 * <p/>
	 * The default key event which will be trapped is KEY_TYPED.
	 * <p/>
	 *
	 * @param action <code>ActionListener</code> to be invoked
	 * @param delay  Delay in milliseconds to wait before calling the action
	 */
	public DelayedTextWatcher(View view, ActionListener action, int delay) {
		super( action, delay );
	}

	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	}

	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	}

	public void afterTextChanged (Editable editable) {
		updateTimer( view );
	}
}
