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

package com.sileria.android;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

import com.sileria.util.Utils;

import static com.sileria.android.Kit.TAG;

/**
 * Resource manager class for accessing strings, messages, images and other
 * resources. This class uses the {@link Resources} to retrieve locale
 * based resources, but provides many shortcuts and convenience methods for
 * accessing these resources programmatically.
 * <p/>
 * Also you do not need to provide {code Context} to this class to be able to
 * access the resources. It uses the application context from the {@code Kit} class.
 * Please read the note below:
 * <p/>
 * <strong>NOTE</strong>: Donot forget to call {@link Kit#init(android.content.Context)}
 * before using this class.
 * <p/>
 *
 * <a name="strings"><h3> Strings </h3></a>
 * Two sets of resource bundles are maintained in this class. One for all
 * the labels which are associated with any GUI widget, e.g. labels, buttons,
 * checkboxes, etc...
 * Following is the samplee on how to get these strings:
 * <blockquote><pre>
 *     Resource.getString( "ok" );
 *     Resource.getString( R.string.cancel );
 * </pre></blockquote>
 * <p/>
 *
 * <a name="messages"><h3> Enum </h3></a>
 * Strings can be defined for enums as well using this Resource loader class.
 * You will have to define the string key in your strings file with the same
 * name as the enum but all in lowercase.</p>
 * Following is the sample on how to get the string for enum.:
 * <blockquote><pre>
 *     enum Scope { PUBLIC, PRIVATE };
 *
 *     Resource.getString( Scope.PUBLIC );
 * </pre></blockquote>
 * <p/>
 *
 * <a name="params"><h3> Parameters </h3></a>
 * <code>getString</code> method also take variable arguments to be filled in
 * for parameters in a string message. Java 5's printf-style
 * formatting is used to define parameters in the string resources.
 * </p>
 * An example fo this is as follows:
 * <blockquote><pre>
 *     // Show a message like: "File 10 of 100"
 *     Resource.getString( "file_x_of_x", i, fileCount );
 *
 *     // Show a message like: "Welcome, Ahmed Shakil"
 *     Resource.getString( "welcome_user", fullname );
 * </pre></blockquote>
 * <p/>
 *
 * <strong>Note:</strong> <i>No exception is thrown when a string or message
 * is not found, instead an error message is printed to <code>System.err</code>
 * and the <code>key<code> which was passed to the message will be returned
 * as an alternative substitute for the missing string resource.
 * .</i>
 * <p/>
 *
 * <a name="images"><h3> Images </h3></a>
 * Images can be loaded as <i>drawable</i> or <i>bitmap</i>. For details
 * on formatting please see {@link java.util.Formatter Formatter} class. 
 * <p/>
 * Examples of the image loader methods are:
 * <blockquote><pre>
 *     // Load an image from a file
 *     Drawable img = Resource.getImage( "logo" );
 *
 *     // Load a bitmap from an image file
 *     Bitmap icon = Resource.getBitmap( "delete" );
 * </pre></blockquote>
 * <a name="rules"><h2> More </h2></a>
 * Please look at the method list to see full API supported in this class.
 *
 * @author Ahmed Shakil
 * @version 1.0
 * @date Jun 25, 2003
 */
public final class Resource {

	private static Context ctx;

	private static String pkg;

	private static Resources res;

	private static Locale defLocale;

    private static HashMap<Integer, Drawable> imageCache;

    /**
     * Initialize the <code>Resource</code> object with an application context.
     * <p/>
     * NOTE: Kit.init() must be called with the correct application
	 * context before any method can be used in this class.
     *
     * @param ctx Make sure to provide application context here.
     */
    static void init (Context ctx) {
        Resource.ctx   = ctx;
        Resource.res   = ctx.getResources();
        Resource.pkg   = ctx.getPackageName();

		if (defLocale != null)
			defLocale = Locale.getDefault();

        clearCache();
    }

	/**
	 * Destroy everything.
	 */
	static void destroy () {
		clearCache();

		Resource.ctx   = null;
		Resource.res   = null;
		Resource.pkg   = null;
	}

	/**
	 * Constructor, package protected.
	 */
	private Resource () {
	}

	/**
	 * Set the app level locale.
	 * @param locale Locale to be set for the app. Can be <code>null</code> to try to set to default.
	 */
	public static void setLocale (Locale locale) {
		if (locale == null && defLocale == null) return;

		if (locale == null) locale = defLocale;

		if (locale.equals( Locale.getDefault() )) return;

		Locale.setDefault( locale );

		Configuration config = new Configuration();
		config.locale = locale;
		res.updateConfiguration(config, res.getDisplayMetrics());
	}


	/**
	 * Gets the string from the resource bundle
	 * and replace the first matching patterns with the provided arg.
	 */
	public static String getString (String key, Object... arg) {

		int id = res.getIdentifier( key, "string", pkg );
		if (id == 0) {
			Log.w( TAG, "Resource.getString() - Missing string: " + key );
			return key;
		}

		return res.getString( id, arg );
	}

	/**
	 * Gets the string from the resource bundle
	 * and replace the first matching patterns with the provided arg.
	 */
	public static String getString (int key, Object... arg) {
		return res.getString( key, arg );
	}

    /**
     * Gets the string from the resource bundle for specified enum key.
     */
    public static String getString (Enum<?> e) {

        String key = e.toString();

        int id = res.getIdentifier( key.toLowerCase(), "string", pkg );
        if (id == 0) {
            Log.w( TAG, "Resource.getString() - Missing enum string: " + key );
            return key;
        }

        return res.getString( id );
    }

	/**
	 * Check to see if a identifier exists.
	 */
	public static boolean hasString (String key) {
		return res.getIdentifier( key, "string", pkg ) != 0;
	}

	/**
	 * Gets the formatted string from the resource bundle
	 * and replace the first matching patterns with the provided arg.
	 */
	public static CharSequence getText (String key) {

		int id = res.getIdentifier( key, "string", pkg );
		if (id == 0) {
			Log.w( TAG, "Resource.getString() - Missing string: " + key );
			return key;
		}

		return res.getText( id );
	}

	/**
	 * Gets the formatted string from the resource bundle
	 * and replace the first matching patterns with the provided arg.
	 */
	public static CharSequence getText (int key) {
		return res.getText( key );
	}

	/**
	 * Convenience method to get multiple strings from the string resource bundle.
	 * @param keys multiple keys
	 * @return strings
	 */
	public static String[] getStrings (String ... keys) {
		if (Utils.isEmpty(keys))
			return Utils.EMPTY_STRING_ARRAY;

		String[] values = new String[keys.length];
		for (int i=0; i<keys.length; i++)
			values[i] = Resource.getString(keys[i]);

		return values;
	}

	/**
	 * Convenience method to get multiple strings from the string resource bundle.
	 * @param e multiple keys
	 * @return strings
	 */
	public static <E extends Enum<E>> String[] getStrings (Class<E> e) {

		if (e == null)
			return Utils.EMPTY_STRING_ARRAY;

		E[] keys = e.getEnumConstants();
		String[] values = new String[keys.length];
		for (int i=0; i<keys.length; i++)
			values[i] = getString( keys[i].toString().toLowerCase() );

		return values;
	}

	/**
	 * Gets the string from the resource bundle
	 * and replace the first matching patterns with the provided arg.
	 */
	public static String getQuantityString (String key, int qty, Object... arg) {

		int id = res.getIdentifier( key, "plurals", pkg );
		if (id == 0) {
			Log.w( TAG, "Resource.getQuanityString() - Missing plural: " + key );
			return key;
		}

		return getQuantityString( id, qty, arg );
	}

	/**
	 * Gets the string from the resource bundle
	 * and replace the first matching patterns with the provided arg.
	 */
	public static String getQuantityString (int key, int qty, Object... arg) {
		if (Utils.isEmpty( arg ))
			return res.getQuantityString( key, qty, qty );
		else
			return res.getQuantityString( key, qty, arg );
	}

	/**
	 * Get image from the resources.
	 * @param image image resource name
	 */
	public static Drawable getImage (String image) {
        return getImage( image, false );
	}

	/**
	 * Get image from the resources by resource id.
	 * @param id image resource id
	 */
	public static Drawable getImage (int id) {
        return getImage( id, false );
	}

	/**
	 * Get image from the resources in sampled width and height.
	 * @param image image resource name
	 * @param width requested in width
	 * @param height requested in heigh
	 */
	public static Drawable getImage (String image, int width, int height) {
		int id = res.getIdentifier( image, "drawable", pkg );

		if (id == 0) {
			Log.w( TAG, "Resource.getImage() - Missing drawable: " + image );
			return null;
		}

        return getImage( id, width, height );
	}

	/**
	 * Get image from the resources in sampled width and height.
	 * @param id image resource id
	 * @param width requested in width
	 * @param height requested in heigh
	 */
	public static Drawable getImage (int id, int width, int height) {
		Bitmap bmp = decodeSampledBitmap( id, width, height );
		return bmp == null ? null : new BitmapDrawable( res, bmp );
	}

	/**
	 * Get image from the resources.
     * @param image image name
     * @param cache if <code>true</code> then cache the image and get the cached image next time;
     *   otherwise get a new image every single time. (Please use wisely)
	 */
	public static Drawable getImage (String image, boolean cache) {
		int id = res.getIdentifier( image, "drawable", pkg );

		if (id == 0) {
			Log.w( TAG, "Resource.getImage() - Missing drawable: " + image );
			return null;
		}

        return getImage( id, cache );
	}

    /**
     * Get image from the resources by id.
     * @param id image id
     * @param cache if <code>true</code> then cache the image and get the cached image next time;
     *   otherwise get a new image every single time. (Please use wisely)
     */
    public static Drawable getImage (int id, boolean cache) {
        Drawable img;
        if (cache) {
            if (imageCache == null)
                imageCache = new HashMap<Integer, Drawable>();
            img = imageCache.get( id );
            if (img == null)
                imageCache.put( id, img = res.getDrawable( id ) );
        }
        else
            img = res.getDrawable( id );

        return img;
    }

	/**
	 * Check to see if a identifier exists.
	 */
	public static boolean hasImage (String key) {
		return res.getIdentifier( key, "drawable", pkg ) != 0;
	}

	/**
	 * Get bitmap from the resources by id.
	 * @param id bitmap id
	 */
	public static Bitmap getBitmap (int id) {
		return BitmapFactory.decodeResource( res, id );
	}

	/**
	 * Get bitmap from the resources.
     * @param bitmap bitmap name
	 */
	public static Bitmap getBitmap (String bitmap) {
		int id = res.getIdentifier( bitmap, "drawable", pkg );

		if (id == 0) {
			Log.w( TAG, "Resource.getBitmap() - Missing bitmap: " + bitmap );
			return null;
		}

        return getBitmap( id );
	}

	/**
	 * Get image from the resources in sampled width and height.
	 * @param bitmap image resource name
	 * @param width requested in width
	 * @param height requested in heigh
	 */
	public static Bitmap getBitmap (String bitmap, int width, int height) {
		int id = res.getIdentifier( bitmap, "drawable", pkg );

		if (id == 0) {
			Log.w( TAG, "Resource.getBitmap() - Missing bitmap: " + bitmap );
			return null;
		}

        return getBitmap( id, width, height );
	}

	/**
	 * Get image from the resources in sampled width and height.
	 * @param id bitmap resource id
	 * @param width requested in width
	 * @param height requested in heigh
	 */
	public static Bitmap getBitmap (int id, int width, int height) {
		return decodeSampledBitmap( id, width, height );
	}

	/**
	 * Get bitmap from the resources.
     * @param bitmap bitmap name
	 * @param op null-ok; Options that control downsampling and whether the
	 *             image should be completely decoded, or just is size returned.
	 * @return The decoded bitmap, or null if the image data could not be
	 *         decoded, or, if opts is non-null, if opts requested only the
	 *         size be returned (in opts.outWidth and opts.outHeight)
	 */
	public static Bitmap getBitmap (String bitmap, BitmapFactory.Options op) {
		int id = res.getIdentifier( bitmap, "drawable", pkg );

		if (id == 0) {
			Log.w( TAG, "Resource.getBitmap() - Missing bitmap: " + bitmap );
			return null;
		}

		return BitmapFactory.decodeResource( res, id, op );
	}

	/**
	 * Get bitmap from the resources.
	 * @param id The resource id of the image data
	 * @param op null-ok; Options that control downsampling and whether the
	 *             image should be completely decoded, or just is size returned.
	 * @return The decoded bitmap, or null if the image data could not be
	 *         decoded, or, if opts is non-null, if opts requested only the
	 *         size be returned (in opts.outWidth and opts.outHeight)
	 */
	public static Bitmap getBitmap (int id, BitmapFactory.Options op) {
		return BitmapFactory.decodeResource( res, id, op );
	}

	/**
	 * Get image resource with specified repeat x/y modes.
	 *
	 * @param id The resource id of the image data
	 * @param xmode The X repeat mode for this drawable. If <code>null</code> then unchanged.
	 * @param ymode The Y repeat mode for this drawable.
	 */
	public static Drawable getImage (int id, Shader.TileMode xmode, Shader.TileMode ymode) {
		Bitmap bmp = getBitmap( id );
		if (bmp == null) return null;

		BitmapDrawable img = new BitmapDrawable( res, bmp );
		img.setTileModeXY( xmode, ymode );
		return img;
	}

    /**
     * Get the xml resource by name.
     */
    public static XmlResourceParser getXml (String xml) {
        int id = res.getIdentifier( xml, "xml", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getXml() - Missing xml: " + xml );
            return null;
        }

        return res.getXml( id );
    }

    /**
     * Get the xml resource by id.
     */
    public static XmlResourceParser getXml (int id) {
        return res.getXml( id );
    }

	/**
	 * Get drawable id.
	 */
	public static int getDrawableId (String image) {
		return res.getIdentifier( image, "drawable", pkg );
	}

	/**
	 * Get color by name.
	 */
	public static int getColor (String name) {
        int id = res.getIdentifier( name, "color", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getColor() - Missing color: " + name );
            return 0;
        }

		return res.getColor( id );
	}

	/**
	 * Get color by id.
	 */
	public static int getColor (int iid) {
		return res.getColor( iid );
	}

	/**
	 * Get dimension by name.
	 */
	public static float getDimension (String name, float defValue) {
        int id = res.getIdentifier( name, "dimen", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getDimension() - Missing dimen: " + name );
            return defValue;
        }

		return res.getDimension( id );
	}

	/**
	 * Get dimension by id.
	 */
	public static float getDimension (int iid) {
		return res.getDimension( iid );
	}

	/**
	 * Get dimension pixel size by name.
	 */
	public static int getDimenSize (String name, int defValue) {
        int id = res.getIdentifier( name, "dimen", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getDimenSize() - Missing dimen: " + name );
            return defValue;
        }

		return res.getDimensionPixelSize( id );
	}

	/**
	 * Get dimension pixel size by id.
	 */
	public static int getDimenSize (int iid) {
		return res.getDimensionPixelSize( iid );
	}

	/**
	 * Get dimension pixel size by name.
	 */
	public static int getDimenOffset (String name, int defValue) {
        int id = res.getIdentifier( name, "dimen", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getDimenOffset() - Missing dimen: " + name );
            return defValue;
        }

		return res.getDimensionPixelOffset( id );
	}

	/**
	 * Get dimension pixel size by id.
	 */
	public static int getDimenOffset (int iid) {
		return res.getDimensionPixelOffset( iid );
	}

	/**
	 * Get int by name.
	 */
	public static int getInteger (String name, int defValue) {
        int id = res.getIdentifier( name, "integer", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getInteger() - Missing integer: " + name );
            return defValue;
        }

		return res.getInteger( id );
	}

	/**
	 * Get int by id.
	 */
	public static int getInteger (int iid) {
		return res.getInteger( iid );
	}

	/**
	 * Get bool by name.
	 */
	public static boolean getBoolean (String name) {
        int id = res.getIdentifier( name, "bool", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getBoolean() - Missing boolean: " + name );
            return false;
        }

		return res.getBoolean( id );
	}

	/**
	 * Get bool by id.
	 */
	public static boolean getBoolean (int bid) {
		return res.getBoolean( bid );
	}

	/**
	 * Get int array by name.
	 */
	public static int[] getIntArray (String name) {
        int id = res.getIdentifier( name, "array", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getIntArray() - Missing array: " + name );
            return null;
        }

		return res.getIntArray( id );
	}

	/**
	 * Get String array by name.
	 */
	public static String[] getStringArray (String name) {
        int id = res.getIdentifier( name, "array", pkg );

        if (id == 0) {
            Log.w( TAG, "Resource.getStringArray() - Missing array: " + name );
            return null;
        }

		return res.getStringArray( id );
	}

	/**
	 * Resources instance this object is using internally.
	 */
	public static Resources getResources () {
		return res;
	}

	/**
     * Clear all caches.
     */
    public static void clearCache () {
        if (imageCache != null) {
            imageCache.clear();
            imageCache = null;
        }
    }

	/**
	 * Get current version number.
	 */
	public static String getVersionName () {
		String version = "?";

		try {
			PackageInfo pi = ctx.getPackageManager().getPackageInfo( ctx.getPackageName(), 0 );
			version = pi.versionName;
		}
		catch (PackageManager.NameNotFoundException e) {
			Log.e( TAG, "Package name not found", e );
		}

		return version;
	}

	/**
	 * Get current version number.
	 */
	public static int getVersionCode () {
		int version = 0;

		try {
			PackageInfo pi = ctx.getPackageManager().getPackageInfo( ctx.getPackageName(), 0 );
			version = pi.versionCode;
		}
		catch (PackageManager.NameNotFoundException e) {
			Log.e( TAG, "Package name not found", e );
		}

		return version;
	}

	/**
	 * Return the name of this application's package.
	 */
	public static String getPackageName () {
		return ctx.getPackageName();
	}


	/**
	 * Get raw resource stream
	 */
	public static InputStream getRawStream (String name) {

		int id = res.getIdentifier( name, "raw", pkg );

		if (id == 0) {
			Log.w( TAG, "Resource.getResourceStream() - Missing resource: " + name );
			return null;
		}

		return res.openRawResource( id );
	}

	/**
	 * Get raw resource stream
	 */
	public static InputStream getRawStream (int rawId) {
		return res.openRawResource( rawId );
	}

	/**
	 * Get asset input stream.
	 * Returns <code>null</code> if asset does not exist, instead of throwing an exception.
	 */
	public static InputStream getAssetStream (String name) {
		try {
			return res.getAssets().open( name );
		}
		catch (IOException e) {
			Log.w( TAG, "Resource.getAssetStream() - Missing asset: " + name );
			return null;
		}
	}

	/**
	 * Get resource identifier of the specified type.
	 */
	public static int getIdentifier (String key, String type) {
		return res.getIdentifier( key, type, pkg );
	}

	/**
	 * Check for resource identifier of the specified type.
	 */
	public static boolean hasIdentifier (String key, String type) {
		return res.getIdentifier( key, type, pkg ) != 0;
	}

	/**
	 * Parses an Enum object from the string saved in preferences with the enum name.
	 *
	 * @param prefs Shared Preferences object
	 * @param key preference key for the string
	 * @param defaultEnum default value to return when null or blank is found
	 *
	 * @return enum value if the correct string was found; otherwise <code>defaultEnum</code> for blank or null values
	 *
	 * @throws NullPointerException if defaultEnum is null
	 * @throws IllegalArgumentException if the specified enum type has
	 *         no constant with the specified name, or the specified
	 *         class object does not represent an enum type
	 */
	public static <E extends Enum<E>> E getEnumPref (SharedPreferences prefs, String key, E defaultEnum) {
		String val = prefs.getString( key, null );
		return Utils.isEmpty( val ) ? defaultEnum : Enum.valueOf( defaultEnum.getDeclaringClass(), val );
	}

	/**
	 * Parses an Enum object from the string saved in preferences with the enum name.
	 *
	 * @param prefs Shared Preferences object
	 * @param key preference key for the string
	 * @param eClass enum class
	 *
	 * @return enum value if the correct string was found; otherwise <code>null</code> for blank or null values
	 *
	 * @throws IllegalArgumentException if the specified enum type has
	 *         no constant with the specified name, or the specified
	 *         class object does not represent an enum type
	 */
	public static <E extends Enum<E>> E getEnumPref (SharedPreferences prefs, String key, Class<E> eClass) {
		String val = prefs.getString( key, null );
		return Utils.isEmpty( val ) ? null : Enum.valueOf( eClass, val );
	}

	/**
	 * Decode sampled bitmap from resources.
	 */
	private static Bitmap decodeSampledBitmap (int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource( res, resId, options );

		// Calculate inSampleSize
		options.inSampleSize = calcInSampleSize( options, reqWidth, reqHeight );

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource( res, resId, options );
	}

	/**
	 * Calculate a the sample size value based on a target width and height
	 */
	public static int calcInSampleSize (BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round( (float)height / (float)reqHeight );
			}
			else {
				inSampleSize = Math.round( (float)width / (float)reqWidth );
			}
		}
		return inSampleSize;
	}
}
