package com.wancho.galleryviewpager;

import com.wancho.galleryviewpager.ArrayPagerAdapter.OnItemClickListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

@SuppressLint("ClickableViewAccessibility")
public class GalleryViewPager extends ViewPager {
	
	@SuppressWarnings("unused")
	private static final String TAG = GalleryViewPager.class.getSimpleName();
	
	private static final int CLICK_INTERVAL = 100;

	private int leftMargin;
	
	private int right;
	
	private OnItemClickListener mItemClickListener; 
	
	private long downTime;
	
	private int downRawX;

	public GalleryViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setChildrenDrawingOrderEnabled(true);
		setPageTransformer(true, new PageTransformer() {
			
			@Override
			public void transformPage(View view, float position) {
				if (position <= -1) {
					view.setScaleX(1.0f);
					view.setScaleY(1.0f);
				} else if(position > -1 && position < 0) {
					float factor = (float) (1.25 + 0.25 * position);
					view.setScaleX(factor);
					view.setScaleY(factor);
				} else if(position == 0) {
					view.setScaleX(1.25f);
					view.setScaleY(1.25f);
				} else if(position > 0 && position < 1) {
					float factor = (float) (1.25 + (-0.25) * position);
					view.setScaleX(factor);
					view.setScaleY(factor);
				} else if(position >= 1) {
					view.setScaleX(1.0f);
					view.setScaleY(1.0f);
				}
			}
		});
		
	}
	
	private Display getDefaultDisplay() {
		Activity activity = (Activity) getContext();
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display;
	}
	
	@SuppressWarnings("deprecation")
	public void initRecommandLayoutParams() {
		MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
		Display display = getDefaultDisplay();
		int screenWidth = display.getWidth();
		leftMargin = (int) (screenWidth * 0.215);
		right = (int) (screenWidth * 0.785);
		lp.leftMargin = leftMargin;
		lp.rightMargin = leftMargin;
		setLayoutParams(lp);

		setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				ViewParent viewParent = getParent();
				if(viewParent != null) {
					ViewGroup viewGroup = (ViewGroup)viewParent;
					viewGroup.invalidate();
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		ViewParent viewParent = getParent();
		if(viewParent != null) {
			ViewGroup viewGroup = (ViewGroup)viewParent;
			viewGroup.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
					return GalleryViewPager.this.dispatchTouchEvent(paramMotionEvent);
				}
				
			});
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (leftMargin == 0) {
			leftMargin = (int) (getDefaultDisplay().getWidth() * 0.215);
		}
		if (right == 0) {
			right = (int) (getDefaultDisplay().getWidth() * 0.785);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downTime = System.currentTimeMillis();
			downRawX = (int) event.getRawX();
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		int currentItem = getCurrentItem();
		if(getAdapter()==null){
			return super.onTouchEvent(arg0);
		}
		int totalCount = getAdapter().getCount();
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_UP:
			int curX = (int) arg0.getRawX();
			if (System.currentTimeMillis() - downTime < CLICK_INTERVAL && mItemClickListener != null && Math.abs(curX - downRawX) < 50) {
				if (downRawX <= leftMargin) {
					if (currentItem > 0) {
						mItemClickListener.onItemClick(this, currentItem - 1);
					}
				} else if (downRawX >= right) {
					if (currentItem < totalCount - 1) {
						mItemClickListener.onItemClick(this, currentItem + 1);
					}
				} else {
					mItemClickListener.onItemClick(this, currentItem);
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(arg0);
	}

	/*@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		if (childCount == 3) {
			if (i == 2) {
				i = 1;
			} else if (i == 1) {
				i = 2;
			}
		} else if (childCount == 2 && getCurrentItem() == getAdapter().getCount() - 1) {
			if (i == 1) {
				i = 0;
			} else if (i == 0) {
				i = 1;
			}
		}
		return super.getChildDrawingOrder(childCount, i);
	}*/
	
	@SuppressWarnings("rawtypes")
	public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
		this.mItemClickListener = itemClickListener;
		ArrayPagerAdapter adapter = (ArrayPagerAdapter) getAdapter();
		if (adapter != null) {
			adapter.setOnItemClickListener(mItemClickListener);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setAdapter(PagerAdapter arg0) {
		if (!(arg0 instanceof ArrayPagerAdapter)) {
			return;
		}
		ArrayPagerAdapter adapter = (ArrayPagerAdapter) arg0;
		if (mItemClickListener != null && adapter != null) {
			adapter.setOnItemClickListener(mItemClickListener);
		}
		super.setAdapter(arg0);
	}

}
