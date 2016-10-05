/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.astuetz;

import java.util.Locale;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.pagerslidingtabstrip.R;

public class PagerSlidingTabStrip extends HorizontalScrollView implements
		OnGlobalLayoutListener
{

	public boolean isSupportScroll() {
		return supportScroll;
	}

	public void setSupportScroll(boolean supportScroll) {
		this.supportScroll = supportScroll;
	}

	public interface IconTabProvider
	{
		public int getPageIconResId(int position);
	}

	public interface TipTabProvider
	{
		public View getTabWidget(int position);
	}
	
	public interface OnTabClickListener {
		public void onClick();
	}

	// @formatter:off
	private static final int[] ATTRS = new int[] { android.R.attr.textSize,
			android.R.attr.textColor };
	// @formatter:on

	private LinearLayout.LayoutParams defaultTabLayoutParams;
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	private final PageListener pageListener = new PageListener();
	public OnPageChangeListener delegatePageListener;
	private OnTabClickListener onTabClickListener;

	private LinearLayout tabsContainer;
	private ViewPager pager;

	private int tabCount;

	private int currentPosition = 0;
	private float currentPositionOffset = 0f;

	private Paint rectPaint;
	private Paint dividerPaint;

	private int indicatorColor = 0xFF666666;
	private int underlineColor = 0x1A000000;
	private int dividerColor = 0x1A000000;

	private boolean shouldExpand = false;//表示tab是否拉伸，拉伸容易导致界面显示异常
	private boolean textAllCaps = true;

	private int scrollOffset = 52;
	private int indicatorHeight = 8;
	private int underlineHeight = 2;
	private int dividerPadding = 12;
	private int tabPadding = 24;
	private int dividerWidth = 1;

	private int tabTextSize = 12;
	private int tabTextColor = 0xFF666666;
	private int tabDeactivateTextColor = 0xFFCCCCCC;

	private Typeface tabTypeface = null;
	private int tabTypefaceStyle = Typeface.NORMAL;

	private int lastScrollX = 0;

	private int tabBackgroundResId = R.drawable.background_tab;
	private int transparentColorId = R.color.transparent;

	private Locale locale;

	private boolean tabSwitch;
	private boolean supportScroll = true;

	public PagerSlidingTabStrip(Context context)
	{
		this(context, null);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public PagerSlidingTabStrip(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);

		setFillViewport(true);
		setWillNotDraw(false);

		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);

		DisplayMetrics dm = getResources().getDisplayMetrics();

		scrollOffset = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		tabPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_PX, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

		// get system attrs (android:textSize and android:textColor)

		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(1, tabTextColor);

		a.recycle();

		// get custom attrs

		a = context.obtainStyledAttributes(attrs,
				R.styleable.PagerSlidingTabStrip);

		indicatorColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsIndicatorColor,
				indicatorColor);
		underlineColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsUnderlineColor,
				underlineColor);
		dividerColor = a
				.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor,
						dividerColor);
		indicatorHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight,
				indicatorHeight);
		underlineHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight,
				underlineHeight);
		dividerPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsDividerPadding,
				dividerPadding);
		tabPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight,
				tabPadding);
		tabBackgroundResId = a.getResourceId(
				R.styleable.PagerSlidingTabStrip_pstsTabBackground,
				tabBackgroundResId);
		shouldExpand = a
				.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand,
						shouldExpand);
		scrollOffset = a
				.getDimensionPixelSize(
						R.styleable.PagerSlidingTabStrip_pstsScrollOffset,
						scrollOffset);
		textAllCaps = a.getBoolean(
				R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
		tabSwitch = a.getBoolean(
				R.styleable.PagerSlidingTabStrip_pstsTabSwitch, tabSwitch);
		tabTextColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsActivateTextColor,
				tabTextColor);
		tabDeactivateTextColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsDeactivateTextColor,
				tabDeactivateTextColor);

		a.recycle();

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		// dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);

		defaultTabLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null)
		{
			locale = getResources().getConfiguration().locale;
		}
	}

	public void setViewPager(ViewPager pager)
	{
		this.pager = pager;

		if (pager.getAdapter() == null)
		{
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}

		pager.setOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}

	public void setOnPageChangeListener(OnPageChangeListener listener)
	{
		this.delegatePageListener = listener;
	}
	
	public void setOnTabClickListener(OnTabClickListener listener) {
		this.onTabClickListener = listener;
	}

	public void notifyDataSetChanged()
	{

		tabsContainer.removeAllViews();

		tabCount = pager.getAdapter().getCount();

		for (int i = 0; i < tabCount; i++)
		{

			if (pager.getAdapter() instanceof IconTabProvider)
			{
				addIconTab(i,
						((IconTabProvider) pager.getAdapter())
								.getPageIconResId(i));
			}
			else if (pager.getAdapter() instanceof TipTabProvider)
			{
				addTab(i, ((TipTabProvider) pager.getAdapter()).getTabWidget(i));
			}
			else
			{
				addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
			}

		}

		updateTabStyles();

		getViewTreeObserver().addOnGlobalLayoutListener(this);

		// getViewTreeObserver().addOnGlobalLayoutListener(new
		// OnGlobalLayoutListener() {
		//
		// @SuppressWarnings("deprecation")
		// @SuppressLint("NewApi")
		// @Override
		// public void onGlobalLayout() {
		//
		// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
		// getViewTreeObserver().removeGlobalOnLayoutListener(this);
		// } else {
		// getViewTreeObserver().removeOnGlobalLayoutListener(this);
		// }
		//
		// currentPosition = pager.getCurrentItem();
		// scrollToChild(currentPosition, 0);
		// }
		// });
	}

	private void addTextTab(final int position, String title)
	{

		TextView tab = new TextView(getContext());
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();
		addTab(position, tab);
	}

	private void addIconTab(final int position, int resId)
	{

		ImageButton tab = new ImageButton(getContext());
		tab.setImageResource(resId);

		addTab(position, tab);
	}

	private void addTab(final int position, View tab)
	{
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (onTabClickListener != null) {
					onTabClickListener.onClick();
				}
				pager.setCurrentItem(position);
			}
		});

		tab.setPadding(tabPadding, 0, tabPadding, 0);

		tabsContainer
				.addView(tab, position, shouldExpand ? expandedTabLayoutParams
						: defaultTabLayoutParams);
	}

	private void updateTabViewColor() {
		for (int i = 0; i < tabCount; i++) {
			if (tabsContainer != null) {
				View v = tabsContainer.getChildAt(i);
				if (v != null) {
					v.setBackgroundResource(!tabSwitch ? tabBackgroundResId
							: transparentColorId);
					if (v instanceof TextView) {
						TextView tab = (TextView) v;
						tab.setTextColor(tabSwitch && i != 0 ? tabDeactivateTextColor
								: tabTextColor);
					} else if (v instanceof ImageButton) {
						ImageButton tab = (ImageButton) v;
						tab.setSelected(tabSwitch && i == 0 ? true : false);
					}
				}
			}
		}
	}
	private void updateTabStyles()
	{

		for (int i = 0; i < tabCount; i++)
		{

			View v = tabsContainer.getChildAt(i);

			v.setBackgroundResource(!tabSwitch ? tabBackgroundResId
					: transparentColorId);

			if (v instanceof TextView)
			{

				TextView tab = (TextView) v;
				tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
				tab.setTypeface(tabTypeface, tabTypefaceStyle);
				tab.setTextColor(tabSwitch && i != 0 ? tabDeactivateTextColor
						: tabTextColor);
				tab.setPadding(tabPadding, 0, tabPadding, 0);

				// setAllCaps() is only available from API 14, so the upper case
				// is made manually if we are on a
				// pre-ICS-build
				if (textAllCaps)
				{
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
					{
						tab.setAllCaps(true);
					}
					else
					{
						tab.setText(tab.getText().toString()
								.toUpperCase(locale));
					}
				}
			}
			else if (v instanceof ImageButton)
			{
				ImageButton tab = (ImageButton) v;
				tab.setSelected(tabSwitch && i == 0 ? true : false);
			}
		}
	}

	public void updateActivateTab(final int position)
	{

		for (int i = 0; i < tabCount; i++)
		{

			View v = tabsContainer.getChildAt(i);

			if (v instanceof TextView)
			{
				TextView tab = (TextView) v;
				tab.setTextColor(position == i ? tabTextColor
						: tabDeactivateTextColor);
				// 这里不需要再重设背景色，背景色会在开始时设置好，如果这里在设置会覆盖下方滑动条的颜色
				// tab.setBackgroundResource(position == i ? tabBackgroundResId
				// : transparentColorId);
			}
			else
			{
				v.setSelected(position == i ? true : false);
			}
		}
	}

	private int mTempOffset = 1;

	public void showActiveTab()
	{

		if (pager == null) return;

		mTempOffset++;
		if (mTempOffset > 5) mTempOffset = 1;
		scrollToChild(pager.getCurrentItem(), mTempOffset);
	}

	private void scrollToChild(int position, int offset)
	{
		if (!supportScroll)
			return;

		if (tabCount == 0)
		{
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0)
		{
			int index = position - 1;
			if (index < 0) {
				index = 0;
			}
			newScrollX -= tabsContainer.getChildAt(index).getWidth();
		}

		if (newScrollX != lastScrollX)
		{
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}

	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0)
		{
			return;
		}

		final int height = getHeight();

		// draw indicator line

		rectPaint.setColor(indicatorColor);

		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right coordinates
		// between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1)
		{

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
					* lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
					* lineRight);
		}

		canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
				rectPaint);

		// draw underline

		rectPaint.setColor(underlineColor);
		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
				height, rectPaint);

		// draw divider

		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++)
		{
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
					height - dividerPadding, dividerPaint);
		}
	}

	private class PageListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels)
		{

			currentPosition = position;
			currentPositionOffset = positionOffset;

			scrollToChild(position, (int) (positionOffset * tabsContainer
					.getChildAt(position).getWidth()));

			invalidate();

			if (delegatePageListener != null)
			{
				delegatePageListener.onPageScrolled(position, positionOffset,
						positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state)
		{
			if (state == ViewPager.SCROLL_STATE_IDLE)
			{
				scrollToChild(pager.getCurrentItem(), 0);
			}

			if (delegatePageListener != null)
			{
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position)
		{

			if (delegatePageListener != null)
			{
				delegatePageListener.onPageSelected(position);
			}

			if (tabSwitch)
			{
				updateActivateTab(position);
			}
		}

	}

	public void setIndicatorColor(int indicatorColor)
	{
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	public void setIndicatorColorResource(int resId)
	{
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	public int getIndicatorColor()
	{
		return this.indicatorColor;
	}

	public void setIndicatorHeight(int indicatorLineHeightPx)
	{
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	public int getIndicatorHeight()
	{
		return indicatorHeight;
	}

	public void setUnderlineColor(int underlineColor)
	{
		this.underlineColor = underlineColor;
		invalidate();
	}

	public void setUnderlineColorResource(int resId)
	{
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	public int getUnderlineColor()
	{
		return underlineColor;
	}

	public void setDividerColor(int dividerColor)
	{
		this.dividerColor = dividerColor;
		invalidate();
	}

	public void setDividerColorResource(int resId)
	{
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	public int getDividerColor()
	{
		return dividerColor;
	}

	public void setUnderlineHeight(int underlineHeightPx)
	{
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	public int getUnderlineHeight()
	{
		return underlineHeight;
	}

	public void setDividerPadding(int dividerPaddingPx)
	{
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	public int getDividerPadding()
	{
		return dividerPadding;
	}

	public void setScrollOffset(int scrollOffsetPx)
	{
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	public int getScrollOffset()
	{
		return scrollOffset;
	}

	public void setShouldExpand(boolean shouldExpand)
	{
		this.shouldExpand = shouldExpand;
		requestLayout();
	}

	public boolean getShouldExpand()
	{
		return shouldExpand;
	}

	public boolean isTextAllCaps()
	{
		return textAllCaps;
	}

	public void setAllCaps(boolean textAllCaps)
	{
		this.textAllCaps = textAllCaps;
	}

	public void setTextSize(int textSizePx)
	{

		final float scale = getResources().getDisplayMetrics().density;
		this.tabTextSize = (int) (textSizePx * scale + 0.5f);

		updateTabStyles();
	}

	public void setTextSizeId(int textSizeId)
	{
		this.tabTextSize = getResources().getDimensionPixelSize(textSizeId);

		updateTabStyles();
	}

	public int getTextSize()
	{
		return tabTextSize;
	}

	public void setTextColor(int textColor)
	{
		this.tabTextColor = textColor;
		updateTabStyles();
	}

	public void setTextColorResource(int resId)
	{
		this.tabTextColor = getResources().getColor(resId);
		updateTabStyles();
	}

	public int getTextColor()
	{
		return tabTextColor;
	}

	public void setTypeface(Typeface typeface, int style)
	{
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}

	public void setTabBackground(int resId)
	{
		this.tabBackgroundResId = resId;
	}

	public int getTabBackground()
	{
		return tabBackgroundResId;
	}

	public void setTabPaddingLeftRight(int paddingPx)
	{
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	public int getTabPaddingLeftRight()
	{
		return tabPadding;
	}

	public void setTabSwitch(boolean tabSwitch)
	{
		this.tabSwitch = tabSwitch;
		updateTabStyles();
	}

	public void setActivateTextColor(int activateTextColor)
	{
		this.tabTextColor = activateTextColor;
		updateTabViewColor();
//		updateTabStyles();
	}

	public void setDeactivateTextColor(int deactivateTextColor)
	{
		this.tabDeactivateTextColor = deactivateTextColor;
		updateTabStyles();
	}

	@Override
	public void onRestoreInstanceState(Parcelable state)
	{
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	static class SavedState extends BaseSavedState
	{
		int currentPosition;

		public SavedState(Parcelable superState)
		{
			super(superState);
		}

		private SavedState(Parcel in)
		{
			super(in);
			currentPosition = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags)
		{
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>()
		{
			@Override
			public SavedState createFromParcel(Parcel in)
			{
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size)
			{
				return new SavedState[size];
			}
		};
	}

	@SuppressLint("NewApi")
	@Override
	public void onGlobalLayout()
	{

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
		{
			getViewTreeObserver().removeGlobalOnLayoutListener(this);
		}
		else
		{
			getViewTreeObserver().removeOnGlobalLayoutListener(this);
		}

		currentPosition = pager.getCurrentItem();
		scrollToChild(currentPosition, 0);

	}

	@Override
	protected void onDetachedFromWindow()
	{
		realseRes();
		super.onDetachedFromWindow();
	}

	@SuppressLint("NewApi")
	public void realseRes()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) getViewTreeObserver()
				.removeOnGlobalLayoutListener(this);
		else
			getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	private ViewGroup mDisallowInterceptTouchEventView;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if (mDisallowInterceptTouchEventView != null)
		{
			int action = ev.getAction();
			switch (action)
			{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				mDisallowInterceptTouchEventView
						.requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mDisallowInterceptTouchEventView
						.requestDisallowInterceptTouchEvent(false);
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void setDisallowInterceptTouchEventView(ViewGroup view)
	{
		mDisallowInterceptTouchEventView = view;
	}
}
