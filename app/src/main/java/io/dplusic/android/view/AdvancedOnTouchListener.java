package io.dplusic.android.view;

import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class AdvancedOnTouchListener implements OnTouchListener {

	private static final int VALID_MOTION_INTERVAL = 300;
	private static final float VALID_CLICK_RANGE = 8;

	private boolean oneClicked = false;
	private long downedTime = 0;
	private long oneClickedTime = 0;

	private float downedX;
	private float downedY;

	private float oldX;
	private float oldY;

	private AdvancedMotionEvent previousAdvancedMotionEvent;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		long currentTime = System.currentTimeMillis();

		float x = event.getX();
		float y = event.getY();
		
		int advancedAction = AdvancedMotionEvent.AACTION_NONE;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (currentTime - oneClickedTime > VALID_MOTION_INTERVAL
					&& calculateDistance(x, y, oldX, oldY) > VALID_CLICK_RANGE) {
				oneClicked = false;
			}
			downedTime = currentTime;
			if (oneClicked == false) {
				downedX = x;
				downedY = y;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (currentTime - downedTime < VALID_MOTION_INTERVAL
					&& calculateDistance(x, y, downedX, downedY) <= VALID_CLICK_RANGE) {
				if (oneClicked) {
					advancedAction = AdvancedMotionEvent.AACTION_DOUBLECLICK;
					oneClicked = false;
				} else {
					advancedAction = AdvancedMotionEvent.AACTION_CLICK;
					oneClicked = true;
					oneClickedTime = currentTime;
				}
			} else {
				oneClicked = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (oneClicked
					&& calculateDistance(x, y, downedX, downedY) > VALID_CLICK_RANGE) {
				downedTime = 0;
			}
			break;
		}

		oldX = x;
		oldY = y;

		AdvancedMotionEvent advancedMotionEvent = new AdvancedMotionEvent(
				event, previousAdvancedMotionEvent);
		advancedMotionEvent.setAdvancedAction(advancedAction);
		advancedMotionEvent.setOneClicked(oneClicked);

		previousAdvancedMotionEvent = advancedMotionEvent;

		return afterPreprocess(v, advancedMotionEvent);
	}

	protected abstract boolean afterPreprocess(View v, AdvancedMotionEvent event);

	private float calculateDistance(float ax, float ay, float bx, float by) {
		float dx = ax - bx;
		float dy = ay - by;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

}
