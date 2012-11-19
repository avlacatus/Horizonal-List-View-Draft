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
 * ParserFactory provides mechanism for creating a single factory class or create all
 * the parsers that you need for you application while also providing a luxury of lazy creating
 * the data parser when it is needed.
 * <p/>
 * This class is used by {@link com.sileria.net.ParsedRequest} and any of its subclasses to provide
 * a way of parsing your raw webservice data from lets say xml or json into concrete model objects.
 * <p/>
 * <strong>Lazy Creation Example:</strong>
 * <blockquote><pre>
 * public class Parsers implements ParserFactory {
 *      private static Map<Class<?>, ReflectiveObject<DataParser<?,?>>> map =
 *           new HashMap<Class<?>, ReflectiveObject<DataParser<?,?>>>();
 *
 *      static {
 *          map.put( Search.class, new ReflectiveObject<DataParser<?,?>>("com.blah.blah.SearchParser") );
 *          map.put( Place.class,  new ReflectiveObject<DataParser<?,?>>("com.blah.blah.LocationParser") );
 *      }
 *
 *      public <T, V> DataParser<T, V> getDataParser (Class<?> clazz) {
 *   	    ReflectiveObject<DataParser<T, V>> ro = (ReflectiveObject)map.get( clazz );
 *
 *   	    if (ro == null)
 *   	        return null;
 *
 *   	    return ro.get();
 *      }
 * }
 *
 * public class Requests {
 *
 *      private final ParserFactory factoryInstance = new ParserFactory();   // Shared instance.
 *
 *      public Cancellable loadCities (URL webserviceUrl, RemoteCallback callback) {
 *     		StringRequest req = new StringRequest (webServiceUrl);
 *     		req.setParser(factoryInstance, Place.class);
 *     	    return new RemoteTask(callback).execute(req);
 *      }
 * }
 *
 * </pre></blockquote>
 *
 * @author Ahmed Shakil
 * @date Mar 20, 2010
 */
public interface ParserFactory {

	/**
	 * Create instance of the <code>RemoteParser</code> for specified type <T>.
	 *
	 * @return instance of data parser
	 */
	<T, V> DataParser<T, V> getDataParser (Class<?> clazz);

}
