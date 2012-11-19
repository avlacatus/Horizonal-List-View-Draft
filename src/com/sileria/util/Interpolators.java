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

package com.sileria.util;

/**
 * Collection of interpolator methods which defines the rate of change of an animation.
 * <p/>
 * These set of methods provide basic animation effects (alpha, scale, translate, rotate) to be
 * accelerated, decelerated, repeated, etc.
 * <p/>
 * This class is useful when coding your own animation logic in a Game or other animated application.
 *
 * @author Ahmed Shakil
 * @date Jan 11 2012
 */
public final class Interpolators {

	/**
	 * Constructor private.
	 */
	private Interpolators () {
	}

	/**
	 * An interpolator where the rate of change is constant.
	 * <p/>
	 * Maps a value representing the elapsed fraction of an animation
	 * to a value that represents the interpolated fraction.
	 * <p/>
	 * This interpolated value is then multiplied by the change in
	 * value of an animation to derive the animated value at the
	 * current elapsed animation time.
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 */
	public static float linear (float input) {
		return input;
	}

	/**
	 * An interpolator where the rate of change starts out slowly and and then accelerates
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 */
	public static float accelerate (float input) {
		return input * input;
	}

	/**
	 * An interpolator where the rate of change starts out slowly and and then accelerates
	 *
	 * @param input  A value between 0 and 1.0 indicating our current
	 *               point in the animation where 0 represents the start
	 *               and 1.0 represents the end
	 * @param factor Degree to which the animation should be eased. Seting
	 *               factor to 1.0f produces a y=x^2 parabola. Increasing factor above
	 *               1.0f  exaggerates the ease-in effect (i.e., it starts even
	 *               slower and ends evens faster)
	 * @return The interpolation value.
	 */
	public static float accelerate (float input, float factor) {
		return factor == 1.0f ? input * input : (float)Math.pow( input, factor * 2.0 );
	}

	/**
	 * An interpolator where the rate of change starts out quickly and
	 * and then decelerates.
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 */
	public static float decelerate (float input) {
		return 1.0f - (1.0f - input) * (1.0f - input);
	}

	/**
	 * An interpolator where the rate of change starts out quickly and
	 * and then decelerates.
	 *
	 * @param input  A value between 0 and 1.0 indicating our current
	 *               point in the animation where 0 represents the start
	 *               and 1.0 represents the end
	 * @param factor Degree to which the animation should be eased. Setting factor to 1.0f produces
	 *               an upside-down y=x^2 parabola. Increasing factor above 1.0f makes exaggerates the
	 *               ease-out effect (i.e., it starts even faster and ends evens slower)
	 * @return The interpolation value.
	 */
	public static float decelerate (float input, float factor) {
		if (factor == 1.0f)
			return 1.0f - (1.0f - input) * (1.0f - input);
		else
			return (float)(1.0f - Math.pow( (1.0f - input), 2.0 * factor ));
	}

	/**
	 * An interpolator where the rate of change starts and ends slowly but
	 * accelerates through the middle.
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 */
	public static float accelerateDecelerate (float input) {
		return (float)(Math.cos( (input + 1) * Math.PI ) / 2.0) + 0.5f;
	}

	/**
	 * An interpolator where the change starts backward then flings forward.
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 */
	public static float anticipate (float input) {
		return anticipate( input, 2f );
	}

	/**
	 * An interpolator where the change starts backward then flings forward.
	 *
	 * @param input   A value between 0 and 1.0 indicating our current
	 *                point in the animation where 0 represents the start
	 *                and 1.0 represents the end
	 * @param tension Amount of anticipation. When tension equals 0.0f, there is
	 *                no anticipation and the interpolator becomes a simple
	 *                acceleration interpolator.
	 * @return The interpolation value.
	 */
	public static float anticipate (float input, float tension) {
		return input * input * ((tension + 1) * input - tension);
	}

	/**
	 * An interpolator where the change starts backward then flings forward.
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 *         This value can be more than 1.0 for interpolators when overshooting the target,
	 *         or less than 0 for interpolators that undershoot the it.
	 */
	public static float anticipateOvershoot (float input) {
		return anticipateOvershoot( input, 2f * 1.5f );
	}

	/**
	 * An interpolator where the change starts backward then flings forward.
	 *
	 * @param input   A value between 0 and 1.0 indicating our current
	 *                point in the animation where 0 represents the start
	 *                and 1.0 represents the end
	 * @param tension Amount of anticipation/overshoot. When tension equals 0.0f,
	 *                there is no anticipation/overshoot and the interpolator becomes
	 *                a simple acceleration/deceleration interpolator.
	 * @return The interpolation value.
	 *         This value can be more than 1.0 for interpolators when overshooting the target,
	 *         or less than 0 for interpolators that undershoot the it.
	 */
	public static float anticipateOvershoot (float input, float tension) {
		if (input < 0.5f)
			return 0.5f * a( input * 2.0f, tension );
		else
			return 0.5f * ( o( input * 2.0f - 2.0f, tension ) + 2.0f );
	}

	/**
	 * An interpolator where the change starts backward then flings forward.
	 *
	 * @param input		A value between 0 and 1.0 indicating our current
	 *                     point in the animation where 0 represents the start
	 *                     and 1.0 represents the end
	 * @param tension	  Amount of anticipation/overshoot. When tension equals 0.0f,
	 *                     there is no anticipation/overshoot and the interpolator becomes
	 *                     a simple acceleration/deceleration interpolator.
	 * @param extraTension Amount by which to multiply the tension. For instance,
	 *                     to get the same overshoot as an OvershootInterpolator with
	 *                     a tension of 2.0f, you would use an extraTension of 1.5f.
	 *                     
	 * @return The interpolation value.
	 *         This value can be more than 1.0 for interpolators when overshooting the target,
	 *         or less than 0 for interpolators that undershoot the it.
	 */
	public static float anticipateOvershoot (float input, float tension, float extraTension) {
		return anticipateOvershoot( input, tension * extraTension );
	}

	/**
	 * An interpolator where the change bounces at the end.
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 */
	public static float bounce (float input) {
		input *= 1.1226f;
		if (input < 0.3535f) return b( input );
		else if (input < 0.7408f) return b( input - 0.54719f ) + 0.7f;
		else if (input < 0.9644f) return b( input - 0.8526f ) + 0.9f;
		else return b( input - 1.0435f ) + 0.95f;
	}

	/**
	 * Repeats the animation for a specified number of cycles. The
	 * rate of change follows a sinusoidal pattern.
	 *
	 * @param input  A value between 0 and 1.0 indicating our current
	 *               point in the animation where 0 represents the start
	 *               and 1.0 represents the end
	 * @param cycles number of cycles
	 * 
	 * @return The interpolation value.
	 */
	public static float cycle (float input, float cycles) {
		return (float)(Math.sin( 2 * cycles * Math.PI * input ));
	}

	/**
	 * An interpolator where the change flings forward and overshoots the last value
	 * then comes back.
	 *
	 * @param input A value between 0 and 1.0 indicating our current
	 *              point in the animation where 0 represents the start
	 *              and 1.0 represents the end
	 * @return The interpolation value.
	 *         This value can be more than 1.0 for interpolators when overshooting the target,
	 *         or less than 0 for interpolators that undershoot the it.
	 */
	public static float overshoot (float input) {
		return overshoot( input, 2f );
	}

	/**
	 * An interpolator where the change flings forward and overshoots the last value
	 * then comes back.
	 *
	 * @param input   A value between 0 and 1.0 indicating our current
	 *                point in the animation where 0 represents the start
	 *                and 1.0 represents the end
	 * @param tension Amount of overshoot. When tension equals 0.0f, there is
	 *                no overshoot and the interpolator becomes a simple
	 *                deceleration interpolator.
	 * @return The interpolation value.
	 *         This value can be more than 1.0 for interpolators when overshooting the target,
	 *         or less than 0 for interpolators that undershoot the it.
	 */
	public static float overshoot (float input, float tension) {
		input -= 1.0f;
		return input * input * ((tension + 1) * input + tension) + 1.0f;
	}

	/**
	 * Bounc algo.
	 */
	private static float b (float t) {
		return t * t * 8.0f;
	}

	/**
	 * Anitcipate algo.
	 */
	private static float a (float t, float s) {
		return t * t * ((s + 1) * t - s);
	}

	/**
	 * Overshoot algo.
	 */
	private static float o (float t, float s) {
		return t * t * ((s + 1) * t + s);
	}
}
