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
package com.madrobot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.madrobot.ui.widgets.SVGImageView;

public class TestActivity extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.main);
//		final SVGImageView iv=(SVGImageView) findViewById(R.id.image);
//		iv.setSVGFromResource(R.raw.gir);
//		iv.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				iv.setSVGZoomFactor(iv.getSVGZoomFactor()+10);
//				
//			}
//		});
//		SVG svg=SVGFactory.getSVGFromResource(getResources(), R.raw.gir,50);
//		Log.d("MadRobot","META-->"+svg.getMetaData().toString());
//		Bitmap bitmap=svg.createBitmap(Bitmap.Config.ARGB_8888);
//		System.out.println("Parsing done");
//		Drawable drawable=svg.createDrawable();
//		iv.setBackgroundDrawable(drawable);
//		iv.setImageBitmap(bitmap);
		
//		BezelImageView gal=(BezelImageView) findViewById(R.id.coverflow);
//		gal.setAdapter(new ImageAdapter(getApplicationContext()));
		
	}

//	
//	 public class ImageAdapter extends BaseAdapter {
//	 		private Context mContext;
//	 		private Integer[] mImageIds = { R.drawable.three, R.drawable.one, R.drawable.two, R.drawable.three,
//	 				R.drawable.four,R.drawable.five
//	 		};
//	 
//	 		public ImageAdapter(Context c) {
//	 			mContext = c;
//	 		}
//	 		public int getCount() {
//	 			return mImageIds.length;
//	 		}
//	 		public Object getItem(int position) {
//	 			return position;
//	 		}
//	 
//	 		public long getItemId(int position) {
//	 			return position;
//	 		}
//	 		public View getView(int position, View convertView, ViewGroup parent) {
//	 
//	 			ImageView i = new ImageView(mContext);
//	 			i.setImageResource(mImageIds[position]);
//	 			i.setLayoutParams(new CoverFlowGallery.LayoutParams(350, 250));
//	 			i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//	 
//	 			BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
//	 			drawable.setAntiAlias(true);
//	 			return i;
//	 		}
//	 
//	 }
}
