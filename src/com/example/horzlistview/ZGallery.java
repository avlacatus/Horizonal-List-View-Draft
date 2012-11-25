package com.example.horzlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class ZGallery extends Gallery {

    // Constants
    protected static final float DRAG_THRESHOLD = 10; // If dragging for more than this amount of pixels, means it's a scroll

    // Properties
    protected boolean isPressed;
    protected float startPressX;
    protected float startPressY;
    protected boolean isDragging;

    // ================================================================================================================
    // CONSTRUCTOR ----------------------------------------------------------------------------------------------------

    public ZGallery(Context context) {
        this(context, null);
    }

    public ZGallery(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.galleryStyle);
    }

	public ZGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // ================================================================================================================
    // EVENT INTERFACE ------------------------------------------------------------------------------------------------

    @Override
    public boolean onInterceptTouchEvent(MotionEvent __e) {
        // Intercepts all touch screen motion events.  This allows you to watch events as they are dispatched to your children, and take ownership of the current gesture at any point.
        // Return true to steal motion events from the children and have them dispatched to this ViewGroup through onTouchEvent().
        // The current target will receive an ACTION_CANCEL event, and no further messages will be delivered here.

        //return super.onInterceptTouchEvent(__e); // super always returns false

        // If this function returns TRUE, NO children get dragging events. This only happens
        // the first interception (mouse down); if true is returned, nothing is intercepted anymore, and
        // events are passed to onTouchEvent directly.
        // If FALSE is returned, this may be called again, but only if there's a children receiving the
        // events instead of this.
        // In sum, once onTouchEvent is called here, onInterceptTouchEvent is not called anymore.

        // Interprets drag data
        return evaluateTouchEvent(__e);

    }

	@Override
    public boolean onTouchEvent(MotionEvent __e) {
        // Interprets drag data
        evaluateTouchEvent(__e);

        // Properly lets superclass interpret touch events (for dragging, fling, etc)
        return super.onTouchEvent(__e);
    }

	protected boolean evaluateTouchEvent(MotionEvent __e) {
        // Interprets motion to see if the user is dragging the View
        // This will run in parallel with the children events
        float dragDeltaX;
        float dragDeltaY;

        switch (__e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Pressing...
                isPressed = true;
                startPressX = __e.getX();
                startPressY = __e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Moving...
                if (isPressed && !isDragging) {
                    dragDeltaX = __e.getX() - startPressX;
                    dragDeltaY = __e.getY() - startPressY;

                    if (Math.abs(dragDeltaX) > DRAG_THRESHOLD || Math.abs(dragDeltaY) > DRAG_THRESHOLD) {
                        // Moved too far, means it's dragging!

                        // Inject click from correct position so superclass code knows where to drag from
                        MotionEvent me = MotionEvent.obtain(__e);
                        me.setAction(MotionEvent.ACTION_DOWN);
                        me.setLocation(__e.getX() - dragDeltaX, __e.getY() - dragDeltaY);
                        super.onTouchEvent(me);

                        isDragging = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // Releasing...
                if (isPressed) {
                    isPressed = false;
                    // Let go while pressed
                    if (isDragging) {
                        // Was dragging, so just go back
                        isDragging = false;
                    } else {
                        // Was not dragging, this will trigger a click
                    }
                }
                break;
        }


        // If not dragging, event should be passed on
        // If dragging, the event should be intercepted and interpreted by this gallery's onTouchEvent instead
        return isDragging;
    }
}