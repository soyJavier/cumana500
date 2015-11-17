package com.cumana.adapters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheakLinearlayout extends LinearLayout implements Checkable {

	private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
	private boolean mChecked;
	public CheakLinearlayout(Context context) {
	super(context);
	}
	public CheakLinearlayout(Context context, AttributeSet attrs) {
	super(context, attrs);
	}
	public CheakLinearlayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			refreshDrawableState();
		}
	}
	@Override
	public boolean isChecked() {
		return mChecked;
	}
	@Override
	public void toggle() {
		setChecked(!mChecked);
	}
	@Override
	public boolean performClick() {
		toggle();
		return super.performClick();
	}
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}
	@Override
	protected Parcelable onSaveInstanceState() {
		SavedState result = new SavedState(super.onSaveInstanceState());
		result.checked = mChecked;
		return result;
	}
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
	
		super.onRestoreInstanceState(ss.getSuperState());
		setChecked(ss.checked);
	}
	protected static class SavedState extends BaseSavedState {
	
		protected boolean checked;
		
		protected SavedState(Parcelable superState) {
			super(superState);
		}
		@Override
		public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeInt(checked ? 1 : 0);
		}
	
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
			
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
		
		private SavedState(Parcel in) {
			super(in);
			checked = in.readInt() == 1;
		}
	}
}
