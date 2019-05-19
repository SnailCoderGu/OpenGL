package opengl.gp.com.testopengl;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import opengl.gp.com.testopengl.render.FirstOpenGLProjectRenderer;



public class FirstOpenGLProjectActivity extends Activity {
    private final static String TAG = "FirstOpenGL";

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

        if(supportsEs2){
            glSurfaceView.setEGLContextClientVersion(2);

            glSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
            renderSet = true;
        }else{
            Toast.makeText(this,"not support OpenGL Es 2.0",Toast.LENGTH_LONG).show();
            return ;
        }
        setContentView(glSurfaceView);
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
