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
 * ContentOptions associated with loading of a <code>Content</code>.
 * <p/>
 * <strong>NOTE</strong>: any implementation must implement {@linkplain Object#equals(Object)}
 * and {@linkplain Object#hashCode()} methods.
 *
 * @author Ahmed Shakil
 * @date 08-26-2012
 *
 * @see com.sileria.android.util.ContentLoader
 * @see com.sileria.android.util.QueuedContentLoader
 * @see com.sileria.android.util.ImageOptions
 */
public interface ContentOptions extends java.io.Serializable {

}
