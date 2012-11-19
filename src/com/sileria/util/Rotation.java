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

package com.sileria.util;

/**
 * Rotation.
 *
 * @author Ahmed Shakil
 * @date 08-11-2012
 */
public enum Rotation {
	
	_0(0), _90(90), _180( 180 ), _270( 270 );

	public final int angle;

	private Rotation (int angle) {this.angle = angle;}

	/**
	 * Get the enum from any of the positive right angles; otherwise returns <code>null</code>.
	 */
	public static Rotation valueOf (int angle) {
		Rotation[] angles = values();
		for (Rotation r : angles)
			if (r.angle == angle)
				return r;

		return null;
	}
}
