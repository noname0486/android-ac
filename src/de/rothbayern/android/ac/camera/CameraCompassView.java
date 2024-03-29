package de.rothbayern.android.ac.camera;

import java.io.IOException;

import de.rothbayern.android.ac.*;

import android.content.Context;
import android.graphics.*;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.*;
import android.widget.RelativeLayout;

public class CameraCompassView extends RelativeLayout implements IAnimCompass {

	public CameraCompassView(Context context) {
		super(context);
		create();
	}

	public CameraCompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		create();
	}

	public CameraCompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		create();
	}

	private void create() {
		Preview mPreview = new Preview(this.getContext());
		mDraw = new CompassSurfaceView(this.getContext());
		addView(mPreview, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addView(mDraw, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
	}

	

	public static class Preview extends SurfaceView implements SurfaceHolder.Callback {
		SurfaceHolder mHolder;
		Camera mCamera;

		public Preview(Context context) {
			super(context);

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, acquire the camera and tell it
			// where
			// to draw.
			mCamera = Camera.open();

			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException exception) {
				mCamera.release();
				mCamera = null;
				// TODO: add more exception handling logic here
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's
			// very
			// important to release it when the activity is paused.
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			// Now that the size is known, set up the camera parameters and
			// begin
			// the preview.
			System.out.println("surfaceChanged: " + w + "x" + h);
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(w, h);
			mCamera.setParameters(parameters);
			mCamera.startPreview();

		}

	}

	class DrawOnTop extends View {


		public DrawOnTop(Context context) {
			super(context);

		}

		@Override
		protected void onDraw(Canvas canvas) {
			

			int height = getHeight();
			int width = getWidth();

			final int VIEW_ANGEL = 56;

			int h2 = height/2;
			int lineHeight2 = height/20;
			
			int startDirection = (int)direction-VIEW_ANGEL/2;
			int endDirection = (int)direction+VIEW_ANGEL/2;
			
			
			
			
			Paint paint = new Paint();
			paint.setTextSize(30);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(3);
			paint.setColor(Color.BLACK);
			
			for(int i=startDirection;i<endDirection;i++){
				if(i%10==0){
					int diff = i -startDirection;
					int pos = diff*width/VIEW_ANGEL;
				    canvas.drawLine(pos, h2-lineHeight2, pos, h2+lineHeight2, paint);
				}
			}
			
			
			System.out.println("onDraw: " + width + "x" + height);
			canvas.drawText("tick: " + (int)direction, width * 1 / 3, height / 2, paint);
			//super.onDraw(canvas);
		}

	}

	private CompassSurfaceView mDraw = null;

	/*
	 * class ActionThread extends Thread { private boolean laeuft = true;
	 * 
	 * public boolean isLaeuft() { return laeuft; }
	 * 
	 * public void setLaeuft(boolean laeuft) { this.laeuft = laeuft; }
	 * 
	 * @Override public void run() { while (laeuft) { try { Thread.sleep(1000);
	 * } catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } counter++;
	 * 
	 * if(mDraw!=null){ Activity activity = (Activity) getContext();
	 * activity.runOnUiThread(new Runnable(){
	 * 
	 * public void run() { mDraw.invalidate(); } });
	 * 
	 * } } } }
	 * 
	 * private ActionThread actionThread = null;
	 */
	
	

	@Override
	public void setCompassLayoutFromPrefs() {
		

	}

	

	private float direction;
/*
	private boolean doAnim() {
		Activity activity = (Activity) getContext();
		activity.runOnUiThread(new Runnable() {

			public void run() {
				mDraw.invalidate();
			}
		});
		return true;
	}
*/	
	@Override
	public boolean setDirection(float direction) {
		direction+=90;
		if(direction<0){
			direction += 360.0f;
		}
		direction = direction % 360.0f;
		this.direction = direction;
		boolean success = mDraw.setDirection(direction);
		return(success);
	}

}
