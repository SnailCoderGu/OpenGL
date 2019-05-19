package opengl.gp.com.testopengl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import opengl.gp.com.testopengl.render.AirHockeyTouchRender;
import opengl.gp.com.testopengl.render.AirHockeyWithBetterMalletsRenderer;



public class AirHockeyTouch extends Activity {
    private final static String TAG="AirHockeyTouch";
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        //setContentView(R.layout.activity_first_open_glproject);

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        final ConfigurationInfo configuration = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configuration.reqGlEsVersion >= 0x20000;
        Log.e(TAG,supportsEs2+"");

        final AirHockeyTouchRender airHockeyTouchRender = new AirHockeyTouchRender(this);
        if(supportsEs2){
            glSurfaceView.setEGLContextClientVersion(2);

            glSurfaceView.setRenderer(airHockeyTouchRender);
            renderSet = true;
        }else{
            Toast.makeText(this,"not support OpenGL Es 2.0",Toast.LENGTH_LONG).show();
            return ;
        }
        setContentView(glSurfaceView);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
                    final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyTouchRender.handleTouchPress(normalizedX, normalizedY);

                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                airHockeyTouchRender.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(renderSet){
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(renderSet){
            glSurfaceView.onPause();
        }
    }
}
