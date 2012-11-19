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
 * AsyncCallback. Intended to be implemented by classes that wish to be notified on completion of an asynchronous task.
 * <p/>
 * This class is used with almost all background threads in the Aniqroid API.
 *
 * @author Ahmed Shakil
 * @date Mar 12, 2010
 * @param <T> Type of object returned to the listener.
 */
public interface AsyncCallback<T> extends AsyncObserver<T, Throwable> {

}
