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

package com.sileria.android.util;

import android.graphics.Bitmap;

/**
 * Convenience taging interface to be used by image loader classes.
 *
 * @see com.sileria.android.util.BitmapLoader
 * @see com.sileria.android.util.QueuedBitmapLoader
 *
 * @author Ahmed Shakil
 * @date Jul 1, 2012
 */
public interface BitmapCallback extends ContentCallback<Bitmap, ImageOptions> {
}
