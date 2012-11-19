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

package com.sileria.android.anim;

import android.graphics.drawable.TransitionDrawable;
import android.view.animation.*;

/**
 * This is a special animation defined specifically for a
 * {@link android.graphics.drawable.TransitionDrawable} class.
 *
 * The purpose of the animation is to be able to add this animation
 * to an {@link AnimationSet} so that the animation can be started
 * as part of other animations being applied to a view.
 *
 * NOTE: Only affective methods at this point are setDuration and start methods.
 * None of the animation listeners or repeat modes or interpolators are implemented.
 *
 * @author Ahmed Shakil
 * @date 4/21/11
 */
public class TransitionAnimation extends Animation {

	private TransitionDrawable transition;
	private boolean started;

	/**
	 * Construct an Animation object with the specified <code>TransitionDrawable</code>.
	 */
	public TransitionAnimation (TransitionDrawable drawable) {
		transition = drawable;
	}

	/**
	 * Construct an Animation object with the specified <code>TransitionDrawable</code> and <code>duration</code>.
	 */
	public TransitionAnimation (TransitionDrawable drawable, int duration) {
		transition = drawable;
		setDuration( duration );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void applyTransformation (float interpolatedTime, Transformation t) {
		if (!started && interpolatedTime > 0f) {
			transition.startTransition( (int)getDuration() );
			started = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset () {
		super.reset();
		started = false;
		transition.resetTransition();
	}
}
