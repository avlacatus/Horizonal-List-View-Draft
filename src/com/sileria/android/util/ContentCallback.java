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

import com.sileria.util.Content;
import com.sileria.util.ContentOptions;

/**
 * ImageLoader callback listener interface.
 *
 * @see ContentLoader
 * @see QueuedContentLoader
 *
 * @author Ahmed Shakil
 * @date Jul 1, 2012
 *
 * @param <T> content type that will be loaded.
 */
public interface ContentCallback<T, O extends ContentOptions> {

    /**
     * This method is invoked on each content load.
     * @param content content that was loaded.
	 * content.id represents ID if passed using the {@link ContentLoader#execute(String, int)};
	 * 	otherwise the index in order it was passed.
	 * content.key represents URL of content that was passed
	 */
    void onContentLoad (Content<T, O> content);

    /**
     * This method is invoked for each content load failure.
     * @param error error that occured during the load
	 * failure.id represents ID if passed using the {@link ContentLoader#execute(String, int)};
	 * 	otherwise the index in order it was passed.
	 * failure.key represents URL of content that was passed
     */
    void onContentFail (Content<Throwable, O> error);
}
