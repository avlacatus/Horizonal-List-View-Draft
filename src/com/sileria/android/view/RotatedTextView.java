/*
 * Copyright (c) 2003 - 2012 Sileria, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.sileria.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

import com.sileria.util.Orientation;
import com.sileria.util.Rotation;

/**
 * RotatedTextView.
 *
 * @author Ahmed Shakil
 * @date 08-11-2012
 */
public class RotatedTextView extends TextView {

	private Orientation flip;
	private Rotation rotation;

	private boolean reverse;
	private boolean vertical;

	public RotatedTextView (Context context) {
		super( context );
	}

	public RotatedTextView (Context context, AttributeSet attrs) {
		super( context, attrs );
	}

	public RotatedTextView (Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
	}

	public void setFlip (Orientation orientation) {
		flip = orientation;
	}

	public void setRotate (Rotation angle) {
		rotation = angle;
		vertical = rotation == Rotation._90 || rotation == Rotation._270;
	}

	public Orientation getFlip () {
		return flip;
	}

	public Rotation getRotate () {
		return rotation;
	}

	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
		if (vertical) {
			super.onMeasure( heightMeasureSpec, widthMeasureSpec );
			setMeasuredDimension( getMeasuredHeight(), getMeasuredWidth() );
		}
		else
			super.onMeasure( widthMeasureSpec, heightMeasureSpec );
	}

	@Override
	protected void onDraw (Canvas canvas) {
		TextPaint textPaint = getPaint();
		textPaint.setColor( getCurrentTextColor() );
		textPaint.drawableState = getDrawableState();

		final int compoundPaddingLeft = getCompoundPaddingLeft();
		final int compoundPaddingTop = getCompoundPaddingTop();
		final int compoundPaddingRight = getCompoundPaddingRight();
		final int compoundPaddingBottom = getCompoundPaddingBottom();
		final int scrollX = getScrollX();
		final int scrollY = getScrollY();
		final int right = getRight();
		final int left = getLeft();
		final int bottom = getBottom();
		final int top = getTop();

		int extendedPaddingTop = getExtendedPaddingTop();
		int extendedPaddingBottom = getExtendedPaddingBottom();

		int x1 = compoundPaddingLeft + scrollX;
		int y1 = extendedPaddingTop + scrollY;
		int x2 = right - left - compoundPaddingRight + scrollX;
		int y2 = bottom - top - extendedPaddingBottom + scrollY;

		canvas.save();

//		if (topDown) {
//			canvas.translate( getWidth(), 0 );
//			canvas.rotate( 90 );
//		}
//		else {
//			canvas.translate( 0, getHeight() );
//			canvas.rotate( -90 );
//		}

		int px = x1 + getWidth() / 2;
		int py = y1 + getHeight() / 2;

		if (rotation != null) {
			if (vertical) {
				reverse = vertical;
				int tx = 0;
				int dx = getHeight() - getWidth();
				int dy = rotation == Rotation._90 ? getHeight() - getWidth() : 0;
//					int pv = px;
				px = x1 + getWidth() / 2;
//					pv = rotation == Rotation.T_90 ? py : px;

				final int gv = Gravity.VERTICAL_GRAVITY_MASK & getGravity();
				final int gh = Gravity.HORIZONTAL_GRAVITY_MASK & getGravity();
				if (gh == Gravity.LEFT) {
					cpr = dx;
				}

				if (gh == Gravity.RIGHT) {
					cpl = dx;
					//cpr = -dx;
				}

				if (gh == Gravity.CENTER_HORIZONTAL) {
					cpr = dx;
				}

				if (gv == Gravity.CENTER_VERTICAL) {
					py = y1 + getHeight()/ 2;
					tx = dx/2;
				}

				if (gv == Gravity.TOP) {
					py = y1 + getWidth() / 2;
					tx = Math.abs(dy - dx);
				}

				if (gv == Gravity.BOTTOM) {
					py = y1 + getHeight() - getWidth() / 2;
					tx = Math.abs(dy - dx);
				}

				canvas.rotate( rotation.angle, px, py );
				canvas.translate( -tx, 0 );
			}
			else
				canvas.rotate( rotation.angle, px, py );
		}
		if (flip != null)
			canvas.scale( flip==Orientation.HORIZONTAL ? -1 : 1, flip==Orientation.VERTICAL ? -1 : 1, px, py );


		//canvas.translate( getCompoundPaddingLeft(), getExtendedPaddingTop() );

		//getLayout().draw( canvas );
		super.onDraw( canvas );
		cpl = cpr = 0;
		canvas.restore();
	}

	private int cpr, cpl;

	/**
	 * A very nasty hack to make 90 and 270 degree angle to work.
	 */
	@Override
	public final int getCompoundPaddingRight () {
		return super.getCompoundPaddingRight() - cpr;
	}

	/**
	 * A very nasty hack to make 90 and 270 degree angle to work.
	 */
	@Override
	public final int getCompoundPaddingLeft () {
		return super.getCompoundPaddingLeft() + cpl;
	}


}
