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
 * ByteCounter return size of bytes that an object takes in memory.
 *
 * @author Ahmed Shakil
 * @date 08-27-2012
 */
public interface ByteCounter<T> {

	/**
	 * Return size of bytes that an object takes in memory.
	 */
	int sizeOf (T object);
}
