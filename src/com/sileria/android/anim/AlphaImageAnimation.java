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

import android.graphics.drawable.Drawable;
import android.view.animation.*;

/**
 * An animation that controls the alpha level of a <code>Drawable</code>
 * image instead of the usual view object. Very useful for fading
 * overlay images in and out.
 * <p/>
 * This animation does not affect or change the alpha property
 * of a {@link Transformation}
 * <p/>
 * An example of using it with layered images:
 * <blockquote><pre>
 *  LayerDrawable bg = (LayerDrawable)Resource.getImage( R.drawable.panel_highlight ).mutate();
 *  panel.setBackgroundDrawable( bg );
 *  AlphaImageAnimation ia = new AlphaImageAnimation( bg.getDrawable( 1 ), 0f, 1f );
 *  ia.setDuration( dur );
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date 8/25/11
 */
public class AlphaImageAnimation extends Animation {

	private float fromAlpha;
	private float toAlpha;

	private int alpha = -1;
	private Drawable image;

    /**
     * Constructor to use when building an AlphaAnimation from code
     *
	 * @param image Drawable image that the alpha will be applied to.
     * @param fromAlpha Starting alpha value for the animation, where 1.0 means
     *        fully opaque and 0.0 means fully transparent.
     * @param toAlpha Ending alpha value for the animation.
     */
    public AlphaImageAnimation (Drawable image, float fromAlpha, float toAlpha) {
		this.image = image;
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
    }

	/**
	 * Changes the alpha property of the supplied {@link Transformation}
	 */
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {

		final int alpha = (int)(255F * (fromAlpha + ((toAlpha - fromAlpha) * interpolatedTime)));
		if (alpha != this.alpha) {
			image.setAlpha( alpha );
			image.invalidateSelf();
			this.alpha = alpha;
		}
	}

	@Override
	public boolean willChangeTransformationMatrix() {
		return false;
	}

	@Override
	public boolean willChangeBounds() {
		return false;
	}

}
