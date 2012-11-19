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

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.*;

import com.sileria.util.Orientation;

/**
 * An animation that rotates the view on the Y axis between two specified angles.
 * This animation also adds a translation on the Z axis (depth) to improve the effect.
 *
 * @author Ahmed Shakil
 * @author Android Team
 */
public class FlipAnimation extends Animation {

	private final float fromDegrees;
	private final float toDegrees;
	private final float centerX;
	private final float centerY;

	private Orientation orientation;
	private boolean reverse;
	private float depthZ = 1f;

	private Camera camera;

	/**
	 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
	 * start angle and its end angle. Both angles are in degrees. The rotation
	 * is performed around a center point on the 2D space, definied by a pair
	 * of X and Y coordinates, called centerX and centerY. When the animation
	 * starts, a translation on the Z axis (depth) is performed. The length
	 * of the translation can be specified, as well as whether the translation
	 * should be reversed in time.
	 *
	 * @param fromDegrees the start angle of the 3D rotation
	 * @param toDegrees   the end angle of the 3D rotation
	 * @param centerX	 the X center of the 3D rotation
	 * @param centerY	 the Y center of the 3D rotation
	 */
	public FlipAnimation (float fromDegrees, float toDegrees, float centerX, float centerY) {
		this( fromDegrees, toDegrees, centerX, centerY, Orientation.HORIZONTAL );
	}

	/**
	 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
	 * start angle and its end angle. Both angles are in degrees. The rotation
	 * is performed around a center point on the 2D space, definied by a pair
	 * of X and Y coordinates, called centerX and centerY. When the animation
	 * starts, a translation on the Z axis (depth) is performed. The length
	 * of the translation can be specified, as well as whether the translation
	 * should be reversed in time.
	 *
	 * @param fromDegrees the start angle of the 3D rotation
	 * @param toDegrees   the end angle of the 3D rotation
	 * @param centerX	 the X center of the 3D rotation
	 * @param centerY	 the Y center of the 3D rotation
	 * @param reverse	 true if the translation should be reversed, false otherwise
	 */
	public FlipAnimation (float fromDegrees, float toDegrees, float centerX, float centerY, boolean reverse) {
		this( fromDegrees, toDegrees, centerX, centerY, Orientation.HORIZONTAL, reverse );
	}

	/**
	 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
	 * start angle and its end angle. Both angles are in degrees. The rotation
	 * is performed around a center point on the 2D space, definied by a pair
	 * of X and Y coordinates, called centerX and centerY. When the animation
	 * starts, a translation on the Z axis (depth) is performed. The length
	 * of the translation can be specified, as well as whether the translation
	 * should be reversed in time.
	 *
	 * @param fromDegrees 	the start angle of the 3D rotation
	 * @param toDegrees   	the end angle of the 3D rotation
	 * @param centerX	 	the X center of the 3D rotation
	 * @param centerY	 	the Y center of the 3D rotation
	 * @param orientation	horizonatal or vertical orientation.
	 */
	public FlipAnimation (float fromDegrees, float toDegrees, float centerX, float centerY, Orientation orientation) {
		this( fromDegrees, toDegrees, centerX, centerY, orientation, false );
	}

	/**
	 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
	 * start angle and its end angle. Both angles are in degrees. The rotation
	 * is performed around a center point on the 2D space, definied by a pair
	 * of X and Y coordinates, called centerX and centerY. When the animation
	 * starts, a translation on the Z axis (depth) is performed. The length
	 * of the translation can be specified, as well as whether the translation
	 * should be reversed in time.
	 *
	 * @param fromDegrees the start angle of the 3D rotation
	 * @param toDegrees   the end angle of the 3D rotation
	 * @param centerX	 the X center of the 3D rotation
	 * @param centerY	 the Y center of the 3D rotation
	 * @param reverse	 true if the translation should be reversed, false otherwise
	 */
	public FlipAnimation (float fromDegrees, float toDegrees, float centerX, float centerY, Orientation orientation, boolean reverse) {
		this.fromDegrees = fromDegrees;
		this.toDegrees = toDegrees;
		this.centerX = centerX;
		this.centerY = centerY;
		this.orientation = orientation;
		this.reverse = reverse;
	}

	@Override
	public void initialize (int width, int height, int parentWidth, int parentHeight) {
		super.initialize( width, height, parentWidth, parentHeight );
		camera = new Camera();
	}

	@Override
	protected void applyTransformation (float interpolatedTime, Transformation t) {
		final float fromDegrees = this.fromDegrees;
		float degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);
		degrees *= (reverse?-1:1);

		final float centerX = this.centerX;
		final float centerY = this.centerY;
		final Camera camera = this.camera;

		final Matrix matrix = t.getMatrix();

		camera.save();

		if (depthZ != 1f)
			camera.translate( 0.0f, 0.0f, depthZ * (reverse ? interpolatedTime : 1.0f - interpolatedTime) );

		if (orientation == Orientation.VERTICAL)
			camera.rotateX( degrees );
		else
			camera.rotateY( degrees );

		camera.getMatrix( matrix );
		camera.restore();

		matrix.preTranslate( -centerX, -centerY );
		matrix.postTranslate( centerX, centerY );
	}

	/**
	 * Set orientation for the animation.
	 *
	 * @see Orientation
	 * @param orientation Orientation
	 */
	public void setOrientation (Orientation orientation) {
		this.orientation = orientation;
	}

	/**
	 * Set the rotation z-depth value.
	 * @param depthZ z-depth value
	 */
	public void setDepthZ (float depthZ) {
		this.depthZ = depthZ;
	}

	/**
	 * Set the reverse flag to <code>true</code> or </code>false</code>.
	 * @param reverse <code>true</code> for reverse; otherse <code>false</code>
	 */
	public void setReverse (boolean reverse) {
		this.reverse = reverse;
	}
}