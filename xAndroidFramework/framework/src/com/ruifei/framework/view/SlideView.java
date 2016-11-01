package com.ruifei.framework.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.ruifei.framework.R;


public class SlideView extends RelativeLayout {

    private Context mContext;
    private View mainView;
    private RelativeLayout contentView;
    // private View bgShadeView;
    private int screenWidth;
    private Drawable mBackground;

    public SlideView(Context context) {
        super(context);
        init(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        mScroller = new Scroller(mContext);
        setBackgroundResource(0);
        if (mContext instanceof Activity) {
            screenWidth = ((Activity) mContext).getWindowManager()
                    .getDefaultDisplay().getWidth();
        }
        View view = inflate(mContext, R.layout.slide_view, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        addViewInLayout(view, -1, lp);
        mainView = view.findViewById(R.id.main_content);
        contentView = (RelativeLayout) view.findViewById(R.id.main_layout);
    }

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private static final int VELOCITY = 50;
    private boolean mIsBeingDragged = false;
    private boolean canSlide = true;

    private boolean forbidSlide = false;

    public void setForbidSlide(boolean forbidSlide) {
        this.forbidSlide = forbidSlide;
    }

    private boolean canSlideRight = false;

    public void setSlideRight(boolean canSlideRight) {
        this.canSlideRight = canSlideRight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (forbidSlide)
            return false;

        if (canSlide) {
            final int action = ev.getAction();
            final float x = ev.getX();
            final float y = ev.getY();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    mIsBeingDragged = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float dx = x - mLastMotionX;
                    final float xDiff = Math.abs(dx);
                    final float yDiff = Math.abs(y - mLastMotionY);
                    if (canSlideRight && dx < 0 && xDiff > mTouchSlop)
                        return false;

                    if (xDiff > mTouchSlop) {
                        if (xDiff > yDiff) {
                            mIsBeingDragged = true;
                            isFirst = true;
                        }
                    }
                    // if(xDiff <= mTouchSlop){
                    // ev.setLocation(mLastMotionX, mLastMotionY);
                    // }
                    break;
            }
            if (mIsBeingDragged)
                return mIsBeingDragged;
            else
                return super.onInterceptTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        if (isFirst) {
            isFirst = false;
            if (mSlideListener != null)
                mSlideListener.slideStart();
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                mLastMotionY = y;

                break;
            case MotionEvent.ACTION_MOVE:

                if (mainView == null)
                    break;

            {
                final float deltaX = mLastMotionX - x;
                mLastMotionX = x;
                float oldScrollX = mainView.getScrollX();
                float scrollX = oldScrollX + deltaX;
                if (scrollX > 0) {
                    scrollX = 0;
                }
                if (deltaX < 0 && oldScrollX < 0) { // left view
                    final float leftBound = 0;
                    final float rightBound = -screenWidth;
                    if (scrollX > leftBound) {
                        scrollX = leftBound;
                    } else if (scrollX < rightBound) {
                        scrollX = rightBound;
                    }
                }
                mainView.scrollTo((int) scrollX, mainView.getScrollY());
                //changeAlpha();

            }
            break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mainView == null)
                    break;

                if (mSlideListener != null)
                    mSlideListener.slideEnd();

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(100);
                float xVelocity = velocityTracker.getXVelocity();
                int oldScrollX = mainView.getScrollX();
                int dx = 0;

                if (oldScrollX <= 0) {// left view
                    if (xVelocity > VELOCITY) {
                        dx = -screenWidth - oldScrollX;
                    } else if (xVelocity < -VELOCITY) {
                        dx = -oldScrollX;
                    } else if (oldScrollX < -screenWidth / 2) {
                        dx = -screenWidth - oldScrollX;
                    } else if (oldScrollX >= -screenWidth / 2) {
                        dx = -oldScrollX;
                    }

                }
                if (oldScrollX > 0) {
                    dx = -oldScrollX;
                }
                smoothScrollTo(dx);
                break;
        }
        return true;
    }

    private void changeAlpha() {

        if (mainView == null)
            return;

        mBackground = new ColorDrawable(Color.BLACK);

        double deltaPha = (screenWidth + mainView.getScrollX() + 0.0)
                / screenWidth;
        double alpha = 225 * deltaPha;
        mBackground.setAlpha((int) alpha);
        setBackgroundDrawable(mBackground);
    }

    private void smoothScrollTo(int dx) {

        if (mainView == null)
            return;

        int duration = 500;
        int oldScrollX = mainView.getScrollX();
        mScroller.startScroll(oldScrollX, mainView.getScrollY(), dx,
                mainView.getScrollY(), duration);
        invalidate();
    }

    @Override
    public void computeScroll() {

        if (mainView == null)
            return;

        if (!mScroller.isFinished()) {
            if (mScroller.computeScrollOffset()) {
                int oldX = mainView.getScrollX();
                int oldY = mainView.getScrollY();
                int x = mScroller.getCurrX();
                int y = mScroller.getCurrY();
                if (oldX != x || oldY != y) {
                    mainView.scrollTo(x, y);
                    //changeAlpha();
                    if (mainView.getScrollX() < -screenWidth + 10) {
                        finish();
                    }
                    if (mainView.getScrollX() == 0 && mSlideListener != null) {//该页面没有finish
                        mSlideListener.keepFragment();
                    }
                }
                postInvalidate();
            }
        }
    }

    public void show() {
        if (mainView != null) {
            mainView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isFinish = false;

    private void finish() {

        if (isFinish)
            return;

        isFinish = true;

        if (mContext == null)
            return;

        if (mOnFinishListener != null) {
            boolean handle = mOnFinishListener.onFinish();
            if (handle) {
                return;
            }
        }

        if (mainView != null)
            mainView.setVisibility(View.GONE);

        /*if (mContext instanceof Activity) {
            if (!((Activity) mContext).isFinishing()) {
                ((Activity) mContext).finish();
            }
        }*/

    }

    private boolean isFirst = true;
    private SlideListener mSlideListener;
    private IOnFinishListener mOnFinishListener;

    public void setOnSlideListener(SlideListener l) {
        mSlideListener = l;
    }

    public void setSlide(boolean canSlide) {
        this.canSlide = canSlide;
    }

    public void setOnFinishListener(IOnFinishListener l) {
        mOnFinishListener = l;
    }

    public interface SlideListener {
        void slideStart();
        void slideEnd();
        void keepFragment();//还在当前页面
    }

    public interface IOnFinishListener {
        /**
         * onFinish:This method will be call when the view is out of screen
         */
        public boolean onFinish();
    }

    @Override
    public void addView(View child, int width, int height) {

        addView(child, width, height);

    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (contentView != null) {
            contentView.addView(child, index, params);
        }
    }

    @Override
    public void addView(View child, int index) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException(
                        "generateDefaultLayoutParams() cannot return null");
            }
        }
        addView(child, index, params);

    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {

        addView(child, -1, params);

    }

    @Override
    public void addView(View child) {

        addView(child, -1);

    }

}
