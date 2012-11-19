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
 * DataParser.  Should be implemented by classes that parse data from a raw format to the desired object type.
 * <p/>
 * The implementation will then be provided to any of the subclasses of {@link com.sileria.net.ParsedRequest}
 * and the {@link #parse(Object)} method will be called of the data was retrieved from the webservice.
 * <p/>
 * Basically purpose of this class is to convert a raw data like json or xml into your model classes by using
 * the provided request classes in the {@link com.sileria.net} package.
 *
 * @author Ahmed Shakil
 * @date March 8, 2010
 *
 * @see com.sileria.net.ParsedRequest
 *
 * @param <T> Parsed object type which is returned
 * @param <R> Raw data type
 */
public interface DataParser<T, R> {

    /**
     * Parse search result into object.
     * 
     * @throws ParseException in case of any unexpected parsing problem
     */
    public T parse (R data) throws ParseException;

}
