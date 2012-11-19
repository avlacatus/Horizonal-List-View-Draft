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

package com.sileria.android;

import android.R;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Calendar;
import java.util.Date;

import android.widget.*;
import android.view.*;

import com.sileria.util.Utils;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Create views and layouts conveniently with factory methods without
 * needing to pass in the {@code Context} to every single one of them.
 * <p/>
 * The factory methods also let you set many common sense attributes for
 * the views being created like e.g. setting the text on a {@code TextView} while
 * creating, or a onClickListener on a {@code Button} and even a {@code Date} to
 * a {@code DatePicker} class which should have been a no-brainer API available
 * by the default Android API instead of giving us methods like setDay, setMonth
 * and setYear.
 * <p/>
 * <strong>Important Note</strong>: <i>Make sure to make one Tools object per Activity.
 * This class is not designed to be shared between different instances of activities.</i>
 *
 * @author Ahmed Shakil
 * @date Aug 30, 2009
 */
public final class Tools {

	private boolean idOff;

	private static int vid = 10000;

	private DisplayMetrics metrics;

	private static int TEXT_UNIT = TypedValue.COMPLEX_UNIT_SP;

	private static float scale = 1;

	private final Rect rect = new Rect();

	private final Context ctx;

	/**
	 * Do first initialization.
	 */
	public static void init () {
		vid = 10000;
	}

	/**
	 * Set the global text size unit as defined in {@link TypedValue}.
	 * This unit will be used whenever you call {@link #setTextSize(TextView, int)}.
	 * @param textUnit text size unit
	 */
	public static void initTextUnit (int textUnit) {
		TEXT_UNIT = textUnit;
	}

	/**
	 * Add scale to adjust the result returned by {@link #px(float)} and {@link #spx(float)}.
	 * It is useful to put set the adjustment for certain screensize
	 */
	public static void initDensityScale (float scale) {
		Tools.scale = scale;
	}

	/**
	 * Constructor, private.
	 */
	public Tools (Context ctx) {
		this.ctx = ctx;
	}

    /**
     * Get the <code>Context</code> object.
     * @return <code>Context</code>
     */
    public Context getContext () {
        return ctx;
    }

    /**
	 * By calling this method u will get an instance
	 * of Tools class which will generate id less widget
	 * for only on next call.
	 * @return <cod>Tools</code>
	 */
	public Tools noid () {
		idOff = true;
		return this;
	}

	/**
	 * Set id and other basic attributes.
	 */
	public <T extends View> T stamp (T v) {
		if (!idOff)
			v.setId( ++vid );
		else
			idOff = false;
		return v;
	}

	/**
	 * Set id and other basic attributes.
	 */
	public <T extends View> T stamp (T v, int id) {
		v.setId( id );
		return v;
	}

	/**
	 * Create a Button label.
	 */
	public <T extends View> T stamp (T view, int id, View.OnClickListener listener) {
		stamp( view, id );

		if (listener != null)
			view.setOnClickListener( listener );

		return view;
	}

	/**
	 * Stamp a TextView with id & text attributes.
	 */
	public <T extends TextView> T stamp (T v, String text) {
		stamp( v );

		if (text != null)
			v.setText( text );

		return v;
	}

	/**
	 * Stamp a TextView with id & text attributes.
	 */
	private <T extends TextView> T stampt (T v, int resId) {
		stamp( v );
		v.setText( resId );
		return v;
	}

	/**
	 * Stamp a TextView with id & text attributes.
	 */
	private <T extends TextView> T stampt (T v, int resId, View.OnClickListener onClick) {
		stampt( v, resId );

		if (onClick != null)
			v.setOnClickListener( onClick );

		return v;
	}

	/**
	 * Create a Button label.
	 */
	public <T extends TextView> T stamp (T v, String text, View.OnClickListener onClick) {
		stamp( v, text );

		if (onClick != null)
			v.setOnClickListener( onClick );

		return v;
	}

	/**
	 * Get ID of specified View only if the object is not null.
	 */
	private static int getId (View v) {
		return v == null ? View.NO_ID : v.getId();
	}

	/**
	 * Get ID of specified View only if the object is not null.
	 */
	private static int getId (Object v) {
		if (v instanceof View)
			return getId( (View)v );
		else if (v instanceof Integer)
			return (Integer)v;
		else
			return View.NO_ID;
	}

	/**
	 * Returns a blank new view object with stamped Id.
	 */
	public View newView () {
		return stamp( new View( ctx ) );
	}

	/**
	 * Returns a blank new view object with stamped Id.
	 * @param color background color code
	 */
	public View newView (int color) {
		View view = stamp( new View( ctx ) );
		view.setBackgroundColor( color );
		return view;
	}

	/**
	 * Returns a blank new view object with stamped Id.
	 * @param background background image
	 */
	public View newView (Drawable background) {
		View view = stamp( new View( ctx ) );
		view.setBackgroundDrawable( background );
		return view;
	}

	/**
	 * Create a Button label.
	 */
	public Button newButton (String text) {
		return stamp( new Button(ctx), text );
	}

	/**
	 * Create a Button label.
	 */
	public Button newButton (String text, View.OnClickListener listener) {
		return stamp( new Button(ctx), text, listener );
	}

	/**
	 * Create a Button label.
	 */
	public Button newButton (int text) {
		return stampt( new Button( ctx ), text );
	}

	/**
	 * Create a Button label.
	 */
	public Button newButton (int sid, View.OnClickListener listener) {
		return stampt( new Button( ctx ), sid, listener );
	}

	/**
	 * Create a Button label.
	 */
	public ImageButton newImageButton (String image) {
		return newImageButton( Utils.isEmpty( image ) ? null : Resource.getImage( image ) );
	}

	/**
	 * Create a Button label.
	 */
	public ImageButton newImageButton (Drawable image) {
		ImageButton button =  stamp( new ImageButton(ctx) );
		button.setImageDrawable( image  );
		return button;
	}

	/**
	 * Create a Button label.
	 */
	public ImageButton newImageButton (int rid) {
		return newImageButton( rid, null );
	}

	/**
	 * Create a Button label.
	 */
	public ImageButton newImageButton (int rid, View.OnClickListener listener) {
		ImageButton button =  newImageButton( (Drawable)null, listener );
        button.setImageResource( rid );
		return button;
	}

	/**
	 * Create a Button label.
	 */
	public ImageButton newImageButton (String image, View.OnClickListener listener) {
		ImageButton button = newImageButton( image );

		if (listener != null)
			button.setOnClickListener( listener );

		return button;
	}

	/**
	 * Create a Button label.
	 */
	public ImageButton newImageButton (Drawable image, View.OnClickListener listener) {
		ImageButton button = newImageButton( image );

		if (listener != null)
			button.setOnClickListener( listener );

		return button;
	}

	/**
	 * Create a TextView label.
	 */
	public TextView newText () {
		return newText( null );
	}

	/**
	 * Create a new TextView instance.
	 */
	public TextView newText (String text) {
		return stamp( new TextView( ctx ), text );
	}

	/**
	 * Create a new TextView instance.
	 */
	public TextView newText (int sid) {
		TextView v = stamp( new TextView( ctx ) );
		v.setText( sid );
		return v;
	}

	/**
	 * Create a single line TextView label.
	 */
	public TextView newTextLine () {
		return newTextLine( null );
	}

	/**
	 * Create a new single line TextView instance.
	 */
	public TextView newTextLine (String text) {
		TextView tv = stamp( new TextView(ctx), text );
		tv.setSingleLine();
		return tv;
	}

	/**
	 * Create a new single line TextView instance.
	 */
	public TextView newTextLine (int sid) {
		TextView v = stamp( new TextView( ctx ) );
		v.setText( sid );
		v.setSingleLine();
		return v;
	}

	/**
	 * Create a new TextView instance.
	 */
	public TextSwitcher newTextSwitcher () {
		return stamp( new TextSwitcher( ctx ) );
	}

	/**
	 * Create a new TextView to be used as a field
	 * Label with default padding.
	 */
	public TextView newLabel (String text) {
        TextView label = stamp( new TextView(ctx) );
        label.setText( text );
        return label;
	}

	/**
	 * Create a new TextView to be used as a field
	 * Label with default padding.
	 */
	public TextView newLabel (int sid) {
        TextView label = stamp( new TextView(ctx) );
        label.setText( sid );
        return label;
	}

	/**
	 * Create a labelled view wrapped inside a linear layout.
	 *
	 * NOTE: This method will create extra layers of linear layouts.
	 * It might be better to use RelativeLayout or another layout in some cases.
	 *
	 * Also the margin to the left of the <code>view</code> is set to 3dips by default.
	 */
	public <V extends View> TableRow newLabeledRow (String label, V view) {
        TableRow row = new TableRow( ctx );
		row.setBaselineAligned( true );

		row.addView( newLabel(label) );
		row.addView( view, setMargin( rowParam(1f), px(4), 0, 0, 0) );

		return row;
	}

	/**
	 * Create a labelled view wrapped inside a linear layout.
	 *
	 * NOTE: This method will create extra layers of linear layouts.
	 * It might be better to use RelativeLayout or another layout in some cases.
	 */
	public <V extends View> TableRow newLabeledRow (int sid, V view) {
		return newLabeledRow( Resource.getString(sid), view );
	}

	/**
	 * Create a new TextView instance.
	 */
	public ProgressBar newProgress () {
		return stamp( new ProgressBar( ctx ) );
	}

	/**
	 * Create a new EditText instance.
	 */
	public EditText newEdit () {
		return stamp( new EditText( ctx ) );
	}

	/**
	 * Create a new EditText instance.
	 */
	public EditText newEdit (String text) {
		return stamp( new EditText( ctx ), text );
	}

	/**
	 * Create a new EditText instance.
	 */
	public EditText newEdit (int tid) {
		return stampt( new EditText( ctx ), tid );
	}

	/**
	 * Create a new single line EditText instance.
	 */
	public EditText newEditLine () {
		EditText t = newEdit();
		t.setSingleLine();
		return t;
	}

	/**
	 * Create a new CheckBox instance.
	 */
	public CheckBox newCheck (int sid) {
		return stampt( new CheckBox( ctx ), sid );
	}

	/**
	 * Create a new RadioButton instance.
	 */
	public RadioButton newRadio (int sid) {
		return stampt( new RadioButton( ctx ), sid );
	}

	/**
	 * Create a new ToggleButton instance.
	 */
	public ToggleButton newToggle (int sid) {
		return stampt( new ToggleButton( ctx ), sid );
	}

	/**
	 * Create a new CheckBox instance.
	 */
	public CheckBox newCheck (String text) {
		return stamp( new CheckBox( ctx ), text );
	}

	/**
	 * Create a new RadioButton instance.
	 */
	public RadioButton newRadio (String text) {
		return stamp( new RadioButton( ctx ), text );
	}

	/**
	 * Create a new ToggleButton instance.
	 */
	public ToggleButton newToggle (String text) {
		return stamp( new ToggleButton( ctx ), text );
	}

	/**
	 * Create a new ImageView instance with nothing set.
	 */
	public ImageView newImage () {
		return stamp( new ImageView( ctx ) );
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (int iid) {
		ImageView iv = stamp( new ImageView( ctx ) );
        iv.setImageResource( iid );
		return iv;
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (String img) {
		return newImage( Utils.isEmpty( img ) ? null : Resource.getImage( img ) );
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (Drawable img) {
		return newImage(  img, null );
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (Bitmap bmp) {
		return newImage( bmp, null );
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (int iid, View.OnClickListener listener) {
		ImageView iv = stamp( new ImageView( ctx ) );
        iv.setImageResource( iid );
		if (listener != null)
			iv.setOnClickListener( listener );
		return iv;
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (String img, View.OnClickListener listener) {
		return newImage( Utils.isEmpty( img ) ? null : Resource.getImage( img ), listener );
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (Drawable img, View.OnClickListener listener) {
		ImageView iv = stamp( new ImageView( ctx ) );

        iv.setImageDrawable( img );

		if (listener != null)
			iv.setOnClickListener( listener );

		return iv;
	}

	/**
	 * Create a new ImageView instance.
	 */
	public ImageView newImage (Bitmap bmp, View.OnClickListener listener) {
		ImageView iv = stamp( new ImageView( ctx ) );

        iv.setImageBitmap( bmp );

		if (listener != null)
			iv.setOnClickListener( listener );

		return iv;
	}

	/**
	 * Create a new ListView with id set to {@link android.R.id#list}.
	 */
	public ListView newList () {
		return stamp( new ListView( ctx ), android.R.id.list );
	}

	/**
	 * Create a new Spinner instance.
	 */
	public Spinner newSpinner () {
		return stamp( new Spinner( ctx ) );
	}

	/**
	 * Create a new Spinner instance with specified array.
	 */
	public Spinner newSpinner (Object[] array) {
        
        Spinner spinner = newSpinner();

        ArrayAdapter adapter = new ArrayAdapter<Object>( ctx, R.layout.simple_spinner_item, array );
        spinner.setAdapter( adapter );
        adapter.setDropDownViewResource( R.layout.simple_spinner_dropdown_item );

		return spinner;
	}

	/**
	 * Create a new ScrollView instance.
	 */
	public ScrollView newScroll (View panel) {
		ScrollView sp = stamp( new ScrollView( ctx ) );
		sp.addView( panel );
		return sp;
	}

	/**
	 * Create a new FrameLayout instance with all specified views added as children.
	 */
	public FrameLayout newFrame (View ... views) {
		FrameLayout frame = stamp( new FrameLayout( ctx ) );
		for (View v : views) {
			frame.addView( v );
		}
		return frame;
	}

	/**
	 * Create linear layout params
	 */
	public static LinearLayout.LayoutParams linearParam (float wt) {
		return linearParam( WRAP_CONTENT, WRAP_CONTENT, wt );
	}

	/**
	 * Create linear layout params
	 */
	public static LinearLayout.LayoutParams linearParam (int w, int h) {
		return new LinearLayout.LayoutParams( w, h );
	}

	/**
	 * Create linear layout params
	 */
	public static LinearLayout.LayoutParams linearParam (int w, int h, float wt) {
		return new LinearLayout.LayoutParams( w, h, wt );
	}

	/**
	 * Create table layout params
	 */
	public static TableLayout.LayoutParams tableParam (float wt) {
		return new TableLayout.LayoutParams( WRAP_CONTENT, WRAP_CONTENT, wt );
	}

	/**
	 * Create table row params
	 */
	public static TableRow.LayoutParams rowParam (float wt) {
		return new TableRow.LayoutParams( WRAP_CONTENT, WRAP_CONTENT, wt );
	}

	/**
	 * Create table row params
	 */
	public static TableRow.LayoutParams rowParam (int column) {
		return new TableRow.LayoutParams( column );
	}

	/**
	 * Create frame layout params
	 */
	public static FrameLayout.LayoutParams frameParam (int gravity) {
		return frameParam( FILL_PARENT, FILL_PARENT, gravity );
	}

	/**
	 * Create frame layout params
	 */
	public static FrameLayout.LayoutParams frameParam (int w, int h, int gravity) {
		return new FrameLayout.LayoutParams( w, h, gravity );
	}

	/**
	 * Create Relative param with additional rules specified by param.
	 * @param param Must be pair of [rules, view].
	 * @return RelativeLayout.LayoutParams
	 */
	public static RelativeLayout.LayoutParams relativeParam (Object ... param) {
		return relativeParam( WRAP_CONTENT, WRAP_CONTENT, param );
	}
	/**
	 * Create Relative param with additional rules specified by param.
	 * @param param Must be pair of [rules, view].
	 * @return RelativeLayout.LayoutParams
	 */
	public static RelativeLayout.LayoutParams relativeParam (int w, int h, Object ... param) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( w, h );

		if (param != null)
			for (int i=0; i<param.length;) {
				lp.addRule( (Integer)param[i++], getId(param[i++]) );
			}

		return lp;
	}

	/**
	 * Create DatePicker widget with the specified <code>date</code>.
	 */
	public DatePicker newDatePicker (Date date) {
		DatePicker dp = new DatePicker( ctx );
		setDate( dp, date );
		return dp;
	}

	/**
	 * Create DatePicker widget with the specified <code>date</code>.
	 */
	public DatePickerDialog newDatePickerDialog (Date date, DatePickerDialog.OnDateSetListener listener) {
		final Calendar c = Calendar.getInstance();
		c.setTime( date );
		return new DatePickerDialog( ctx, listener, c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) );
	}

	/**
	 * Parse date from the date picker object.
	 */
	public static Date getDate (DatePicker dp) {
		final Calendar c = Calendar.getInstance();

		c.set( Calendar.YEAR, dp.getYear() );
		c.set( Calendar.MONTH, dp.getMonth() );
		c.set( Calendar.DAY_OF_MONTH, dp.getDayOfMonth() );

		return c.getTime();
	}

	/**
	 * Parse date and time both from the date picker and time picker objects.
	 */
	public static Date getDate (DatePicker dp, TimePicker tp) {
		final Calendar c = Calendar.getInstance();

		c.set( Calendar.YEAR, dp.getYear() );
		c.set( Calendar.MONTH, dp.getMonth() );
		c.set( Calendar.DAY_OF_MONTH, dp.getDayOfMonth() );
		c.set( Calendar.HOUR_OF_DAY, tp.getCurrentHour() );
		c.set( Calendar.MINUTE, tp.getCurrentMinute() );

		return c.getTime();
	}

	/**
	 * Surprisingly (stupidly in honest words) DatePicker does not take {@linkplain java.util.Date} as a parameter.
	 */
	public static void setDate (DatePicker dp, Date date) {
		final Calendar c = Calendar.getInstance();
		c.setTime( date );
		dp.updateDate( c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) );
	}

	/**
	 * Surprisingly (stupidly in honest words) DatePickerDialog does not take {@linkplain java.util.Date} as a parameter.
	 */
	public static void setDate (DatePickerDialog dp, Date date) {
		final Calendar c = Calendar.getInstance();
		c.setTime( date );
		dp.updateDate( c.get( Calendar.YEAR ), c.get( Calendar.MONTH ), c.get( Calendar.DAY_OF_MONTH ) );
	}

	/**
	 * Surprisingly (stupidly in honest words) TimePicker does not take {@linkplain java.util.Date} as a parameter.
	 */
	public static void setTime (TimePicker tp, Date time) {
		final Calendar c = Calendar.getInstance();
		c.setTime( time );
		tp.setCurrentHour( c.get( Calendar.HOUR_OF_DAY ) );
		tp.setCurrentMinute( c.get( Calendar.MINUTE ) );
	}

	/**
	 * Surprisingly (stupidly in honest words) TimePickerDialog does not take {@linkplain java.util.Date} as a parameter.
	 */
	public static void setTime (TimePickerDialog tp, Date time) {
		final Calendar c = Calendar.getInstance();
		c.setTime( time );
		tp.updateTime( c.get( Calendar.HOUR_OF_DAY ), c.get( Calendar.MINUTE ) );
	}

    /**
     * Internal metrics object.
     */
    private DisplayMetrics metrics () {
        if (metrics == null)
            metrics = ctx.getResources().getDisplayMetrics();
        return metrics;
    }

    /**
     * Convert pixels to dips.
	 * Note: This result is not reversible by calling {@link #px(float)}.
     */
    public float dip (int pixels) {
        return (float)pixels / metrics().density + 0.5f;
    }

    /**
     * Convert dips to pixels.
     */
    public int px (float dips) {
        return (int) (dips * metrics().density * scale + 0.5f);
    }

	/**
	 * Convert sp to pixels.
	 */
	public int spx (float sp) {
		return (int)(sp * metrics().scaledDensity * scale + 0.5f);
	}

	/**
	 * Convert points to pixels.
	 */
	public int ppx (float pt) {
		return (int)(pt * metrics().xdpi * (1.0f/72) + 0.5f);
	}

	/**
	 * Convert millimeters to pixels.
	 */
	public int mpx (float mm) {
		return (int)(mm * metrics().xdpi * (1.0f/25.4f) + 0.5f);
	}

	/**
	 * Convert inches to pixels.
	 */
	public int ipx (float inches) {
		return (int)(inches * metrics().xdpi + 0.5f);
	}

	/**
	 * Calculate text width.
	 */
	public int getTextWidth (Paint p, String text) {
		p.getTextBounds( text, 0, text.length(), rect );
		return (int)(rect.width() * metrics().density + 0.5f);
	}

	/**
	 * Calculate text height.
	 */
	public int getTextHeight (Paint p, String text) {
		p.getTextBounds( text, 0, text.length(), rect );
		return (int)(rect.height() * metrics().density + 0.5f);
	}

	/**
	 * Convenience method to set size using a global {@link #TEXT_UNIT}
	 * @param text TextView to set the size of
	 * @param size size in units defined by {@link #initTextUnit(int)}
	 */
	public void setTextSize (TextView text, int size) {
		text.setTextSize( TEXT_UNIT, size );
	}

	/**
	 * Set the text size on the Paint in sp.
	 * @param paint Paint to set the text size of.
	 * @param size size in units defined by {@link #initTextUnit(int)}
	 */
	public void setTextSize (Paint paint, int size) {
		paint.setTextSize( spx( size ) );
	}

    /**
     * Convenience method to set padding in dips instead of pixels
     * 
     * @param view View class to set padding of
     * @param left left padding in dips
     * @param top top padding in dips
     * @param right right padding in dips
     * @param bottom bottom padding in dips
     */
    public void setPadding (View view, int left, int top, int right, int bottom) {
        view.setPadding( px(left), px(top), px(right), px(bottom) );
    }

    /**
     * Convenience method to set padding in dips instead of pixels
     *
     * @param view View class to set padding of
	 * @param padding right, top, left, bottom padding in dips
     */
    public void setPadding (View view, int padding) {
		padding = px(padding);
        view.setPadding( padding, padding, padding, padding );
    }

    /**
     * Convinience method to set margin in dips instead of pixels.
     * 
     * @param layout layout params class to set margin of
     * @param margin right, top, left, bottom margins in dips
     */
    public <T extends ViewGroup.MarginLayoutParams> T setMargin (T layout, int margin) {
		margin = px(margin);
        layout.setMargins( margin, margin, margin, margin );
        return layout;
    }

    /**
     * Convinience method to set margin in dips instead of pixels.
     *
     * @param layout layout params class to set margin of
     * @param left left margin in dips
     * @param top top margin in dips
     * @param right right margin in dips
     * @param bottom bottom margin in dips
     */
    public <T extends ViewGroup.MarginLayoutParams> T setMargin (T layout, int left, int top, int right, int bottom) {
        layout.setMargins( px(left), px(top), px(right), px(bottom) );
        return layout;
    }
}
