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
 * Cancelable interface defines a single cancel method to be implemented
 * by any network or threaded call that can be cancelled by the user.
 *
 * @author Ahmed Shakil
 * @date Mar 12, 2010
 */
public interface Cancellable {

    /**
     * Cancel the request or a thread.
     * <p/>
     * Note: This method does not guarentee immediate
     * cancellation, but may take a while to effectively
     * cancel the request.
     */
    void cancel ();
}
