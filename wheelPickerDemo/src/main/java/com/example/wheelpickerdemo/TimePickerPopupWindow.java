package com.example.wheelpickerdemo;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.PopupWindow;

public class TimePickerPopupWindow extends PopupWindow {

	private View mMenuView;
	private Button btnCancel;
	private Button btnConfirm;
	private onTimeSelectListener selectListener;
	private String timeString = null;
	private String[] timedata;
	private WheelView wheelView;

	public TimePickerPopupWindow(Context context,
			final onTimeSelectListener listener, String[] timeDate) {
		super(context);
		// TODO Auto-generated constructor stub
		this.selectListener = listener;
		this.timedata = timeDate;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_menu, null);
		btnCancel = (Button) mMenuView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.getTimeString(wheelView.getCurrentItemValue());
				dismiss();
			}
		});
		btnConfirm = (Button) mMenuView.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.getTimeString(wheelView.getCurrentItemValue());
				dismiss();
			}
		});
		wheelView = (WheelView) mMenuView.findViewById(R.id.timeWheel);
		initWheelView();

		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);

		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xff000000);
		this.setBackgroundDrawable(dw);

		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						listener.getTimeString(wheelView.getCurrentItemValue());
						dismiss();
					}
				}
				return true;
			}
		});

	}

	private void initWheelView() {
		wheelView.setAdapter(new StrericWheelAdapter(timedata));
		wheelView.setCurrentItem(1);
		wheelView.setCyclic(true);
		wheelView.setInterpolator(new AnticipateOvershootInterpolator());
	}

	public String getTimeString() {
		return this.timeString;
	}

	public interface onTimeSelectListener {
		public abstract void getTimeString(String timeString);
	}
}
