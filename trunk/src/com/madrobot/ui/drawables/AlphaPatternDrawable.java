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
package com.madrobot.ui.drawables;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * This drawable that draws a simple white and gray chessboard pattern.
 * It's pattern you will often see as a background behind a
 * partly transparent image in many applications.
 * 
 */
public class AlphaPatternDrawable extends Drawable {

	/**
	 * Bitmap in which the pattern will be cahched.
	 */
	private Bitmap mBitmap;

	private Paint mPaint = new Paint();
	private Paint mPaintGray = new Paint();
	private Paint mPaintWhite = new Paint();

	private int mRectangleSize = 10;
	private int numRectanglesHorizontal;

	private int numRectanglesVertical;

	public AlphaPatternDrawable(int rectangleSize) {
		mRectangleSize = rectangleSize;
		mPaintWhite.setColor(0xffffffff);
		mPaintGray.setColor(0xffcbcbcb);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, null, getBounds(), mPaint);
	}

	/**
	 * This will generate a bitmap with the pattern
	 * as big as the rectangle we were allow to draw on.
	 * We do this to chache the bitmap so we don't need to
	 * recreate it each time draw() is called since it
	 * takes a few milliseconds.
	 */
	private void generatePatternBitmap() {

		if(getBounds().width() <= 0 || getBounds().height() <= 0){
			return;
		}

		mBitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);

		Rect r = new Rect();
		boolean verticalStartWhite = true;
		for(int i = 0; i <= numRectanglesVertical; i++){

			boolean isWhite = verticalStartWhite;
			for(int j = 0; j <= numRectanglesHorizontal; j++){

				r.top = i * mRectangleSize;
				r.left = j * mRectangleSize;
				r.bottom = r.top + mRectangleSize;
				r.right = r.left + mRectangleSize;

				canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);

				isWhite = !isWhite;
			}

			verticalStartWhite = !verticalStartWhite;

		}

	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);

		int height = bounds.height();
		int width = bounds.width();

		numRectanglesHorizontal = (int) Math.ceil((width / mRectangleSize));
		numRectanglesVertical = (int) Math.ceil(height / mRectangleSize);

		generatePatternBitmap();

	}

	@Override
	public void setAlpha(int alpha) {
		throw new UnsupportedOperationException("Alpha is not supported by this drawwable.");
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		throw new UnsupportedOperationException("ColorFilter is not supported by this drawwable.");
	}

}
