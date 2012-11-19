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

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import com.sileria.android.Command;

/**
 * A DialogPreference which executes a command after
 * confirmation from the user.
 * <p/>
 * Usage:
 * <blockquote><pre>
 *    // Reset preference
 *    PrefsDialogCommand optReset = new PrefsDialogCommand( this );
 *    optReset.setCommand( new ReflectiveAction(this, "evReset") );
 *    optReset.setDialogTitle( R.string.msg_reset_options );
 *    optReset.setTitle( R.string.reset_options );
 *    prefs.addPreference( optReset );
 *
 * // invoked when user press ok from the reset dialog option
 * public void evReset () {
 *     // reset your setting here
 * }
 *
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date 1/23/11
 */
public class PrefsDialogCommand extends DialogPreference {

	private Command command;

	/**
	 * Construct with specified context
	 */
	public PrefsDialogCommand(Context context) {
		super( context, null );
	}

	/**
	 * Construct with specified context and attributes
	 */
	public PrefsDialogCommand(Context context, AttributeSet attributeSet) {
		super( context, attributeSet );
	}

	/**
	 * Construct with specified context and command
	 */
	public PrefsDialogCommand(Context context, Command cmd) {
		super( context, null );
		command = cmd;
	}

	/**
	 * Get the command which is set to be executed.
	 * @return Command
	 */
	public Command getCommand () {
		return command;
	}

	/**
	 * Set the command object to be executed on OK
	 * @param command Command
	 */
	public void setCommand (Command command) {
		this.command = command;
	}

	/**
	 * Execute command on positive result
	 */
	@Override
	protected void onDialogClosed (boolean positive) {
		if (positive && command != null)
			command.execute();
	}
}
