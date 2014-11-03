package com.b509.xiaoliao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class ViewFlipperActivity extends mActivity implements
		android.view.GestureDetector.OnGestureListener {

	private int[] imgs = { R.drawable.img1, R.drawable.img2, R.drawable.img3,
			R.drawable.img4};

	private GestureDetector gestureDetector = null;
	private ViewFlipper viewFlipper = null;
	private Activity mActivity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mActivity = this;

		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		gestureDetector = new GestureDetector(this);

		for (int i = 0; i < imgs.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(imgs[i]);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			viewFlipper.addView(iv, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		viewFlipper.stopFlipping();
		viewFlipper.setAutoStart(false);
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub

		if (e2.getX() - e1.getX() > 120) {
			Animation rInAnim = AnimationUtils.loadAnimation(mActivity,
					R.anim.push_right_in);
			Animation rOutAnim = AnimationUtils.loadAnimation(mActivity,
					R.anim.push_right_out);
			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			return true;
		} else if (e2.getX() - e1.getX() < -120) {
			Animation lInAnim = AnimationUtils.loadAnimation(mActivity,
					R.anim.push_left_in);
			Animation lOutAnim = AnimationUtils.loadAnimation(mActivity,
					R.anim.push_left_out);
			viewFlipper.setInAnimation(lInAnim);
			viewFlipper.setOutAnimation(lOutAnim);
			viewFlipper.showNext();
			return true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub

		// ��ȡSharedPreFerences����Ҫ������,ʹ��SharedPreFerences����¼����������ʹ�ô���
		SharedPreferences preferences = getSharedPreferences("xiaoliao",
				Context.MODE_PRIVATE);
		// ȡ����Ӧ��ֵ,���û�и�ֵ,˵����δд��,��true��ΪĬ��ֵ
		boolean isFirstIn = preferences.getBoolean("isFirst", true);
		// �жϳ���ڼ�������
		Log.i("SP", "isFirst2========" + isFirstIn);
		System.out.println("isFirst=======" + isFirstIn);
		if (!isFirstIn) {

			this.finish();
		} else {

			Editor editor = preferences.edit();

			editor.putBoolean("isFirst", false);
			// �ύ�޸�
			editor.commit();
			Intent intent = new Intent(ViewFlipperActivity.this, ChooseActivity.class);
			ViewFlipperActivity.this.startActivity(intent);
			ViewFlipperActivity.this.finish();
		}

		return false;

	}

}
