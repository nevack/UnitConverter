package org.nevack.unitconverter.converter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * A FrameLayout subclass that dispatches WindowInsets to its children instead of adjusting its padding. 
 * Useful for Fragment containers.
 * 
 * @author Pkmmte Xeleon
 */
public class WindowInsetsFrameLayout extends FrameLayout {
	public WindowInsetsFrameLayout(Context context) {
		super(context);
	}

	public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public WindowInsets onApplyWindowInsets(WindowInsets insets) {
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++)
			getChildAt(index).dispatchApplyWindowInsets(insets);

		return insets;
	}
}
