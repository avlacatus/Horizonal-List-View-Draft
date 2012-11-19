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

import android.view.*;

/**
 * A OnClick adapater class that shows the context menu for on click even of any view.
 *
 * <p/>Usage:</p>
 * <blockquote><pre>
 *		activity.registerForContextMenu( button );
 *		button.setOnClickListener( new ContextMenuOnClick() );
 * </pre></blockquote>
 *
 * NOTE: The view must either be registered to show a context menu with the activity using
 * {@link android.app.Activity#registerForContextMenu(android.view.View)} or you should provide
 * the view with your own {@link android.view.View.OnCreateContextMenuListener} by calling
 * {@link View#setOnCreateContextMenuListener(android.view.View.OnCreateContextMenuListener)}.
 *
 * @author Ahmed Shakil
 * @date May 10, 2011
 */
public class ContextMenuOnClick implements View.OnClickListener {

	/**
	 * Simply show context menu of the specified <code>View</code>
	 * @param v View that is clicked
	 */
	public void onClick (View v) {
		v.showContextMenu();
	}
}
