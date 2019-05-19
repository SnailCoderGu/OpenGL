package opengl.gp.com.testopengl.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import opengl.gp.com.testopengl.R;
import opengl.gp.com.testopengl.android.program.ColorShaderProgram;
import opengl.gp.com.testopengl.android.program.ColorShaderProgram2;
import opengl.gp.com.testopengl.android.program.TextureShaderProgram;
import opengl.gp.com.testopengl.objects.Mallet;
import opengl.gp.com.testopengl.objects.Puck;
import opengl.gp.com.testopengl.objects.Table;
import opengl.gp.com.testopengl.util.MatrixHelper;
import opengl.gp.com.testopengl.util.TextureHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;


public class AirHockeyWithBetterMalletsRenderer implements GLSurfaceView.Renderer {
    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectMatrix = new float[16];


    private Puck puck;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram2 colorShaderProgram;

    private int texture;


    private Context mContext;
    private Table table;
    private Mallet mallet;



    public AirHockeyWithBetterMalletsRenderer(Context context) {
        mContext = context;
    }
    float s = 4f;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        glClearColor(0.0f,0.0f,0.0f,0.0f);
        table = new Table();
        
        mallet = new Mallet(0.08f,0.15f,32);
        puck = new Puck(0.06f,0.02f,32);

        textureShaderProgram = new TextureShaderProgram(mContext);
        colorShaderProgram = new ColorShaderProgram2(mContext);

        texture = TextureHelper.loadTexture(mContext,R.drawable.air_hockey_surface);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                s = s - 0.01f;
                if(s <= 0){
                    s = 4f;
                }
                setLookAtM(viewMatrix,0,0,s,2.2f,0f,0f,0f,0f,1f,0f);
            }
        },1000,50);


   }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
        final float aspectRatio = width > height ? (float)width/(float)height: (float)height/(float)width;

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);

        setLookAtM(viewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        positionTableScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectMatrix,texture);
        table.bindData(textureShaderProgram);
        table.draw();

        positionObjectScene(0f,mallet.height/2f,-0.4f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectMatrix,1f,0f,0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        positionObjectScene(0f,puck.height/2f,0f);
        colorShaderProgram.setUniforms(modelViewProjectMatrix,0.8f,0.8f,1f);
        puck.bindData(colorShaderProgram);
        puck.draw();

        positionObjectScene(0f,mallet.height/2f,0.4f);
        colorShaderProgram.setUniforms(modelViewProjectMatrix,0f,0f,1f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();



    }
    private void positionTableScene(){

        setIdentityM(modelMatrix,0);
        rotateM(modelMatrix,0,-90f,1,0f,0f);
        multiplyMM(modelViewProjectMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }
    private void positionObjectScene(float x,float y,float z){
        setIdentityM(modelMatrix,0);
        translateM(modelMatrix,0,x,y,z);
        multiplyMM(modelViewProjectMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

}
