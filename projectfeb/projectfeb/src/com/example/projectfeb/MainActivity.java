package com.example.projectfeb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.carl.view.CurlView;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
/**
 * Simple Activity for curl testing.
 *
 * @author harism
 * @modify LamPhucDuy
 */
public class MainActivity extends Activity {

	private CurlView mCurlView;
	private BitmapProvider2 mBitmapProvider = null;	
	
	Integer a[]=new Integer[5];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		a[0]=R.drawable.d;
		a[1]=R.drawable.ic_launcher;

		int index = 0;
		if (getLastNonConfigurationInstance() != null) {
			index = (Integer) getLastNonConfigurationInstance();
		}
		mCurlView = (CurlView) findViewById(R.id.curl);
		mBitmapProvider = new BitmapProvider2();
		mCurlView.setBitmapProvider(mBitmapProvider);
		mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		mCurlView.setCurrentIndex(index);
		mCurlView.setBackgroundColor(0x66202833);

	}

	@Override
	public void onPause() {
		super.onPause();
		mCurlView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mCurlView.onResume();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return mCurlView.getCurrentIndex();
	}

	public class BitmapProvider2 implements CurlView.BitmapProvider {
		Uri mCurrentUri = null;
		int mImagesCount = 2;
		Cursor mImageCur = null;

		@Override
		public Bitmap getBitmap(int width, int height, int index) {
			Bitmap b = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			b.eraseColor(0xFFFFFFFF);
			Canvas c = new Canvas(b);
	
			try {
				Bitmap bitmap = getBitmapFromUri(a[index]);
				Drawable d = new BitmapDrawable(bitmap);
				int margin = 7;
				int border = 3;
				Rect r = new Rect(margin, margin, width - margin, height - margin);

				int imageWidth = r.width() - (border * 2);
				int imageHeight = imageWidth * d.getIntrinsicHeight()
						/ d.getIntrinsicWidth();
				if (imageHeight > r.height() - (border * 2)) {
					imageHeight = r.height() - (border * 2);
					imageWidth = imageHeight * d.getIntrinsicWidth()
							/ d.getIntrinsicHeight();
				}

				r.left += ((r.width() - imageWidth) / 2) - border;
				r.right = r.left + imageWidth + border + border;
				r.top += ((r.height() - imageHeight) / 2) - border;
				r.bottom = r.top + imageHeight + border + border;

				Paint p = new Paint();
				p.setColor(0xFFC0C0C0);
				c.drawRect(r, p);
				r.left += border;
				r.right -= border;
				r.top += border;
				r.bottom -= border;

				d.setBounds(r);
				d.draw(c);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return b;
		}

		@Override
		public int getBitmapCount() {
			return mImagesCount;
		}

		@Override
		public Uri getCurrentUri() {
			return mCurrentUri;
		}

	}

	/**
	 * CurlView size changed observer.
	 */
	private class SizeChangedObserver implements CurlView.SizeChangedObserver {
		@Override
		public void onSizeChanged(int w, int h) {
			if (w > h) {
				mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
//				mCurlView.setMargins(.1f, .05f, .1f, .05f);
				mCurlView.setMargins(.02f, .0f, .02f, .0f);
			} else {
				mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
//				mCurlView.setMargins(.1f, .1f, .1f, .1f);
				mCurlView.setMargins(.02f, .02f, .02f, .02f);
			}
		}
	}


	public Bitmap getBitmapFromUri(Integer path) throws IOException {	  
		
		Bitmap resizeBitmap = BitmapFactory.decodeResource(getResources(), path);

	    return resizeBitmap;
	}


}