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

package com.sileria.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.*;

import com.sileria.android.event.LayoutListener;

/**
 * An extension of {@code FrameLayout} that allows you to get a callback
 * of the method {@link android.view.ViewGroup#onLayout(boolean, int, int, int, int)} instead
 * of needing to override this layout. This method is called when the
 * layout is fully laid out and ready to use and you have access to attributes
 * like {@linkplain #getWidth()} and {@linkplain #getHeight()}.
 *
 * @author Ahmed Shakil
 * @date 12/31/10
 */
public class FramePanel extends FrameLayout {

	private LayoutListener listener;

	public FramePanel (Context context) {
		super( context );
	}

	public FramePanel (Context context, AttributeSet attrs) {
		super( context, attrs );
	}

	public FramePanel (Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
	}

	@Override
	protected void onLayout (boolean changed, int l, int t, int r, int b) {
		super.onLayout( changed, l, t, r, b );

		if (listener != null) {
			final LayoutListener listener = this.listener;
			this.listener = null;
			listener.onLaidOut();
		}
	}

	public void setLayoutListener (LayoutListener listener) {
		this.listener = listener;
	}
}
