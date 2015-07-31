package com.wancho.galleryviewpager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public abstract class ArrayPagerAdapter<T> extends PagerAdapter {

	private List<T> mObjects;

	private Context mContext;
	
	private List<View> mViews;
	
	public static interface OnItemClickListener {

		void onItemClick(View view, int position);

	}
	
	private OnItemClickListener mItemClickListener;

	public OnItemClickListener getmItemClickListener() {
		return mItemClickListener;
	}

	public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (mItemClickListener != null) {
					int position = (Integer) paramView.getTag(R.id.gallery_viewpager_itemclick_pos);
					mItemClickListener.onItemClick(paramView, position);
				}
			}
		};
		int N = mViews.size();
		if (listener != null && N > 0) {
			for (int i = 0; i < N; i++) {
				View viewChild = mViews.get(i);
				viewChild.setTag(R.id.gallery_viewpager_itemclick_pos, i);
				viewChild.setOnClickListener(listener);
			}
		}
	}

	public ArrayPagerAdapter(Context context, List<T> objects) {
		this.mContext = context;
		this.mObjects = objects;
		mViews = new ArrayList<View>();
		int N = mObjects.size();
		for (int i = 0; i < N; i++) {
			View view = getView(i, mObjects.get(i));
			mViews.add(view);
		}
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(mViews.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (mViews.size() < position) {
			return null;
		}

		((ViewPager) container).addView(mViews.get(position), 0);
		return mViews.get(position);
	}
	
	public abstract View getView(int position, T data);

	public Context getContext() {
		return mContext;
	}

	public T getItem(int position) {
		return mObjects.get(position);
	}

	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

}
