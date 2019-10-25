package io.dplusic.android.view;

import android.os.Parcel;
import android.util.FloatMath;
import android.view.MotionEvent;

public class AdvancedMotionEvent {

	public static final int AACTION_NONE = 0;
	public static final int AACTION_CLICK = 1;
	public static final int AACTION_DOUBLECLICK = 2;

	private MotionEvent motionEvent;

	private int advancedAction;
	private boolean multiTouch;
	private boolean oneClicked;
	private float angle;
	private float space;
	private float x;
	private float y;
	private float deltaAngle;
	private float deltaSpace;
	private float deltaX;
	private float deltaY;

	public AdvancedMotionEvent(MotionEvent motionEvent,
			AdvancedMotionEvent previousAdvancedMotionEvent) {

		this.motionEvent = motionEvent;

		multiTouch = motionEvent.getPointerCount() > 1;

		if (multiTouch) {

			float dx = motionEvent.getX(1) - motionEvent.getX(0);
			float dy = motionEvent.getY(1) - motionEvent.getY(0);

			space = (float) Math.sqrt(dx * dx + dy * dy);

			x = (motionEvent.getX(0) + motionEvent.getX(1)) / 2;
			y = (motionEvent.getY(0) + motionEvent.getY(1)) / 2;

			angle = (float) Math.toDegrees(Math.atan(dy / dx));

		} else {
			space = 0;
			x = motionEvent.getX();
			y = motionEvent.getY();
			angle = 0;
		}

		if (previousAdvancedMotionEvent != null) {
			deltaAngle = angle - previousAdvancedMotionEvent.getAngle();
			if (deltaAngle < -90) {
				deltaAngle = 180 - deltaAngle;
			} else if (deltaAngle > 90) {
				deltaAngle = deltaAngle - 180;
			}
			deltaSpace = space - previousAdvancedMotionEvent.getSpace();
			deltaX = x - previousAdvancedMotionEvent.getX();
			deltaY = y - previousAdvancedMotionEvent.getY();
		} else {
			oneClicked = false;
			deltaAngle = 0;
			deltaSpace = 0;
			deltaX = 0;
			deltaY = 0;
		}
	}

	public void setAdvancedAction(int advancedAction) {
		this.advancedAction = advancedAction;
	}

	public boolean isMultiTouch() {
		return multiTouch;
	}

	public boolean isOneClicked() {
		return oneClicked;
	}

	protected void setOneClicked(boolean oneClicked) {
		this.oneClicked = oneClicked;
	}

	public int getAdvancedAction() {
		return advancedAction;
	}

	public float getAngle() {
		return angle;
	}

	public float getSpace() {
		return space;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getDeltaAngle() {
		return deltaAngle;
	}

	public float getDeltaSpace() {
		return deltaSpace;
	}

	public float getDeltaX() {
		return deltaX;
	}

	public float getDeltaY() {
		return deltaY;
	}

	public final void addBatch(long eventTime, float x, float y,
			float pressure, float size, int metaState) {
		motionEvent.addBatch(eventTime, x, y, pressure, size, metaState);
	}

	public int describeContents() {
		return motionEvent.describeContents();
	}

	public boolean equals(Object o) {
		return motionEvent.equals(o);
	}

	public final int findPointerIndex(int pointerId) {
		return motionEvent.findPointerIndex(pointerId);
	}

	public final int getAction() {
		return motionEvent.getAction();
	}

	public final int getActionIndex() {
		return motionEvent.getActionIndex();
	}

	public final int getActionMasked() {
		return motionEvent.getActionMasked();
	}

	public final int getDeviceId() {
		return motionEvent.getDeviceId();
	}

	public final long getDownTime() {
		return motionEvent.getDownTime();
	}

	public final int getEdgeFlags() {
		return motionEvent.getEdgeFlags();
	}

	public final long getEventTime() {
		return motionEvent.getEventTime();
	}

	public final long getHistoricalEventTime(int pos) {
		return motionEvent.getHistoricalEventTime(pos);
	}

	public final float getHistoricalPressure(int pointerIndex, int pos) {
		return motionEvent.getHistoricalPressure(pointerIndex, pos);
	}

	public final float getHistoricalPressure(int pos) {
		return motionEvent.getHistoricalPressure(pos);
	}

	public final float getHistoricalSize(int pointerIndex, int pos) {
		return motionEvent.getHistoricalSize(pointerIndex, pos);
	}

	public final float getHistoricalSize(int pos) {
		return motionEvent.getHistoricalSize(pos);
	}

	public final float getHistoricalX(int pointerIndex, int pos) {
		return motionEvent.getHistoricalX(pointerIndex, pos);
	}

	public final float getHistoricalX(int pos) {
		return motionEvent.getHistoricalX(pos);
	}

	public final float getHistoricalY(int pointerIndex, int pos) {
		return motionEvent.getHistoricalY(pointerIndex, pos);
	}

	public final float getHistoricalY(int pos) {
		return motionEvent.getHistoricalY(pos);
	}

	public final int getHistorySize() {
		return motionEvent.getHistorySize();
	}

	public final int getMetaState() {
		return motionEvent.getMetaState();
	}

	public final int getPointerCount() {
		return motionEvent.getPointerCount();
	}

	public final int getPointerId(int pointerIndex) {
		return motionEvent.getPointerId(pointerIndex);
	}

	public final float getPressure() {
		return motionEvent.getPressure();
	}

	public final float getPressure(int pointerIndex) {
		return motionEvent.getPressure(pointerIndex);
	}

	public final float getRawX() {
		return motionEvent.getRawX();
	}

	public final float getRawY() {
		return motionEvent.getRawY();
	}

	public final float getSize() {
		return motionEvent.getSize();
	}

	public final float getSize(int pointerIndex) {
		return motionEvent.getSize(pointerIndex);
	}

	public final float getX(int pointerIndex) {
		return motionEvent.getX(pointerIndex);
	}

	public final float getXPrecision() {
		return motionEvent.getXPrecision();
	}

	public final float getY(int pointerIndex) {
		return motionEvent.getY(pointerIndex);
	}

	public final float getYPrecision() {
		return motionEvent.getYPrecision();
	}

	public int hashCode() {
		return motionEvent.hashCode();
	}

	public final void offsetLocation(float deltaX, float deltaY) {
		motionEvent.offsetLocation(deltaX, deltaY);
	}

	public void recycle() {
		motionEvent.recycle();
	}

	public final void setAction(int action) {
		motionEvent.setAction(action);
	}

	public final void setEdgeFlags(int flags) {
		motionEvent.setEdgeFlags(flags);
	}

	public final void setLocation(float x, float y) {
		motionEvent.setLocation(x, y);
	}

	public String toString() {
		return motionEvent.toString();
	}

	public void writeToParcel(Parcel out, int flags) {
		motionEvent.writeToParcel(out, flags);
	}

}
