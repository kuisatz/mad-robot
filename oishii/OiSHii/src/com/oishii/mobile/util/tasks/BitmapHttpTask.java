package com.oishii.mobile.util.tasks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.oishii.mobile.util.tasks.BitmapHttpTask.ResponseParam;

public class BitmapHttpTask extends
		AsyncTask<BitmapRequestParam, Void, ResponseParam> {

	class ResponseParam {
		Bitmap bitmap;
		ImageView image;
		ProgressBar bar;
		ViewGroup parent;
	}

	@Override
	protected ResponseParam doInBackground(BitmapRequestParam... params) {
		com.oishii.mobile.util.HttpTaskHelper helper = new com.oishii.mobile.util.HttpTaskHelper(
				params[0].bitmapUri);
		HttpResponse entity;
		Bitmap bitmap = null;
		try {
			entity = helper.execute();
			InputStream is = entity.getEntity().getContent();
			bitmap = BitmapFactory.decodeStream(is, null,
					new BitmapFactory.Options());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseParam resp = new ResponseParam();
		resp.bitmap = bitmap;
		resp.image = params[0].image;
		resp.bar = params[0].progress;
		resp.parent = params[0].parent;
		System.out.println("REtrn resp for->"+params[0].bitmapUri) ;
		return resp;
	}

	protected void onPostExecute(ResponseParam result) {
		if (result.bitmap != null && result.image != null) {
			if (result.bar != null) {
				result.bar.setVisibility(View.GONE);
				if (result.parent != null) {
					System.out.println("removing progress");
					result.parent.removeView(result.bar);
				}
			}
			result.image.setImageBitmap(result.bitmap);
			result.image.setVisibility(View.VISIBLE);
		}
	}
}
