package com.wancho.galleryviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
		TextView tv = (TextView) findViewById(R.id.tv);
		
		LayoutParams lp = tv.getLayoutParams();
		android.widget.RelativeLayout.LayoutParams lpTarget = new RelativeLayout.LayoutParams(lp);
		TextView tvTarget = new TextView(this);
		tvTarget.setPadding(12, 12, 12, 12);
		tvTarget.setText("今天的天气还是");
		rl.addView(tvTarget, lpTarget);
	}
	
}
