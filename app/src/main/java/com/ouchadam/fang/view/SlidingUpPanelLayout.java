package com.ouchadam.fang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class SlidingUpPanelLayout extends ViewGroup {
    private static final String TAG = "SlidingPaneLayout";

    /**
     * Default peeking out panel height
     */
    private static final int DEFAULT_PANEL_HEIGHT = 68; // dp;

    /**
     * Default height of the shadow above the peeking out panel
     */
    private static final int DEFAULT_SHADOW_HEIGHT = 4; // dp;

    /**
     * If no fade color is given by default it will fade to 80% gray.
     */
    private static final int DEFAULT_FADE_COLOR = 0x99000000;

    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * The fade color used for the panel covered by the slider. 0 = no fading.
     */
    private int mCoveredFadeColor = DEFAULT_FADE_COLOR;

    /**
     * The paint used to dim the main layout when sliding
     */
    private final Paint mCoveredFadePaint = new Paint();

    /**
     * Drawable used to draw the shadow between panes.
     */
    private Drawable mShadowDrawable;

    /**
     * The size of the overhang in pixels.
     */
    private int mPanelHeight;

    /**
     * The size of the shadow in pixels.
     */
    private final int mShadowHeight;

    /**
     * True if a panel can slide with the current measurements
     */
    private boolean mCanSlide;

    /**
     * The child view that can slide, if any.
     */
    private View mSlideableView;

    /**
     * How far the panel is offset from its expanded position.
     * range [0, 1] where 0 = expanded, 1 = collapsed.
     */
    private float mSlideOffset;

    /**
     * How far in pixels the slideable panel may move.
     */
    private int mSlideRange;

    /**
     * A panel view is locked into internal scrolling or another condition that
     * is preventing a drag.
     */
    private float mInitialMotionY;

    private PanelSlideListener mPanelSlideListener;

    private final ViewDragHelper mDragHelper;

    /**
     * Stores whether or not the pane was expanded the last time it was slideable.
     * If expand/collapse operations are invoked this state is modified. Used by
     * instance state save/restore.
     */
    private boolean mPreservedExpandedState;
    private boolean mFirstLayout = true;

    private final Rect mTmpRect = new Rect();
    private boolean dragging;
    private int defaultHeight;

    /**
     * Listener for monitoring events about sliding panes.
     */
    public interface PanelSlideListener {
        /**
         * Called when a sliding pane's position changes.
         *
         * @param panel       The child view that was moved
         * @param slideOffset The new offset of this sliding pane within its range, from 0-1
         */
        public void onPanelSlide(View panel, float slideOffset);

        /**
         * Called when a sliding pane becomes slid completely collapsed. The pane may or may not
         * be interactive at this point depending on if it's shown or hidden
         *
         * @param panel The child view that was slid to an collapsed position, revealing other panes
         */
        public void onPanelCollapsed(View panel);

        /**
         * Called when a sliding pane becomes slid completely expanded. The pane is now guaranteed
         * to be interactive. It may now obscure other views in the layout.
         *
         * @param panel The child view that was slid to a expanded position
         */
        public void onPanelExpanded(View panel);
    }

    /**
     * No-op stubs for {@link PanelSlideListener}. If you only want to implement a subset
     * of the listener methods you can extend this instead of implement the full interface.
     */
    public static class SimplePanelSlideListener implements PanelSlideListener {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
        }

        @Override
        public void onPanelCollapsed(View panel) {
        }

        @Override
        public void onPanelExpanded(View panel) {
        }
    }

    public SlidingUpPanelLayout(Context context) {
        this(context, null);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final float density = context.getResources().getDisplayMetrics().density;
        mPanelHeight = (int) (DEFAULT_PANEL_HEIGHT * density + 0.5f);
        mShadowHeight = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);

        defaultHeight = mPanelHeight;
        setWillNotDraw(false);

        mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        mDragHelper.setMinVelocity(MIN_FLING_VELOCITY * density);

        mCanSlide = true;

        setCoveredFadeColor(DEFAULT_FADE_COLOR);
    }

    public void hidePanel() {
        setPanelHeight(0);
    }

    public void showPanel() {
        setPanelHeight(defaultHeight);
    }

    /**
     * Set the color used to fade the pane covered by the sliding pane out when the pane
     * will become fully covered in the expanded state.
     *
     * @param color An ARGB-packed color value
     */
    public void setCoveredFadeColor(int color) {
        mCoveredFadeColor = color;
        invalidate();
    }

    /**
     * @return The ARGB-packed color value used to fade the fixed pane
     */
    public int getCoveredFadeColor() {
        return mCoveredFadeColor;
    }

    /**
     * Set the collapsed panel height in pixels
     *
     * @param val A height in pixels
     */
    public void setPanelHeight(int val) {
        mPanelHeight = val;
    }

    /**
     * @return The current collapsed panel height
     */
    public int getPanelHeight() {
        return mPanelHeight;
    }

    public void setPanelSlideListener(PanelSlideListener listener) {
        mPanelSlideListener = listener;
    }

    /**
     * Set the shadow for the sliding panel
     */
    public void setShadowDrawable(Drawable drawable) {
        mShadowDrawable = drawable;
    }

    private void dispatchOnPanelSlide(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
        }
    }

    private void dispatchOnPanelExpanded(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelExpanded(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    private void dispatchOnPanelCollapsed(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelCollapsed(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    void updateObscuredViewVisibility() {
        if (getChildCount() == 0) {
            return;
        }
        final int leftBound = getPaddingLeft();
        final int rightBound = getWidth() - getPaddingRight();
        final int topBound = getPaddingTop();
        final int bottomBound = getHeight() - getPaddingBottom();
        final int left;
        final int right;
        final int top;
        final int bottom;
        if (mSlideableView != null && hasOpaqueBackground(mSlideableView)) {
            left = mSlideableView.getLeft();
            right = mSlideableView.getRight();
            top = mSlideableView.getTop();
            bottom = mSlideableView.getBottom();
        } else {
            left = right = top = bottom = 0;
        }
        View child = getChildAt(0);
        final int clampedChildLeft = Math.max(leftBound, child.getLeft());
        final int clampedChildTop = Math.max(topBound, child.getTop());
        final int clampedChildRight = Math.min(rightBound, child.getRight());
        final int clampedChildBottom = Math.min(bottomBound, child.getBottom());
        final int vis;
        if (clampedChildLeft >= left && clampedChildTop >= top &&
                clampedChildRight <= right && clampedChildBottom <= bottom) {
            vis = INVISIBLE;
        } else {
            vis = VISIBLE;
        }
        child.setVisibility(vis);
    }

    void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == INVISIBLE) {
                child.setVisibility(VISIBLE);
            }
        }
    }

    private static boolean hasOpaqueBackground(View v) {
        final Drawable bg = v.getBackground();
        if (bg != null) {
            return bg.getOpacity() == PixelFormat.OPAQUE;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
        } else if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Height must have an exact value or MATCH_PARENT");
        }

        int layoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
        int panelHeight = mPanelHeight;

        final int childCount = getChildCount();

        if (childCount > 2) {
            Log.e(TAG, "onMeasure: More than two child views are not supported.");
        } else if (getChildAt(1).getVisibility() == GONE) {
            panelHeight = 0;
        }

        // We'll find the current one below.
        mSlideableView = null;
        mCanSlide = true;

        // First pass. Measure based on child LayoutParams width/height.
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int height = layoutHeight;
            if (child.getVisibility() == GONE) {
                lp.dimWhenOffset = false;
                continue;
            }

            if (i == 1) {
                lp.slideable = true;
                lp.dimWhenOffset = true;
                mSlideableView = child;
                mCanSlide = true;
            } else {
                height -= panelHeight;
            }

            int childWidthSpec;
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            }

            int childHeightSpec;
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
            } else if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            }

            child.measure(childWidthSpec, childHeightSpec);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();

        final int childCount = getChildCount();
        int yStart = paddingTop;
        int nextYStart = yStart;

        if (mFirstLayout) {
            mSlideOffset = mCanSlide && mPreservedExpandedState ? 0.f : 1.f;
        }

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childHeight = child.getMeasuredHeight();

            if (lp.slideable) {
                mSlideRange = childHeight - mPanelHeight;
                yStart += (int) (mSlideRange * mSlideOffset);
            } else {
                yStart = nextYStart;
            }

            final int childTop = yStart;
            final int childBottom = childTop + childHeight;
            final int childLeft = paddingLeft;
            final int childRight = childLeft + child.getMeasuredWidth();
            child.layout(childLeft, childTop, childRight, childBottom);

            nextYStart += child.getHeight();
        }

        if (mFirstLayout) {
            updateObscuredViewVisibility();
        }

        mFirstLayout = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate sliding panes and their details
        if (h != oldh) {
            mFirstLayout = true;
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && !mCanSlide) {
            mPreservedExpandedState = child == mSlideableView;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        final float x = ev.getX();
        final float y = ev.getY();

        mDragHelper.shouldInterceptTouchEvent(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                dragging = false;
                mInitialMotionY = y;

                return false;
            }

            case MotionEvent.ACTION_UP: {
                dragging = false;
            }

            case MotionEvent.ACTION_MOVE: {
                return withinThreshold(x, y) && isExpanded();
            }
        }

        return false;
    }

    private boolean withinThreshold(float x, float y) {
        boolean result = false;
        if (dragging || y < (mInitialMotionY - 10) || y > (mInitialMotionY + 10)) {
            dragging = true;
            mInitialMotionY = y;
            result = true;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }

    private boolean expandPane(View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(0.f, initialVelocity)) {
            mPreservedExpandedState = true;
            return true;
        }
        return false;
    }

    private boolean collapsePane(View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(1.f, initialVelocity)) {
            mPreservedExpandedState = false;
            return true;
        }
        return false;
    }

    /**
     * Collapse the sliding pane if it is currently slideable. If first layout
     * has already completed this will animate.
     *
     * @return true if the pane was slideable and is now collapsed/in the process of collapsing
     */
    public boolean collapsePane() {
        return collapsePane(mSlideableView, 0);
    }

    /**
     * Expand the sliding pane if it is currently slideable. If first layout
     * has already completed this will animate.
     *
     * @return true if the pane was slideable and is now expanded/in the process of expading
     */
    public boolean expandPane() {
        if (!isPaneVisible()) {
            showPane();
        }
        return expandPane(mSlideableView, 0);
    }

    /**
     * Check if the layout is completely expanded.
     *
     * @return true if sliding panels are completely expanded
     */
    public boolean isExpanded() {
        return mCanSlide && mSlideOffset == 0;
    }

    /**
     * Check if the content in this layout cannot fully fit side by side and therefore
     * the content pane can be slid back and forth.
     *
     * @return true if content in this layout can be expanded
     */
    public boolean isSlideable() {
        return mCanSlide;
    }

    public boolean isPaneVisible() {
        if (getChildCount() < 2) {
            return false;
        }
        View slidingPane = getChildAt(1);
        return slidingPane.getVisibility() == View.VISIBLE;
    }

    public void showPane() {
        if (getChildCount() < 2) {
            return;
        }
        View slidingPane = getChildAt(1);
        slidingPane.setVisibility(View.VISIBLE);
        requestLayout();
    }

    public void hidePane() {
        if (mSlideableView == null) {
            return;
        }
        mSlideableView.setVisibility(View.GONE);
        requestLayout();
    }

    private void onPanelDragged(int newTop) {
        final int topBound = getPaddingTop();
        mSlideOffset = (float) (newTop - topBound) / mSlideRange;
        dispatchOnPanelSlide(mSlideableView);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        boolean result;
        final int save = canvas.save(Canvas.CLIP_SAVE_FLAG);

        boolean drawScrim = false;

        if (mCanSlide && !lp.slideable && mSlideableView != null) {
            // Clip against the slider; no sense drawing what will immediately be covered.
            canvas.getClipBounds(mTmpRect);
            mTmpRect.bottom = Math.min(mTmpRect.bottom, mSlideableView.getTop());
            canvas.clipRect(mTmpRect);
            if (mSlideOffset < 1) {
                drawScrim = true;
            }
        }

        result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(save);

        if (drawScrim) {
            final int baseAlpha = (mCoveredFadeColor & 0xff000000) >>> 24;
            final int imag = (int) (baseAlpha * (1 - mSlideOffset));
            final int color = imag << 24 | (mCoveredFadeColor & 0xffffff);
            mCoveredFadePaint.setColor(color);
            canvas.drawRect(mTmpRect, mCoveredFadePaint);
        }

        return result;
    }

    /**
     * Smoothly animate mDraggingPane to the target X position within its range.
     *
     * @param slideOffset position to animate to
     * @param velocity    initial velocity in case of fling, or 0.
     */
    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!mCanSlide) {
            // Nothing to do.
            return false;
        }

        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mSlideRange);

        if (mDragHelper.smoothSlideViewTo(mSlideableView, mSlideableView.getLeft(), y)) {
            setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            if (!mCanSlide) {
                mDragHelper.abort();
                return;
            }

            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);

        if (mSlideableView == null) {
            // No need to draw a shadow if we don't have one.
            return;
        }

        final int right = mSlideableView.getRight();
        final int top = mSlideableView.getTop() - mShadowHeight;
        final int bottom = mSlideableView.getTop();
        final int left = mSlideableView.getLeft();

        if (mShadowDrawable != null) {
            mShadowDrawable.setBounds(left, top, right, bottom);
            mShadowDrawable.draw(c);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams
                ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.isExpanded = isSlideable() ? isExpanded() : mPreservedExpandedState;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.isExpanded) {
            expandPane();
        } else {
            collapsePane();
        }
        mPreservedExpandedState = ss.isExpanded;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return ((LayoutParams) child.getLayoutParams()).slideable;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                if (mSlideOffset == 0) {
                    updateObscuredViewVisibility();
                    dispatchOnPanelExpanded(mSlideableView);
                    mPreservedExpandedState = true;
                } else {
                    dispatchOnPanelCollapsed(mSlideableView);
                    mPreservedExpandedState = false;
                }
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            // Make all child views visible in preparation for sliding things around
            setAllChildrenVisible();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            onPanelDragged(top);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            if (yvel > 0 || (yvel == 0 && mSlideOffset > 0.5f)) {
                top += mSlideRange;
            }
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mSlideRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = topBound + mSlideRange;

            final int newLeft = Math.min(Math.max(top, topBound), bottomBound);

            return newLeft;
        }

    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = new int[]{
                android.R.attr.layout_weight
        };

        /**
         * True if this pane is the slideable pane in the layout.
         */
        boolean slideable;

        /**
         * True if this view should be drawn dimmed
         * when it's been offset from its default position.
         */
        boolean dimWhenOffset;

        Paint dimPaint;

        public LayoutParams() {
            super(MATCH_PARENT, MATCH_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
            a.recycle();
        }

    }

    static class SavedState extends BaseSavedState {
        boolean isExpanded;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            isExpanded = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(isExpanded ? 1 : 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}