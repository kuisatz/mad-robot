/*******************************************************************************
 * Copyright (c) 2011 MadRobot.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *  Elton Kent - initial API and implementation
 ******************************************************************************/
package com.madrobot.ui;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.widget.TextView;

public class UIUtils {
	public abstract static class ClickSpan extends ClickableSpan {

	}

	/**
	 * Set part of the text in a textview clickable.
	 * <p>
	 * The entire text needs to set to the textview before calling this method
	 * </p>
	 * @param view Textview to assign the clickage text to.
	 * @param clickableText Text in the textview that needs to be made clickable.
	 * @param span
	 */
	public static void clickify(TextView view, final String clickableText, final ClickSpan span) {

		CharSequence text = view.getText();
		String string = text.toString();

		int start = string.indexOf(clickableText);
		int end = start + clickableText.length();
		if (start == -1)
			return;

		if (text instanceof Spannable) {
			((Spannable) text).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			SpannableString s = SpannableString.valueOf(text);
			s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(s);
		}

		MovementMethod m = view.getMovementMethod();
		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			view.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

	/**
	 * Returns the scaled size for different resolutions.
	 * 
	 * @param fntSize
	 * @return
	 */
	public static int getDensityIndependentSize(int size, Context ctx) {
		int density = ctx.getResources().getDisplayMetrics().densityDpi;
		if (160 == density) {
			int newSize = (int) (size / (1.5));
			return newSize;
		} else if (density == 120) {
			int newSize = (size / (2));
			return newSize;
		}

		return size;
	}
}
