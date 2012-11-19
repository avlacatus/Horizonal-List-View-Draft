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

package com.sileria.android.bc;

import android.view.*;

/**
 * ICS 4.0 WRAPPER.  API Level - 14 & 15.
 *
 * @author Ahmed Shakil
 * @date Jan 16, 2012
 */
class IceCream extends HoneyComb {


	/**
	 * Release surface which is not available until API level 14.
	 */
	@Override
	public void release (Surface surface) {
		surface.release();
	}

}
